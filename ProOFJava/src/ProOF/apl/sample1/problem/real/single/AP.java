/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ProOF.apl.sample1.problem.real.single;

import ProOF.gen.codification.FunctionSingle.RealSingle;

/**
 * <pre>
 * function Aluffi-Pentini's Problem (AP) (Aluffi-Pentini et al., 1985)   
 * name   : AP(x[2])     
 * domine : [-10, +10]     
 * optimal: AP(1.0465, 0) = -0.3523 
 * author : marcio 
 * source : M. Ali, C. Khompatraporn, Z. Zabinsky, 2005, A Numerical Evaluation of Several Stochastic Algorithms on Selected Continuous Global Optimization Test Problems, Jornal of Global Optimization, pp. 635-672.
 * </pre>
 */
public class AP extends RealSingle{
    @Override
    public String name() {
        return "AP";
    }
    @Override
    public int size() throws Exception {
        return 2;
    }
    @Override
    public double F(double[] X) throws Exception {
        double x1 = decode(X[0], -10, +10);
        double x2 = decode(X[1], -10, +10);
        return 0.25*Math.pow(x1, 4) - 0.5*Math.pow(x1, 2) + 0.1*x1 + 0.5*Math.pow(x2, 2);
    }
}
