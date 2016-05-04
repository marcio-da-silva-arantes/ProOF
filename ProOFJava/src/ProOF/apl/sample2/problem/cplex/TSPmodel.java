/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ProOF.apl.sample2.problem.cplex;

import ProOF.CplexExtended.CplexExtended;
import ProOF.apl.sample1.problem.TSP.TSPInstance;
import ilog.concert.IloNumExpr;
import ilog.concert.IloNumVar;

/**
 *
 * @author marcio
 */
public class TSPmodel {
    public final TSPInstance inst;
    public final CplexExtended cpx;  
    public IloNumVar Xij[][];   //binary relaxed
    public IloNumVar Ui[];
    
    public TSPmodel(TSPInstance inst, CplexExtended cpx) {
        this.inst = inst;
        this.cpx = cpx;
    }
    public void model(boolean relaxed) throws Exception {
        if(relaxed){
            Xij = cpx.numVarArray(inst.N, inst.N, 0.0, 1.0, "Xij"); //binary relaxed
        }else{
            Xij = cpx.boolVarArray(inst.N, inst.N, "Xij");
        }
        Ui  = cpx.numVarArray(inst.N-1, 0, inst.N*2, "Ui");
        
        IloNumExpr sum = null;
        for(int i=0; i<inst.N; i++){
            for(int j=0; j<inst.N; j++){
                sum = cpx.SumProd(sum, inst.Cij[i][j], Xij[i][j]);
            }
        }
        cpx.addMinimize(sum);
        
        for(int j=0; j<inst.N; j++){
            sum = null;
            for(int i=0; i<inst.N; i++){
                sum = cpx.SumProd(sum, 1, Xij[i][j]);
            }
            cpx.addEq(sum, 1, "Col["+(j+1)+"]");
        }
        for(int i=0; i<inst.N; i++){
            sum = null;
            for(int j=0; j<inst.N; j++){
                sum = cpx.SumProd(sum, 1, Xij[i][j]);
            }
            cpx.addEq(sum, 1, "Row["+(i+1)+"]");
        }        
        for(int i=0; i<inst.N-1; i++){
            for(int j=0; j<inst.N-1; j++){
                if(i!=j){
                    IloNumExpr aux[] = new IloNumExpr[3];
                    aux[0] = cpx.prod(+1, Ui[i]);
                    aux[1] = cpx.prod(-1, Ui[j]);
                    aux[2] = cpx.prod(inst.N, Xij[i][j]);
                    cpx.addLe(cpx.sum(aux), inst.N-1);
                }
            }
        }
        for(int i=0; i<inst.N; i++){
            Xij[i][i].setLB(0);
            Xij[i][i].setUB(0);
        }
    }
    public void print() throws Exception {
        double vXij[][] = cpx.getValues(Xij);
        System.out.println("------------------------[ Xij ]------------------------");
        for(int i=0; i<inst.N; i++){
            for(int j=0; j<inst.N; j++){
                System.out.format("%8g ", vXij[i][j]);
            }
            System.out.println();
        }
    }
    
}
