/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ProOF.apl.sample1.method;

import ProOF.apl.factorys.fProblem;
import ProOF.apl.factorys.fStop;
import ProOF.com.Linker.LinkerApproaches;
import ProOF.com.Linker.LinkerParameters;
import ProOF.gen.operator.Crossover;
import ProOF.gen.operator.Initialization;
import ProOF.gen.operator.Mutation;
import ProOF.gen.stopping.Stop;
import ProOF.opt.abst.run.MetaHeuristic;
import ProOF.opt.abst.problem.meta.Problem;
import ProOF.opt.abst.problem.meta.Solution;
import ProOF.utilities.uTournament;

/**
 *
 * @author marcio
 */
public class GeneticAlgorithm extends MetaHeuristic{
    private Problem problem;
    private Stop stop;
    private Initialization init;
    private Crossover cross;
    private Mutation mut;
    
    private int pop_size;
    private int tour_size;
    private double mut_rate;
    
    @Override
    public String name() {
        return "GA";
    }
    @Override
    public void services(LinkerApproaches link) throws Exception {
        problem = link.get(fProblem.obj, problem);
        stop    = link.get(fStop.obj, stop);
        init    = link.add(Initialization.obj);
        cross   = link.add(Crossover.obj);
        mut     = link.add(Mutation.obj);
    }
    @Override
    public void parameters(LinkerParameters link) throws Exception {
        pop_size  = link.Int("population size",  100,  10, 10000);
        tour_size = link.Int("tournament size",    3,   2, 16   );
        mut_rate  = link.Dbl("mutation rate"  , 0.10,   0, 1    );
    }
    @Override
    public void execute() throws Exception {
        //Declares the population and makes memory allocation
        Solution pop[] = new Solution[pop_size];
        for(int i=0; i<pop.length; i++){
            pop[i] = problem.build_sol();
        }
        
        //Creates the managed selection by tournament
        uTournament tour = new uTournament(pop, tour_size);
        
        //Initiates and evaluates the population
        for(Solution ind : pop){
            init.initialize(ind);
            problem.evaluate(ind);
        }
        do{
            //Selection
            int p1 = tour.select();
            int p2 = tour.select();

            //Crossover
            Solution child = cross.crossover(pop[p1], pop[p2]);

            //Mutation
            if(Math.random() < mut_rate){
                mut.mutation(child);
            }

            //Evaluation
            problem.evaluate(child);

            //Insert: replaces the worst parent
            int worse = pop[p1].GT(pop[p2]) ? p1 : p2;
            pop[worse] = child;
        }while(!stop.end());
    }
}
