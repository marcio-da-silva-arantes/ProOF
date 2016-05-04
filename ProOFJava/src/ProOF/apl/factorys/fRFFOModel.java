/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ProOF.apl.factorys;

import ProOF.apl.advanced2.problem.RFFO.GCIST_RFFO;
import ProOF.apl.advanced2.problem.RFFO.MLCLSPwB_RFFO;
import ProOF.apl.advanced2.FMS.RFFO.RFFOModel;
import ProOF.apl.sample2.problem.RFFO.TSP_RFFO;
import ProOF.com.language.Factory;

/**
 *
 * @author marcio
 */
public class fRFFOModel extends Factory<RFFOModel>{
    public static final fRFFOModel obj = new fRFFOModel();
    @Override
    public String name() {
        return "fRFFOModel";
    }
    @Override
    public RFFOModel build(int index) throws Exception {
        switch(index){
            case 0: return new TSP_RFFO();
            case 1: return new GCIST_RFFO();
            case 2: return new MLCLSPwB_RFFO();
        }
        return null;
    }
}
