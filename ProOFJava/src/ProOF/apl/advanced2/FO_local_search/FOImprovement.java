/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ProOF.apl.advanced2.FO_local_search;


import ProOF.apl.advanced1.FMS.local_search.*;
import ProOF.com.Linker.LinkerApproaches;
import ProOF.com.Linker.LinkerParameters;
import ProOF.opt.abst.problem.meta.Problem;
import ProOF.opt.abst.problem.meta.Solution;

/**
 *
 * @author marcio
 */
public class FOImprovement extends LocalImprovement{
    private FOOperator operators; 
    
    
    @Override
    public String name() {
        return "FO Improvement";
    }
    @Override
    public void services(LinkerApproaches link) throws Exception {
        operators       = link.add(FOOperator.obj);
    }

    @Override
    public void parameters(LinkerParameters link) throws Exception {
        super.parameters(link); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public void execute(Problem problem, Solution best) throws Exception {
        Solution current = operators.execute_FO_strategy(best);
        best.copyIfBetter(problem, current);
    }
}
