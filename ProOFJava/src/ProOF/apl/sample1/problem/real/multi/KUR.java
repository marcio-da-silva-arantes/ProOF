/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ProOF.apl.sample1.problem.real.multi;

import ProOF.gen.codification.FunctionMulti.RealMulti;
import ProOF.opt.abst.problem.meta.objective.BoundDbl;

/**<pre>
 * function with 2 objectives
 * name   : KUR(x[3])
 * domine : [-5, +5]
 * optimal: ?
 * </pre>
 * @author marcio
 */
public class KUR extends RealMulti{
    @Override
    public String name() {
        return "KUR";
    }
    @Override
    public int size() throws Exception {
        return 3;
    }
    @Override
    public int goals() throws Exception {
        return 2;
    }
    @Override
    public double F(int goal, double[] X) throws Exception {
        switch(goal){
            case 0: return f1(X);
            case 1: return f2(X);
        }
        throw new Exception("index of goal = "+goal+" not exists");
    }
    private double f1(double... X) {
        double sum = 0;
        for(int i=0; i<X.length-1; i++){
            double xi = decode(X[i], -5, +5);
            double xi_1 = decode(X[i+1], -5, +5);

            sum += -10*Math.exp(0.2*Math.sqrt(xi*xi + xi_1*xi_1));
        }
        return sum;
    }
    private double f2(double... X) {
        double sum = 0;
        for(double x : X){
            x = decode(x, -5, +5);
            sum += Math.pow(Math.abs(x),0.8) + 5*Math.sin(Math.pow(x, 3));
        }
        return sum;
    }
    private static final BoundDbl bound = new BoundDbl(-1e+4, +1e+4, 1e-2);
    private static final BoundDbl bounds[] = new BoundDbl[]{bound, bound};
    @Override
    public BoundDbl[] bounds() throws Exception {
        return bounds;
    }
}
