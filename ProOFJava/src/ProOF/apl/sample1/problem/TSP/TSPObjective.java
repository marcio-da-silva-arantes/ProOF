/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ProOF.apl.sample1.problem.TSP;

import ProOF.opt.abst.problem.meta.objective.SingleObjective;

/**
 *
 * @author marcio
 */
public class TSPObjective extends SingleObjective<TSP, cTSP, TSPObjective> {
    public TSPObjective() throws Exception {
        super();
    }
    @Override
    public void evaluate(TSP prob, cTSP codif) throws Exception {
        double fitness = 0;
        int i = codif.path[codif.path.length-1];    //last city
        for(int j : codif.path){
            fitness += prob.inst.Cij[i][j];
            i = j;
        }
        set(fitness);       //set de fitness to the ProOF
    }
    @Override
    public TSPObjective build(TSP prob) throws Exception {
        return new TSPObjective();
    }
}
