/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ProOF.apl.sample1.problem.NSP;

import ProOF.opt.abst.problem.meta.codification.Codification;

/**
 *
 * @author dexter
 */
public class cNSP  extends Codification<NSP, cNSP>{

    
    /** values range from -1 to number of shifts.  
     * -1 means that physician is not available at the time.
    */
    public int schedule[][];
    
        
    public cNSP(NSP prob) 
    {   
        schedule = new int [prob.inst.getNOE()][prob.inst.getNOD()];
    }
    
    @Override
    public void copy(NSP prob, cNSP source) throws Exception {
        for(int i = 0; i<prob.inst.getNOE();i++)
        {
            for(int j=0;j<prob.inst.getNOD(); j++){
                System.arraycopy(source.schedule[i], 0, this.schedule[i], 0, this.schedule[i].length);
            }
        }
    }
    
    @Override
    public cNSP build(NSP prob) throws Exception {
        return new cNSP(prob);
        
    }

}
