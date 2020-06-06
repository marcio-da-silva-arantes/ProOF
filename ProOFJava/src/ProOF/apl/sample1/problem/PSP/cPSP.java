/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ProOF.apl.sample1.problem.PSP;


import ProOF.opt.abst.problem.meta.codification.Codification;
import java.io.FileNotFoundException;

/**
 *
 * @author dexter
 */
public class cPSP  extends Codification<PSP, cPSP>{

    
    /** values range from -1 to number of shifts.  
     * -1 means that physician is not available at the time.
    */
    public int schedule[][];
        
    public cPSP(PSP prob) throws FileNotFoundException 
    { 
        schedule = new int [prob.inst.getNOE()][prob.inst.getNOD()];
    }
    
    @Override
    public void copy(PSP prob, cPSP source) throws Exception {
        for(int i = 0; i<prob.inst.getNOE();i++)
        {
            for(int j=0;j<prob.inst.getNOD(); j++){
               this.schedule[i][j] = source.schedule[i][j];
            }
        }
    }
    
    @Override
    public cPSP build(PSP prob) throws Exception {
        return new cPSP(prob);
        
    }

}
