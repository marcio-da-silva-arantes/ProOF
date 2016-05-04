/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ProOF.apl.advanced2.problem.cplex;

import ProOF.com.Linker.LinkerApproaches;
import ProOF.com.Linker.LinkerResults;
import ProOF.CplexOpt.CplexFull;
import ProOF.apl.advanced2.problem.MLCLSPwBInstance;
import ilog.concert.IloException;
import ilog.concert.IloNumExpr;
import ilog.concert.IloNumVar;
import ilog.cplex.IloCplex.Status;

/**
 *
 * @author marcio
 */
public class MLCLSPwBFull extends CplexFull{
    private MLCLSPwBInstance inst = new MLCLSPwBInstance();
    
    private IloNumVar Xit[][];
    private IloNumVar Sit[][];
    private IloNumVar Bit[][];
    private IloNumVar Wft[][];
    
    private IloNumExpr ObjHold;
    private IloNumExpr ObjBacklogging;
    private IloNumExpr ObjValue;
    
    public MLCLSPwBFull() throws IloException {
        
    }
    @Override
    public String name() {
        return "MLCLSPw-Full";
    }
    @Override
    public void services(LinkerApproaches link) throws Exception {
        super.services(link); //To change body of generated methods, choose Tools | Templates.
        inst = link.add(inst);
    }
    
    @Override
    public void model() throws Exception {
        //--------------------------Definindo as variaveis ---------------------
        Xit = cpx.numVarArray(inst.NI, inst.NT, 0, Double.MAX_VALUE, "X");
        Sit = cpx.numVarArray(inst.NI, inst.NT, 0, Double.MAX_VALUE, "S");
        Bit = cpx.numVarArray(inst.NI, inst.NT, 0, Double.MAX_VALUE, "B");
        Wft = cpx.boolVarArray(inst.NF, inst.NT, "W");
        

        //--------------------------Definindo função objetivo ------------------
        ObjHold = null;
        for(int i=0; i<inst.NI; i++){
            for(int t=0; t<inst.NT; t++){
                ObjHold = cpx.SumProd(ObjHold, inst.BC * inst.hc[i], Bit[i][t]);
            }
        }
        ObjBacklogging = null;
        for(int i=0; i<inst.NI; i++){
            for(int t=0; t<inst.NT; t++){
                ObjBacklogging = cpx.SumProd(ObjBacklogging, inst.hc[i], Sit[i][t]);
            }
        }
        ObjValue = cpx.sum(ObjHold, ObjBacklogging);
        cpx.addMinimize(ObjValue);
        
        //--------------------------------- sub(2) and sub(3) -----------------------------
        for(int i=0; i<inst.NI; i++){
            for(int t=0; t<inst.NT; t++){
                if(i<inst.endp){
                    if(t==0){
                        cpx.addSubject(Sit[i][t],"-",Bit[i][t], "Eq", Xit[i][t],"-",inst.d[i][t]);
                    }else{
                        cpx.addSubject(Sit[i][t],"-",Bit[i][t], "Eq", Sit[i][t-1],"-",Bit[i][t-1],"+",Xit[i][t],"-",inst.d[i][t]);
                    }
                }else{
                    IloNumExpr aux = null;
                    for(int j=0; j<inst.NI; j++){
                        aux = cpx.SumProd(aux, inst.r[i][j], Xit[j][t]);
                    }
                    if(t==0){
                        cpx.addSubject(Sit[i][t], "Eq", Xit[i][t],"-",aux);
                    }else{
                        cpx.addSubject(Sit[i][t], "Eq", Sit[i][t-1],"+",Xit[i][t],"-",aux);
                    }
                }
            }
        }
        //--------------------------------- sub(4) -----------------------------
        for(int i=0; i<inst.NI; i++){
            for(int f=0; f<inst.NF; f++){
                if(inst.pf[i][f]==1.0){
                    for(int t=0; t<inst.NT; t++){
                        cpx.addLe(Xit[i][t], cpx.prod(inst.ub[i][t], Wft[f][t]));
                    }
                }else{
                    for(int t=0; t<inst.NT; t++){
                        cpx.addLe(Xit[i][t], inst.ub[i][t]);
                    }
                }
            }
        }

        //--------------------------------- sub(6) -----------------------------
        for(int k=0; k<inst.NK; k++){
            for(int t=0; t<inst.NT; t++){
                IloNumExpr sum = null;
                for(int i=0; i<inst.NI; i++){
                    sum = cpx.SumProd(sum, inst.a[i][k], Xit[i][t]);//sum = sum + a * x
                }
                for(int f=0; f<inst.NF; f++){
                    sum = cpx.SumProd(sum, inst.st[f][k], Wft[f][t]);
                }
                cpx.addLe(sum, inst.c[k][t]);
            }
        }
    }

    @Override
    public void print() throws Exception{
        System.out.println("Solution status           = " + cpx.getStatus());
        System.out.println("Solution value            = " + cpx.getObjValue());
        System.out.println("Solution Backlogging      = " + cpx.getValue(ObjBacklogging));
        System.out.println("Solution Hold             = " + cpx.getValue(ObjHold));

        System.out.println("----------------- Wft(f,t)---------------------");
        double[][] v_Wft = cpx.getValues(Wft);
        for(int f=0; f<inst.NF; f++){
            System.out.printf(" W(%2d) | ", f+1);
            for(int t=0; t<inst.NT; t++){
                System.out.printf("%4.0f ", v_Wft[f][t]);
            }
            System.out.println();
        }
        System.out.println("----------------- Xjt(j,t)---------------------");
        double[][] v_Xit = cpx.getValues(Xit);
        for(int i=0; i<inst.NI; i++){
            System.out.printf(" X(%2d) | ", i+1);
            for(int t=0; t<inst.NT; t++){
                System.out.printf("%4.0f ", v_Xit[i][t]);
            }
            System.out.println();
        }
        System.out.println("----------------- Ijt(j,t)---------------------");
        double[][] v_Iit = cpx.getValues(Sit);
        for(int i=0; i<inst.NI; i++){
            System.out.printf(" I(%2d) | ", i+1);
            for(int t=0; t<inst.NT; t++){
                System.out.printf("%4.0f ", v_Iit[i][t]);
            }
            System.out.println();
        }
        System.out.println("----------------- Bjt(j,t)---------------------");
        double[][] v_Bit = cpx.getValues(Bit);
        for(int i=0; i<inst.NI; i++){
            System.out.printf(" B(%2d) | ", i+1);
            for(int t=0; t<inst.NT; t++){
                System.out.printf("%4.0f ", v_Bit[i][t]);
            }
            System.out.println();
        }
    }
    @Override
    public void results(LinkerResults link) throws Exception {
        super.results(link); //To change body of generated methods, choose Tools | Templates.
        if(cpx.getStatus() == Status.Optimal || cpx.getStatus() == Status.Feasible){
            link.writeDbl("Backlogging", cpx.getValue(ObjBacklogging));
            link.writeDbl("Holding", cpx.getValue(ObjHold));
        }
    }
}
