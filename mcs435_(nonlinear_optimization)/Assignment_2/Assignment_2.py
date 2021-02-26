import numpy as np

def randQuad(n):
    A = np.random.randn(n,n) #random matrix A
    B = A.T
    Q = np.matmul(B,A) # Symmetric positive definite matrix
    b = np.random.randn(n,1) #random vector b
    return [Q,b]

def myQuad(x, Q, b):
    r = 0.5*((x.T) @ Q @ x) - ((b.T) @ x) #value of gradient at x
    g = (Q @ x) - b # gradient vector at x
    return [r,g]

def myArmijo(x,p,a,r,f):
    c = pow(10, -4)
    #get value and grad for xk and xk+1
    [f_curr, df_curr] = f(x)
    [f_next,df_next] = f(x + a*p)
    #called f twice
    count = 2
    #while eq 2.2 is not satisfied...
    while (f_next > (f_curr + c*a*( (df_curr.T) @ p ))):
        #update a and fk+1
        a *= r
        [f_next, g_2] = f(x + a * p)
        count += 1
    return [a, count]

def mySteep(x,tol,f):
    #initialize grad(x_initial)
    [v,g] = f(x)
    p = -1*g
    x_next = x
    #called f once
    count = 1

    #while tolerance condition is not met
    while(np.linalg.norm(p) > tol):
        #get alpha for xk
        [a, c] = myArmijo(x_next, p, 5, 0.7, f)
        #update xk
        x_next = x_next + a*p
        #update p = -grad(xk+1)
        [v, p] = f(x_next)
        p *= -1
        count += 1
    #return f(x_optimal) and x_optimal
    [f_n, g_n] = f(x_next)
    return [x_next, f_n, count]





