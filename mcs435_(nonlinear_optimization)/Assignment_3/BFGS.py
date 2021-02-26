import numpy as np
import scipy as sc

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

def myQNewt(x, tol, f, H):
    x_next = x
    [v_curr,g_curr] = f(x)

    while(np.linalg.norm(g_curr) > tol):

        p = -1*H @ g_curr

        #update x and grad f(x)
        x_curr = x_next
        [v_curr, g_curr] = f(x_curr)
        [a, c] = myArmijo(x_curr, -g_curr, 5, 0.7, f)

        x_next = x_next + (a * p)
        [v_next, g_next] = f(x_next)

        print("x_curr:", x_curr)
        print("x_next:", x_next)

        #def delta x and dela grad f(x)
        deltaX = x_next - x_curr
        deltaG = g_next - g_curr

        print("delX:", deltaX)
        print("delG", deltaG)

        term1 = (1 + ((deltaG.T @ H @ deltaG)/(deltaG.T @ deltaX))) * ((deltaX @ deltaX.T)/(deltaX.T @ deltaG))
        term2 = ((H @ deltaG @ deltaX.T) + (H @ deltaG @ deltaX.T).T) / (deltaG.T @ deltaX)

        #print("t1",term1)
        #print("t2",term2)
        #print("sumBull", ((H @ deltaG @ deltaX.T) + H @ deltaG @ deltaX.T).T / (deltaG.T @ deltaX))

        H = H + term1 - term2

        print(H)


if __name__ == "__main__":
    Q = np.array([[1,0,0],[0,1,0],[0,0,1]])
    b = np.array([1,1,1])
    x = np.array([0.1, 0.1, 0.1])

    f=lambda x:myQuad(x,Q,b)


    tol = pow(10, -4)


    myQNewt(x, tol, f, Q)


