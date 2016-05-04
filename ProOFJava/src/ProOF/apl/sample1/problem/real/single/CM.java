/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ProOF.apl.sample1.problem.real.single;

import ProOF.com.Linker.LinkerParameters;
import ProOF.gen.codification.FunctionSingle.RealSingle;

/**
 * <pre>
 * function Cosine Mixture Problem (CM) (Breiman and Cutler, 1993)
 * name   : CM(x[n])     
 * domine : [-1, +1]     
 * optimal: CM(0...0) = -0.2   for n=2
 * optimal: CM(0...0) = -0.4   for n=4
 * author : marcio         
 * source : M. Ali, C. Khompatraporn, Z. Zabinsky, 2005, A Numerical Evaluation of Several Stochastic Algorithms on Selected Continuous Global Optimization Test Problems, Jornal of Global Optimization, pp. 635-672.
 * </pre>
 */
public class CM extends RealSingle{
    private int n;
    @Override
    public String name() {
        return "CM";
    }
    @Override
    public void parameters(LinkerParameters link) throws Exception {
        super.parameters(link);
        n = link.Int("CM-n", 2, 1, 1073741824, "default n=2 or 4");
    }
    @Override
    public int size() throws Exception {
        return n;
    }
    @Override
    public double F(double[] X) throws Exception {
        double sum1 = 0;
        double sum2 = 0;
        for(double x : X){
            double xi = decode(x, -1, +1);
            sum1 += Math.cos(5*Math.PI*xi);
            sum2 += xi * xi;
        }
        return -( 0.1*sum1 + sum2 );
    }
}
