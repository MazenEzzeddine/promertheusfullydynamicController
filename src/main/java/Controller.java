import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ExecutionException;

public class Controller implements  Runnable {



    private static final Logger log = LogManager.getLogger(Main.class);

    static BinPack3pp bp;


    //static BinPack200 bp;






    public static void init () throws InterruptedException, ExecutionException {
        bp = new BinPack3pp();

        log.info("Warming for 10 seconds.");
        Thread.sleep(10*1000);
        while (true) {
            log.info("Querying Prometheus");
            Controller.QueryingPrometheus();
            log.info("Sleeping for 15 seconds");
            log.info("========================================");
            Thread.sleep(1000);
        }
    }


    static void QueryingPrometheus() throws ExecutionException, InterruptedException {
        ArrivalRates.arrivalRateTopic1();

        //until prometheus discover  processing rates from consumer
        if (ArrivalRates.processingRate != 0) {
            //scaleLogic();
            bp.scaleAsPerBinPack();
        }
        // scaleLogic();
    }


    private static void scaleLogic() throws InterruptedException, ExecutionException {
        if  (Duration.between(bp.LastUpScaleDecision, Instant.now()).getSeconds() >10){
            bp.scaleAsPerBinPack();
        } else {
            log.info("No scale group 1 cooldown");
        }


        //bp.scaleAsPerBinPack();
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
