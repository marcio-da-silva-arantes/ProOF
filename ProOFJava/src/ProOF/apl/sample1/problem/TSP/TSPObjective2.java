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
public class TSPObjective2 extends SingleObjective<TSP2, cTSP, TSPObjective2> {
    public TSPObjective2() throws Exception {
        super();
    }
    @Override
    public void evaluate(TSP2 prob, cTSP codif) throws Exception {
        double fitness = 0;
        int i = codif.path[codif.path.length-1];    //last city
        for(int j : codif.path){
            fitness += prob.inst.Cij[i][j];
            i = j;
        }
        set(fitness);       //set de fitness to the ProOF
    }
    @Override
    public TSPObjective2 build(TSP2 prob) throws Exception {
        return new TSPObjective2();
    }
}
