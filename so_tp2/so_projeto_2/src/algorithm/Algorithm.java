package algorithm;

import knaptests.KnapTest;

import java.util.ArrayList;
import java.util.Random;

public class Algorithm {
    private final ArrayList<Integer> values;
    private final ArrayList<Integer> weights;
    public final int ALFA;

    private final int maxWeight;

    private final int numberOfItems;

    private boolean running;

    private final Storage storage;

    public Algorithm(KnapTest knapTest) {
        values = new ArrayList<>(knapTest.getValues());
        weights = new ArrayList<>(knapTest.getWeights());
        maxWeight = knapTest.getMaxWeight();
        numberOfItems = values.size();
        ALFA = numberOfItems / 2;
        sortByValueWeightRatio();
        storage = new Storage();
        running = true;
    }

    public void stop() {
        running = false;
    }

    public int beamSearch(int alfa, int lowerBound) {
        ArrayList<ArrayList<Integer>> solutions = initialSolution();
        while (solutions.size() != 0 && running) {
            ArrayList<ArrayList<Integer>> possibleSolutions = getChilds(solutions);
            for (int i = 0; i < possibleSolutions.size(); i++) {
                int upperBound = upperBound(possibleSolutions.get(i));
                int currentWeight = computeWeights(possibleSolutions.get(i));
                if (upperBound >= lowerBound && currentWeight <= maxWeight) {
                    int solutionValue = computeValues(possibleSolutions.get(i));
                    if (solutionValue > lowerBound) {
                        lowerBound = solutionValue;
                        storage.lowerBoundValue = lowerBound;
                        storage.lowerBoundWeight = currentWeight;
                    }
                } else {
                    possibleSolutions.remove(i--);
                }
            }
            solutions = selectSolutions(alfa, possibleSolutions);
        }
        return lowerBound;
    }

    public int lowerBound() {
        int lowerBound = 0, currentWeight = 0;
        for (int i = 0; i < numberOfItems; i++) {
            currentWeight += weights.get(i);
            if (currentWeight < maxWeight) {
                lowerBound += values.get(i);
                storage.lowerBoundValue = lowerBound;
                storage.lowerBoundWeight = currentWeight;
            } else break;
        }
        return lowerBound;
    }

    public ArrayList<ArrayList<Integer>> selectSolutions(int alfa, ArrayList<ArrayList<Integer>> solutions) {
        Random rng = new Random();
        ArrayList<ArrayList<Integer>> selectedSolutions = new ArrayList<>();
        for (int i = 0; i < alfa; i++) {
            if (solutions.size() == 0) break;
            int randomIndex = rng.nextInt(solutions.size());
            selectedSolutions.add(solutions.remove(randomIndex));
        }
        return selectedSolutions;
    }

    public ArrayList<ArrayList<Integer>> getChilds(ArrayList<ArrayList<Integer>> solutions) {
        ArrayList<ArrayList<Integer>> childs = new ArrayList<>();
        for (ArrayList<Integer> solution : solutions) {
            int firstUndecidedIndex = computeFirstUndecidedIndex(solution);
            if (firstUndecidedIndex != -1) {
                ArrayList<Integer> solutionAdd = new ArrayList<>(solution);
                solutionAdd.set(firstUndecidedIndex, 1);
                childs.add(solutionAdd);
                ArrayList<Integer> solutionNotAdd = new ArrayList<>(solution);
                solutionNotAdd.set(firstUndecidedIndex, 0);
                childs.add(solutionNotAdd);
            }
        }
        return childs;
    }

    public ArrayList<ArrayList<Integer>> initialSolution() {
        ArrayList<ArrayList<Integer>> initialSolution = new ArrayList<>();
        ArrayList<Integer> root = new ArrayList<>();
        for (int i = 0; i < numberOfItems; i++) {
            root.add(-1);
        }
        initialSolution.add(root);
        return initialSolution;
    }

    public int upperBound(ArrayList<Integer> solution) {
        ArrayList<Integer> modifiedSolution = new ArrayList<>(solution);
        int firstUndecidedIndex = computeFirstUndecidedIndex(modifiedSolution);
        int resultAfter = 0, resultBefore = 0;
        if (firstUndecidedIndex != -1) {
            int exceedingIndex = computeExceedingIndex(modifiedSolution);
            if (exceedingIndex != -1) {
                int weightInserted = computeWeights(modifiedSolution, firstUndecidedIndex, exceedingIndex);
                int baseResult = computeValues(solution) + computeValues(modifiedSolution, firstUndecidedIndex, exceedingIndex);
                int weightOut = computeWeightOut(solution, weightInserted);
                double ratioAfter, ratioBefore;
                if (exceedingIndex < numberOfItems - 1)
                    ratioAfter = computeRatio(values.get(exceedingIndex + 1), weights.get(exceedingIndex + 1));
                else ratioAfter = 0;
                resultAfter = (int) (baseResult + Math.floor(weightOut * ratioAfter));
                ratioBefore = computeRatio(values.get(exceedingIndex - 1), weights.get(exceedingIndex - 1));
                resultBefore = (int) (baseResult + Math.floor(values.get(exceedingIndex) - (weights.get(exceedingIndex) - weightOut) * ratioBefore));
            }
        }
        return Math.max(resultAfter, resultBefore);
    }

    public int computeWeightOut(ArrayList<Integer> solution, int weightsOut) {
        int w = maxWeight;
        w -= weightsOut;
        w -= computeWeights(solution);
        return w;
    }

    public int computeExceedingIndex(ArrayList<Integer> solution) {
        int startingIndex = computeFirstUndecidedIndex(solution);
        int exceedingIndex = -1;
        if (startingIndex != -1) {
            for (int i = startingIndex; i < numberOfItems; i++) {
                solution.set(i, 1);
                int currentWeight = computeWeights(solution);
                if (currentWeight > maxWeight) {
                    exceedingIndex = i;
                    solution.set(i, -1);
                    break;
                }
            }
        }
        return exceedingIndex;
    }

    public int computeFirstUndecidedIndex(ArrayList<Integer> solution) {
        int index = 0;
        for (Integer integer : solution) {
            if (integer == -1) {
                return index;
            }
            index++;
        }
        return -1;
    }


    public int computeValues(ArrayList<Integer> solution, int firstIndex, int lastIndex) {
        ArrayList<Integer> temp = switchOutsideOfIntervalToUndecided(solution, firstIndex, lastIndex);
        return computeValues(temp);
    }

    public int computeValues(ArrayList<Integer> solution) {
        int currentValue = 0;
        for (int i = 0; i < solution.size(); i++) {
            if (solution.get(i) != -1) currentValue += solution.get(i) * values.get(i);
        }
        return currentValue;
    }

    public int computeWeights(ArrayList<Integer> solution, int firstIndex, int lastIndex) {
        ArrayList<Integer> temp = switchOutsideOfIntervalToUndecided(solution, firstIndex, lastIndex);
        return computeWeights(temp);
    }

    private ArrayList<Integer> switchOutsideOfIntervalToUndecided(ArrayList<Integer> solution, int firstIndex, int lastIndex) {
        ArrayList<Integer> temp = new ArrayList<>(solution);
        for (int i = 0; i < firstIndex; i++) {
            temp.set(i, -1);
        }
        for (int i = lastIndex; i < temp.size(); i++) {
            temp.set(i, -1);
        }
        return temp;
    }

    public int computeWeights(ArrayList<Integer> solution) {
        int currentWeight = 0;
        for (int i = 0; i < solution.size(); i++) {
            if (solution.get(i) != -1) currentWeight += solution.get(i) * weights.get(i);
        }
        return currentWeight;
    }


    public void sortByValueWeightRatio() {
        for (int i = 0; i < numberOfItems - 1; i++) {
            int indexMax = i;
            double maxRatio = computeRatio(values.get(i), weights.get(i));
            for (int j = i; j < numberOfItems; j++) {
                double currentRatio = computeRatio(values.get(j), weights.get(j));
                if (currentRatio > maxRatio) {
                    maxRatio = currentRatio;
                    indexMax = j;
                }
            }
            swap(values, indexMax, i);
            swap(weights, indexMax, i);
        }
    }

    private void swap(ArrayList<Integer> arrayList, int index1, int index2) {
        int temp = arrayList.get(index1);
        arrayList.set(index1, arrayList.get(index2));
        arrayList.set(index2, temp);
    }

    private double computeRatio(int value, int weight) {
        return ((double) value) / weight;
    }

    public ArrayList<Integer> getValues() {
        return values;
    }

    public ArrayList<Integer> getWeights() {
        return weights;
    }

    public int getStoredLowerBound() {
        return this.storage.lowerBoundValue;
    }

    public int getStoredLowerBoundWeight() {
        return this.storage.lowerBoundWeight;
    }

    private static class Storage {
        private int lowerBoundValue;
        private int lowerBoundWeight;

        public Storage() {
            lowerBoundValue = 0;
            lowerBoundWeight = 0;
        }
    }
}
