/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ProOF.apl.advanced1.FMS.temperature;

import ProOF.com.Linker.LinkerParameters;
import ProOF.com.Linker.LinkerResults;
import ProOF.com.Linker.LinkerApproaches;
import ProOF.com.Linker.LinkerValidations;
import ProOF.com.language.Approach;

/**
 *
 * @author marcio
 */
public abstract class Temperature extends Approach{
    public abstract double decress();
    public abstract double temperature();
    public abstract boolean end();
    
    @Override
    public void services(LinkerApproaches win) throws Exception {
        
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
    public void results(LinkerResults win) throws Exception {
        
    }
}
