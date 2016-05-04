/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ProOF.apl.factorys;

import ProOF.apl.sample1.problem.real.single.ACK;
import ProOF.apl.sample1.problem.real.single.AP;
import ProOF.apl.sample1.problem.real.single.B2;
import ProOF.apl.sample1.problem.real.single.BF1;
import ProOF.apl.sample1.problem.real.single.BF2;
import ProOF.apl.sample1.problem.real.single.BL;
import ProOF.apl.sample1.problem.real.single.BP;
import ProOF.apl.sample1.problem.real.single.CB3;
import ProOF.apl.sample1.problem.real.single.CB6;
import ProOF.apl.sample1.problem.real.single.CM;
import ProOF.apl.sample1.problem.real.single.DA;
import ProOF.apl.sample1.problem.real.single.EM;
import ProOF.apl.sample1.problem.real.single.EP;
import ProOF.apl.sample1.problem.real.single.EXP;
import ProOF.apl.sample1.problem.real.single.GP;
import ProOF.com.language.Factory;
import ProOF.gen.codification.FunctionSingle.RealSingle;

/**
 *
 * @author marcio
 */
public final class fRealSingle extends Factory<RealSingle>{
    public static final fRealSingle obj = new fRealSingle(); 
    private fRealSingle(){}
    @Override
    public String name() {
        return "fRealSingle";
    }
    @Override
    public RealSingle build(int index) {
        switch(index){
            case 0: return new ACK();
            case 1: return new AP();
            case 2: return new B2();
            case 3: return new BF1();
            case 4: return new BF2();
            case 5: return new BL();
            case 6: return new BP();
            case 7: return new CB3();
            case 8: return new CB6();
            case 9: return new CM();
            case 10: return new DA();
            case 11: return new EM();
            case 12: return new EP();
            case 13: return new EXP();
            case 14: return new GP();
        }
        return null;
    }
}

