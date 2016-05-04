/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ProOF.apl.sample1.problem.real.single;

import ProOF.gen.codification.FunctionSingle.RealSingle;

/**
 * <pre>
 * function Goldstein and Price (GP) (Dixon and Szego, 1978)
 * name   : GP(x[2])     
 * domine : [-2, +2]     
 * optimal: DA(0, -1) = 3
 * author : marcio
 * source : M. Ali, C. Khompatraporn, Z. Zabinsky, 2005, A Numerical Evaluation of Several Stochastic Algorithms on Selected Continuous Global Optimization Test Problems, Jornal of Global Optimization, pp. 635-672.
 * </pre>
 */
public class GP extends RealSingle{
    @Override
    public String name() {
        return "GP";
    }
    @Override
    public int size() throws Exception {
        return 2;
    }
    @Override
    public double F(double[] X) throws Exception {
        double x1 = decode(X[0], -2, +2);
        double x2 = decode(X[1], -2, +2);
        return (1+Math.pow(x1+x2+1, 2)*(19-14*x1+3*x2*x2-14*x2+6*x1*x2+3*x2*x2))*(30+Math.pow(2*x1-3*x2, 2)*(18-32*x1+12*x2*x2+48*x2-36*x1*x2+27*x2*x2));
    }
}
