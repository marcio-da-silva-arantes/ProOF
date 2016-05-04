/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ProOF.apl.sample1.problem.TSP;

import ProOF.com.Linker.LinkerApproaches;
import ProOF.com.Linker.LinkerParameters;
import ProOF.com.Linker.LinkerResults;
import ProOF.gen.best.BestSol;
import ProOF.opt.abst.problem.meta.Objective;
import ProOF.opt.abst.problem.meta.Problem;
import ProOF.opt.abst.problem.meta.codification.Codification;

/**
 *
 * @author marcio
 */
public class TSP extends Problem<BestSol>{
    public final TSPInstance inst = new TSPInstance();
    
    
    @Override
    public String name() {
        return "TSP";
    }
    @Override
    public Codification build_codif() throws Exception {
        return new cTSP(this);
    }
    @Override
    public Objective build_obj() throws Exception {
        return new TSPObjective();
    }
    @Override
    public void services(LinkerApproaches link) throws Exception {
        super.services(link);
        link.add(inst);
        link.add(TSPOperator.obj);
    }
    @Override
    public BestSol best() {
        return BestSol.object();
    }
    @Override
    public void start() throws Exception {
        add_gap("gap", inst.optimal);
    }
}
