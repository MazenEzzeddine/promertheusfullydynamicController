
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

public class BinPack3pp {

    //TODO give fup and fdown as paramters to the functions.
    private static final Logger log = LogManager.getLogger(BinPack3pp.class);
    public static   int size =1;
    public   Instant LastUpScaleDecision = Instant.now();

    private final static double wsla = 0.5;

    static List<Consumer> assignment =  new ArrayList<Consumer>();

    static List<Consumer> currentAssignment = assignment;
    static List<Consumer> tempAssignment;

    private static KafkaConsumer<byte[], byte[]> metadataConsumer;

    static {
        currentAssignment.add(new Consumer("0", (long) (200f * wsla * .9),
                200f * .9));
        for (Partition p : ArrivalRates.topicpartitions) {
            currentAssignment.get(0).assignPartition(p);
        }
    }



    public  void scaleAsPerBinPack() {
        log.info("Currently we have this number of consumers group {} {}","testgroup1", size );
        log.info("We have this processing rate {}", ArrivalRates.processingRate);
        int neededsize = binPackAndScale();
        log.info("We currently need the following consumers for group1 (as per the bin pack) {}", neededsize);
        int replicasForscale = neededsize - size;
        tempAssignment = List.copyOf(assignment);
        if (replicasForscale > 0) {
            //TODO IF and Else IF can be in the same logic
            log.info("We have to upscale  group1 by {}", replicasForscale);
            size = neededsize;
            currentAssignment = List.copyOf(assignment);
            LastUpScaleDecision= Instant.now();
            try (final KubernetesClient k8s = new KubernetesClientBuilder().build() ) {
                k8s.apps().deployments().inNamespace("default").withName("latency").scale(neededsize);
                log.info("I have upscaled group {} you should have {}", "testgroup1", neededsize);
            }

        }
        else {
            int neededsized = binPackAndScaled();
            int replicasForscaled = size - neededsized;
            if (replicasForscaled > 0) {
                log.info("We have to downscale  group by {} {}", "testgroup1", replicasForscaled);
                size = neededsized;
                LastUpScaleDecision = Instant.now();
                try (final KubernetesClient k8s = new KubernetesClientBuilder().build()) {
                    k8s.apps().deployments().inNamespace("default").withName("latency").scale(neededsized);
                    log.info("I have downscaled group {} you should have {}", "testgroup1", neededsized);
                    currentAssignment = List.copyOf(assignment);

                }
            } else if (assignmentViolatesTheSLA2()) {

                ///
                if(metadataConsumer== null) {
                    KafkaConsumerConfig config = KafkaConsumerConfig.fromEnv();
                    Properties props = KafkaConsumerConfig.createProperties(config);
                    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                            "org.apache.kafka.common.serialization.StringDeserializer");
                    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                            "org.apache.kafka.common.serialization.StringDeserializer");
                    metadataConsumer = new KafkaConsumer<>(props);
                }
                metadataConsumer.enforceRebalance();
                currentAssignment = tempAssignment;
                ///

            }
        }
        log.info("===================================");
    }






    private  int binPackAndScale() {
        log.info(" shall we upscale group {}", "testgroup1");
        List<Consumer> consumers = new ArrayList<>();
        int consumerCount = 1;
        List<Partition> parts = new ArrayList<>(ArrivalRates.topicpartitions);
        float fraction = 0.9f;

        //if a certain partition has an arrival rate  higher than R  set its arrival rate  to R
        //that should not happen in a well partionned topic
        for (Partition partition : parts) {
            if (partition.getArrivalRate() >ArrivalRates.processingRate *fraction) {
                log.info("Since partition {} has arrival rate {} higher than consumer service rate {}" +
                                " we are truncating its arrival rate", partition.getId(),
                        String.format("%.2f", partition.getArrivalRate()),
                        String.format("%.2f",ArrivalRates.processingRate *fraction ));
                partition.setArrivalRate(ArrivalRates.processingRate*fraction );
            }
        }

        for (Partition partition : parts) {
            if (partition.getLag() > ArrivalRates.processingRate *fraction *wsla) {
                log.info("Since partition {} has lag {} higher than consumer capacity times wsla {}" +
                        " we are truncating its lag", partition.getId(), partition.getLag(),
                        ArrivalRates.processingRate*wsla* fraction);
                partition.setLag((long)(ArrivalRates.processingRate*wsla* fraction));
            }
        }


        //start the bin pack FFD with sort
        Collections.sort(parts, Collections.reverseOrder());

        while (true) {
            int j;
            consumers.clear();
            for (int t = 0; t < consumerCount; t++) {
                consumers.add(new Consumer((String.valueOf(t)),  (long)(ArrivalRates.processingRate*wsla*fraction),
                        ArrivalRates.processingRate*fraction));
            }

            for (j = 0; j < parts.size(); j++) {
                int i;
                Collections.sort(consumers, Collections.reverseOrder());
                for (i = 0; i < consumerCount; i++) {

                    if (consumers.get(i).getRemainingArrivalCapacity() >= parts.get(j).getArrivalRate() &&
                            consumers.get(i).getRemainingLagCapacity() >= parts.get(j).getLag()) {
                        consumers.get(i).assignPartition(parts.get(j));
                        break;
                    }
                }
                if (i == consumerCount) {
                    consumerCount++;
                    break;
                }
            }
            if (j == parts.size())
                break;
        }
        log.info(" The BP up scaler recommended for group {} {}", "testgroup1", consumers.size());
        assignment = consumers;
        tempAssignment = assignment;
        return consumers.size();
    }

    private  int binPackAndScaled() {
        log.info(" shall we down scale group {} ", "testgroup1");
        List<Consumer> consumers = new ArrayList<>();
        int consumerCount = 1;
        List<Partition> parts = new ArrayList<>(ArrivalRates.topicpartitions);
        double fractiondynamicAverageMaxConsumptionRate = ArrivalRates.processingRate*0.4;


        //if a certain partition has an arrival rate  higher than R  set its arrival rate  to R
        //that should not happen in a well partionned topic
        for (Partition partition : parts) {
            if (partition.getArrivalRate() > fractiondynamicAverageMaxConsumptionRate) {
                log.info("Since partition {} has arrival rate {} higher than consumer service rate {}" +
                                " we are truncating its arrival rate", partition.getId(),
                        String.format("%.2f", partition.getArrivalRate()),
                        String.format("%.2f", fractiondynamicAverageMaxConsumptionRate));
                partition.setArrivalRate(fractiondynamicAverageMaxConsumptionRate);
            }
        }

        for (Partition partition : parts) {
            if (partition.getLag() > fractiondynamicAverageMaxConsumptionRate *wsla) {
                log.info("Since partition {} has lag {} higher than consumer capacity times wsla {}" +
                        " we are truncating its lag", partition.getId(), partition.getLag(), fractiondynamicAverageMaxConsumptionRate*wsla);
                partition.setLag((long)(fractiondynamicAverageMaxConsumptionRate*wsla));
            }
        }
        //start the bin pack FFD with sort
        Collections.sort(parts, Collections.reverseOrder());
        while (true) {
            int j;
            consumers.clear();
            for (int t = 0; t < consumerCount; t++) {
                consumers.add(new Consumer((String.valueOf(t)),
                        (long)(fractiondynamicAverageMaxConsumptionRate*wsla),
                        fractiondynamicAverageMaxConsumptionRate));
            }

            for (j = 0; j < parts.size(); j++) {
                int i;
                Collections.sort(consumers, Collections.reverseOrder());
                for (i = 0; i < consumerCount; i++) {

                    if (consumers.get(i).getRemainingArrivalCapacity() >= parts.get(j).getArrivalRate() &&
                            consumers.get(i).getRemainingLagCapacity() >= parts.get(j).getLag()) {
                        consumers.get(i).assignPartition(parts.get(j));
                        break;
                    }
                }
                if (i == consumerCount) {
                    consumerCount++;
                    break;
                }
            }
            if (j == parts.size())
                break;
        }
        log.info(" The BP down scaler recommended  for group {} {}", "testgroup1", consumers.size());
        assignment = consumers;
        return consumers.size();
    }


//    private  boolean assignmentViolatesTheSLA() {
//        for (Consumer cons : currentAssignment) {
//            if (cons.getRemainingLagCapacity() <  (long) (wsla*ArrivalRates.processingRate*.9f)||
//                    cons.getRemainingArrivalCapacity() < ArrivalRates.processingRate*0.9f){
//                return true;
//            }
//        }
//        return false;
//    }




    private static boolean assignmentViolatesTheSLA2() {




        List<Partition> partsReset = new ArrayList<>(ArrivalRates.topicpartitions);


        float fraction = 0.9f;
        for (Partition partition : partsReset) {
            if (partition.getLag() > ArrivalRates.processingRate * wsla * fraction) {
                partition.setLag((long) (ArrivalRates.processingRate * wsla * fraction));
            }
        }

        for (Partition partition : partsReset) {
            if (partition.getArrivalRate() > ArrivalRates.processingRate * fraction) {
                partition.setArrivalRate(ArrivalRates.processingRate * fraction);
            }
        }




        for (Consumer cons : currentAssignment) {
            double sumPartitionsArrival = 0;
            double sumPartitionsLag = 0;
            for (Partition p : cons.getAssignedPartitions()) {
                sumPartitionsArrival += partsReset.get(p.getId()).getArrivalRate();
                sumPartitionsLag += partsReset.get(p.getId()).getLag();
            }

            if (sumPartitionsLag  > ( wsla * ArrivalRates.processingRate  * .9f)
                    || sumPartitionsArrival > ArrivalRates.processingRate* 0.9f) {
                return true;
            }
        }
        return false;
    }




}
