package algorithm;

import knaptests.KnapTest;
import knaptests.TestResult;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;

public class AdvancedAlgorithm extends Thread {

    private static CentralMemory centralMemory;

    private static ArrayList<AdvancedAlgorithm> threads;

    private final Algorithm algorithm;
    private int lowerBound;
    private static long saveEvery;
    private TestResult testResult;
    private boolean exit;


    public AdvancedAlgorithm() {
        algorithm = new Algorithm(centralMemory.knapTest);
        testResult = new TestResult();
    }

    public synchronized void updateCentralMemory() {
        centralMemory.updateTest(testResult);
    }

    public synchronized void updateInternalResults() {
        testResult = centralMemory.bestTest;
        lowerBound = testResult.getBestValueFound();
    }

    public void stopAlgorithm() {
        algorithm.stop();
    }

    @Override
    public void run() {
        LocalTime lStartTime = LocalTime.now();
        lowerBound = algorithm.lowerBound();
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

    public static void waitForTimerAndUpdate(int timeToRun) {
        LocalTime lStart = LocalTime.now();
        Duration duration = Duration.ZERO;
        while (duration.getSeconds() < timeToRun) {
            duration = Duration.between(lStart, LocalTime.now());
            if (duration.toMillis() % saveEvery == 0 && duration.toMillis() != 0) {
                for (AdvancedAlgorithm thread : threads) {
                    thread.updateCentralMemory();
                }
                for (AdvancedAlgorithm thread : threads) {
                    thread.updateInternalResults();
                }
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        threads = new ArrayList<>();
        centralMemory = new CentralMemory(new TestResult());
        centralMemory.knapTest = new KnapTest(args[0]);
        int timeToRun = Integer.parseInt(args[2]);
        saveEvery = (long) ((timeToRun * Float.parseFloat(args[3]) / 100) * 1000);
        for (int i = 0; i < Integer.parseInt(args[1]); i++) {
            AdvancedAlgorithm thread = new AdvancedAlgorithm();
            threads.add(thread);
            thread.exit = false;
            thread.start();
        }
        waitForTimerAndUpdate(timeToRun);
        for (AdvancedAlgorithm thread : threads) {
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
            if (testResult.getBestValueFound() > bestTest.getBestValueFound()) {
                bestTest = testResult;
            } else if (testResult.getBestValueFound() == bestTest.getBestValueFound()) {
                if (testResult.getTimeToBestValue() < bestTest.getTimeToBestValue()) {
                    bestTest = testResult;
                }
            }
        }
    }
}
