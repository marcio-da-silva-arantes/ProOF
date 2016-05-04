/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ProOF.apl.sample1.problem.real.single;

import ProOF.gen.codification.FunctionSingle.RealSingle;

/**
 * function Bohachevsky 2
 * name   : B2(x,y)
 * domine : [-50, +50]
 * optimal: B2(0,0) = 0
 * author : marcio
 * source : M. Ali, C. Khompatraporn, Z. Zabinsky, 2005, A Numerical Evaluation of Several Stochastic Algorithms on Selected Continuous Global Optimization Test Problems, Jornal of Global Optimization, pp. 635-672.
 */
public class B2 extends RealSingle{
    @Override
    public String name() {
        return "B2";
    }
    @Override
    public int size() throws Exception {
        return 2;
    }
    @Override
    public double F(double[] X) throws Exception {
        double x = decode(X[0], -50, +50);
        double y = decode(X[1], -50, +50);
        return x*x + 2*y*y - 0.3*Math.cos(3*Math.PI*x) - 0.4*Math.cos(4*Math.PI*y) + 0.7;
    }

    
    
}