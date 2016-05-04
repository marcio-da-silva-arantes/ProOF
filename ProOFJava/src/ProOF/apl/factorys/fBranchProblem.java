/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ProOF.apl.factorys;

import ProOF.apl.sample1.FMS.branch.BranchProblem;
import ProOF.apl.sample1.problem.bTSP.bTSP;
import ProOF.com.language.Factory;


/**
 *
 * @author marcio
 */
public final class fBranchProblem extends Factory<BranchProblem>{
    public static final fBranchProblem obj = new fBranchProblem(); 
    @Override
    public String name() {
        return "fBranchProblem";
    }
    @Override
    public BranchProblem build(int index) {
        switch(index){
            case 0: return new bTSP();
        }
        return null;
    }
}
