/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ProOF.apl.advanced1.FMS.tabu;

import ProOF.com.Linker.LinkerParameters;
import ProOF.com.Linker.LinkerResults;
import ProOF.com.Linker.LinkerApproaches;
import ProOF.com.Linker.LinkerValidations;
import ProOF.com.language.Approach;
import ProOF.gen.operator.oLocalMove;

/**
 *
 * @author marcio
 */
public abstract class Tabu extends Approach{
    
    public abstract oLocalMove select() throws Exception;

    public abstract void update(oLocalMove best_mov) throws Exception;

    @Override
    public String description() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public void services(LinkerApproaches link) throws Exception {
        
    }
    @Override
    public void parameters(LinkerParameters win) throws Exception {
        
    }
    @Override
    public boolean validation(LinkerValidations com) throws Exception {
        return true;
    }
    @Override
    public void load() throws Exception {
        
    }
    @Override
    public void start() throws Exception {
        
    }
    @Override
    public void results(LinkerResults win) throws Exception {
        
    }
}
