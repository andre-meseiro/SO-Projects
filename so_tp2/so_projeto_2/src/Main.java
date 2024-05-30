import algorithm.AdvancedAlgorithm;
import algorithm.BaseAlgorithm;
import knaptests.KnapTest;
import knaptests.TestResult;

import java.time.Duration;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        if (args.length >= 4) {
            String filename = args[1];
            String numberOfThreads = args[2];
            String executionTime = args[3];

            if (args.length == 5) {
                String saveAt = args[4];
                //(i.e) kp ex05.txt 10 5 25
                runAdvancedAlgorithmBatch(filename, numberOfThreads, executionTime, saveAt);
            } else {
                runBaseAlgorithmBatch(filename, numberOfThreads, executionTime);
            }
        }
    }

    private static void runBaseAlgorithmBatch(String filename, String numberOfThreads, String executionTime) {
        System.out.println("#######################################################  Base Algorithm  ##################################################");
        Map<TestResult, Boolean> testResults = new HashMap<>();
        printHeader();
        for (int i = 1; i <= 10; i++) {
            try {
                LocalTime startTime = LocalTime.now();
                BaseAlgorithm.main(new String[]{filename, numberOfThreads, executionTime});
                testResults.put(BaseAlgorithm.getResult(), BaseAlgorithm.achievedBestValue());
                printTest(i, Integer.parseInt(numberOfThreads), Duration.between(startTime, LocalTime.now()).toMillis(), BaseAlgorithm.getKnapTest(), BaseAlgorithm.getResult());

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println("###########################################################################################################################");
        printStatistics(testResults);
    }

    private static void runAdvancedAlgorithmBatch(String filename, String numberOfThreads, String executionTime, String saveAt) {
        System.out.println("###################################################  Advanced Algorithm  ##################################################");
        Map<TestResult, Boolean> testResults = new HashMap<>();
        printHeader();
        for (int i = 1; i <= 10; i++) {
            try {
                LocalTime startTime = LocalTime.now();
                AdvancedAlgorithm.main(new String[]{filename, numberOfThreads, executionTime, saveAt});
                testResults.put(AdvancedAlgorithm.getResult(), AdvancedAlgorithm.achievedBestValue());
                printTest(i, Integer.parseInt(numberOfThreads), Duration.between(startTime, LocalTime.now()).toMillis(), AdvancedAlgorithm.getKnapTest(), AdvancedAlgorithm.getResult());

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println("###########################################################################################################################");
        printStatistics(testResults);
    }


    private static void printHeader() {
        System.out.println(" # |          Test (#items) | Exec. Time (ms) | #Threads | Best Value | Best Weight | Time to Best Value (ms) | #Iterations");
    }

    private static void printTest(int testNumber, int numberOfThreads, long executionTime, KnapTest knapTest, TestResult testResult) {
        System.out.printf("%2d | %14s (%5d) | %15d | %8d | %10d | %11d | %23d | %10d\n", testNumber, knapTest.getTestName(), knapTest.getNumberOfItems(), executionTime, numberOfThreads, testResult.getBestValueFound(), testResult.getWeightOfValueFound(), testResult.getTimeToBestValue(), testResult.getNumberOfIterations());
    }

    private static void printStatistics(Map<TestResult, Boolean> testResults) {
        int totalTime = 0;
        for (TestResult testResult : testResults.keySet()) {
            totalTime += testResult.getTimeToBestValue();
        }
        int totalIterations = 0;
        for (TestResult testResult : testResults.keySet()) {
            totalIterations += testResult.getNumberOfIterations();
        }
        int totalAchieved = 0;
        for (Map.Entry<TestResult, Boolean> testResult : testResults.entrySet()) {
            if (testResult.getValue()) totalAchieved++;
        }
        System.out.printf("                             Avg. Time: %.2f ms   Avg. Iterations: %.2f   # Achieved Best Result : %d\n", ((double) totalTime) / testResults.size(), ((double) totalIterations) / testResults.size(), totalAchieved);
    }
}
