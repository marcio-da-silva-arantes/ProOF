/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ProOF.apl.sample1.problem.TSP;

import ProOF.opt.abst.problem.meta.codification.Codification;

/**
 *
 * @author marcio
 */
public class cTSP extends Codification<TSP, cTSP> {
    protected int path[];

    public cTSP(TSP prob) {
        this.path = new int[prob.inst.N];
    }
    @Override
    public void copy(TSP prob, cTSP source) throws Exception {
        System.arraycopy(source.path, 0, this.path, 0, this.path.length);
    }
    @Override
    public cTSP build(TSP prob) throws Exception {
        return new cTSP(prob);
    }
}
