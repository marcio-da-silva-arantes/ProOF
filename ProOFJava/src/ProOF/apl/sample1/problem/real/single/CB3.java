/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ProOF.apl.sample1.problem.real.single;

import ProOF.gen.codification.FunctionSingle.RealSingle;

/**
 * <pre>
 * function Camel Back – 3 Three Hump Problem (CB3) (Dixon and Szego , 1975)
 * name   : CB3(x[2])     
 * domine : [-5, +5]     
 * optimal: CB3(0, 0) = 0
 * author : marcio
 * source : M. Ali, C. Khompatraporn, Z. Zabinsky, 2005, A Numerical Evaluation of Several Stochastic Algorithms on Selected Continuous Global Optimization Test Problems, Jornal of Global Optimization, pp. 635-672.   
 * </pre>
 */
public class CB3 extends RealSingle{
    @Override
    public String name() {
        return "CB3";
    }
    @Override
    public int size() throws Exception {
        return 2;
    }
    @Override
    public double F(double[] X) throws Exception {
        double x1 = decode(X[0], -5, +5);
        double x2 = decode(X[1], -5, +5);
        return 2*Math.pow(x1, 2) - 1.05*Math.pow(x1, 4) + Math.pow(x1, 6)/6 + x1*x2 + Math.pow(x2, 2);
    }
}
