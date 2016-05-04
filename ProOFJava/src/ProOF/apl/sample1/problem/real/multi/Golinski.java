/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ProOF.apl.sample1.problem.real.multi;

import ProOF.gen.codification.FunctionMulti.RealMulti;
import ProOF.opt.abst.problem.meta.objective.BoundDbl;

/**
 * <pre>
 * function with 2 objectives
 * name   : Golinski(x[7])
 * </pre>
 * @author marcio
 */
public class Golinski extends RealMulti{
    private static final double LB[] = {2.6, 0.7, 17, 7.3, 7.3, 2.9, 5.0};
    private static final double UB[] = {3.6, 0.8, 28, 8.3, 7.3, 3.9, 5.5};
    @Override
    public String name() {
        return "Golinski";
    }
    @Override
    public int size() throws Exception {
        return 7;
    }
    @Override
    public int goals() throws Exception {
        return 2;
    }
    @Override
    public double F(int goal, double[] X) throws Exception {
        double y[] = decode(X, LB, UB);
        switch(goal){
            case 0: return f1(y);
            case 1: return f2(y);
        }
        throw new Exception("index of goal = "+goal+" not exists");
    }
    private double f1(double... y) {
        return 0.7854*y[0]*y[1]*y[1]*(10*y[2]*y[2]/3 + 14.933*y[2] - 43.0934) - 1.508*y[0]*(y[5]*y[5] + y[6]*y[6]) + 7.477*(y[5]*y[5]*y[5] + y[6]*y[6]*y[6]) + 0.7854*(y[3]*y[5]*y[5]+y[4]*y[6]*y[6]);
    }
    private double f2(double... y) {
        return Math.sqrt((745*y[3]/(y[1]*y[2]))*(745*y[3]/(y[1]*y[2])) + 16900000)/(0.1*y[5]*y[5]*y[5]);
    }
    @Override
    protected double G(double[] X) throws Exception {
        double y[] = decode(X, LB, UB);
        double violation = 0;
        violation += LE(1.0/(y[0]*y[1]*y[1]*y[2]), 1.0/27);
        //violation += LE(1.0/(y[0]*y[1]*y[1]*y[2]), 1.0/27);
        violation += LE(y[3]*y[3]*y[3]/(y[1]*y[2]*y[2]*Math.pow(y[5],4)), 1.0/1.93);
        violation += LE(y[4]*y[4]*y[4]/(y[1]*y[2]*Math.pow(y[6],4)), 1.0/1.93);
        violation += LE(y[1]*y[2], 40);
        violation += LE(y[0]/y[1], 12);
        violation += LE(5, y[0]/y[1]);
        violation += LE(1.9+1.5*y[5], y[3]);
        violation += LE(1.9+1.1*y[6], y[4]);
        violation += LE(f2(y), 1300);
        violation += LE(Math.sqrt((745*y[4]/(y[1]*y[2]))*(745*y[4]/(y[1]*y[2])) + 157500000)/(0.1*Math.pow(y[6], 3)), 1100);
        return violation;
    }
    @Override
    public BoundDbl[] bounds() throws Exception {
        BoundDbl[] bounds = new BoundDbl[goals()];
        for(int i=0; i<goals(); i++){
            bounds[i] = new BoundDbl(-1e4, 1e9, 1e-3);
        }
        return bounds;
    }
}
