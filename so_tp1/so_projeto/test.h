/**
 * @file test.h
 * @author André Meseiro 202100225, Pedro Anjos 202100230
 * @brief Definition of a Test
 *
 *  Defines the type Test and associated operations.
 *
 * @version 0.1
 * @date 2022-11-30
 *
 * @copyright Copyright (c) 2022
 *
 */

#pragma once

#include <stdlib.h>
#include <stdbool.h>
#include <stdio.h>

/**
 * @brief Test Struct
 * 
 */
typedef struct test
{
    int testNumber;
    char *testName;
    int numberOfItems;
    int executionTime;
    int processNumber;
    int bestValue;
    int weightSum;
    int numberOfIterations;
    int timeToValue;
    bool achievedBest;
} Test;

/**
 * @brief Create a Test object
 *
 * @return Test* test created
 */
Test *createTest();

/**
 * @brief Prints a formated test
 *
 * @param test test to print
 */
void printTest(Test *test);
