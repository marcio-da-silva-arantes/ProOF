/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ProOF.apl.sample1.method;

import ProOF.apl.factorys.fProblem;
import ProOF.apl.factorys.fStop;
import ProOF.com.Communication;
import ProOF.com.Linker.LinkerApproaches;
import ProOF.com.Linker.LinkerParameters;
import ProOF.com.Stream.StreamPlot2D;
import ProOF.gen.operator.Crossover;
import ProOF.gen.operator.Initialization;
import ProOF.gen.operator.Mutation;
import ProOF.gen.stopping.Stop;
import ProOF.opt.abst.problem.meta.MultiProblem;
import ProOF.opt.abst.problem.meta.Solution;
import ProOF.opt.abst.run.MetaHeuristic;
import ProOF.utilities.uSort;
import ProOF.utilities.uTournament;
import java.awt.Color;
import java.util.LinkedList;

/**
 *
 * @author marcio
 */
public class NSGAII extends MetaHeuristic{
    private MultiProblem problem;
    private Stop stop;
    private Initialization init;
    private Crossover cross;
    private Mutation mut;
    
    private int pop_size;
    private int tour_size;
    private double mut_rate;
    
    private StreamPlot2D ploter;
    
    @Override
    public String name() {
        return "NSGA-II";
    }
    @Override
    public void services(LinkerApproaches link) throws Exception {
        problem = link.get(fProblem.obj, MultiProblem.class, problem);
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
  
    
    private Sol R[];
    
    private Sol P(int i){
        return R[i];
    }
    private Sol Q(int i){
        return R[pop_size+i];
    }
    
    @Override
    public void execute() throws Exception {
        //Declara a população e faz alocação de mémoria
        R = new Sol[pop_size*2];
        for(int i=0; i<R.length; i++){
            R[i] = new Sol();
        }
        
        //Inicia e avalia P0
        for(int i=0; i<pop_size; i++){
            P(i).new_sol();
            P(i).initialize();
            P(i).evaluate();
        }
        
        //Cria o gerenciado de seleção
        uTournament tour = new uTournament(R, tour_size);
        
        do{
            //Gera e avalia Qt a partir de Pt
            for(int i=0; i<pop_size; i++){
                int p1 = tour.select_in(pop_size);
                int p2 = tour.select_in(pop_size);
                
                //Crossover
                Q(i).crossover(P(p1), P(p2));
                
                //Mutação
                if(Math.random() < mut_rate){
                    Q(i).mutation();
                }
                
                //Avaliação
                Q(i).evaluate();
            }
            
            Integer ranks[][] = fast_nondominated_sorting();
            for(Integer[] I : ranks){
                crowding_distance_assignment(I);
            }
            //Make next population
            goal = GOAL_ALL;
            uSort.sort(R);
            
            plot();
        }while(!stop.end());
    }
    private void plot() throws Exception{
        if(problem.goals()==2){
            for(int i=0; i<pop_size; i++){
                double x = problem.goal(0, P(i).sol);
                double y = problem.goal(1, P(i).sol);
                Color color;
                if(P(i).rank==1){
                    color = Color.RED;
                }else if(P(i).rank==2){
                    color = Color.GREEN;
                }else if(P(i).rank==3){
                    color = Color.BLUE;
                }else{
                    color = Color.darkGray;
                }
                ploter.point(i, x, y, color);
            }
            Thread.sleep(100);
        }
    }
    private Integer[][] fast_nondominated_sorting(){
        LinkedList<LinkedList<Integer>> ranks = new LinkedList<LinkedList<Integer>>();
        
        
        LinkedList<Integer>[] Sp = new LinkedList[R.length];
        int[] Np = new int[R.length];
        LinkedList<Integer> Fi = new LinkedList<Integer>();
        
        for(int p=0; p<R.length; p++){
            Sp[p] = new LinkedList<Integer>();
            Np[p] = 0;
            for(int q=0; q<R.length; q++){
                if(R[p].dominates(R[q])){
                    Sp[p].addLast(q);
                }else if(R[q].dominates(R[p])){
                    Np[p]++;
                }
            }
            if(Np[p]==0){
                R[p].rank = 1;
                Fi.addLast(p);
            }
        }
        ranks.addLast(Fi);
        
        int i=1;
        while(Fi.size()>0){
            LinkedList<Integer> Q = new LinkedList<Integer>();
            for(int p : Fi){
                for(int q : Sp[p]){
                    Np[q]--;
                    if(Np[q]==0){
                        R[q].rank = i+1;
                        Q.addLast(q);
                    }
                }
            }
            i++;
            Fi = Q;
            if(Fi.size()>0){
                ranks.addLast(Fi);
            }
        }
        Integer array[][] = new Integer[ranks.size()][];
        int j=0;
        for(LinkedList<Integer> rank : ranks){
            array[j] = rank.toArray(new Integer[rank.size()]);
            j++;
        }
        return array;
    }
    private void crowding_distance_assignment(Integer I[]) throws Exception{
        final int l = I.length-1;
        for(int i : I){
            R[i].distance = 0;
        }
        for(int m=0; m<problem.goals(); m++){
            sort(I, m);
            for(int i=1; i<l; i++){
                double goal_A = problem.goal(m, R[I[i-1]].sol);
                double goal_B = problem.goal(m, R[I[i+1]].sol);
                
                R[I[i]].distance += (goal_B - goal_A) / (MAX[m] - MIN[m]);
            }
            R[I[0]].distance += Integer.MAX_VALUE;
            R[I[l]].distance += Integer.MAX_VALUE;
        }
    }
    private void sort(Integer I[], int m){
        goal = m;
        uSort.sort(R, I);
        goal = GOAL_ALL;
    }
    
    private static final int GOAL_ALL = -1;
    private int goal;
    private double MAX[];
    private double MIN[];

    @Override
    public void start() throws Exception {
        goal = GOAL_ALL;
        MAX = new double[problem.goals()];
        MIN = new double[problem.goals()];
        for(int m=0; m<problem.goals(); m++){
            MAX[m] = Integer.MIN_VALUE;
            MIN[m] = Integer.MAX_VALUE;
        }
        if(problem.goals()==2){
            ploter = Communication.mkPlot2D("NSGA-II plot2D");
            ploter.background(Color.WHITE);
        }
    }
    
    private class Sol implements Comparable<Sol>{
        private Solution sol;
        private int rank;
        private double distance;    //crowding-distance
        
        public Sol() {
            this.sol = null;
            this.rank = -1;
            this.distance = -1;
        }
        @Override
        public int compareTo(Sol o) {
            if(goal==GOAL_ALL){
                if(rank != o.rank){
                    return Integer.compare(rank, o.rank);
                }else{
                    return Double.compare(o.distance, distance);
                }
            }else{
                return problem.compareTo(goal, this.sol, o.sol);
            }
        }
        private boolean dominates(Sol o) {
            return sol.compareTo(o.sol) < 0;
        }

        private void new_sol() throws Exception {
            sol = problem.build_sol();   
        }
        private void initialize() throws Exception {
            init.initialize(sol);
        }
        private void evaluate() throws Exception {
            problem.evaluate(sol);
            for(int m=0; m<problem.goals(); m++){
                double goal = problem.goal(m, sol);
                MAX[m] = Math.max(MAX[m], goal);
                MIN[m] = Math.min(MIN[m], goal);
            }
        }                
        private void crossover(Sol P1, Sol P2) throws Exception {
            sol = cross.crossover(P1.sol, P2.sol);
        }
        private void mutation() throws Exception {
            mut.mutation(sol);
        }
    }
}
