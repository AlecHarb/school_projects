import numpy as np

def randQuad(n):
    A = np.random.randn(n,n) #random matrix A
    B = A.T
    Q = np.matmul(B,A) # Symmetric positive definite matrix
    b = np.random.randn(n,1) #random vector b
    return [Q,b]

def myQuad(x, Q, b):
    r = ((x.T) @ Q @ x) - ((b.T) @ x) #value of gradient at x
    g = (Q @ x) - b # gradient vector at x
    return [r,g]

def myPerm(x):
    n = x.size
    r = 0
    for i in range(1, n+1):
        inner = 0
        for j in range(1, n+1):
            inner += (j + 10) * ((pow(x[j-1], i) - (1/pow((j), i)))) #value of perm function at x[i-1] (since index starts at 1)
        r += pow(inner, 2)

    # I understand that my current implementation of computing the gradient is incorrect
    g = np.zeros(n)
    for i in range(1, n+1):
        lhs = 0
        rhs = 0
        for j in range(1, n+1):
            lhs += (j + 10) * ((pow(x[j-1], i) - (1/pow((j), i)))) #first part of the chain rule
            rhs += (j+10) * (i * pow(x[j-1], i-1))  #second part of the chain rule
        g[i-1] = 2 * lhs * rhs
    return [r, g]