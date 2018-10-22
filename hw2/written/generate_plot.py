import numpy as np
import matplotlib
matplotlib.use('TkAgg')
import matplotlib.pyplot as plt
from sympy.solvers import solve
from sympy import Symbol




def main():
    def f1(x1):
        return 8 - 4 * x1


    def f2(x1):
        return 3 - 0.5 * x1
    x = Symbol('x')
    x1, = solve(f1(x) - f2(x))

    y1 = f1(x1)


    plt.fill([0,0, x1,   2], [0,3,y1,   0], 'red', alpha=0.5)

    xr = np.linspace(0, 6, 100)
    y1r = f1(xr)
    y2r = f2(xr)


    plt.plot(xr,y1r,'k--')
    plt.plot(xr,y2r,'k--')

    plt.xlim(0,6)
    plt.ylim(0,8)

    plt.xlabel(r"$x_{1}$")
    plt.ylabel(r"$x_{2}$")

    plt.show()

def main2():

    def f1(x1):
        return 2-4*x1

    def f2(x1):
        return 3-0.5*x1
    plt.rc('text', usetex=True)
    plt.rc('font', family='serif')

    plt.fill([6,0,0,30],[0,3,30,0], 'red', alpha=0.5)

    xr=np.linspace(0,6,100)
    y1r=f1(xr)
    y2r=f2(xr)

    plt.plot(xr,y1r,'k--')
    plt.plot(xr,y2r,'k--')

    plt.xlim(0,6)
    plt.ylim(0,3)

    plt.xlabel(r"$x_{1}$")
    plt.ylabel(r"$x_{2}$")

    plt.show()

if __name__ == '__main__':
    main()