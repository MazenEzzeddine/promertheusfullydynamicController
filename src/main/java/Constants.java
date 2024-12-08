public class Constants {
    public  static Integer rate;

    static String topic1ar;
    static String topic1p0;
    static String topic1p1;
    static String topic1p2;
    static String topic1p3;
    static String topic1p4;


    static String topic1p5;
    static String topic1p6;
    static String topic1p7;
    static String topic1p8;
    static String topic1p9;



    public static void init(){
        if(Controller.decisionIntervalms <= 20000)
        {
            rate = 20;
        }
        else {
            rate = (int) Controller.decisionIntervalms/1000;
        }

        topic1ar = "http://prometheus-operated:9090/api/v1/query?" +
                "query=sum(rate(kafka_topic_partition_current_offset%7Btopic=%22testtopic1%22,namespace=%22default%22%7D%5B1m%5D))%20by%20(topic)";

        topic1p0 = "http://prometheus-operated:9090/api/v1/query?" +
                "query=sum(rate(kafka_topic_partition_current_offset%7Btopic=%22testtopic1%22,partition=%220%22,namespace=%22default%22%7D%5B"+ rate +"s%5D))";
        topic1p1 = "http://prometheus-operated:9090/api/v1/query?" +
                "query=sum(rate(kafka_topic_partition_current_offset%7Btopic=%22testtopic1%22,partition=%221%22,namespace=%22default%22%7D%5B"+ rate +"s%5D))";

        topic1p2 = "http://prometheus-operated:9090/api/v1/query?" +
                "query=sum(rate(kafka_topic_partition_current_offset%7Btopic=%22testtopic1%22,partition=%222%22,namespace=%22default%22%7D%5B"+ rate +"s%5D))";

        topic1p3 = "http://prometheus-operated:9090/api/v1/query?" +
                "query=sum(rate(kafka_topic_partition_current_offset%7Btopic=%22testtopic1%22,partition=%223%22,namespace=%22default%22%7D%5B"+ rate +"s%5D))";

        topic1p4 = "http://prometheus-operated:9090/api/v1/query?" +
                "query=sum(rate(kafka_topic_partition_current_offset%7Btopic=%22testtopic1%22,partition=%224%22,namespace=%22default%22%7D%5B"+rate +"s%5D))";


        topic1p5 = "http://prometheus-operated:9090/api/v1/query?" +
                "query=sum(rate(kafka_topic_partition_current_offset%7Btopic=%22testtopic1%22,partition=%225%22,namespace=%22default%22%7D%5B"+ rate +"s%5D))";
        topic1p6 = "http://prometheus-operated:9090/api/v1/query?" +
                "query=sum(rate(kafka_topic_partition_current_offset%7Btopic=%22testtopic1%22,partition=%226%22,namespace=%22default%22%7D%5B"+ rate +"s%5D))";

        topic1p7 = "http://prometheus-operated:9090/api/v1/query?" +
                "query=sum(rate(kafka_topic_partition_current_offset%7Btopic=%22testtopic1%22,partition=%227%22,namespace=%22default%22%7D%5B"+ rate +"s%5D))";

        topic1p8 = "http://prometheus-operated:9090/api/v1/query?" +
                "query=sum(rate(kafka_topic_partition_current_offset%7Btopic=%22testtopic1%22,partition=%228%22,namespace=%22default%22%7D%5B"+ rate +"s%5D))";

        topic1p9 = "http://prometheus-operated:9090/api/v1/query?" +
                "query=sum(rate(kafka_topic_partition_current_offset%7Btopic=%22testtopic1%22,partition=%229%22,namespace=%22default%22%7D%5B"+rate +"s%5D))";
    }







    static String topic1lag = "http://prometheus-operated:9090/api/v1/query?query=" +
            "sum(kafka_consumergroup_lag%7Bconsumergroup=%22testgroup1%22,topic=%22testtopic1%22,namespace=%22default%22%7D)%20by%20(consumergroup,topic)";
    static String topic1p0lag = "http://prometheus-operated:9090/api/v1/query?query=" +
            "kafka_consumergroup_lag%7Bconsumergroup=%22testgroup1%22,topic=%22testtopic1%22,partition=%220%22,namespace=%22default%22%7D";
    static String topic1p1lag = "http://prometheus-operated:9090/api/v1/query?query=" +
            "kafka_consumergroup_lag%7Bconsumergroup=%22testgroup1%22,topic=%22testtopic1%22,partition=%221%22,namespace=%22default%22%7D";
    static String topic1p2lag = "http://prometheus-operated:9090/api/v1/query?query=" +
            "kafka_consumergroup_lag%7Bconsumergroup=%22testgroup1%22,topic=%22testtopic1%22,partition=%222%22,namespace=%22default%22%7D";
    static String topic1p3lag = "http://prometheus-operated:9090/api/v1/query?query=" +
            "kafka_consumergroup_lag%7Bconsumergroup=%22testgroup1%22,topic=%22testtopic1%22,partition=%223%22,namespace=%22default%22%7D";
    static String topic1p4lag = "http://prometheus-operated:9090/api/v1/query?query=" +
            "kafka_consumergroup_lag%7Bconsumergroup=%22testgroup1%22,topic=%22testtopic1%22,partition=%224%22,namespace=%22default%22%7D";





    /*    //HttpClient client = HttpClient.newHttpClient();
    static String topic1ar = "http://prometheus-operated:9090/api/v1/query?" +
            "query=sum(rate(kafka_topic_partition_current_offset%7Btopic=%22testtopic1%22,namespace=%22default%22%7D%5B1m%5D))%20by%20(topic)";
    static String topic1p0 = "http://prometheus-operated:9090/api/v1/query?" +
            "query=sum(rate(kafka_topic_partition_current_offset%7Btopic=%22testtopic1%22,partition=%220%22,namespace=%22default%22%7D%5B20s%5D))";
    static String topic1p1 = "http://prometheus-operated:9090/api/v1/query?" +
            "query=sum(rate(kafka_topic_partition_current_offset%7Btopic=%22testtopic1%22,partition=%221%22,namespace=%22default%22%7D%5B20s%5D))";
    static String topic1p2 = "http://prometheus-operated:9090/api/v1/query?" +
            "query=sum(rate(kafka_topic_partition_current_offset%7Btopic=%22testtopic1%22,partition=%222%22,namespace=%22default%22%7D%5B20s%5D))";
    static String topic1p3 = "http://prometheus-operated:9090/api/v1/query?" +
            "query=sum(rate(kafka_topic_partition_current_offset%7Btopic=%22testtopic1%22,partition=%223%22,namespace=%22default%22%7D%5B20s%5D))";
    static String topic1p4 = "http://prometheus-operated:9090/api/v1/query?" +
            "query=sum(rate(kafka_topic_partition_current_offset%7Btopic=%22testtopic1%22,partition=%224%22,namespace=%22default%22%7D%5B20s%5D))";




    static String topic1lag = "http://prometheus-operated:9090/api/v1/query?query=" +
            "sum(kafka_consumergroup_lag%7Bconsumergroup=%22testgroup1%22,topic=%22testtopic1%22,namespace=%22default%22%7D)%20by%20(consumergroup,topic)";
    static String topic1p0lag = "http://prometheus-operated:9090/api/v1/query?query=" +
            "kafka_consumergroup_lag%7Bconsumergroup=%22testgroup1%22,topic=%22testtopic1%22,partition=%220%22,namespace=%22default%22%7D";
    static String topic1p1lag = "http://prometheus-operated:9090/api/v1/query?query=" +
            "kafka_consumergroup_lag%7Bconsumergroup=%22testgroup1%22,topic=%22testtopic1%22,partition=%221%22,namespace=%22default%22%7D";
    static String topic1p2lag = "http://prometheus-operated:9090/api/v1/query?query=" +
            "kafka_consumergroup_lag%7Bconsumergroup=%22testgroup1%22,topic=%22testtopic1%22,partition=%222%22,namespace=%22default%22%7D";
    static String topic1p3lag = "http://prometheus-operated:9090/api/v1/query?query=" +
            "kafka_consumergroup_lag%7Bconsumergroup=%22testgroup1%22,topic=%22testtopic1%22,partition=%223%22,namespace=%22default%22%7D";
    static String topic1p4lag = "http://prometheus-operated:9090/api/v1/query?query=" +
            "kafka_consumergroup_lag%7Bconsumergroup=%22testgroup1%22,topic=%22testtopic1%22,partition=%224%22,namespace=%22default%22%7D";*/

//for a single instance
/*    static String processingLatency = "http://prometheus-operated:9090/api/v1/query?query=" +
            "avg_over_time(processingGauge%5B30s%5D)";
    static String processingLatencyPercentile = "http://prometheus-operated:9090/api/v1/query?query=" +
            "quantile_over_time(0.95%2CprocessingGauge%5B1m%5D)";*/



    static String processingLatencyAvg = "http://prometheus-operated:9090/api/v1/query?query=" +
            "avg(avg_over_time(processingGauge%5B10s%5D))";
    static String processingLatencyPercentileAvg = "http://prometheus-operated:9090/api/v1/query?query=" +
            "avg(quantile_over_time(0.95%2CprocessingGauge%5B10s%5D))";


/*    static String totalLatency = "http://prometheus-operated:9090/api/v1/query?query=" +
            "avg_over_time(totallatencygauge%5B30s%5D)";*/


    static String pr = "http://prometheus-operated:9090/api/v1/query?query=" +
            "1000/(avg(rate(events_latency_sum[2s])/rate(events_latency_count[2s])))";



}



