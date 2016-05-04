/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ProOF.apl.sample1.method;

import ProOF.apl.factorys.fProblem;
import ProOF.apl.factorys.fStop;
import ProOF.apl.factorys.fTemp;
import ProOF.apl.sample1.FMS.temperature.aTemp;
import ProOF.com.Linker.LinkerApproaches;
import ProOF.com.Linker.LinkerResults;
import ProOF.gen.operator.Initialization;
import ProOF.gen.operator.LocalMove;
import ProOF.gen.stopping.Stop;
import ProOF.gen.stopping.CountIteration;
import ProOF.opt.abst.problem.meta.Problem;
import ProOF.opt.abst.problem.meta.Solution;
import ProOF.opt.abst.run.MetaHeuristic;


/**
 *
 * @author marcio
 */
public final class SimulatedAnnealing extends MetaHeuristic{
    private Problem problem;
    private Stop stop;
    private aTemp function;
    
    private Initialization init;
    private LocalMove moves;
    
    private final CountIteration loop = CountIteration.obj;
    
    @Override
    public String name() {
        return "SA";
    }
    @Override
    public String description() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    @Override
    public void services(LinkerApproaches link) throws Exception {
        link.add(loop);
        problem = link.get(fProblem.obj, problem);
        stop    = link.get(fStop.obj, stop);
        function = link.get(fTemp.obj, function);
        init    = link.add(Initialization.obj);
        moves   = link.add(LocalMove.obj);
    }
    @Override
    public void execute() throws Exception {
        Solution sol = problem.build_sol();
        init.initialize(sol);
        problem.evaluate(sol);
        do{
            function.start();
            do{
                double x = problem.rnd.nextDouble();
                
                Solution neibor = moves.local_search(sol);
                
                problem.evaluate(neibor);

                double delta = neibor.compareToAbs(sol);
                
                if(delta < 0){
                    sol = neibor;
                }else if(x < Math.exp(-delta/function.temperature())){
                    sol = neibor;
                }
                function.decress();
            }while(!function.end());
            
            loop.iteration();
        }while(!stop.end());
    }
    @Override
    public void results(LinkerResults win) throws Exception {
        win.writeLong("iterations", loop.value());
    }
}
