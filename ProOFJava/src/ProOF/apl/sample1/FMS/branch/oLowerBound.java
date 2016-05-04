/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ProOF.apl.sample1.FMS.branch;


import ProOF.opt.abst.problem.meta.codification.Operator;

/**
 *
 * @author marcio
 * @param <Prob>
 * @param <No>
 */
public abstract class oLowerBound <
        Prob extends BranchProblem, No extends BranchNode
> extends Operator {
    public abstract double lower_bound(Prob prob, No base) throws Exception;
}
