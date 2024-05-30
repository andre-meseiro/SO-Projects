package knaptests;

import java.util.ArrayList;

public class KnapTest {
    private final String testName;
    private ArrayList<Integer> values;
    private ArrayList<Integer> weights;

    private int numberOfItems;

    private int maxWeight;

    private int bestValue;

    public KnapTest(String filename) {
        testName = filename;
        loadTest(filename);
    }

    private void loadTest(String filename) {
        TestLoader testLoader = new TestLoader(filename);
        values = testLoader.loadValuesToArray();
        weights = testLoader.loadWeightsToArray();
        maxWeight = testLoader.loadMaxWeight();
        bestValue = testLoader.loadBestValue();
        numberOfItems = values.size();
    }

    public ArrayList<Integer> getValues() {
        return values;
    }

    public ArrayList<Integer> getWeights() {
        return weights;
    }

    public int getMaxWeight() {
        return maxWeight;
    }

    public int getBestValue() {
        return bestValue;
    }

    public String getTestName() {
        return testName;
    }

    public int getNumberOfItems() {
        return numberOfItems;
    }
}
