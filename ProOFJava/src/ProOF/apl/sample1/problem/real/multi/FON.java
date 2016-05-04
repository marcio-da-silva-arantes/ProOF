/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ProOF.apl.sample1.problem.real.multi;

import ProOF.gen.codification.FunctionMulti.RealMulti;

/**
 * <pre>
 * function with 2 objectives
 * name   : FON(x[3])
 * domine : [-4, +4]
 * optimal: x1=x2=x3 in [-1/sqrt(3) ... +1/sqrt(3)]
 * </pre>
 * @author marcio
 */
public class FON extends RealMulti{
    @Override
    public String name() {
        return "FON";
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
        for(double x : X){
            x = decode(x, -4, +4);
            sum += (x-1/Math.sqrt(3))*(x-1/Math.sqrt(3));
        }
        return 1 - Math.exp(-sum);
    }
    private double f2(double... X) {
        double sum = 0;
        for(double x : X){
            x = decode(x, -4, +4);
            sum += (x+1/Math.sqrt(3))*(x+1/Math.sqrt(3));
        }
        return 1 - Math.exp(-sum);
    }
}
