#include <stdbool.h>
#include <string.h>

#include "loadTests.h"
#include "algorithm.h"
#include "test.h"

int main(int argc, char *argv[])
{
    if (argc != 5)
    {
        printf("Usage: #teste indice filename nprocess time\n"); // /kp 1 ex05.txt 10 60
    }
    else
    {
        // Filename
        char fileName[50];
        strcpy(fileName, argv[2]);

        // Test number
        char testNum[10];
        strcpy(testNum, argv[1]);

        // Amount of seconds to execute
        char secondsToExecute[10];
        strcpy(secondsToExecute, argv[4]);

        // Number of process to create
        char numOfProcesses[10];
        strcpy(numOfProcesses, argv[3]);

        // Calculates the best value
        srand(time(NULL));
        // Default algorithm
        //Test *test = algorithmAJKPA(fileName, atoi(testNum), atoi(secondsToExecute));

        // Base competitive algorithm
        //Test *test = baseAlgorithmAJKPA(fileName, atoi(testNum), atoi(secondsToExecute), atoi(numOfProcesses));

        // Advanced competitive algorithm
        Test *test = advancedAlgorithmAJKPA(fileName, atoi(testNum), atoi(secondsToExecute), atoi(numOfProcesses));
        printTest(test);
        free(test);
    }
    return 0;
}