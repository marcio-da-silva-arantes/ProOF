/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ProOF.apl.sample1.problem.real.multi;

import ProOF.gen.codification.FunctionMulti.RealMulti;
import ProOF.opt.abst.problem.meta.objective.BoundDbl;

/**<pre>
 * function with 2 objectives
 * name   : POL(x[2])
 * domine : [-PI, +PI]
 * optimal: ?
 * </pre>
 * @author marcio
 */
public class POL extends RealMulti{
    @Override
    public String name() {
        return "POL";
    }
    @Override
    public int size() throws Exception {
        return 2;
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
        double x1 = decode(X[0], -Math.PI, +Math.PI);
        double x2 = decode(X[1], -Math.PI, +Math.PI);
        double B1 = B1(x1,x2);
        double B2 = B2(x1,x2);
        return 1 + Math.pow(A1-B1, 2) + Math.pow(A2-B2, 2);
    }
    private double f2(double... X) {
        double x1 = decode(X[0], -Math.PI, +Math.PI);
        double x2 = decode(X[1], -Math.PI, +Math.PI);
        return Math.pow(x1+3, 2) + Math.pow(x2+1, 2);
    }
    private static final double A1 = B1(1,2);
    private static final double A2 = B2(1,2);
    private static final double B1(double x1, double x2){
        return 0.5*Math.sin(x1) - 2*Math.cos(x1) + Math.sin(x2) - 1.5*Math.cos(x2);
    }
    private static final double B2(double x1, double x2){
        return 1.5*Math.sin(x1) - Math.cos(x1) + 2*Math.sin(x2) - 0.5*Math.cos(x2);
    }
    
    
    private static final BoundDbl bound = new BoundDbl(-1e+8, +1e+8, 1e-6);
    private static final BoundDbl bounds[] = new BoundDbl[]{bound, bound};
    @Override
    public BoundDbl[] bounds() throws Exception {
        return bounds;
    }
}
