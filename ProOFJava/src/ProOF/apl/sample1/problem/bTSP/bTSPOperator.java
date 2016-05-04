/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ProOF.apl.sample1.problem.bTSP;

import ProOF.apl.sample1.FMS.branch.oExpand;
import ProOF.apl.sample1.FMS.branch.oLowerBound;
import ProOF.apl.sample1.problem.TSP.TSPInstance;
import ProOF.com.language.Factory;
import ProOF.opt.abst.problem.meta.codification.Operator;
import java.util.LinkedList;

/**
 *
 * @author marcio
 */
public class bTSPOperator extends Factory<Operator>{
    public static final bTSPOperator obj = new bTSPOperator();
    
    @Override
    public String name() {
        return "ExactTSP Operators";
    }
    @Override
    public Operator build(int index) {
        switch(index){
            case 0: return new EXPAND();
            case 1: return new LOWER_BOUND_01();
        }
        return null;
    }
    private class EXPAND extends oExpand<bTSP, bTSPNode>{
        @Override
        public String name() {
            return "Expand";
        }
        @Override
        public bTSPNode[] expand(bTSP prob, bTSPNode node) throws Exception {
            LinkedList<bTSPNode> list = new LinkedList<bTSPNode>();
            if(!node.is_integer(prob)){
                for(int i=0; i<prob.inst.N; i++){
                    if(!node.contains(i)){
                        bTSPNode no = new bTSPNode(prob, node, i);
                        list.addLast(no);
                    }
                }
            }
            return list.toArray(new bTSPNode[list.size()]);
        }
    }
    private class LOWER_BOUND_01 extends oLowerBound<bTSP, bTSPNode>{
        private double adj_i[][];
        @Override
        public String name() {
            return "LB-01";
        }
        public void calc_adj(TSPInstance inst) throws Exception {
            if(adj_i==null){
                adj_i = new double[inst.N][inst.N-1];
                for(int i=0; i<inst.N; i++){
                    int k=0;
                    for(int j=0; j<inst.N; j++){
                        if(i!=j){
                            adj_i[i][k] = inst.Cij[i][j];
                            k++;
                        }
                    }
                    for(k=1; k<inst.N-1; k++){
                        double aux = adj_i[i][k];
                        int j = k-1;
                        while(j>=0 && adj_i[i][j]>aux){
                            adj_i[i][j+1] = adj_i[i][j];
                            j--;
                        }
                        adj_i[i][j+1] = aux;
                    }
                }
            }
        }
        
        @Override
        public double lower_bound(bTSP prob, bTSPNode node) throws Exception {
            if(node.is_integer(prob)){
                return node.cur_cost;
            }else{
                double lower = adj_i[node.city()][0];
                for(int a=0; a<prob.inst.N; a++){
                    if(!node.contains(a)){
                        lower += adj_i[a][0];
                    }
                }
                return node.cur_cost + lower;
            }
        }
    }
}
