#include "test.h"

Test *createTest()
{
    Test *test = (Test *)malloc(sizeof(Test));
    test->numberOfItems = 0;
    test->testName = "none";
    test->processNumber = 0;
    test->testNumber = 0;
    test->executionTime = 0;
    test->bestValue = 0;
    test->achievedBest = false;
    test->timeToValue = 0;
    test->numberOfIterations = 0;
    test->weightSum = 0;
    return test;
}

void printTest(Test *test)
{
    printf("%5d|%12s (%3d)|%14d|%10d|%10d|%12d|%13s|%11d|%18d\n", test->testNumber, test->testName, test->numberOfItems, test->executionTime, test->processNumber, test->bestValue, test->weightSum, (test->achievedBest) ? "Yes" : "No", test->numberOfIterations, test->timeToValue);
}