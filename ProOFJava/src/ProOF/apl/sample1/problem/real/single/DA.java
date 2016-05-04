/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ProOF.apl.sample1.problem.real.single;

import ProOF.gen.codification.FunctionSingle.RealSingle;

/**
 * <pre>
 * function Dekkers and Aarts Problem (DA) (Dekkers and Aarts, 1991)
 * name   : DA(x[2])     
 * domine : [-20, +20]     
 * optimal: DA(0, +15) = -24777
 * optimal: DA(0, -15) = -24777
 * author : marcio
 * source : M. Ali, C. Khompatraporn, Z. Zabinsky, 2005, A Numerical Evaluation of Several Stochastic Algorithms on Selected Continuous Global Optimization Test Problems, Jornal of Global Optimization, pp. 635-672.
 * </pre>
 */
public class DA extends RealSingle{
    @Override
    public String name() {
        return "DA";
    }
    @Override
    public int size() throws Exception {
        return 2;
    }
    @Override
    public double F(double[] X) throws Exception {
        double x1 = decode(X[0], -20, +20);
        double x2 = decode(X[1], -20, +20);
        return 1e5*Math.pow(x1, 2) + Math.pow(x2, 2) - Math.pow(Math.pow(x1, 2)+Math.pow(x2, 2), 2) + 1e-5*Math.pow(Math.pow(x1, 2)+Math.pow(x2, 2), 4);
    }
}
