import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ExecutionException;

public class Main {

    private static final Logger log = LogManager.getLogger(Main.class);

    static BinPack3pp bp;





    public static void main(String[] args) throws InterruptedException, ExecutionException {
        bp = new BinPack3pp();

        log.info("Warming for 10 seconds.");
        Thread.sleep(10*1000);
        while (true) {
            log.info("Querying Prometheus");
            Main.QueryingPrometheus();
            log.info("Sleeping for 1 seconds");
            log.info("========================================");
            Thread.sleep(1000);
        }
    }


    static void QueryingPrometheus() throws ExecutionException, InterruptedException {
        ArrivalRates.arrivalRateTopic1();

        //until prometheus discover  processing rates from consumer
        if (ArrivalRates.processingRate != 0) {
            scaleLogic();
        }
    }


    private static void scaleLogic() throws InterruptedException, ExecutionException {
        if  (Duration.between(bp.LastUpScaleDecision, Instant.now()).getSeconds() >3){
            bp.scaleAsPerBinPack();
        } else {
            log.info("No scale group 1 cooldown");
        }
    }

}
