/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ProOF.apl.advanced1.FMS.population;

import ProOF.com.Linker.LinkerResults;
import ProOF.com.Linker.LinkerApproaches;
import ProOF.com.Linker.LinkerValidations;
import ProOF.com.language.Approach;
import ProOF.opt.abst.problem.meta.Problem;
import ProOF.opt.abst.problem.meta.Solution;
import ProOF.com.Linker.LinkerParameters;
import ProOF.gen.operator.Crossover;
import ProOF.gen.operator.Initialization;
import ProOF.gen.operator.Mutation;

/**
 *
 * @author marcio
 */
public abstract class Population extends Approach{
    protected Initialization init;
    protected Crossover cross;
    protected Mutation mut;
    
    protected final int ID;
    protected final int MaxID;
    
    protected final Problem problem;
    
    protected Population(Problem problem, Population base, int ID, int MaxID) throws Exception {
        this.problem = problem;
        this.init = base.init;
        this.cross = base.cross;
        this.mut = base.mut;
        this.ID = ID;
        this.MaxID = MaxID;
    }
    public Population() {
        this.problem = null;
        this.init = null;
        this.cross = null;
        this.mut = null;
        this.ID = -1;
        this.MaxID = -1;
    }
    
    public abstract void evolution()throws Exception;
    public abstract void initialize() throws Exception;
    public abstract void evaluate() throws Exception;
    public abstract void reinitialize() throws Exception;
    public abstract Solution best() throws Exception;
    public abstract void migrate(Solution best) throws Exception;
    public abstract void organize() throws Exception;
    public abstract Population Clone(Problem problem, int nInds, int ID, int MaxID) throws Exception;


    @Override
    public void services(LinkerApproaches link) throws Exception {
        init    = link.add(Initialization.obj);
        cross   = link.add(Crossover.obj);
        mut     = link.add(Mutation.obj);
    }

    @Override
    public void parameters(LinkerParameters com) throws Exception {
        
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
