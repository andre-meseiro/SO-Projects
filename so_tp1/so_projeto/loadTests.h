/**
 * @file loadTests.h
 * @author André Meseiro 202100225, Pedro Anjos 202100230
 * @brief Simple file loader to load tests
 * @version 0.1
 * @date 2022-11-30
 *
 * @copyright Copyright (c) 2022
 *
 */

#pragma once

#include <stdio.h>
#include <string.h>
#include <stdlib.h>

#define LOADING_OK 0
#define LOADING_FAILED 1

/**
 * @brief Loads a test file to an array of weights and values, and passes the values of variables in the file
 *
 * @param fileName Test file to load
 * @param maxWeight Variable to store the max weight
 * @param numberOfItems Variable to store the number of items
 * @param bestValue Variable to store the best value
 * @param weightArray Variable to store the array of weights
 * @param valueArray Variable to store the array of values
 * @return int
 */
int loadFromFileToArray(char *fileName, int *maxWeight, int *numberOfItems, int *bestValue, int **weightArray, int **valueArray);