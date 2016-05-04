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
import java.util.TreeSet;

/**
 *
 * @author marcio
 */
public class TSP2 extends TSP{
    //protected final TreeSet<TSPObjective> memory = new TreeSet<TSPObjective>();
    private double memory[] = new double[100000];
    private int size = 0;
    
   
    
    @Override
    public String name() {
        return "TSP2";
    }
//    @Override
//    public Codification build_codif() throws Exception {
//        return new cTSP(this);
//    }
    @Override
    public Objective build_obj() throws Exception {
        return new TSPObjective2();
    }
//    @Override
//    public void services(LinkerApproaches link) throws Exception {
//        super.services(link);
//        link.add(inst);
//        link.add(TSPOperator.obj);
//    }
//    @Override
//    public BestSol best() {
//        return BestSol.object();
//    }
//    @Override
//    public void start() throws Exception {
//        add_gap("gap", inst.optimal);
//    }
}
