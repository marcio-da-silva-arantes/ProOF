/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ProOF.apl.advanced1.FMS.immune_system;

import ProOF.com.Linker.LinkerApproaches;
import ProOF.com.Linker.LinkerParameters;
import ProOF.com.Linker.LinkerResults;
import ProOF.com.Linker.LinkerValidations;
import ProOF.com.language.Approach;

/**
 * Artificial Immune System process
 * @author marcio
 */
public abstract class AIS extends Approach{
    @Override
    public String description() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    @Override
    public void services(LinkerApproaches link) throws Exception {
        
    }
    @Override
    public void parameters(LinkerParameters link) throws Exception {
        
    }
    @Override
    public void load() throws Exception {
        
    }
    @Override
    public void start() throws Exception {
        
    }
    @Override
    public boolean validation(LinkerValidations link) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    @Override
    public void results(LinkerResults link) throws Exception {
        
    }

    public abstract int n_antibodies();

    public abstract int n_clones(int index);

    public abstract int n_keep();
}
