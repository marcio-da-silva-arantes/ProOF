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
import ProOF.gen.operator.oTrailPheromone;
import ProOF.gen.stopping.CountIteration;
import ProOF.opt.abst.run.MetaHeuristic;
import ProOF.opt.abst.problem.meta.Problem;
import ProOF.gen.stopping.Stop;
import ProOF.opt.abst.problem.meta.Solution;

/**
 *
 * @author marcio
 */
public class ACO extends MetaHeuristic{
    private final CountIteration loop = CountIteration.obj;
    
    private Stop stop;
    private Problem problem;
    private oTrailPheromone trails[];
    
    private final Factory fStop;
    private final Factory fProblem;
    
    private int Nants;  //number of ants
    private double convergence; //convergence rate

    public ACO(Factory fStop, Factory fProblem) {
        this.fStop = fStop;
        this.fProblem = fProblem;
    }
    
    @Override
    public String name() {
        return "Ant Colony Optimization";
    }
    @Override
    public void services(LinkerApproaches link) throws Exception {
        link.add(loop);
        stop                = link.get(fStop, stop);
        problem             = link.get(fProblem, problem);
        trails              = link.needs(oTrailPheromone.class, new oTrailPheromone[1]);
    }
    @Override
    public void parameters(LinkerParameters link) throws Exception {
        super.parameters(link); //To change body of generated methods, choose Tools | Templates.
        Nants = link.Int("N-ants", 100, 1, 1000000);
        convergence = link.Dbl("convergence", 0.5, 1e-3, 1.0);
    }
    @Override
    public void execute() throws Exception {
        //Initialize trails pheromone of each ant colony
        for(oTrailPheromone tr : trails){
            tr.initialize(problem);
        }
        //Alocate memory for each ant in all colonies
        Solution ants[][] = new Solution[trails.length][Nants];
        for(int t=0; t<trails.length; t++){
            for(int k=0; k<Nants; k++){
                ants[t][k] = problem.build_sol();
            }
        }
        Solution best[] = new Solution[trails.length];  //best solution on colony
        Solution migr[] = new Solution[trails.length];  //migrate solution
        int trails_life[] = new int[trails.length];
        int max_life = 1;
        do{
            //------------------- build ants ---------------------
            
            for(int t=0; t<trails.length; t++){ //for each colony or pheromone trail (t)
                double prob = 1;
                for(int k=0; k<Nants; k++){
                    // if is not the best and is not the migrate solution then rebuild the ant
                    if(ants[t][k] != best[t] && ants[t][k] != migr[t]){ 
                        //prob = Math.max(prob,trails[t].build(problem, ants[t][k]));
                        prob *= trails[t].build(problem, ants[t][k]);
                        problem.evaluate(ants[t][k]);
                    }
                }
//                if(trails_life[t]>max_life){
//                    max_life = trails_life[t];
//                    //System.out.println("max-life = "+max_life);
//                }
                //System.out.printf("%d : %8d : %g\n",t, trails_life[t], prob);
                if(prob>convergence){
                    //System.out.printf("RESTART trail = %d : life = %8d : max = %8d: prob = %g\n",t, trails_life[t], max_life, prob);
                    
                    trails_life[t] = 0;
                    //System.out.println("--------------[RESTART trail = "+trails[t]+" ]-------------");
                    trails[t].initialize(problem);
                    t--;    //repeat the build process
                }else{
                    trails_life[t]++;
                }
            }
            //------------------ find bests ----------------------
            for(int t=0; t<trails.length; t++){
                int b = Solution.minIndex(ants[t]);
                best[t] = ants[t][b];
            }
            //------------------ migrations ----------------------
            if(trails.length>1){    //if we have more than one colony
                for(int t=0; t<trails.length; t++){
                    int s = (t+1)%trails.length;
                    int w = Solution.maxIndex(ants[s]);
                    ants[s][w].copy(problem, best[t]);   //copy the best ant from t on worst ant from t+1
                    migr[s] = ants[s][w];
                }
            }
                
            //------------------ update trails -------------------
            for(int t=0; t<trails.length; t++){
                trails[t].evaporate(problem);
                for(int k=0; k<Nants; k++){
                    trails[t].deposit(problem, ants[t][k], 1.0);
                }
                trails[t].deposit(problem, best[t], trails_life[t]);
            }
            
            loop.iteration();
        }while(!stop.end());
    }
    @Override
    public void results(LinkerResults win) throws Exception {
        win.writeLong("iterations", loop.value());
    }
}
