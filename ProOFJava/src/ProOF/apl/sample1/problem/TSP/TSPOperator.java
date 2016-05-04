/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ProOF.apl.sample1.problem.TSP;

import ProOF.com.Linker.LinkerParameters;
import ProOF.com.language.Factory;
import ProOF.gen.operator.oCrossover;
import ProOF.gen.operator.oGreedyConstruction;
import ProOF.gen.operator.oInitialization;
import ProOF.gen.operator.oLocalMove;
import ProOF.gen.operator.oMutation;
import ProOF.gen.operator.oTrailPheromone;
import ProOF.opt.abst.problem.meta.codification.Operator;
import ProOF.opt.abst.problem.meta.objective.SingleObjective;
import ProOF.utilities.uRoulette;
import ProOF.utilities.uRouletteList;
import ProOF.utilities.uUtil;
import java.util.LinkedList;
import java.util.Random;
import jsc.util.Arrays;

/**
 *
 * @author marcio
 */
public class TSPOperator extends Factory<Operator>{
    public static final TSPOperator obj = new TSPOperator();
    
    @Override
    public String name() {
        return "TSP Operators";
    }
    @Override
    public Operator build(int index) {  //build the operators
        switch(index){
            case 0: return new RandomTour();    //initialization
            case 1: return new MutExchange();   //mutation
            case 2: return new TwoPoints();     //crossover
            case 3: return new MovExchange();   //local movement
            case 4: return new GreedyConstruction();    //greedy construction (by GRASP)
            case 5: return new TrailConst();        //(by ACO: Ant Colony Optimization)
            case 6: return new TrailRnd();        //(by ACO: Ant Colony Optimization)
            case 7: return new TrailCij();        //(by ACO: Ant Colony Optimization)
        }
        return null;
    }
    
    private class RandomTour extends oInitialization<TSP, cTSP>{
        @Override
        public String name() {
            return "Random Tour";
        }
        @Override
        public void initialize(TSP prob, cTSP ind) throws Exception {
            for(int i=0; i<ind.path.length; i++){
                ind.path[i] = i;
            }
            for(int i=0; i<ind.path.length; i++){
                random_swap(prob.rnd, ind);
            }
        }
    }
    private class MutExchange extends oMutation<TSP, cTSP>{
        @Override
        public String name() {
            return "Mut-Exchange";
        }
        @Override
        public void mutation(TSP prob, cTSP ind) throws Exception {
            random_swap(prob.rnd, ind);
        }
    }
    private class MovExchange extends oLocalMove<TSP, cTSP>{
        @Override
        public String name() {
            return "Mov-Exchange";
        }
        @Override
        public void local_search(TSP prob, cTSP ind) throws Exception {
            random_swap(prob.rnd, ind);
        }
    }
    private class TwoPoints extends oCrossover<TSP, cTSP>{
        @Override
        public String name() {
            return "TwoPoints";
        }
        @Override
        public cTSP crossover(TSP prob, cTSP ind1, cTSP ind2) throws Exception {
            cTSP child = ind1.build(prob);
            int p[] = prob.rnd.cuts_points(prob.inst.N, 2);
            boolean selected[] = new boolean[prob.inst.N];
            for(int i=p[0]; i<p[1]; i++){
                child.path[i] = ind1.path[i];
                selected[child.path[i]] = true;
            }
            int i=p[1];
            int j=p[1];
            while(i!=p[0]){
                while(selected[ind2.path[j]]){
                    j = (j+1) % prob.inst.N;
                }
                child.path[i] = ind2.path[j];
                selected[child.path[i]] = true;
                i = (i+1) % prob.inst.N;
            }
            return child;
        }
    }
    
    private static void random_swap(Random rmd, cTSP ind){
        int a =  rmd.nextInt(ind.path.length);
        int b =  rmd.nextInt(ind.path.length);
        int aux = ind.path[a];
        ind.path[a] = ind.path[b];
        ind.path[b] = aux;
    }
    
    private class GreedyConstruction extends oGreedyConstruction<TSP, cTSP>{
        @Override
        public String name() {
            return "Greedy Construction";
        }
        @Override
        public void initialize(TSP prob, cTSP ind, double alpha) throws Exception {
            //List with all candidates
            LinkedList<Integer> C = new LinkedList<Integer>(); 
            for(int i=0; i<prob.inst.N; i++){
                C.addLast(i);
            }
            //Frist city is choise
            ind.path[0] = C.remove(prob.rnd.nextInt(C.size()));
            //Build step by step a randomized greedy solution
            for(int n=1; n<ind.path.length; n++){
                int i = ind.path[n-1];
                double min = Double.POSITIVE_INFINITY;
                double max = Double.NEGATIVE_INFINITY;
                for(int j: C){
                    min = Math.min(min, prob.inst.Cij[i][j]);
                    max = Math.max(max, prob.inst.Cij[i][j]);
                }
                //Build the list with restricted candidates
                LinkedList<Integer> LCR = new LinkedList<Integer>(); 
                for(int j: C){
                    if(prob.inst.Cij[i][j] <= min + (max-min) * alpha + 1e-6){
                        LCR.addLast(j);
                    }
                }
                //selecte a candidate form LCR
                Integer c = LCR.remove(prob.rnd.nextInt(LCR.size()));
                ind.path[n] = c;
                //update the candidate list
                C.remove(c);
            }
        }
    }
    
    
    
    private abstract class TrailPheromone extends oTrailPheromone<TSP, cTSP, SingleObjective>{
        private double decay_rate;  //decay rate of the pheromone
        private double Tij[][];     //pheromone trail for TSP
        
        @Override
        public void parameters(LinkerParameters link) throws Exception {
            super.parameters(link); //To change body of generated methods, choose Tools | Templates.
            decay_rate = link.Dbl("decay rate", 0.5, 0.0, 1.0);
        }
        protected abstract void fill(TSP prob, double Tij[][]);
        @Override
        public void initialize(TSP prob) throws Exception {
            Tij = new double[prob.inst.N][prob.inst.N];
            fill(prob, Tij);
            
            double sumTij = 0;
            for(double array[] : Tij){
                sumTij += Arrays.sum(array);
            }
            sumTij = sumTij/prob.inst.N;
            
            double sumCij = 0;
            for(double array[] : prob.inst.Cij){
                sumCij += Arrays.sum(array);
            }
            sumCij = sumCij/prob.inst.N;
//            System.out.println("sumTij/N        = "+sumTij);
//            System.out.println("sumCij/N        = "+sumCij);
//            System.out.println("N*N/Tij*Cij     = "+prob.inst.N*prob.inst.N/(sumTij*sumCij));
            for(int i=0; i<prob.inst.N; i++){
                for(int j=0; j<prob.inst.N; j++){
                    if(i!=j){
                        Tij[i][j] = (Tij[i][j]*prob.inst.N*prob.inst.N)/(sumTij*sumCij);
                    }
                }
            }
            
//            System.out.println("----------------- initialize ["+this.name()+"] --------------------");
//            uUtil.Print(System.out, "%10.6f", Tij);
        }
        @Override
        public double build(TSP prob, cTSP ant) throws Exception {
            //List with all candidates
            LinkedList<Integer> C = new LinkedList<Integer>(); 
            for(int i=0; i<prob.inst.N; i++){
                C.addLast(i);
            }
            //Frist city is choise
            ant.path[0] = C.remove(prob.rnd.nextInt(C.size()));
            
            uRouletteList roulette = new uRouletteList(prob.rnd);
            
            double probability = 1.0;
            //Build step by step a path following pheromone trail 
            for(int n=1; n<ant.path.length; n++){
                int i = ant.path[n-1];
                roulette.clear();
                for(int j : C){
                    roulette.add(Tij[i][j], j);
                }
                
                //selecte a candidate by roulette
                Integer c = roulette.roulette_wheel();
                probability *= roulette.probability(c);
                ant.path[n] = c;
                //update the candidate list
                C.remove(c);
            }
            return probability;
        }
        @Override
        public void evaporate(TSP prob) throws Exception {
//            System.out.println("----------------- evaporate ["+this.name()+"] --------------------");
//            uUtil.Print(System.out, "%10.6f", Tij);
            
            for(int i=0; i<prob.inst.N; i++){
                for(int j=0; j<prob.inst.N; j++){
                    Tij[i][j] = (1-decay_rate)*Tij[i][j];
                }
            }
        }

        @Override
        public void deposit(TSP prob, cTSP ant, SingleObjective obj, double weight) throws Exception {
            for(int n=1; n<ant.path.length; n++){
                int i = ant.path[n-1];
                int j = ant.path[n];
                Tij[i][j] += weight / obj.abs_value();
            }
        }
    }
    
    private class TrailConst extends TrailPheromone{
        @Override
        public String name() {
            return "Trail <-- 1/N";
        }
        @Override
        protected void fill(TSP prob, double[][] Tij) {
            for(int i=0; i<prob.inst.N; i++){
                for(int j=0; j<prob.inst.N; j++){
                    if(i!=j){
                        Tij[i][j] = 1.0;
                    }
                }
            }
        }
    }
    private class TrailRnd extends TrailPheromone{
        @Override
        public String name() {
            return "Trail <-- random";
        }
        @Override
        protected void fill(TSP prob, double[][] Tij) {
            for(int i=0; i<prob.inst.N; i++){
                for(int j=0; j<prob.inst.N; j++){
                    if(i!=j){
                        Tij[i][j] = prob.rnd.nextDouble();
                    }
                }
            }
        }
    }
    private class TrailCij extends TrailPheromone{
        @Override
        public String name() {
            return "Trail <-- 1/Cij";
        }
        @Override
        protected void fill(TSP prob, double[][] Tij) {
            for(int i=0; i<prob.inst.N; i++){
                for(int j=0; j<prob.inst.N; j++){
                    if(i!=j){
                        Tij[i][j] = 1.0/prob.inst.Cij[i][j];
                    }
                }
            }
        }
    }
}
