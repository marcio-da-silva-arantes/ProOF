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
public class ThresholdAccepting extends LocalImprovement{
    private double Threshold;
    private double Th_improved;
    private double Th_accept;
    private double Th_reject;
    private int neighborhood;       //number of neighbors generates to get the next current solution
    private LocalMove moves;
    
    
    @Override
    public String name() {
        return "Threshold Accepting";
    }
    @Override
    public void services(LinkerApproaches link) throws Exception {
        moves       = link.add(LocalMove.obj);
    }

    @Override
    public void parameters(LinkerParameters link) throws Exception {
        super.parameters(link); //To change body of generated methods, choose Tools | Templates.
        neighborhood    =   link.Int("neighborhood",100,    1,      1000000,    "number of neighbors generates to get the next current solution");
        Threshold       =   link.Dbl("Threshold",   1.0,    1e-2,   1e2,        "threshold to acept neibor as current solution");
        Th_improved     =   link.Dbl("Th-improved", 0.02,   1e-3,   1e2,        "threshold decrement when the best solution is improved");
        Th_accept       =   link.Dbl("Th-accept",   0.01,   1e-3,   1e2,        "threshold decrement when the neibor is acepted as current solution");
        Th_reject       =   link.Dbl("Th-reject",   0.005,  1e-3,   1e2,        "threshold decrement when the neibor is rejected as current solution");
    }
    
    @Override
    public void execute(Problem problem, Solution best) throws Exception {
        Solution current = best;
        
        double Th = Threshold;
        int iter = 0;
        do{
            Solution neibor = moves.local_search(current);
            problem.evaluate(neibor);
            if(neibor.deviation(best) < Th){
                current = neibor;
                if(best.copyIfBetter(problem, current)){
                    Th = Math.max(Th - Th_improved, 0);   //best is improved
                    iter = 0;
                }else{
                    Th = Math.max(Th - Th_accept, 0);     //current is accepted
                }
            }else{
                Th = Math.max(Th - Th_reject, 0);     //current is rejected
            }
            if(Th < 1e-3){
                Th = Threshold;
            }
        }while(iter++<neighborhood);
    }
}
