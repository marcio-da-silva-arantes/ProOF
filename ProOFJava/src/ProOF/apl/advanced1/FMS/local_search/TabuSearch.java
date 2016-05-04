/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ProOF.apl.advanced1.FMS.local_search;


import ProOF.apl.advanced1.FMS.tabu.Tabu;
import ProOF.com.Linker.LinkerApproaches;
import ProOF.com.Linker.LinkerParameters;
import ProOF.opt.abst.problem.meta.Problem;
import ProOF.opt.abst.problem.meta.Solution;
import ProOF.com.language.Factory;
import ProOF.gen.operator.LocalMove;
import ProOF.gen.operator.oLocalMove;

/**
 *
 * @author marcio
 */
public class TabuSearch extends LocalImprovement{
    private int neighborhood;       //number of neighbors generates to get the next current solution
    private int stop;               //number of iterations without improvements to stop the evolution
    private Tabu tabu;
    private LocalMove moves;
    
    private final Factory fTabu;
    public TabuSearch(Factory fTabu) {
        this.fTabu = fTabu;
    }
    
    @Override
    public String name() {
        return "Tabu Search";
    }
    @Override
    public void services(LinkerApproaches link) throws Exception {
        tabu        = link.get(fTabu, tabu);
        moves       = link.add(LocalMove.obj);
    }

    @Override
    public void parameters(LinkerParameters link) throws Exception {
        super.parameters(link); //To change body of generated methods, choose Tools | Templates.
        neighborhood    =   link.Int("neighborhood", 100, 1, 1000000, "number of neighbors generates to get the next current solution");
        stop            =   link.Int("stop", 10, 1, 1000000, "number of iterations without improvements to stop the evolution");
    }
    
    @Override
    public void execute(Problem problem, Solution best) throws Exception {
        Solution current = best;

        int iter = 0;
        do{
            //------------------ find best neibor and movment ----------------
            oLocalMove best_mov = null;
            Solution best_neibor = null;
            for(int n=0; n<neighborhood; n++){
                oLocalMove mov = tabu.select();
                Solution neibor = moves.local_search(current, mov);
                problem.evaluate(neibor);
                //System.out.println("neibor = "+neibor+" from mov = "+mov);
                if(neibor.LT(best_neibor)){
                    best_mov = mov;
                    best_neibor = neibor;
                }
            }
            //System.out.println("best_neibor = "+best_neibor+" best_mov mov = "+best_mov);

            //update the tabu list
            tabu.update(best_mov);

            //make best neibor the current solution
            current = best_neibor;
            
            //if best is improved then reset the iteration count
            if(best.copyIfBetter(problem, best_neibor)){
                iter = 0;
            }
        }while(iter++<stop);
    }
}
