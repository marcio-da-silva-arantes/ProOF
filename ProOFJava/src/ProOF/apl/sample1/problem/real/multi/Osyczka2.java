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
 * name   : Osyczka2(x[6])
 * </pre>
 * @author marcio
 */
public class Osyczka2 extends RealMulti{
    private static final double LB[] = {0, 0, 1, 0, 1, 0};
    private static final double UB[] = {10, 10, 5, 6, 5, 10};
    @Override
    public String name() {
        return "Osyczka2";
    }
    @Override
    public int size() throws Exception {
        return 6;
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
        return - ( 25*(y[0]-2)*(y[0]-2) + (y[1]-2)*(y[1]-2) + (y[2]-1)*(y[2]-1)*(y[3]-4)*(y[3]-4) + (y[5]-1)*(y[5]-1));
    }
    private double f2(double... y) {
        return y[0]*y[0] + y[1]*y[1] + y[2]*y[2] + y[3]*y[3] + y[4]*y[4] + y[5]*y[5];
    }
    @Override
    protected double G(double[] X) throws Exception {
        double y[] = decode(X, LB, UB);
        double violation = 0;
        violation += LE(0, y[0]+y[1]-2);
        violation += LE(0, 6-y[0]-y[1]);
        violation += LE(0, 2+y[0]-y[1]);
        violation += LE(0, 2-y[0]+3*y[2]);
        violation += LE(0, 4-(y[2]-3)*(y[2]-3)-y[3]);
        violation += LE(0, (y[4]-3)*(y[4]-3)*(y[4]-3)+y[5]-4);
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
