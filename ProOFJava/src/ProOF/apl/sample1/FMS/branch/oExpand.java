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
 * @param <Node>
 */
public abstract class oExpand <
        Prob extends BranchProblem, Node extends BranchNode
> extends Operator {
    public abstract Node[] expand(Prob prob, Node node) throws Exception;
}
