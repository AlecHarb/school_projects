import numpy as np
import scipy as sc

#returns a random c vector
def randC(n):
    return np.random.uniform(low=-100, high=100, size=(n,))

#returns a random set of constraints and b vector
def randA(m,n):
    A = np.random.uniform(low=-100, high=100, size=(m,n))
    b = np.random.uniform(low=0, high=10, size=(m,))
    return [A,b]

#sum all entries in column vector
def sumVec(s):
    n = s.size
    return n

def sumMultipy(x,y):
    sum = 0
    for i in range(0, x.size):
        sum += x[i] * y[i]
    return sum


def myOptCheck(c,A,b,x):

    #check if proposed solution is feasible
    g = b - (A@x)
    print(g)
    for i in range(0, g.size):
        if (g[i] < 0 and abs(g[i]) > pow(10,-6)):
            print(pow(10,-6))
            print(g[i])
            return -1

    #define objective for dual as the negative of the b's
    obj = -c

    #define dual constraints
    D = A.T

    #define


if __name__ == "__main__":
    n = 4
    m = 3

    RandA = randA(m,n)
    RandC = randC(n)

    A = np.array([[-5, 4, 2], [3, 2, -1], [-2, 1, 0], [-5, 2, 0]])
    c = np.array([4, 1, 2]).reshape(m, 1)
    b = np.array([9,3,5,8]).reshape(n,1)
    x = np.array([15,0,42]).reshape(m,1)

    #print(np.linalg.solve(A,b))
    b_s = A@x
    #print(b - b_s)

    l = myOptCheck(c, A, b, x)
    print(l)
