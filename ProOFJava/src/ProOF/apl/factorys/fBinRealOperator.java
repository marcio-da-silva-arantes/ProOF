/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ProOF.apl.factorys;


import ProOF.com.language.Factory;
import ProOF.gen.codification.BinaryReal.iBinRealCrossOX;
import ProOF.gen.codification.BinaryReal.iBinRealInitRandom;
import ProOF.gen.codification.BinaryReal.iBinRealMovInvert;
import ProOF.gen.codification.BinaryReal.iBinRealMutInvert;
import ProOF.opt.abst.problem.meta.codification.Operator;


/**
 *
 * @author marcio
 */
public class fBinRealOperator extends Factory<Operator>{
    public static final fBinRealOperator obj = new fBinRealOperator();

    @Override
    public String name() {
        return "Codif-Real Operators";
    }
    
    @Override
    public Operator build(int index) {
        switch(index){
            case  0: return new iBinRealInitRandom();
            case  1: return new iBinRealCrossOX();
            case  2: return new iBinRealMutInvert();
            case  3: return new iBinRealMovInvert();    
        }
        return null;
    }
}
