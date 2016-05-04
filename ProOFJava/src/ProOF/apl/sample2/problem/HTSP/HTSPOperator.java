/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ProOF.apl.sample2.problem.HTSP;

import ProOF.com.language.Factory;
import ProOF.gen.operator.oCrossover;
import ProOF.gen.operator.oInitialization;
import ProOF.gen.operator.oMutation;
import ProOF.opt.abst.problem.meta.codification.Operator;
import java.util.LinkedList;
import java.util.Random;

/**
 *
 * @author marcio
 */
public class HTSPOperator extends Factory<Operator>{
    public static final HTSPOperator obj = new HTSPOperator();
    
    @Override
    public String name() {
        return "TSP Operators";
    }
    @Override
    public Operator build(int index) {  //build the operators
        switch(index){
            case 0: return new RandomEdges();    //initialization
            case 1: return new MutTwoEdges();   //mutation
            case 2: return new SelectEdges();     //crossover
            case 3: return new GreedyEdges();   //initialization
        }
        return null;
    }
    
    private class RandomEdges extends oInitialization<HTSP, HTSPCodification>{
        @Override
        public String name() {
            return "Random Edges";
        }
        @Override
        public void initialize(HTSP prob, HTSPCodification ind) throws Exception {
            int vet[] = new int[prob.inst.N];
            for(int i=0; i<prob.inst.N; i++){
                vet[i] = i;
            }
            for(int i=0; i<prob.inst.N; i++){
                random_swap(prob.rnd, vet);
            }
            LinkedList<Integer> list = new LinkedList<Integer>();
            for(int i=0; i<prob.inst.N; i++){
                list.addLast(vet[i]);
            }
            for(int i=0; i<ind.set.length; i++){
                ind.set[i] = new HTSPEdge(list.removeFirst(), list.removeFirst());
            }
        }
    }
    private class GreedyEdges extends oInitialization<HTSP, HTSPCodification>{
        @Override
        public String name() {
            return "Greedy Edges";
        }
        @Override
        public void initialize(HTSP prob, HTSPCodification ind) throws Exception {
            int vet[] = new int[prob.inst.N];
            for(int i=0; i<prob.inst.N; i++){
                vet[i] = i;
            }
            for(int i=0; i<prob.inst.N; i++){
                random_swap(prob.rnd, vet);
            }
            LinkedList<Integer> list = new LinkedList<Integer>();
            for(int i=0; i<prob.inst.N; i++){
                list.addLast(vet[i]);
            }
            for(int n=0; n<ind.set.length; n++){
                int i = list.removeFirst();
                int j = find_and_remove(prob, i, list);
                
                ind.set[n] = new HTSPEdge(i, j);
            }
        }
        private int find_and_remove(HTSP prob, int i, LinkedList<Integer> list) {
            Integer index = -1;
            double cost = Double.POSITIVE_INFINITY;
            for(Integer j : list){
                if(prob.inst.Cij[i][j] < cost){
                    cost = prob.inst.Cij[i][j];
                    index = j;
                }
            }
            list.remove(index);
            return index;
        }
    }
    private class MutTwoEdges extends oMutation<HTSP, HTSPCodification>{
        @Override
        public String name() {
            return "Mut-TwoEdges";
        }
        @Override
        public void mutation(HTSP prob, HTSPCodification ind) throws Exception {
            int a =  prob.rnd.nextInt(ind.set.length);
            int b =  prob.rnd.nextInt(ind.set.length);
            HTSPEdge A = ind.set[a];                    //A  = [p -> q]
            HTSPEdge B = ind.set[b];                    //B  = [r -> t]
            ind.set[a] = new HTSPEdge(A.i, B.j);        //A' = [p -> t]
            ind.set[b] = new HTSPEdge(B.i, A.j);        //B' = [r -> q]
        }
    }
    
    private class SelectEdges extends oCrossover<HTSP, HTSPCodification>{
        @Override
        public String name() {
            return "SelectEdges";
        }
        @Override
        public HTSPCodification crossover(HTSP prob, HTSPCodification ind1, HTSPCodification ind2) throws Exception {
            HTSPCodification child = ind1.build(prob);
            
//            System.out.println("ind1  = "+ind1);
//            System.out.println("ind2  = "+ind2);
            
            boolean used[] = new boolean[prob.inst.N];
            int useds = 0;
            for(int n=0; n<ind1.set.length; n++){
                if(prob.rnd.nextBoolean()){
                    if(!used[ind1.set[n].i] && !used[ind1.set[n].j]){   //from ind1 frist
                        child.set[n] = ind1.set[n];
                        used[ind1.set[n].i] = true;
                        used[ind1.set[n].j] = true;
                        useds+=2;
                    }else if(!used[ind2.set[n].i] && !used[ind2.set[n].j]){     //try ind2
                        child.set[n] = ind2.set[n];
                        used[ind2.set[n].i] = true;
                        used[ind2.set[n].j] = true;
                        useds+=2;
                    }
                }else{
                    if(!used[ind2.set[n].i] && !used[ind2.set[n].j]){   //from ind2 frist
                        child.set[n] = ind2.set[n];
                        used[ind2.set[n].i] = true;
                        used[ind2.set[n].j] = true;
                        useds+=2;
                    }else if(!used[ind1.set[n].i] && !used[ind1.set[n].j]){     //try ind1
                        child.set[n] = ind1.set[n];
                        used[ind1.set[n].i] = true;
                        used[ind1.set[n].j] = true;
                        useds+=2;
                    }
                }
            }
//            System.out.println("child = "+child);
//            System.out.println("useds = "+useds+" from total "+used.length);
//            for(int i=0; i<used.length; i++){
//                System.out.printf("%2d%s ", i, !used[i]?"*":" ");
//            }
//            System.out.println();
            
            if(useds<used.length){
                int vet[] = new int[used.length-useds];
                int k = 0;
                for(int i=0; i<used.length; i++){
                    if(!used[i]){
                        vet[k] = i;
                        k++;
                    }
                }
                for(int n=0; n<vet.length; n++){
                    random_swap(prob.rnd, vet);
                }
                LinkedList<Integer> list = new LinkedList<Integer>();
                for(int n=0; n<vet.length; n++){
                    list.addLast(vet[n]);
                }
                for(int n=0; n<child.set.length; n++){
                    if(child.set[n]==null){
                        child.set[n] = new HTSPEdge(list.removeFirst(), list.removeFirst());
                    }
                }
            }
            //System.out.println("child ok");
            return child;
        }
    }
    
    private static void random_swap(Random rmd, int vet[]){
        int a =  rmd.nextInt(vet.length);
        int b =  rmd.nextInt(vet.length);
        int aux = vet[a];
        vet[a] = vet[b];
        vet[b] = aux;
    }
}
