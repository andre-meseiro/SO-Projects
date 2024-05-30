#include "algorithm.h"

Test *baseAlgorithmAJKPA(char *filename, int testNumber, int secondsToExecute, int numberOfProcesses)
{
    // Create shared memory map
    int size = sizeof(Test);
    int protection = PROT_READ | PROT_WRITE;
    int visibility = MAP_ANONYMOUS | MAP_SHARED;
    void *shmem = mmap(NULL, size, protection, visibility, 0, 0);

    // Create semaphores
    sem_unlink("compare");
    sem_t *compare = sem_open("compare", O_CREAT, 0644, 1);

    for (int i = 0; i < numberOfProcesses; i++)
    {
        if (fork() == 0)
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
                test->processNumber = numberOfProcesses;
                memcpy(shmem, test, sizeof(Test));
                int iterations = 0;
                int *chosen = (int *)calloc(numberOfItems, sizeof(int));
                do
                {
                    iterations++;
                    generateChosenItems(numberOfItems, chosen);
                    int currentValue = calculateValues(values, chosen, numberOfItems);
                    int currentWeight = calculateWeights(weights, chosen, numberOfItems);
                    sem_wait(compare);
                    if (currentWeight <= maxWeight && currentValue > test->bestValue)
                    {
                        test->bestValue = currentValue;
                        gettimeofday(&tv2, NULL);
                        timersub(&tv2, &tv1, &tvresult);
                        test->numberOfIterations = iterations;
                        test->timeToValue = tvresult.tv_sec;
                        test->weightSum = currentWeight;
                        test->achievedBest = bestValuePossible == test->bestValue;
                        memcpy(shmem, test, sizeof(Test));
                    }
                    sem_post(compare);
                    gettimeofday(&tv2, NULL);
                    timersub(&tv2, &tv1, &tvresult);
                } while (secondsToExecute > tvresult.tv_sec);
                free(chosen);
            }
            exit(0);
        }
    }
    for (int i = 0; i < numberOfProcesses; i++)
    {
        wait(NULL);
    }
    sem_close(compare);
    Test *test = createTest();
    memcpy(test, shmem, sizeof(Test));
    return test;
}