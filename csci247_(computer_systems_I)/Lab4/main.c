#include <stdio.h>
#include <math.h>

#define SIGN 0x80000000
#define EXPONENT 0x7F800000
#define MANTISSA 0x007FFFFF


typedef unsigned float_bits;
int floatClass(float_bits);
float_bits float_negate(float_bits f);
float_bits float_absval(float_bits f);
float_bits float_twice(float_bits f);
float_bits float_half(float_bits f);

int main(int argc, char* argv[]) {
    float_bits value;
    printf("Enter float value: \n");
    scanf("%d", &value);

    //printf("%d\n", floatClass(value));
    //printf("%d\n", float_negate(value));
    //printf("%d\n", float_absval(value));
    //printf("%d\n", float_twice(value));
    //printf("%d\n", float_half(value));
}

int floatClass(float_bits f) {

    if (f == 0.0) {
        return 0;
    }
    else if (f & EXPONENT) {
        return 1;
    }
    else if (f != f) { // intrinsic property of NaN
        return 2;
    }
    else if (f & EXPONENT || !(f ^ EXPONENT)) {
        return 3;
    }
    else {
        return 4;
    }
}

float_bits float_negate(float_bits f) {
    f = ~f + 1;
    return f;
}

float_bits float_absval(float_bits f) {
    if (f & SIGN) {
        return float_negate(f);
    }
    else {
        return f;
    }
}

float_bits float_twice(float_bits f) {
    return f << 1;
}

float_bits float_half(float_bits f) {
    return f >> 1;
}
