/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ProOF.apl.advanced1.FMS.local_search;


import ProOF.com.Linker.LinkerApproaches;
import ProOF.com.Linker.LinkerParameters;
import ProOF.opt.abst.problem.meta.Problem;
import ProOF.opt.abst.problem.meta.Solution;
import ProOF.gen.operator.LocalMove;

/**
 *
 * @author marcio
 */
public class FristImprovement extends LocalImprovement{
    private int neighborhood;       //number of neighbors generates to get the next current solution
    private LocalMove moves;
    
    
    @Override
    public String name() {
        return "Frist Improvement";
    }
    @Override
    public void services(LinkerApproaches link) throws Exception {
        moves       = link.add(LocalMove.obj);
    }

    @Override
    public void parameters(LinkerParameters link) throws Exception {
        super.parameters(link); //To change body of generated methods, choose Tools | Templates.
        neighborhood    =   link.Int("neighborhood", 100, 1, 1000000, "number of neighbors generates to get the next current solution");
    }
    
    @Override
    public void execute(Problem problem, Solution best) throws Exception {
        Solution current = best;
        int iter = 0;
        do{
            Solution neibor = moves.local_search(current);
            problem.evaluate(neibor);
            if(neibor.LT(current)){
                current = neibor;
                if(best.copyIfBetter(problem, current)){
                    iter = 0;
                }
            }
        }while(iter++<neighborhood);
    }
}
