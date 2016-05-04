/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ProOF.apl.advanced1.FMS.local_search;


import ProOF.com.Linker.LinkerApproaches;
import ProOF.opt.abst.problem.meta.Problem;
import ProOF.opt.abst.problem.meta.Solution;
import ProOF.apl.advanced1.FMS.temperature.Temperature;
import ProOF.com.language.Factory;
import ProOF.gen.operator.LocalMove;

/**
 *
 * @author marcio
 */
public class SimulatedAnnealing extends LocalImprovement{
    private Temperature function;
    private LocalMove moves;
    
    private final Factory fTemperature;
    public SimulatedAnnealing(Factory fTemperature) {
        this.fTemperature = fTemperature;
    }
    
    @Override
    public String name() {
        return "Simulated Annealing";
    }
    @Override
    public void services(LinkerApproaches link) throws Exception {
        function    = link.get(fTemperature, function);
        moves       = link.add(LocalMove.obj);
    }
    
    @Override
    public void execute(Problem problem, Solution best) throws Exception {
        Solution current = best;
        function.start();
        do{
            double x = problem.rnd.nextDouble();

            Solution neibor = moves.local_search(current);

            problem.evaluate(neibor);

            double delta = neibor.compareToAbs(current);

            if(delta < 0){
                current = neibor;
                best.copyIfBetter(problem, current);
            }else if(x < Math.exp(-delta/function.temperature())){
                current = neibor;
            }
            function.decress();
        }while(!function.end());
    }
}
