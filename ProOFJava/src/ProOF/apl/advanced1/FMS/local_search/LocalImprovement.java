/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ProOF.apl.advanced1.FMS.local_search;

import ProOF.com.Linker.LinkerParameters;
import ProOF.com.Linker.LinkerResults;
import ProOF.com.Linker.LinkerApproaches;
import ProOF.com.Linker.LinkerValidations;
import ProOF.com.language.Approach;
import ProOF.opt.abst.problem.meta.Problem;
import ProOF.opt.abst.problem.meta.Solution;

/**
 *
 * @author marcio
 */
public abstract class LocalImprovement extends Approach{
    public abstract void execute(Problem problem, Solution best) throws Exception;
    
    @Override
    public String description() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    @Override
    public void services(LinkerApproaches com) throws Exception {
        
    }
    @Override
    public void parameters(LinkerParameters link) throws Exception {
        
    }
    @Override
    public boolean validation(LinkerValidations com) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    @Override
    public void load() throws Exception {
        
    }

    @Override
    public void start() throws Exception {
        
    }

    @Override
    public void results(LinkerResults com) throws Exception {
        
    }
}
