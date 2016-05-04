/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ProOF.apl.sample1.problem.real.single;

import ProOF.com.Linker.LinkerParameters;
import ProOF.gen.codification.FunctionSingle.RealSingle;

/**
 * <pre>
 * function Exponential Problem (EXP) (Breiman and Cutler, 1993)
 * name   : EXP(x[n])     
 * domine : [-1, +1]     
 * optimal: EXP(0...0) = 1   for n=10
 * author : marcio         
 * source : M. Ali, C. Khompatraporn, Z. Zabinsky, 2005, A Numerical Evaluation of Several Stochastic Algorithms on Selected Continuous Global Optimization Test Problems, Jornal of Global Optimization, pp. 635-672.
 * </pre>
 */
public class EXP extends RealSingle{
    private int n;
    @Override
    public String name() {
        return "EXP";
    }
    @Override
    public void parameters(LinkerParameters link) throws Exception {
        super.parameters(link);
        n = link.Int("EXP-n", 2, 1, 1073741824, "default n=10");
    }
    @Override
    public int size() throws Exception {
        return n;
    }
    @Override
    public double F(double[] X) throws Exception {
        double sum = 0;
        for(double x : X){
            double xi = decode(x, -1, +1);
            sum += xi * xi;
        }
        return -( Math.exp(-0.5*sum) );
    }
}
