#include <stdio.h>
#include <stdbool.h>

#define BUFFSIZE 4096

char* itoa(int num, char* str, int base);

int main(int argc, char *argv[]) {
    int number, base;

    if (argc != 3) {
        printf("Usage: ./Lab2 <number> <base>\n");
        return 1;
    }

    int res = sscanf(argv[1], "%d", &number);
    if (!res || res == EOF) {
        printf("Could not parse %s\n", argv[1]);
        return 1;
    }

    res = sscanf(argv[2], "%d", &base);
    if (!res || res == EOF) {
        printf("Could not parse %s\n", argv[2]);
        return 1;
    } else if (!(2 <= base && base <= 36)) {
        printf("Base must be between 2 and 36\n");
        return 1;
    }

    char buf[BUFFSIZE];

    if (itoa(number, buf, base) != buf) {
        printf("Failed to convert %d to base %d\n", number, base);
        return 1;
    }

    printf("Here is %d converted to base %d:\n%s\n", number, base, buf);

    return 0;
}

char* itoa(int num, char* str, int base) {

    int i = 0;
    int lsd; // least significant digit
    int newNum;
    bool isNegative = false;

    if (num < 0) {
        isNegative = true;
        num *= -1;
    }

    for (int j = 0; j < 100; j++) {
        str[j] = 0;
    }

    if (num == 0) {
        str[i] = '0';
        return str;
    }
    /* else if (num < 0) {
        num *= -1;
        str[i] = 45; // first char in string is a negative sign
        i++; // increment index so number starts after the negative
    } */

    while (num > 0) {
        lsd = num % base; // least significat digit of base
        newNum = num / base;

        if (lsd < 10) {
            lsd += 48;
            str[i] = lsd; // assigning ascii character to least significant digit in 'str'
            i++;
            num = newNum;
        }
        else {
            lsd += 55;
            str[i] = lsd; // assigning ascii character to least significant digit in 'str'
            i++;
            num = newNum;
        }
    }

    int strLength = 0;
    int increment = 0;
    while (str[strLength] != 0) {
        strLength++; // one index higher than length of str array
    }
    strLength--; // setting str length to highest index of str array

    char reverseArr[strLength];

    for (int n = strLength; n >= 0; n--) { // creating reverse array of str
        reverseArr[increment] = str[n];
        increment++;
    }

    int space = 0;
    if (isNegative) {
        str[0] = '-';
        space += 1;
    }

    for (int c = 0; c <= strLength; c++) { // copying the reverse array to str
        str[space] = reverseArr[c];
        space++;
    }
    return str;
}
