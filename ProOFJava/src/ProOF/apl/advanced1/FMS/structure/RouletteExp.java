/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ProOF.apl.advanced1.FMS.structure;

import ProOF.com.Linker.LinkerParameters;
import ProOF.com.Linker.LinkerValidations;
import ProOF.opt.abst.problem.meta.Problem;

/**
 *
 * @author marcio
 */
public class RouletteExp extends Roulette{
    private double Max;
    private RouletteExp(Problem problem, int nInds, double Max) throws Exception {
        super(problem, nInds);
        this.Max = Max;
    }
    public RouletteExp() {
        super();
    }
    
    @Override
    public String name() {
        return "Roulette-Exp";
    }
    @Override
    public String description() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    @Override
    public void parameters(LinkerParameters com) throws Exception {
        Max  = com.Dbl("Max",    2,      1,      1000);
    }
    @Override
    public boolean validation(LinkerValidations com) throws Exception {
        return Max<inds.length;
    }
    @Override
    public double weigth(int i) {   //1 ... R
        double factor = Math.pow(Max, 1.0/(size()-1));
        return Math.pow(factor, i);
    }
    @Override
    public Structure Clone(Problem problem, int nInds) throws Exception {
        return new RouletteExp(problem, nInds, Max);
    }
}
