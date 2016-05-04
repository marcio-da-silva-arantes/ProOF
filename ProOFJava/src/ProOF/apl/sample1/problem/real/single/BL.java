/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ProOF.apl.sample1.problem.real.single;

import ProOF.gen.codification.FunctionSingle.RealSingle;

/**
 * <pre>
 * function Becker and Lago Problem (BL) (Price, 1977)
 * name   : BL(x[2])     
 * domine : [-10, +10]     
 * optimal: BL(-5, -5) = 0
 * optimal: BL(-5, +5) = 0
 * optimal: BL(+5, -5) = 0
 * optimal: BL(+5, +5) = 0
 * author : marcio
 * source : M. Ali, C. Khompatraporn, Z. Zabinsky, 2005, A Numerical Evaluation of Several Stochastic Algorithms on Selected Continuous Global Optimization Test Problems, Jornal of Global Optimization, pp. 635-672.
 * </pre>
 */
public class BL extends RealSingle{
    @Override
    public String name() {
        return "BL";
    }
    @Override
    public int size() throws Exception {
        return 2;
    }
    @Override
    public double F(double[] X) throws Exception {
        double x1 = decode(X[0], -10, +10);
        double x2 = decode(X[1], -10, +10);
        return Math.pow(Math.abs(x1) - 5, 2) + Math.pow(Math.abs(x2) - 5, 2);
    }
}
