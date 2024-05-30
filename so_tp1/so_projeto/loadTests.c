#include "loadTests.h"

int loadFromFileToArray(char *fileName, int *maxWeight, int *numberOfItems, int *bestValue, int **weightArray, int **valueArray)
{
    char *folder = "knap_tests/";
    char *filePath = (char *)malloc(strlen(folder) + strlen(fileName));
    strcpy(filePath, folder);
    strcat(filePath, fileName);

    FILE *stream = fopen(filePath, "r");

    /* If file does not exist, abort */
    if (stream == NULL)
    {
        free(filePath);
        return LOADING_FAILED;
    }
    else
    {
        char line[100];
        fgets(line, 100, stream);
        int countItems = atoi(line);
        *numberOfItems = countItems;
        fgets(line, 100, stream);
        int weight = atoi(line);
        *maxWeight = weight;

        int *weights = (int *)calloc(sizeof(int), countItems);
        int *values = (int *)calloc(sizeof(int), countItems);

        for (int i = 0; i < countItems; i++)
        {
            fgets(line, 100, stream);
            char *temp = strtok(line, " ");
            values[i] = atoi(temp);
            temp = strtok(NULL, " ");
            weights[i] = atoi(temp);
        }

        fgets(line, 100, stream);
        *bestValue = atoi(line);

        *valueArray = values;
        *weightArray = weights;
        fclose(stream);
        free(filePath);
        return LOADING_OK;
    }
}