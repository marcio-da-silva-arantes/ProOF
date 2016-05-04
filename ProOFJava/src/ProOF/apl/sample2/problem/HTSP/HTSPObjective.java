/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ProOF.apl.sample2.problem.HTSP;

import ProOF.opt.abst.problem.meta.objective.SingleObjective;

/**
 *
 * @author marcio
 */
public class HTSPObjective extends SingleObjective<HTSP, HTSPCodification, HTSPObjective> {
    public HTSPObjective() throws Exception {
        super();
    }
    @Override
    public void evaluate(HTSP prob, HTSPCodification codif) throws Exception {
        prob.fix(codif);
        set(prob.solve());       //set de fitness to the ProOF
    }
    @Override
    public HTSPObjective build(HTSP prob) throws Exception {
        return new HTSPObjective();
    }
}
