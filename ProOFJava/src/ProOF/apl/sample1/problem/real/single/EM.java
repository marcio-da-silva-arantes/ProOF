/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ProOF.apl.sample1.problem.real.single;

import ProOF.com.Linker.LinkerParameters;
import ProOF.gen.codification.FunctionSingle.RealSingle;

/**
 * <pre>
 * function Epistatic Michalewicz Problem (EM) (second ICEO)
 * name   : EM(x[n])
 * domine : [0, +pi]
 * optimal: EM(2.693, 0.259, 2.074, 1.023, 1.720) = -4.687658                                       for n=5
 * optimal: EM(2.693, 0.259, 2.074, 1.023, 2.275, 0.500, 2.138, 0.794, 2.219, 0.533) = -9.660152    for n=10
 * author : marcio
 * source : M. Ali, C. Khompatraporn, Z. Zabinsky, 2005, A Numerical Evaluation of Several Stochastic Algorithms on Selected Continuous Global Optimization Test Problems, Jornal of Global Optimization, pp. 635-672.
 * </pre>
 */
public class EM extends RealSingle{
    private static final double cosO = Math.cos(Math.PI/6);
    private static final double sinO = Math.sin(Math.PI/6);
    
    private int n;
    @Override
    public String name() {
        return "EM";
    }
    @Override
    public void parameters(LinkerParameters link) throws Exception {
        super.parameters(link);
        n = link.Int("EM-n", 5, 1, 1073741824, "default n=5 or 10");
    }
    @Override
    public int size() throws Exception {
        return n;
    }
    @Override
    public double F(double[] X) throws Exception {
        double sum = 0;
        for(int i=0; i<X.length; i++){
            double xi = decode(X[i], 0, +Math.PI);
            double yi;
            if(i+1==n){
                yi = xi;
            }else if(i % 2 == 0){
                double xj = decode(X[i+1], 0, +Math.PI);
                yi = xi*cosO - xj*sinO;
            }else{
                double xj = decode(X[i+1], 0, +Math.PI);
                yi = xi*sinO + xj*cosO;
            }
            sum += Math.sin(yi)*Math.pow(Math.sin((i+1)*yi*yi/Math.PI), 20);
        }
        return -sum;
    }
}
