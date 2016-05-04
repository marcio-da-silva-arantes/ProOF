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
public class RouletteLinear extends Roulette{
    private double Max;
    private RouletteLinear(Problem problem, int nInds, double Max) throws Exception {
        super(problem, nInds);
        this.Max = Max;
    }
    public RouletteLinear() {
        super();
    }
    
    @Override
    public String name() {
        return "Roulette-Linear";
    }
    @Override
    public String description() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    @Override
    public void parameters(LinkerParameters com) throws Exception {
        Max = com.Dbl("Max",   2,      1,      1000);
    }
    @Override
    public boolean validation(LinkerValidations com) throws Exception {
        return Max<inds.length;
    }
    @Override
    public double weigth(int i) {   //1 ... R
        return 1 + i*(Max-1)/(size()-1);
    }

    @Override
    public Structure Clone(Problem problem, int nInds) throws Exception {
        return new RouletteLinear(problem, nInds, Max);
    }
}
