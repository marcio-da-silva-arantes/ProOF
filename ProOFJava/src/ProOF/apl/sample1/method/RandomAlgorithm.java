/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ProOF.apl.sample1.method;

import ProOF.apl.factorys.fProblem;
import ProOF.apl.factorys.fStop;
import ProOF.com.Linker.LinkerApproaches;
import ProOF.gen.operator.Initialization;
import ProOF.gen.stopping.Stop;
import ProOF.opt.abst.run.MetaHeuristic;
import ProOF.opt.abst.problem.meta.Problem;
import ProOF.opt.abst.problem.meta.Solution;

/**
 *
 * @author marcio
 */
public class RandomAlgorithm extends MetaHeuristic{
    private Initialization init;
    private Problem problem;
    private Stop stop;
    
    @Override
    public String name() {
        return "Random Algorithm";
    }
    @Override
    public void services(LinkerApproaches link) throws Exception {
        init    = link.add(Initialization.obj);
        problem = link.get(fProblem.obj, problem);
        stop    = link.get(fStop.obj, stop);
    }
    @Override
    public void execute() throws Exception {
        //Declares the solution and makes memory allocation
        Solution sol = problem.build_sol();
        //Generates random solutions until the stopping criterion is reached
        do{
            init.initialize(sol);
            problem.evaluate(sol);
        }while(!stop.end());
    }
}
