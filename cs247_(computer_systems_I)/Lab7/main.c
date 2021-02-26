/*
 * Method 1:
 *
 * 64:
 * L1-icache-loads: 285595
 * L1-icache-load-misses: 15267
 * L1-dcache-loads: 8591274
 * L1-dcache-load-misses: 279069
 *
 * 128:
 * L1-icache-loads: 387309
 * L1-icache-load-misses: 16214
 * L1-dcache-loads: 8873723
 * L1-dcache-load-misses: 279086
 *
 * 256:
 * L1-icache-loads: 755489
 * L1-icache-load-misses: 17200
 * L1-dcache-loads: 10103019
 * L1-dcache-load-misses: 315077
 *
 * 512:
 * L1-icache-loads: 2265406
 * L1-icache-load-misses: 28204
 * L1-dcache-loads: 14865851
 * L1-dcache-load-misses: 582951
 *
 * The bigger the number the longer the cpu would take to process (understandably so). As for the loads and catches, it is clear
 * that there are many more dcache loads/load-misses
 *
 * Method 2:
 *  64:
 * L1-icache-loads: 289253
 * L1-icache-load-misses: 15605
 * L1-dcache-loads: 8598785
 * L1-dcache-load-misses: 277956
 *
 * 128:
 * L1-icache-loads: 383998
 * L1-icache-load-misses: 15467
 * L1-dcache-loads: 8895310
 * L1-dcache-load-misses: 274817
 *
 * 256:
 * L1-icache-loads: 759221
 * L1-icache-load-misses: 18791
 * L1-dcache-loads: 10072533
 * L1-dcache-load-misses: 279717
 *
 * 512:
 * L1-icache-loads: 2241045
 * L1-icache-load-misses: 28672
 * L1-dcache-loads: 15008730
 * L1-dcache-load-misses: 310453
 *
 * The results from method 2 are very similar to those from method 1. This leads me to believe that either I did the algorithm
 * wrong, or traversing through a 2D array by rows x columns rather than by columns x rows does not cause a significant change
 * to the cache. I did notice, however, that there were many more context switches with the dcache than the icache. This makes
 * sense because the dcache has so many more loads than the icache, so the dcache is much more likely to run into a conext switch
 * simply because it is so much faster.
 */

#include <stdio.h>
#include <stdlib.h>

int main(int argc, char* argv[]) {

    if (atoi(argv[1]) <= 0) {
        printf("Invalid input: <matrix size> <computation method>\n");
        exit(0);
    }

    int matrixSize = atoi(argv[1]);
    int computationMethod = atoi(argv[2]);

    int** arr = malloc(matrixSize * sizeof(int*));

    for (int i = 0; i < matrixSize; i++) {
        arr[i] = malloc(matrixSize * sizeof(int*));
    }

    for (int i = 0; i < matrixSize; i++){
        for (int j = 0; j < matrixSize; j++) {
            arr[i][j] = i + j;
        }
    }

    long sum = 0;

    if (computationMethod == 1) {
        for (int r = 0; r < matrixSize; r++) {
            for (int c = 0; c < matrixSize; c++){
                sum += arr[c][r]; // rows first
            }
        }
    }
    else if (computationMethod == 2) {
        for (int r = 0; r < matrixSize; r++) {
            for (int c = 0; c < matrixSize; c++){
                sum += arr[r][c]; // columns first
            }
        }
    }
    else {
        printf("Invalid computation method.\n");
        exit(0);
    }

    printf("sum: %ld\n", sum);
}

