/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ProOF.apl.sample1.problem.real.single;

import ProOF.gen.codification.FunctionSingle.RealSingle;

/**
 * <pre>
 * function Camel Back – 6 Three Hump Problem (CB6) (Dixon and Szego , 1975)
 * name   : CB6(x[2])     
 * domine : [-5, +5]     
 * optimal: CB6(+0.089842, -0.712656) = -1.0316
 * optimal: CB6(-0.089842, +0.712656) = -1.0316
 * author : marcio
 * source : M. Ali, C. Khompatraporn, Z. Zabinsky, 2005, A Numerical Evaluation of Several Stochastic Algorithms on Selected Continuous Global Optimization Test Problems, Jornal of Global Optimization, pp. 635-672.      
 * </pre>
 */
public class CB6 extends RealSingle{
    @Override
    public String name() {
        return "CB6";
    }
    @Override
    public int size() throws Exception {
        return 2;
    }
    @Override
    public double F(double[] X) throws Exception {
        double x1 = decode(X[0], -5, +5);
        double x2 = decode(X[1], -5, +5);
        return 4*Math.pow(x1, 2) - 2.1*Math.pow(x1, 4) + Math.pow(x1, 6)/3 + x1*x2 - 4*Math.pow(x2, 2)+ 4*Math.pow(x2, 4);
    }
}
