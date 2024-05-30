#include "algorithm.h"

int i, nProcess;
int *pids;
void *shmem;
Test *test;
sem_t *update, *wr;

void signal_handler1(int signal)
{
    for (int i = 0; i < nProcess; i++)
    {
        kill(pids[i], SIGUSR2);
    }
    sem_post(update);
}

void signal_handler2(int signal)
{
    memcpy(test, shmem, sizeof(Test));
}

Test *advancedAlgorithmAJKPA(char *filename, int testNumber, int secondsToExecute, int numberOfProcesses)
{
    // Create shared memory map
    int size = sizeof(Test);
    int protection = PROT_READ | PROT_WRITE;
    int visibility = MAP_ANONYMOUS | MAP_SHARED;
    shmem = mmap(NULL, size, protection, visibility, 0, 0);

    // Create semaphores
    sem_unlink("write");
    sem_t *write = sem_open("write", O_CREAT, 0644, 1);
    sem_unlink("update");
    update = sem_open("update", O_CREAT, 0644, 0);
    signal(SIGUSR1, signal_handler1);
    signal(SIGUSR2, signal_handler2);

    // Allocate memory to array of pids
    pids = (int *)calloc(numberOfProcesses, sizeof(int));
    nProcess = 0;

    for (i = 0; i < numberOfProcesses; i++)
    {
        pids[i] = fork();
        nProcess++;
        if (pids[i] == 0)
        {
            int iterations = 0, numberOfItems, maxWeight, bestValuePossible;
            int *weights, *values;
            if (loadFromFileToArray(filename, &maxWeight, &numberOfItems, &bestValuePossible, &weights, &values) == LOADING_OK)
            {
                // Initiates the timer and the RNG
                struct timeval tv1, tv2, tvresult;
                tvresult.tv_sec = (long)0;
                gettimeofday(&tv1, NULL);
                test = createTest();
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

                    sem_wait(write);
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
                        kill(getppid(), SIGUSR1);
                        sem_wait(update);
                    }
                    sem_post(write);
                    gettimeofday(&tv2, NULL);
                    timersub(&tv2, &tv1, &tvresult);
                } while (true);
                free(chosen);
            }
        }
    }
    struct timeval tv1, tv2, tvresult;
    tvresult.tv_sec = (long)0;
    gettimeofday(&tv1, NULL);
    while (tvresult.tv_sec < secondsToExecute)
    {
        gettimeofday(&tv2, NULL);
        timersub(&tv2, &tv1, &tvresult);
    }
    for (int i = 0; i < numberOfProcesses; i++)
    {
        kill(pids[i], SIGKILL);
    }
    sem_close(write);
    sem_close(update);
    Test *test = createTest();
    memcpy(test, shmem, sizeof(Test));
    free(pids);
    return test;
}