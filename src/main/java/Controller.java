import org.apache.kafka.common.ConsumerGroupState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ExecutionException;

public class Controller implements  Runnable {



    private static final Logger log = LogManager.getLogger(Main.class);

    static BinPack3pp bp;


    //static BinPack200 bp;


    static long  decisionIntervalms = 1000;




    public static void init () throws InterruptedException, ExecutionException {
        bp = new BinPack3pp();

        Constants.init();
        Lag.readEnvAndCrateAdminClient();

        log.info("Warming for 10 seconds.");
        Thread.sleep(10*1000);
        while (true) {
            log.info("Querying Prometheus");
            Controller.QueryingPrometheus();
            log.info("Sleeping for 15 seconds");
            log.info("========================================");
            Thread.sleep(decisionIntervalms);
        }
    }


    static void QueryingPrometheus() throws ExecutionException, InterruptedException {
        ArrivalRates.arrivalRateTopic1();
        Lag.getCommittedLatestOffsetsAndLag();

        //until prometheus discover  processing rates from consumer
        if (ArrivalRates.processingRate != 0) {
            scaleLogic();
        }
    }


    private static void scaleLogic() throws InterruptedException, ExecutionException {

        if (Lag.queryConsumerGroup() != BinPack3pp.size  || Lag.queryConsumerGroupState() != ConsumerGroupState.STABLE) {
            log.info("no action, previous action is not seen yet");
            return;
        }
        bp.scaleAsPerBinPack();
    }


    @Override
    public void run() {
        try {
            init();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

}
