package knaptests;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class TestLoader {


    private static final String KNAP_TESTS_ROOT = "knap_tests_extended/";
    private static final int LINE_OF_NUMBER_OF_ITEMS = 0;
    private static final int LINE_OF_MAX_WEIGHT = 1;

    private static final int LINE_OF_ITEMS_START = 2;

    private final ArrayList<String> testLines;

    public TestLoader(String fileName) {
        testLines = loadTestLinesToArray(fileName);
    }

    private ArrayList<String> loadTestLinesToArray(String filename) {
        try {
            ArrayList<String> data = new ArrayList<>();
            File file = new File(KNAP_TESTS_ROOT + filename);
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                data.add(line);
            }
            bufferedReader.close();
            return data;
        } catch (IOException exception) {
            throw new RuntimeException();
        }
    }

    public ArrayList<Integer> loadWeightsToArray() {
        return getPropertyFromItems("weights");
    }

    public int loadMaxWeight() {
        return Integer.parseInt(testLines.get(LINE_OF_MAX_WEIGHT));
    }

    public int loadBestValue() {
        int lineOfBestValue = LINE_OF_ITEMS_START + Integer.parseInt(testLines.get(LINE_OF_NUMBER_OF_ITEMS));
        return Integer.parseInt(testLines.get(lineOfBestValue));
    }

    public ArrayList<Integer> loadValuesToArray() {
        return getPropertyFromItems("values");
    }

    private ArrayList<Integer> getPropertyFromItems(String property) {
        int numberOfItems = Integer.parseInt(testLines.get(LINE_OF_NUMBER_OF_ITEMS));
        int propertyIndex = (property.equals("weights")) ? 1 : 0;
        ArrayList<Integer> list = new ArrayList<>();
        for (int i = LINE_OF_ITEMS_START; i < LINE_OF_ITEMS_START + numberOfItems; i++) {
            String item = testLines.get(i);
            String[] split = item.split(" ");
            list.add(Integer.parseInt(split[propertyIndex]));
        }
        return list;
    }
}
