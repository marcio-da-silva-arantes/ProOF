/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ProOF.apl.advanced1.FMS.local_search;

import ProOF.opt.abst.problem.meta.Problem;
import ProOF.opt.abst.problem.meta.Solution;


/**
 *
 * @author marcio
 */
public class Nothing extends LocalImprovement{
    @Override
    public String name() {
        return "Nothing";
    }
    @Override
    public String description() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    @Override
    public void execute(Problem problem, Solution best) throws Exception {
        
    }
}
