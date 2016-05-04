package ProOF.apl.advanced1.method;



/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import ProOF.apl.advanced1.FMS.local_search.LocalImprovement;
import ProOF.com.Linker.LinkerApproaches;
import ProOF.com.Linker.LinkerParameters;
import ProOF.com.Linker.LinkerResults;
import ProOF.com.language.Factory;
import ProOF.gen.operator.GreedyConstruction;
import ProOF.gen.stopping.CountIteration;
import ProOF.opt.abst.run.MetaHeuristic;
import ProOF.opt.abst.problem.meta.Problem;
import ProOF.gen.stopping.Stop;
import ProOF.opt.abst.problem.meta.Solution;

/**
 *
 * @author marcio
 */
public class GRASP extends MetaHeuristic{
    private final CountIteration loop = CountIteration.obj;
    private final GreedyConstruction init = GreedyConstruction.obj;
    
    private Stop stop;
    private LocalImprovement local_improvement;
    private Problem problem;
    
    private final Factory fStop;
    private final Factory fLocalImprovement;
    private final Factory fProblem;
    
    private double alpha;

    public GRASP(Factory fStop, Factory fLocalImprovement, Factory fProblem) {
        this.fStop = fStop;
        this.fLocalImprovement = fLocalImprovement;
        this.fProblem = fProblem;
    }
    
    @Override
    public String name() {
        return "GRASP";
    }
    @Override
    public void services(LinkerApproaches link) throws Exception {
        link.add(loop);
        link.add(init);
        
        stop                = link.get(fStop, stop);
        local_improvement   = link.get(fLocalImprovement, local_improvement);
        problem             = link.get(fProblem, problem);
    }
    @Override
    public void parameters(LinkerParameters link) throws Exception {
        super.parameters(link); //To change body of generated methods, choose Tools | Templates.
        alpha = link.Dbl("alpha", 0.1, 0.0, 1.0, "GRASP: alpha = 0.0 -> random | aplha = 1.0 -> greedy ");
    }
    @Override
    public void execute() throws Exception {
        Solution sol = problem.build_sol();
        do{
            init.initialize(sol, alpha);
            problem.evaluate(sol);
            local_improvement.execute(problem, sol);
            loop.iteration();
        }while(!stop.end());
    }
    @Override
    public void results(LinkerResults win) throws Exception {
        win.writeLong("iterations", loop.value());
    }
}
