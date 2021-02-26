import numpy as np
import scipy as sc
from scipy import optimize
import matplotlib.pyplot as plt
import random

#returns a random c vector
def randN(n):
    return np.random.uniform(low=-100, high=100, size=(n,))

#returns a random set of constraints and b vector
def randA(m,n):
    A = np.random.uniform(low=-100, high=100, size=(m,n))
    b = np.random.uniform(low=0, high=10, size=(m,))
    return [A,b]

#f,g are lists. axis/title are strings
def plot(f,g,h,z,x_axis,y_axis,title):

    #plot graph
    plt.plot(f, g, label="bounded")
    plt.plot(h,z, label="unbounded")

    #name x axis
    plt.xlabel(x_axis)

    #name y axis
    plt.ylabel(y_axis)

    #name title
    plt.title(title)

    #show plot
    plt.legend()
    plt.show()

def randSimplex():
    # initialize empty array
    x = np.zeros(shape=(500, 4))

    # generate 500 random problems and solve
    # returns 500x4 array
    # index 0 = m, index 1 = n, index 2 = iterations,
    # index 3 is 0 if unbounded and 1 if a solution is found
    for i in range(0, 500):
        n = random.randint(1, 76)
        m = random.randint(1, 76)
        c = randN(n)
        [A, b] = randA(m, n)

        res = sc.optimize.linprog(-c, A_ub=A, b_ub=b, method='revised simplex')
        x[i, 0] = m
        x[i, 1] = n
        x[i, 2] = res.nit
        if (res.success):
            x[i, 3] = 1
        else:
            x[i, 3] = 0
    return x

#The remainder of this code is a lot of copy and pasting as to answer the questions.
#This allows me to get all the graphs in one compile

#Does the growth in m+n correspond to a growth in the number of iterations? If so, is this linear? Exponential?
def question1(x):
    # initialize empty lists for bounded/unbounded graphs
    x1 = ([])
    x2 = ([])
    g1 = ([])
    g2 = ([])

    # fill list (x-axis - m+n, y-axis - iterations)
    for i in range(0, 500):
        if x[i, 3] == 1:
            x1.append(x[i][0] + x[i][1])
            x2.append(x[i][2])
        else:
            g1.append(x[i][0] + x[i][1])
            g2.append(x[i][2])

    # sort lists for graphing
    x1.sort()
    x2.sort()
    g1.sort()
    g2.sort()

    plot(x1, x2, g1, g2, "size (m+n)", "Iterations", "Question 1")

#Does the growth in m lead to a growth in the number of iterations? If so, is this linear? Exponential?
def question2(x):
    # initialize empty lists for bounded/unbounded graphs
    x1 = ([])
    x2 = ([])
    g1 = ([])
    g2 = ([])

    # fill list (x-axis - m+n, y-axis - iterations)
    for i in range(0, 500):
        if x[i, 3] == 1:
            x1.append(x[i][0])
            x2.append(x[i][2])
        else:
            g1.append(x[i][0])
            g2.append(x[i][2])

    # sort lists for graphing
    x1.sort()
    x2.sort()
    g1.sort()
    g2.sort()

    plot(x1, x2, g1, g2, "size (m)", "Iterations", "Question 2")

#Question 4
def tall(x):
    # initialize empty lists for bounded/unbounded graphs
    x1 = ([])
    x2 = ([])
    g1 = ([])
    g2 = ([])

    # fill list (x-axis - m+n, y-axis - iterations)
    for i in range(0, 500):
        if x[i,0] < x[i,1]:
            if x[i, 3] == 1:
                x1.append(x[i][0] + x[i][1])
                x2.append(x[i][2])
            else:
                g1.append(x[i][0] + x[i][1])
                g2.append(x[i][2])

    # sort lists for graphing
    x1.sort()
    x2.sort()
    g1.sort()
    g2.sort()

    plot(x1, x2, g1, g2, "size (m+n)", "Iterations", "Tall Problems")

#Question 3 continued
def wide(x):
    # initialize empty lists for bounded/unbounded graphs
    x1 = ([])
    x2 = ([])
    g1 = ([])
    g2 = ([])

    # fill list (x-axis - m+n, y-axis - iterations)
    for i in range(0, 500):
        if x[i, 0] >  x[i, 1]:
            if x[i, 3] == 1:
                x1.append(x[i][0] + x[i][1])
                x2.append(x[i][2])
            else:
                g1.append(x[i][0] + x[i][1])
                g2.append(x[i][2])

    # sort lists for graphing
    x1.sort()
    x2.sort()
    g1.sort()
    g2.sort()

    plot(x1, x2, g1, g2, "size (m+n)", "Iterations", "Wide Problems")

if __name__ == "__main__":
    # x = randSimplex()
    # question1(x)
    # question2(x)
    # tall(x)
    # wide(x)

    c = np.array([1,1]).reshape(1,2)
    A = np.array([[2,1],[2,4],[1,3]])
    b = np.array([1,2,3]).reshape(3,1)

    cDual = np.array([1,2,3]).reshape(1,3)
    ADual = np.array([[2,2,1],[1,4,3]])
    bDual = np.array([1,1]).reshape(1,2)

    res = sc.optimize.linprog(-c, A_ub=A, b_ub=b, method='revised simplex')
    #res = sc.optimize.linprog(-cDual, A_ub=ADual, b_ub=bDual, method='revised simplex')
    x = res.x
    print(res)
    print(x)
