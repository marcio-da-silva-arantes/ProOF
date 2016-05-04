/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ProOF.apl.sample1.problem.real.single;

import ProOF.gen.codification.FunctionSingle.RealSingle;

/**
 * <pre>
 * function Easom Problem (EP) (Michalewicz, 1996)
 * name   : EP(x[2])     
 * domine : [-10, +10]     
 * optimal: EP(pi, pi) = -1
 * author : marcio
 * source : M. Ali, C. Khompatraporn, Z. Zabinsky, 2005, A Numerical Evaluation of Several Stochastic Algorithms on Selected Continuous Global Optimization Test Problems, Jornal of Global Optimization, pp. 635-672.
 * </pre>
 */
public class EP extends RealSingle{
    @Override
    public String name() {
        return "EP";
    }
    @Override
    public int size() throws Exception {
        return 2;
    }
    @Override
    public double F(double[] X) throws Exception {
        double x1 = decode(X[0], -10, +10);
        double x2 = decode(X[1], -10, +10);
        return -Math.cos(x1)*Math.cos(x2)*Math.exp(-Math.pow(x1-Math.PI, 2)-Math.pow(x2-Math.PI, 2));
    }
}
