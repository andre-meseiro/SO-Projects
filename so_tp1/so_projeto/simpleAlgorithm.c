#include "algorithm.h"

Test *algorithmAJKPA(char *filename, int testNumber, int secondsToExecute)
{
    int iterations = 0, numberOfItems, maxWeight, bestValuePossible;
    int *weights, *values;
    if (loadFromFileToArray(filename, &maxWeight, &numberOfItems, &bestValuePossible, &weights, &values) == LOADING_OK)
    {
        // Initiates the timer and the RNG
        struct timeval tv1, tv2, tvresult;
        tvresult.tv_sec = (long)0;
        gettimeofday(&tv1, NULL);
        Test *test = createTest();
        test->testNumber = testNumber;
        test->testName = filename;
        test->numberOfItems = numberOfItems;
        test->executionTime = secondsToExecute;
        test->processNumber = 1;
        int iterations = 0;
        int *chosen = (int *)calloc(numberOfItems, sizeof(int));
        do
        {
            iterations++;
            generateChosenItems(numberOfItems, chosen);
            int currentValue = calculateValues(values, chosen, numberOfItems);
            int currentWeight = calculateWeights(weights, chosen, numberOfItems);
            if (currentWeight <= maxWeight && currentValue > test->bestValue)
            {
                test->bestValue = currentValue;
                gettimeofday(&tv2, NULL);
                timersub(&tv2, &tv1, &tvresult);
                test->numberOfIterations = iterations;
                test->timeToValue = tvresult.tv_sec;
                test->weightSum = currentWeight;
                test->achievedBest = bestValuePossible == test->bestValue;
            }
            gettimeofday(&tv2, NULL);
            timersub(&tv2, &tv1, &tvresult);
        } while (secondsToExecute > tvresult.tv_sec);
        free(chosen);
        return test;
    }
}

void generateChosenItems(int maxNumber, int *array)
{
    int rng = rand() % (maxNumber);
    array[rng] = 1 - array[rng];
}

int calculateWeights(int *weights, int *weightsToConsider, int numberOfItems)
{
    int sum = 0;
    for (int i = 0; i < numberOfItems; i++)
    {
        sum += weights[i] * weightsToConsider[i];
    }
    return sum;
}

int calculateValues(int *values, int *valuesToConsider, int numberOfItems)
{
    int sum = 0;
    for (int i = 0; i < numberOfItems; i++)
    {
        sum += values[i] * valuesToConsider[i];
    }
    return sum;
}