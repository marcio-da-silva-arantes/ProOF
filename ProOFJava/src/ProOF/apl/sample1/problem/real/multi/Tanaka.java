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
 * name   : Tanaka(x[2])
 * domine : [-pi, +pi]
 * </pre>
 * @author marcio
 */
public class Tanaka extends RealMulti{
    @Override
    public String name() {
        return "Tanaka";
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
        double y[] = decode(X, -Math.PI, Math.PI);
        switch(goal){
            case 0: return f1(y);
            case 1: return f2(y);
        }
        throw new Exception("index of goal = "+goal+" not exists");
    }
    private double f1(double... y) {
        return y[0];
    }
    private double f2(double... y) {
        return y[1];
    }
    @Override
    protected double G(double[] X) throws Exception {
        double y[] = decode(X, -Math.PI, Math.PI);
        double violation = 0;
        violation += LE(-y[0]*y[0]-y[1]*y[1]+1+0.1*Math.cos(16*Math.atan2(y[0], y[1])), 0);
        violation += LE(+(y[0]-0.5)*(y[0]-0.5)+(y[1]-0.5)*(y[1]-0.5), 0.5);
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
