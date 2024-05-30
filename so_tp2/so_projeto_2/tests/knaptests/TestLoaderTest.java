package knaptests;

import knaptests.TestLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TestLoaderTest {

    String fileName = "ex05.txt";

    TestLoader testLoader;

    @BeforeEach
    public void setUp() {
        testLoader = new TestLoader(fileName);
    }

    @Test
    public void test_load_weights_to_array() {
        ArrayList<Integer> weights = testLoader.loadWeightsToArray();
        assertEquals(5, weights.size());
    }

    @Test
    public void test_load_values_to_array() {
        ArrayList<Integer> values = testLoader.loadValuesToArray();
        assertEquals(5, values.size());
    }

    @Test
    public void test_value_weight_correct() {
        ArrayList<Integer> values = testLoader.loadValuesToArray();
        ArrayList<Integer> weights = testLoader.loadWeightsToArray();
        assertEquals("[33, 24, 36, 37, 12]", values.toString());
        assertEquals("[15, 20, 17, 8, 31]", weights.toString());
    }

    @Test
    public void test_load_max_weight() {
        assertEquals(80, testLoader.loadMaxWeight());
    }

    @Test
    public void test_load_best_value() {
        assertEquals(130, testLoader.loadBestValue());
    }
}