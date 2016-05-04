package ProOF.apl.advanced1.method;



/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import ProOF.apl.advanced1.FMS.immune_system.AIS;
import ProOF.apl.advanced1.FMS.local_search.LocalImprovement;
import ProOF.com.Linker.LinkerApproaches;
import ProOF.com.Linker.LinkerParameters;
import ProOF.com.Linker.LinkerResults;
import ProOF.com.language.Factory;
import ProOF.gen.operator.Initialization;
import ProOF.gen.operator.LocalMove;
import ProOF.gen.operator.Mutation;
import ProOF.gen.operator.oTrailPheromone;
import ProOF.gen.stopping.CountIteration;
import ProOF.opt.abst.run.MetaHeuristic;
import ProOF.opt.abst.problem.meta.Problem;
import ProOF.gen.stopping.Stop;
import ProOF.opt.abst.problem.meta.Solution;
import java.util.Arrays;
import java.util.LinkedList;

/**
 *
 * @author marcio
 */
public class CLONALG extends MetaHeuristic{
    private final CountIteration loop = CountIteration.obj;
    private final Initialization init = Initialization.obj;
    private final LocalMove moves = LocalMove.obj;
    
    private Stop stop;
    private Problem problem;
    private AIS ais;            //Artificial Immune System process
    private LocalImprovement local_improvement;
    
    private final Factory fStop;
    private final Factory fProblem;
    private final Factory fAIS;
    private final Factory fLocalImprovement;
    
    private int keep_mode;

    public CLONALG(Factory fStop, Factory fAIS, Factory fLocalImprovement, Factory fProblem) {
        this.fStop = fStop;
        this.fAIS = fAIS;
        this.fLocalImprovement = fLocalImprovement;
        this.fProblem = fProblem;
        
    }
    
    @Override
    public String name() {
        return "CLONal selection ALGorithm";
    }
    @Override
    public void services(LinkerApproaches link) throws Exception {
        link.add(loop);
        link.add(init);
        link.add(moves);
        stop                = link.get(fStop, stop);
        ais                 = link.get(fAIS, ais);
        local_improvement   = link.get(fLocalImprovement, local_improvement);
        problem             = link.get(fProblem, problem);
    }

    @Override
    public void parameters(LinkerParameters link) throws Exception {
        super.parameters(link); //To change body of generated methods, choose Tools | Templates.
        keep_mode = link.Itens("Keep", 1, "none", "best", "all");
    }
    
    @Override
    public void execute() throws Exception {
        //Alocate memory for a population of antibodies
        Solution antibodies[] = new Solution[ais.n_antibodies()];
        for(int i=0; i<antibodies.length; i++){
            antibodies[i] = problem.build_sol();
        }
        
        //Initalize antibodies
        for (Solution a : antibodies) {
            init.initialize(a);
            problem.evaluate(a);
        }
        
        //Sort in ascending order
        Arrays.sort(antibodies);
        
        do{
            LinkedList<Solution> list = new LinkedList<>();
            
            switch(keep_mode){
                case 0:     //doesn't keep any solution to concur for a place in the next generation
                    break;
                case 1:     //keep the best solution to concur for a place in the next generation
                    list.addLast(antibodies[0]);
                    break;
                case 2:     //keep the all solutions to concur for a place in the next generation
                    for (Solution a : antibodies) {
                        list.addLast(a);
                    }
                    break;
                default:
                    throw new Exception("Unknow keep_mode = "+keep_mode);
            }
            
            for(int i=0; i<antibodies.length; i++){
                int n_clones = ais.n_clones(i);
                //
                for(int j=0; j<n_clones; j++){
                    Solution clone = moves.local_search(antibodies[i]);
                    problem.evaluate(clone);
                    list.addLast(clone);
                }
            }
            
            Solution clones[] = list.toArray(new Solution[list.size()]);
            
            //Sort in ascending order
            Arrays.sort(clones);
            
            
            //Select all n_keep bests clones to next generation, but ignore clones with equals values of fitness
            int n = 0;
            for (Solution clone : clones) {
                if (!contains(antibodies, clone, n)) {
                    antibodies[n] = clone;
                    n++;
                    if(n>=ais.n_keep()){
                        break;
                    }
                }
            }
            
            //restart all bettewen [n_keep() to n_antibodies()]
            while(n<antibodies.length){
                init.initialize(antibodies[n]);
                problem.evaluate(antibodies[n]);
                local_improvement.execute(problem, antibodies[n]);
                n++;
            }
            
            Arrays.sort(antibodies);
            
            loop.iteration();
        }while(!stop.end());
    }
    
    private boolean contains(Solution vet[], Solution sol, int n){
        for(int i=0; i<n; i++){
            if(vet[i].EQ(sol)){
                return true;
            }
        }
        return false;
    }
    
    @Override
    public void results(LinkerResults win) throws Exception {
        win.writeLong("iterations", loop.value());
    }
}
