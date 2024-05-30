/**
 * @file algorithm.h
 * @author André Meseiro 202100225, Pedro Anjos 202100230
 * @brief Provides algorithms to solve the knapsack problem
 *
 * Provides a simple algorithm, algorithmAJKPA, and two competitive algorithms, baseAlgorithmAJKPA and advancedAlgorithmAJKPA
 *
 * @version 0.1
 * @date 2022-11-30
 *
 * @copyright Copyright (c) 2022
 *
 */
#pragma once

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdbool.h>
#include <semaphore.h>
#include <fcntl.h>
#include <signal.h>
#include <unistd.h>
#include <sys/time.h>
#include <sys/wait.h>
#include <sys/mman.h>
#include <time.h>

#include "loadTests.h"
#include "test.h"

#define SUCESSFUL 0
#define FAIL_TO_LOAD 1

/**
 * @brief Simple AJKPA algorithm
 *
 * @param filename File to load
 * @param testNumber Number of the test to excecute
 * @param secondsToExecute Amount of time to execute in seconds
 * @return Test* the test created
 */
Test *algorithmAJKPA(char *filename, int testNumber, int secondsToExecute);

/**
 * @brief Base competitive AJKPA algorithm
 *
 * @param filename File to load
 * @param testNumber Number of the test to excecute
 * @param secondsToExecute Amount of time to execute in seconds
 * @param numberOfProcesses Number of process to execute the algorithm
 * @return Test* the test created
 */
Test *baseAlgorithmAJKPA(char *filename, int testNumber, int secondsToExecute, int numberOfProcesses);

/**
 * @brief Advanced competitive AJKPA algorithm
 *
 * @param filename File to load
 * @param testNumber Number of the test to excecute
 * @param secondsToExecute Amount of time to execute in seconds
 * @param numberOfProcesses Number of process to execute the algorithm
 * @return Test* the test created
 */
Test *advancedAlgorithmAJKPA(char *filename, int testNumber, int secondsToExecute, int numberOfProcesses);

void generateChosenItems(int maxNumber, int *array);

/**
 * @brief Calculates the weights in a given bag
 *
 * @param weights array of weights
 * @param weightsToConsider array of weights to consider (1 to consider, 0 to discard)
 * @param numberOfItems number of items
 * @return int weight calculated
 */
int calculateWeights(int *weights, int *weightsToConsider, int numberOfItems);

/**
 * @brief Calculates the values in a given bag
 *
 * @param values array of values
 * @param valuesToConsider array of values to consider (1 to consider, 0 to discard)
 * @param numberOfItems number of items in the array
 * @return int value calculated
 */
int calculateValues(int *values, int *valuesToConsider, int numberOfItems);