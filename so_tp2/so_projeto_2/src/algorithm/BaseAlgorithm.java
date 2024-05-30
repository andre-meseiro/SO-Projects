package algorithm;

import knaptests.KnapTest;
import knaptests.TestResult;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class BaseAlgorithm extends Thread {

    private static CentralMemory centralMemory;

    private final Algorithm algorithm;

    private TestResult testResult;

    private boolean exit;


    public BaseAlgorithm() {
        algorithm = new Algorithm(centralMemory.knapTest);
        testResult = new TestResult();
    }

    public synchronized void updateCentralMemory() {
        centralMemory.updateTest(testResult);
    }

    public void stopAlgorithm() {
        algorithm.stop();
    }

    @Override
    public void run() {
        LocalTime lStartTime = LocalTime.now();
        int lowerBound = algorithm.lowerBound();
        int iterations = 1;
        testResult = new TestResult(algorithm.getStoredLowerBound(), algorithm.getStoredLowerBoundWeight(), 0, 0);
        while (!exit) {
            algorithm.beamSearch(algorithm.ALFA, lowerBound);
            LocalTime lEndTime = LocalTime.now();
            Duration duration = Duration.between(lStartTime, lEndTime);
            if (algorithm.getStoredLowerBound() > lowerBound) {
                lowerBound = algorithm.getStoredLowerBound();
                testResult = new TestResult(algorithm.getStoredLowerBound(), algorithm.getStoredLowerBoundWeight(), (int) duration.toMillis(), iterations);
            }
            iterations++;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ArrayList<BaseAlgorithm> threads = new ArrayList<>();
        centralMemory = new CentralMemory(new TestResult());
        int timeToRun = Integer.parseInt(args[2]);
        centralMemory.knapTest = new KnapTest(args[0]);
        for (int i = 0; i < Integer.parseInt(args[1]); i++) {
            BaseAlgorithm thread = new BaseAlgorithm();
            threads.add(thread);
            thread.exit = false;
            thread.start();
        }
        sleep(TimeUnit.SECONDS.toMillis(timeToRun));
        for (BaseAlgorithm thread : threads) {
            thread.updateCentralMemory();
            thread.stopAlgorithm();
            thread.exit = true;
            thread.join();
        }
    }

    public static TestResult getResult() {
        return centralMemory.bestTest;
    }

    public static KnapTest getKnapTest() {
        return centralMemory.knapTest;
    }

    public static boolean achievedBestValue() {
        return centralMemory.knapTest.getBestValue() == centralMemory.bestTest.getBestValueFound();
    }

    private static class CentralMemory {
        private TestResult bestTest;
        private KnapTest knapTest;

        public CentralMemory(TestResult bestTest) {
            this.bestTest = bestTest;
        }

        private synchronized void updateTest(TestResult testResult) {
            if (testResult.getBestValueFound() > bestTest.getBestValueFound())
                bestTest = testResult;
        }
    }
}
