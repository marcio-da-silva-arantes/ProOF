package ProOF.apl.factorys;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */




import ProOF.apl.sample1.problem.TSP.TSP;
import ProOF.com.language.Factory;
import ProOF.gen.codification.FunctionSingle.SingleBinRealFunction;
import ProOF.gen.codification.FunctionSingle.SingleRealFunction;
import ProOF.gen.codification.FunctionMulti.MultiBinRealFunction;
import ProOF.gen.codification.FunctionMulti.MultiRealFunction;
import ProOF.opt.abst.problem.meta.Problem;

/**
 *
 * @author marcio
 */
public final class fProblem extends Factory<Problem>{
    public static final fProblem obj = new fProblem();
    
    @Override
    public String name() {
        return "Problem";
    }
    
    @Override
    public Problem build(int index) throws Exception {
        switch(index){
            case 0: return new TSP();
            case 1: return new SingleRealFunction   (fRealSingle.obj,   fRealOperator.obj);
            case 2: return new SingleBinRealFunction(fRealSingle.obj,   fBinRealOperator.obj);
            case 3: return new MultiRealFunction    (fRealMulti.obj,    fRealOperator.obj);
            case 4: return new MultiBinRealFunction (fRealMulti.obj,    fBinRealOperator.obj);
        }
        return null;
    }
}
