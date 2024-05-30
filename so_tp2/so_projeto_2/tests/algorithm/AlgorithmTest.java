package algorithm;

import knaptests.KnapTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AlgorithmTest {

    private ArrayList<Integer> testSolution;
    private Algorithm algorithm;
    private KnapTest knapTest;

    private final int NUMBER_OF_BEAM_TESTS_TO_EXECUTE = 5;

    @BeforeEach
    public void setUp() {
        knapTest = new KnapTest("ex08.txt");
        algorithm = new Algorithm(knapTest);
        testSolution = new ArrayList<>();
        testSolution.add(1);
        testSolution.add(0);
        testSolution.add(-1);
        testSolution.add(-1);
        testSolution.add(-1);
        testSolution.add(-1);
        testSolution.add(-1);
        testSolution.add(-1);
    }

    @Test
    public void test_ordered() {
        knapTest = new KnapTest("ex05.txt");
        algorithm = new Algorithm(knapTest);
        assertEquals("[33, 24, 36, 37, 12]", knapTest.getValues().toString());
        assertEquals("[15, 20, 17, 8, 31]", knapTest.getWeights().toString());
        assertEquals("[37, 33, 36, 24, 12]", algorithm.getValues().toString());
        assertEquals("[8, 15, 17, 20, 31]", algorithm.getWeights().toString());
    }

    @Test
    public void test_lower_bound() {
        assertEquals(265, algorithm.lowerBound());
    }

    @Test
    public void test_exceeding_index() {
        assertEquals(5, algorithm.computeExceedingIndex(testSolution));
    }

    @Test
    public void test_index_of_first_undecided() {
        assertEquals(2, algorithm.computeFirstUndecidedIndex(testSolution));
    }

    @Test
    public void test_upper_bound() {
        assertEquals(206, algorithm.upperBound(testSolution));
    }

    @Test
    public void test_get_childs() {
        ArrayList<ArrayList<Integer>> solutions = new ArrayList<>();
        solutions.add(testSolution);
        assertEquals("[[1, 0, 1, -1, -1, -1, -1, -1], [1, 0, 0, -1, -1, -1, -1, -1]]", algorithm.getChilds(solutions).toString());
    }

    @Test
    public void test_get_childs_when_all_defined() {
        ArrayList<ArrayList<Integer>> solutions = new ArrayList<>();
        testSolution = new ArrayList<>();
        testSolution.add(1);
        testSolution.add(0);
        testSolution.add(0);
        testSolution.add(0);
        testSolution.add(0);
        testSolution.add(0);
        testSolution.add(1);
        testSolution.add(0);
        solutions.add(testSolution);
        assertEquals(0, algorithm.getChilds(solutions).size());
    }

    @Test
    public void test_select_solutions() {
        ArrayList<ArrayList<Integer>> solutions = new ArrayList<>();
        solutions.add(testSolution);
        solutions = algorithm.getChilds(solutions);
        solutions = algorithm.selectSolutions(1, solutions);
        assertTrue(solutions.toString().equals("[[1, 0, 1, -1, -1, -1, -1, -1]]") || solutions.toString().equals("[[1, 0, 0, -1, -1, -1, -1, -1]]"));
    }

    @Test
    public void test_beam_search_ex08() {
        int actualValue = 0;
        for (int i = 0; i < NUMBER_OF_BEAM_TESTS_TO_EXECUTE; i++) {
            int beamResult = algorithm.beamSearch(algorithm.ALFA, algorithm.lowerBound());
            if (beamResult > actualValue) actualValue = beamResult;
        }
        assertEquals(280, actualValue);
    }

    @Test
    public void test_beam_search_ex25() {
        algorithm = new Algorithm(new KnapTest("ex25.txt"));
        int actualValue = 0;
        for (int i = 0; i < NUMBER_OF_BEAM_TESTS_TO_EXECUTE; i++) {
            int beamResult = algorithm.beamSearch(algorithm.ALFA, algorithm.lowerBound());
            if (beamResult > actualValue) actualValue = beamResult;
        }
        assertEquals(4506, actualValue);
    }

    @Test
    public void test_beam_search_ex50_1() {
        algorithm = new Algorithm(new KnapTest("ex50_1.txt"));
        int actualValue = 0;
        for (int i = 0; i < NUMBER_OF_BEAM_TESTS_TO_EXECUTE; i++) {
            int beamResult = algorithm.beamSearch(algorithm.ALFA, algorithm.lowerBound());
            if (beamResult > actualValue) actualValue = beamResult;
        }
        assertEquals(8373, actualValue);
    }

    @Test
    public void test_beam_search_ex500_1() {
        algorithm = new Algorithm(new KnapTest("ex500_1.txt"));
        int actualValue = 0;
        for (int i = 0; i < NUMBER_OF_BEAM_TESTS_TO_EXECUTE; i++) {
            int beamResult = algorithm.beamSearch(algorithm.ALFA, algorithm.lowerBound());
            if (beamResult > actualValue) actualValue = beamResult;
        }
        assertEquals(28857, actualValue);
    }

    @Test
    public void test_storage() {
        assertEquals(0, algorithm.getStoredLowerBound());
        Algorithm otherAlgorithm = new Algorithm(new KnapTest("ex25.txt"));
        otherAlgorithm.beamSearch(otherAlgorithm.ALFA, otherAlgorithm.lowerBound());
        assertEquals(0, algorithm.getStoredLowerBound());
        assertEquals(4506, otherAlgorithm.getStoredLowerBound());
    }
}