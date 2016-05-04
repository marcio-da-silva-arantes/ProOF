/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ProOF.apl.sample1.problem.real.single;

import ProOF.gen.codification.FunctionSingle.RealSingle;

/**
 * <pre>
 * function Bohachevsky 1 Problem (BF1) (Bohachevsky et al., 1986)
 * name   : BF1(x[2])     
 * domine : [-50, +50]     
 * optimal: BF1(0, 0) = 0 
 * author : marcio
 * source : M. Ali, C. Khompatraporn, Z. Zabinsky, 2005, A Numerical Evaluation of Several Stochastic Algorithms on Selected Continuous Global Optimization Test Problems, Jornal of Global Optimization, pp. 635-672.
 * </pre>
 */
public class BF1 extends RealSingle{
    @Override
    public String name() {
        return "BF1";
    }
    @Override
    public int size() throws Exception {
        return 2;
    }
    @Override
    public double F(double[] X) throws Exception {
        double x1 = decode(X[0], -50, +50);
        double x2 = decode(X[1], -50, +50);
        return Math.pow(x1, 2) + 2*Math.pow(x2, 2) - 0.3*Math.cos(3*Math.PI*x1) - 0.4*Math.cos(4*Math.PI*x2) + 0.7;
    }
}
