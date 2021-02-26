#include <stdio.h>

int CountSetBits(unsigned int var);

int main(int argc, char* argv[]) {
    CountSetBits(5);
}

int CountSetBits(unsigned int var) {

    printf("Enter Integer: \n");
    scanf("%d", var);
}