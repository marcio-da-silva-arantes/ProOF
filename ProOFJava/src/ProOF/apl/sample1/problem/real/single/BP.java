/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ProOF.apl.sample1.problem.real.single;

import ProOF.gen.codification.FunctionSingle.RealSingle;

/**
 * <pre>
 * function Branin Problem (BP) (Dixon and Szego , 1978)
 * name   : BP(x[2])     
 * domine : x1 = [-5, +10] and x2 = [0, +15]     
 * optimal: BF2(-pi, 12.275) = 5/(4pi)
 * optimal: BF2(+pi,  2.275) = 5/(4pi)
 * optimal: BF2(3pi,  2.475) = 5/(4pi)
 * author : marcio
 * source : M. Ali, C. Khompatraporn, Z. Zabinsky, 2005, A Numerical Evaluation of Several Stochastic Algorithms on Selected Continuous Global Optimization Test Problems, Jornal of Global Optimization, pp. 635-672.
 * </pre>
 */
public class BP extends RealSingle{
    private final static double a = 1;
    private final static double b = 5.1/(4*Math.PI*Math.PI);
    private final static double c = 5.0/(Math.PI);
    private final static double d = 6;
    private final static double g = 10;
    private final static double h = 1.0/(8*Math.PI);
    
    @Override
    public String name() {
        return "BP";
    }
    @Override
    public int size() throws Exception {
        return 2;
    }
    @Override
    public double F(double[] X) throws Exception {
        double x1 = decode(X[0], -5, +10);
        double x2 = decode(X[1],  0, +15);
        return a*Math.pow( x2 - b*x1*x1 + c*x1 - d ,2) + g*(1-h)*Math.cos(x1) + g;
    }
}
