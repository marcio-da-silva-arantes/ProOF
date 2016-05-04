/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ProOF.apl.sample1.problem.real.single;

import ProOF.com.Linker.LinkerParameters;
import ProOF.gen.codification.FunctionSingle.RealSingle;

/**
 * <pre>
 * function Ackley's Problem (ACK) (Storn and Price, 1997)    
 * name   : ACK(x[n])     
 * domain : [-30, +30]     
 * optimal: ACK(0...0) = 0 for n=10 
 * author : marcio     
 * source : M. Ali, C. Khompatraporn, Z. Zabinsky, 2005, A Numerical Evaluation of Several Stochastic Algorithms on Selected Continuous Global Optimization Test Problems, Jornal of Global Optimization, pp. 635-672.
 * </pre>
 */
public class ACK extends RealSingle{
    private int n;
    @Override
    public String name() {
        return "ACK";
    }
    @Override
    public void parameters(LinkerParameters link) throws Exception {
        super.parameters(link);
        n = link.Int("ACK-n", 10, 1, 1073741824, "default n=10");
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
            double xi = decode(x, -30, +30);
            sum1 += xi * xi;
            sum2 += Math.cos(2*Math.PI*xi);
        }
        return -20.0*Math.exp(-0.02*Math.sqrt(sum1/X.length))
                - Math.exp(sum2/X.length) + 20 + Math.E;
    }
}
