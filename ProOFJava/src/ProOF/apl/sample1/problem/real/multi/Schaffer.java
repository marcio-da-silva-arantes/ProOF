/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ProOF.apl.sample1.problem.real.multi;

import ProOF.gen.codification.FunctionMulti.RealMulti;

/**<pre>
 * function with 2 objectives
 * name   : Schaffer(x[1])
 * domine : [-1000, +1000]
 * </pre>
 * @author marcio
 */
public class Schaffer extends RealMulti{
    @Override
    public String name() {
        return "Schaffer";
    }
    @Override
    public int size() throws Exception {
        return 1;
    }
    @Override
    public int goals() throws Exception {
        return 2;
    }
    @Override
    public double F(int goal, double[] X) throws Exception {
        double x = decode(X[0], -1e3, +1e3);
        switch(goal){
            case 0: return f1(x);
            case 1: return f2(x);
        }
        throw new Exception("index of goal = "+goal+" not exists");
    }
    private double f1(double x) {
        return x*x;
    }
    private double f2(double x) {
        return (x-2)*(x-2);
    }
}
