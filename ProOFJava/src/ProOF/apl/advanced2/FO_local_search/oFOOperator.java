/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ProOF.apl.advanced2.FO_local_search;

import ProOF.opt.abst.problem.meta.Problem;
import ProOF.opt.abst.problem.meta.codification.Codification;
import ProOF.opt.abst.problem.meta.codification.Operator;

/**
 *
 * @author marcio
 */
public abstract class oFOOperator<
        Prod extends Problem, Codif extends Codification
> extends Operator {
    public abstract Codif execute_FO_strategy(Prod prod, Codif ind) throws Exception;
}
