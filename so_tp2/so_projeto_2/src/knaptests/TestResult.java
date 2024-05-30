package knaptests;

import java.time.Duration;

public class TestResult {
    private final int bestValueFound;
    private final int weightOfValueFound;

    private final int timeToBestValue;

    private final int numberOfIterations;

    public TestResult() {
        bestValueFound = 0;
        weightOfValueFound = 0;
        timeToBestValue = 0;
        numberOfIterations = 0;
    }

    public TestResult(int bestValueFound, int weightOfValueFound, int timeToBestValue, int numberOfIterations) {
        this.bestValueFound = bestValueFound;
        this.weightOfValueFound = weightOfValueFound;
        this.timeToBestValue = timeToBestValue;
        this.numberOfIterations = numberOfIterations;
    }

    public int getBestValueFound() {
        return bestValueFound;
    }

    public int getWeightOfValueFound() {
        return weightOfValueFound;
    }

    public int getTimeToBestValue() {
        return timeToBestValue;
    }

    public int getNumberOfIterations() {
        return numberOfIterations;
    }

    @Override
    public String toString() {
        return bestValueFound + " | " + weightOfValueFound + " | " + timeToBestValue + " | " + numberOfIterations;
    }
}
