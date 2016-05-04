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
 * name   : Srinivas(x[2])
 * domine : [-20, +20]
 * </pre>
 * @author marcio
 */
public class Srinivas extends RealMulti{
    @Override
    public String name() {
        return "Srinivas";
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
        double y[] = decode(X, -20, 20);
        switch(goal){
            case 0: return f1(y);
            case 1: return f2(y);
        }
        throw new Exception("index of goal = "+goal+" not exists");
    }
    private double f1(double... y) {
        return (y[0]-2)*(y[0]-2) + (y[1]-1)*(y[1]-1) + 2;
    }
    private double f2(double... y) {
        return 9*y[0] - (y[1]-1)*(y[1]-1);
    }
    @Override
    protected double G(double[] X) throws Exception {
        double y[] = decode(X, -20, 20);
        double violation = 0;
        violation += LE(y[0]*y[0]+y[1]*y[1], 225);
        violation += LE(y[0]-3*y[1], -10);
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
