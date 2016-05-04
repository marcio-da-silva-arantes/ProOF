/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ProOF.apl.advanced2.problem.RFFO;

import ProOF.apl.advanced2.FMS.RFFO.RelaxVar;
import ProOF.apl.advanced2.FMS.RFFO.RFFOModel;
import ProOF.apl.advanced2.problem.MLCLSPwBInstance;
import ProOF.apl.advanced2.FMS.RFFO.RFFOModel;
import ProOF.apl.advanced2.FMS.RFFO.RelaxVar;
import ProOF.com.Linker.LinkerApproaches;
import ProOF.com.Linker.LinkerParameters;
import ProOF.com.Linker.LinkerResults;
import ilog.concert.IloException;
import ilog.concert.IloNumExpr;
import ilog.concert.IloNumVar;
import ilog.concert.IloNumVarType;
import ilog.cplex.IloCplex;
import java.util.ArrayList;

/**
 *
 * @author marcio
 */
public class MLCLSPwB_RFFO extends RFFOModel{
    
    private MLCLSPwBInstance inst = new MLCLSPwBInstance();
    
    private IloNumVar Xit[][];
    private IloNumVar Sit[][];
    private IloNumVar Bit[][];
    private IloNumVar Wft[][];
    
    private IloNumExpr ObjHold;
    private IloNumExpr ObjBacklogging;
    private IloNumExpr ObjValue;
    
    private int windowsTypeRF;
    private int windowsTypeFO;

    public MLCLSPwB_RFFO() throws IloException {
    
    }
    
    @Override
    public String name() {
        return "MLCLSPwB";
    }
    @Override
    public void services(LinkerApproaches link) throws Exception {
        super.services(link); //To change body of generated methods, choose Tools | Templates.
        inst = link.add(inst);
    }
    @Override
    public void parameters(LinkerParameters link) throws Exception {
        super.parameters(link);
        
        windowsTypeRF = link.Itens("RF-Type", 0, "value-wise", 
                "row-wise", "column-wise");
        
        windowsTypeFO = link.Itens("FO-Type", 2,
                "only row", "only column", 
                "row>>column", "column>>row");
    }
    @Override
    public ArrayList<RelaxVar> relax_variables() throws Exception {
        switch(windowsTypeRF){
            case 0 : return byNearRef(row_wise(), 0.5);
            case 1 : return row_wise(); 
            case 2 : return column_wise();
        }
        throw new Exception("windowsTypeRF = "+windowsTypeRF+" is invalid"); 
    }

    @Override
    public ArrayList<RelaxVar> fix_variables() throws Exception {
        ArrayList<RelaxVar> list = new ArrayList<RelaxVar>();
        if(windowsTypeFO==0){
            for(int f=0; f<inst.NF; f++){
                for(int t=0; t<inst.NT; t++){
                    list.add(new RelaxVar(Wft[f][t]));
                }
            }
        }else if(windowsTypeFO==1){
            for(int t=0; t<inst.NT; t++){
                for(int f=0; f<inst.NF; f++){
                    list.add(new RelaxVar(Wft[f][t]));
                }
            }
        }else if(windowsTypeFO==2){
            for(int f=0; f<inst.NF; f++){
                for(int t=0; t<inst.NT; t++){
                    list.add(new RelaxVar(Wft[f][t]));
                }
            }
            for(int t=0; t<inst.NT; t++){
                for(int f=0; f<inst.NF; f++){
                    list.add(new RelaxVar(Wft[f][t]));
                }
            }
        }else if(windowsTypeFO==3){
            for(int t=0; t<inst.NT; t++){
                for(int f=0; f<inst.NF; f++){
                    list.add(new RelaxVar(Wft[f][t]));
                }
            }
            for(int f=0; f<inst.NF; f++){
                for(int t=0; t<inst.NT; t++){
                    list.add(new RelaxVar(Wft[f][t]));
                }
            }
        }else{
            throw new Exception("windowsTypeFO = "+windowsTypeFO+" is invalid"); 
        }
        return list;
    }
    
    private ArrayList<RelaxVar> row_wise() {
        ArrayList<RelaxVar> list = new ArrayList<RelaxVar>();
        for(int f=0; f<inst.NF; f++){
            for(int t=0; t<inst.NT; t++){
                list.add(new RelaxVar(Wft[f][t]));
            }
        }
        return list;
    }
    private ArrayList<RelaxVar> column_wise() {
        ArrayList<RelaxVar> list = new ArrayList<RelaxVar>();
        for(int t=0; t<inst.NT; t++){
            for(int f=0; f<inst.NF; f++){
                list.add(new RelaxVar(Wft[f][t]));
            }
        }
        return list;
    }
    
    @Override
    public void extra_conversion() throws IloException {

    }
    @Override
    public void model() throws Exception {
        //--------------------------Definindo as variaveis ---------------------
        Xit = cpx.numVarArray(inst.NI, inst.NT, 0, Double.MAX_VALUE, "X");
        Sit = cpx.numVarArray(inst.NI, inst.NT, 0, Double.MAX_VALUE, "S");
        Bit = cpx.numVarArray(inst.NI, inst.NT, 0, Double.MAX_VALUE, "B");
        Wft = cpx.numVarArray(inst.NF, inst.NT, 0.0, 1.0, "W");
        
        cpx.setOut(null);
        
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
                        if(inst.r[i][j]>0.001){
                            aux = cpx.SumProd(aux, inst.r[i][j], Xit[j][t]);
                        }
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
                    if(inst.a[i][k]>0.001){
                        sum = cpx.SumProd(sum, inst.a[i][k], Xit[i][t]);
                    }
                }
                for(int f=0; f<inst.NF; f++){
                    if(inst.st[f][k]>0.001){
                        sum = cpx.SumProd(sum, inst.st[f][k], Wft[f][t]);
                    }
                }
                cpx.addLe(sum, inst.c[k][t]);
            }
        }
    }
    
    @Override
    public void results(LinkerResults link) throws Exception {
        super.results(link); //To change body of generated methods, choose Tools | Templates.
        if(cpx.getStatus() == IloCplex.Status.Optimal || cpx.getStatus() == IloCplex.Status.Feasible){
            link.writeDbl("Backlogging", cpx.getValue(ObjBacklogging));
            link.writeDbl("Holding", cpx.getValue(ObjHold));
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
}
