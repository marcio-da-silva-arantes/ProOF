/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ProOF.apl.factorys;


import ProOF.com.language.Factory;
import ProOF.gen.codification.Binary.iBinCrossOX;
import ProOF.gen.codification.Binary.iBinInitRandom;
import ProOF.gen.codification.Binary.iBinMovInvert;
import ProOF.gen.codification.Binary.iBinMutInvert;
import ProOF.opt.abst.problem.meta.codification.Operator;


/**
 *
 * @author marcio
 */
public class fBinaryOperator extends Factory<Operator>{
    public static final fBinaryOperator obj = new fBinaryOperator();

    @Override
    public String name() {
        return "Codif-Binary Operators";
    }
    
    @Override
    public Operator build(int index) {
        switch(index){
            case  0: return new iBinInitRandom();
            case  1: return new iBinCrossOX();
            case  2: return new iBinMutInvert();
            case  3: return new iBinMovInvert();    
        }
        return null;
    }
}
