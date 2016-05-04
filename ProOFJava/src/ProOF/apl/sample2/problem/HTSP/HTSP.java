/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ProOF.apl.sample2.problem.HTSP;

import ProOF.CplexExtended.CplexExtended;
import ProOF.apl.sample1.problem.TSP.*;
import ProOF.apl.sample2.problem.cplex.TSPmodel;
import ProOF.com.Linker.LinkerApproaches;
import ProOF.gen.best.BestSol;
import ProOF.opt.abst.problem.meta.Objective;
import ProOF.opt.abst.problem.meta.Problem;
import ProOF.opt.abst.problem.meta.codification.Codification;
import ilog.concert.IloException;
import ilog.cplex.IloCplex;

/**
 *
 * @author marcio
 */
public class HTSP extends Problem<BestSol>{
    public final TSPInstance inst = new TSPInstance();
    
    public CplexExtended cpx;
    private TSPmodel model;
    
    @Override
    public String name() {
        return "HTSP";
    }
    @Override
    public Codification build_codif() throws Exception {
        return new HTSPCodification(this);
    }
    @Override
    public Objective build_obj() throws Exception {
        return new HTSPObjective();
    }
    @Override
    public void services(LinkerApproaches link) throws Exception {
        super.services(link);
        link.add(inst);
        link.add(HTSPOperator.obj);
    }
    @Override
    public BestSol best() {
        return BestSol.object();
    }
    @Override
    public void start() throws Exception {
        add_gap("gap", inst.optimal);
        
        cpx = new CplexExtended();
        cpx.setWarning(null);
        //cpx.setOut(null);
        cpx.setParam(IloCplex.IntParam.Threads, 1);
        //cpx.setParam(IloCplex.DoubleParam.TiLim, 10);
        cpx.setParam(IloCplex.LongParam.NodeLim, 100);
        cpx.setParam(IloCplex.DoubleParam.EpGap, 0.001);
        model = new TSPmodel(inst, cpx);
        model.model(false);
    }
    public void fix(HTSPCodification ind) throws IloException{
        //Free all variables
        for(int i=0; i<inst.N; i++){
            for(int j=0; j<inst.N; j++){
                if(i!=j){
                    model.Xij[i][j].setLB(0);
                    model.Xij[i][j].setUB(1);
                }
            }
        }
        //Fix the correct variables
        for (HTSPEdge s : ind.set) {
            int i = s.i;
            int j = s.j;
            model.Xij[i][j].setLB(1);
        }
    }
    public double solve() throws IloException{
        if(cpx.solve()){
            return cpx.getObjValue();
        }else{
            return 1e+6;
        }
    }
}
