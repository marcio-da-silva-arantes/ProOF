/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ProOF.apl.advanced2.problem.RFFO;

import ProOF.apl.advanced2.problem.GCISTInstance;
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
public class GCIST_RFFO extends RFFOModel{
    
    private GCISTInstance inst = new GCISTInstance();
    private int windowsTypeRF;
    private int windowsTypeFO;
    
    private IloNumVar Yitk[][][];
    private IloNumVar Nitk[][][];
    private IloNumVar Zijtk[][][][];
    private IloNumVar Qt[];
    private IloNumVar Idt[];
    private IloNumVar Iit[][];
    
    private IloNumExpr ObjIdle;
    private IloNumExpr ObjHold;
    private IloNumExpr ObjSwaping;
    private IloNumExpr ObjExSwap;
    private IloNumExpr ObjValue;

    public GCIST_RFFO() throws IloException {
    
    }
    
    @Override
    public String name() {
        return "GCIST";
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
                "product>machine>period", "product>period>machine", 
                "machine>product>period", "machine>period>product",
                "period>machine>product", "period>product>machine");
        
        
        windowsTypeFO = link.Itens("FO-Type", 6,
                "product>machine>period", "product>period>machine", 
                "machine>product>period", "machine>period>product",
                "period>machine>product", "period>product>machine",
                "period>machine|product", "period>snake");
    }
    @Override
    public ArrayList<RelaxVar> relax_variables() throws Exception {
        switch(windowsTypeRF){
            case 0 : return byNearRef(product_machine_period(), 0.5);
            case 1 : return product_machine_period();
            case 2 : return product_period_machine();
            case 3 : return machine_product_period();
            case 4 : return machine_period_product();
            case 5 : return period_machine_product();
            case 6 : return period_product_machine();
            case 7 : return period_machine_product_snake();
        }
        throw new Exception("windowsTypeRF = "+windowsTypeRF+" is invalid"); 
    }

    @Override
    public ArrayList<RelaxVar> fix_variables() throws Exception {
        ArrayList<RelaxVar> list = new ArrayList<RelaxVar>();
        if(windowsTypeFO==0){
            for(int i=0; i<inst.N; i++){//product_machine_period
                for(int k=0; k<inst.K; k++){
                    for(int t=0; t<inst.T; t++){
                        list.add(new RelaxVar(Yitk[i][t][k], RelaxVar.extra(IloNumVarType.Int, 0, inst.MAXik[i][k], Nitk[i][t][k])));
                    }
                }
            }
        }else if(windowsTypeFO==1){
            for(int i=0; i<inst.N; i++){//product_period_machine
                for(int t=0; t<inst.T; t++){
                    for(int k=0; k<inst.K; k++){
                        list.add(new RelaxVar(Yitk[i][t][k], RelaxVar.extra(IloNumVarType.Int, 0, inst.MAXik[i][k], Nitk[i][t][k])));
                    }
                }
            }
        }else if(windowsTypeFO==2){
            for(int k=0; k<inst.K; k++){//machine_product_period
                for(int i=0; i<inst.N; i++){
                    for(int t=0; t<inst.T; t++){
                        list.add(new RelaxVar(Yitk[i][t][k], RelaxVar.extra(IloNumVarType.Int, 0, inst.MAXik[i][k], Nitk[i][t][k])));
                    }
                }
            }
        }else if(windowsTypeFO==3){
            for(int k=0; k<inst.K; k++){//machine_period_product
                for(int t=0; t<inst.T; t++){
                    for(int i=0; i<inst.N; i++){
                        list.add(new RelaxVar(Yitk[i][t][k], RelaxVar.extra(IloNumVarType.Int, 0, inst.MAXik[i][k], Nitk[i][t][k])));
                    }
                }
            }
        }else if(windowsTypeFO==4){
            for(int t=0; t<inst.T; t++){//period_machine_product
                for(int k=0; k<inst.K; k++){
                    for(int i=0; i<inst.N; i++){
                        list.add(new RelaxVar(Yitk[i][t][k], RelaxVar.extra(IloNumVarType.Int, 0, inst.MAXik[i][k], Nitk[i][t][k])));
                    }
                }
            }
        }else if(windowsTypeFO==5){
            for(int t=0; t<inst.T; t++){//period_product_machine
                for(int i=0; i<inst.N; i++){
                    for(int k=0; k<inst.K; k++){
                        list.add(new RelaxVar(Yitk[i][t][k], RelaxVar.extra(IloNumVarType.Int, 0, inst.MAXik[i][k], Nitk[i][t][k])));
                    }
                }
            }
        }else if(windowsTypeFO==6){
            for(int t=0; t<inst.T; t++){//period_machine_product
                for(int k=0; k<inst.K; k++){
                    for(int i=0; i<inst.N; i++){
                        list.add(new RelaxVar(Yitk[i][t][k], RelaxVar.extra(IloNumVarType.Int, 0, inst.MAXik[i][k], Nitk[i][t][k])));
                    }
                }
            }
            for(int t=0; t<inst.T; t++){//period_product_machine
                for(int i=0; i<inst.N; i++){
                    for(int k=0; k<inst.K; k++){
                        list.add(new RelaxVar(Yitk[i][t][k], RelaxVar.extra(IloNumVarType.Int, 0, inst.MAXik[i][k], Nitk[i][t][k])));
                    }
                }
            }
        }else if(windowsTypeFO==7){
            for(int t=0; t<inst.T; t++){//period>snake
                if(t%2==0){
                    for(int k=0; k<inst.K; k++){
                        for(int i=0; i<inst.N; i++){
                            list.add(new RelaxVar(Yitk[i][t][k], RelaxVar.extra(IloNumVarType.Int, 0, inst.MAXik[i][k], Nitk[i][t][k])));
                        }
                    }
                }else{
                    for(int k=inst.K-1; k>=0; k--){
                        for(int i=inst.N-1; i>=0; i--){
                            list.add(new RelaxVar(Yitk[i][t][k], RelaxVar.extra(IloNumVarType.Int, 0, inst.MAXik[i][k], Nitk[i][t][k])));
                        }
                    }
                }
            }
            for(int t=0; t<inst.T; t++){//period>snake
                if(t%2==0){
                    for(int i=0; i<inst.N; i++){
                        for(int k=0; k<inst.K; k++){
                            list.add(new RelaxVar(Yitk[i][t][k], RelaxVar.extra(IloNumVarType.Int, 0, inst.MAXik[i][k], Nitk[i][t][k])));
                        }
                    }
                }else{
                    for(int i=inst.N-1; i>=0; i--){
                        for(int k=inst.K-1; k>=0; k--){
                            list.add(new RelaxVar(Yitk[i][t][k], RelaxVar.extra(IloNumVarType.Int, 0, inst.MAXik[i][k], Nitk[i][t][k])));
                        }
                    }
                }
            }
        }else{
            throw new Exception("windowsTypeFO = "+windowsTypeFO+" is invalid"); 
        }
        return list;
    }
    
    public ArrayList<RelaxVar> product_machine_period() {
        ArrayList<RelaxVar> list = new ArrayList<RelaxVar>();
        for(int i=0; i<inst.N; i++){
            for(int k=0; k<inst.K; k++){
                for(int t=0; t<inst.T; t++){
                    list.add(new RelaxVar(Yitk[i][t][k], RelaxVar.extra(IloNumVarType.Int, 0, inst.MAXik[i][k], Nitk[i][t][k])));
                }
            }
        }
        return list;
    }
    public ArrayList<RelaxVar> product_period_machine() {
        ArrayList<RelaxVar> list = new ArrayList<RelaxVar>();
        for(int i=0; i<inst.N; i++){
            for(int t=0; t<inst.T; t++){
                for(int k=0; k<inst.K; k++){
                    list.add(new RelaxVar(Yitk[i][t][k], RelaxVar.extra(IloNumVarType.Int, 0, inst.MAXik[i][k], Nitk[i][t][k])));
                }
            }
        }
        return list;
    }
    public ArrayList<RelaxVar> machine_product_period() {
        ArrayList<RelaxVar> list = new ArrayList<RelaxVar>();
        for(int k=0; k<inst.K; k++){
            for(int i=0; i<inst.N; i++){
                for(int t=0; t<inst.T; t++){
                    list.add(new RelaxVar(Yitk[i][t][k], RelaxVar.extra(IloNumVarType.Int, 0, inst.MAXik[i][k], Nitk[i][t][k])));
                }
            }
        }
        return list;
    }
    public ArrayList<RelaxVar> machine_period_product() {
        ArrayList<RelaxVar> list = new ArrayList<RelaxVar>();
        for(int k=0; k<inst.K; k++){
            for(int t=0; t<inst.T; t++){
                for(int i=0; i<inst.N; i++){
                    list.add(new RelaxVar(Yitk[i][t][k], RelaxVar.extra(IloNumVarType.Int, 0, inst.MAXik[i][k], Nitk[i][t][k])));
                }
            }
        }
        return list;
    }
    public ArrayList<RelaxVar> period_machine_product() {
        ArrayList<RelaxVar> list = new ArrayList<RelaxVar>();
        for(int t=0; t<inst.T; t++){
            for(int k=0; k<inst.K; k++){
                for(int i=0; i<inst.N; i++){
                    list.add(new RelaxVar(Yitk[i][t][k], RelaxVar.extra(IloNumVarType.Int, 0, inst.MAXik[i][k], Nitk[i][t][k])));
                }
            }
        }
        return list;
    }
    public ArrayList<RelaxVar> period_product_machine() {
        ArrayList<RelaxVar> list = new ArrayList<RelaxVar>();
        for(int t=0; t<inst.T; t++){
            for(int i=0; i<inst.N; i++){
                for(int k=0; k<inst.K; k++){
                    list.add(new RelaxVar(Yitk[i][t][k], RelaxVar.extra(IloNumVarType.Int, 0, inst.MAXik[i][k], Nitk[i][t][k])));
                }
            }
        }
        return list;
    }
    
    public ArrayList<RelaxVar> period_machine_product_snake() {
        ArrayList<RelaxVar> list = new ArrayList<RelaxVar>();
        for(int t=0; t<inst.T; t++){
            if(t%2==0){
                for(int k=0; k<inst.K; k++){
                    for(int i=0; i<inst.N; i++){
                        list.add(new RelaxVar(Yitk[i][t][k], RelaxVar.extra(IloNumVarType.Int, 0, inst.MAXik[i][k], Nitk[i][t][k])));
                    }
                }
            }else{
                for(int k=inst.K-1; k>=0; k--){
                    for(int i=inst.N-1; i>=0; i--){
                        list.add(new RelaxVar(Yitk[i][t][k], RelaxVar.extra(IloNumVarType.Int, 0, inst.MAXik[i][k], Nitk[i][t][k])));
                    }
                }
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
        Yitk    = cpx.numVarArray(inst.N, inst.T, inst.K, 0, 1, "Y");
        Nitk    = cpx.numVarArray(inst.N, inst.T, inst.K, 0, 10000,"N");
        Zijtk   = cpx.numVarArray(inst.N, inst.N, inst.T, inst.K, 0, 1, "Z"); 
        Qt      = cpx.numVarArray(inst.T, 0, Double.MAX_VALUE, "Q");
        Idt     = cpx.numVarArray(inst.T, 0, Double.MAX_VALUE, "Id");
        Iit     = cpx.numVarArray(inst.N, inst.T, 0, Double.MAX_VALUE, "I");
        
        //--------------------------Definindo função objetivo ------------------
        IloNumExpr Obj_t[] = new IloNumExpr[inst.T];
        for(int t=0; t<inst.T; t++){
            Obj_t[t] = cpx.prod(Idt[t], inst.W);
        }

        IloNumExpr Obj_ijtk[][][][] = new IloNumExpr[inst.N][inst.N][inst.T][inst.K];
        for(int i=0; i<inst.N; i++){
            for(int j=0; j<inst.N; j++){
                for(int t=0; t<inst.T; t++){
                    for(int k=0; k<inst.K; k++){
                        Obj_ijtk[i][j][t][k] = cpx.prod(Zijtk[i][j][t][k], inst.Cijk[i][j][k]);
                    }
                }
            }
        }

        IloNumExpr Obj_it[][] = new IloNumExpr[inst.N][inst.T];
        for(int i=0; i<inst.N; i++){
            for(int t=0; t<inst.T; t++){
                Obj_it[i][t] = cpx.prod(Iit[i][t], inst.Hi[i]);
            }
        }
        
        IloNumExpr Obj_ijtk2[][][][] = new IloNumExpr[inst.N][inst.N][inst.T][inst.K];
        for(int i=0; i<inst.N; i++){
            for(int j=0; j<inst.N; j++){
                for(int t=0; t<inst.T; t++){
                    for(int k=0; k<inst.K; k++){
                        
                        Obj_ijtk2[i][j][t][k] = cpx.prod(Zijtk[i][j][t][k], inst.Hi[j/*i ERRADO*/]*inst.Sijk[i][j][k]*(inst.T-t));
                    }
                }
            }
        }
        
        ObjIdle      = cpx.Sum(Obj_t);
        ObjHold      = cpx.Sum(Obj_it);
        ObjSwaping   = cpx.Sum(Obj_ijtk);
        ObjExSwap    = cpx.Sum(Obj_ijtk2);
        ObjValue     = cpx.sum(ObjIdle, ObjHold, ObjSwaping, ObjExSwap);
        cpx.addMinimize(ObjValue);

        //--------------------------------- sub(2) -----------------------------
        for(int i=0; i<inst.N; i++){
            for(int t=0; t<inst.T; t++){

                IloNumExpr Sum_Zjitk_Sjik[][] = new IloNumExpr[inst.N][inst.K];
                for(int j=0; j<inst.N; j++){
                    for(int k=0; k<inst.K; k++){
                        Sum_Zjitk_Sjik[j][k] = cpx.prod(Zijtk[j][i][t][k], -inst.Sijk[j][i][k]);
                    }
                }

                IloNumExpr Sum_Nitk_Pik[]= new IloNumExpr[inst.K];
                for(int k=0; k<inst.K; k++){
                    Sum_Nitk_Pik[k] = cpx.prod(Nitk[i][t][k], inst.Pik[i][k]);
                }
                if(t>0){
                    cpx.addEq(cpx.sum(Iit[i][t], inst.Dit[i][t]) , cpx.sum(Iit[i][t-1], cpx.Sum(Sum_Nitk_Pik), cpx.Sum(Sum_Zjitk_Sjik))
                        , "Estoque("+i+","+t+")");
                }else{
                    cpx.addEq(cpx.sum(Iit[i][t], inst.Dit[i][t]) , cpx.sum(cpx.Sum(Sum_Nitk_Pik), cpx.Sum(Sum_Zjitk_Sjik))
                        , "Estoque("+i+","+t+")");
                }
            }
        }
        

        //--------------------------------- sub(3) -----------------------------
        for(int t=0; t<inst.T; t++){
            IloNumExpr Sum_Nitk_Pik[][] = new IloNumExpr[inst.N][inst.K];
            for(int i=0; i<inst.N; i++){
                for(int k=0; k<inst.K; k++){
                    Sum_Nitk_Pik[i][k] = cpx.prod(Nitk[i][t][k], inst.Pik[i][k]);
                }
            }
            cpx.addEq(cpx.sum(cpx.Sum(Sum_Nitk_Pik), Idt[t]), cpx.prod(Qt[t], inst.C)
                    , "Capacidade("+t+")");
        }

        //--------------------------------- sub(4) -----------------------------
        for(int i=0; i<inst.N; i++){
            for(int t=0; t<inst.T; t++){
                for(int k=0; k<inst.K; k++){
                    //Nitk[i][t][k].setLB(inst.MINik[i][k]);
                    Nitk[i][t][k].setUB(inst.MAXik[i][k]);
                }
            }
        }
        for(int i=0; i<inst.N; i++){
            for(int t=0; t<inst.T; t++){
                for(int k=0; k<inst.K; k++){
                    cpx.addLe(Nitk[i][t][k], cpx.prod(Yitk[i][t][k], inst.MAXik[i][k])
                            , "Uper("+i+","+t+","+k+")");
                }
            }
        }
        //--------------------------------- sub(5) -----------------------------
        for(int i=0; i<inst.N; i++){
            for(int t=0; t<inst.T; t++){
                for(int k=0; k<inst.K; k++){
                    cpx.addGe(Nitk[i][t][k], cpx.prod(Yitk[i][t][k], inst.MINik[i][k])
                            , "Lower("+i+","+t+","+k+")");
                }
            }
        }
        
        
        //--------------------------------- sub(6) -----------------------------
        for(int t=0; t<inst.T; t++){
            for(int k=0; k<inst.K; k++){
                IloNumExpr Sum_Yitk[] = new IloNumExpr[inst.N];
                for(int i=0; i<inst.N; i++){
                    Sum_Yitk[i] = Yitk[i][t][k];
                }
                cpx.addLe(cpx.Sum(Sum_Yitk), 1
                    , "MaxUmProd("+t+","+k+")");
            }
        }
        //--------------------------------- sub(7) -----------------------------
        for(int t=0; t<inst.T-1; t++){
            for(int k=0; k<inst.K; k++){
                IloNumExpr Sum_Yitk[] = new IloNumExpr[inst.N];
                for(int i=0; i<inst.N; i++){
                    Sum_Yitk[i] = Yitk[i][t][k];
                }

                IloNumExpr Sum_Yit_1k[] = new IloNumExpr[inst.N];
                for(int i=0; i<inst.N; i++){
                    Sum_Yit_1k[i] = Yitk[i][t+1][k];
                }
                cpx.addGe(cpx.Sum(Sum_Yitk), cpx.Sum(Sum_Yit_1k)
                    , "Fluxo("+t+","+k+")");
            }
        }
        //--------------------------------- sub(8) -----------------------------
        for(int t=0; t<inst.T; t++){
            for(int k=0; k<inst.K; k++){
                IloNumExpr Sum_Yitk[] = new IloNumExpr[inst.N];
                for(int i=0; i<inst.N; i++){
                    Sum_Yitk[i] = Yitk[i][t][k];
                }
                cpx.addEq(Qt[t], cpx.Sum(Sum_Yitk)
                    , "AtivaForno("+t+","+k+")");
            }
        }

        //--------------------------------- sub(9) -----------------------------
        for(int i=0; i<inst.N; i++){
            for(int j=0; j<inst.N; j++){
                for(int t=0; t<inst.T; t++){
                    for(int k=0; k<inst.K; k++){
                        if(t==0){
                            Zijtk[i][j][t][k].setUB(0);
                        }else{
                            cpx.addLe(cpx.sum(Yitk[j][t][k], Yitk[i][t-1][k],cpx.prod(Zijtk[i][j][t][k], -1)), 1
                                , "Troca("+i+","+j+","+t+","+k+")");
                        }
                    }
                }
            }
        }

        
        //--------------------------------- sub(10) -----------------------------
        for(int t=0; t<inst.T; t++){
            for(int k=0; k<inst.K; k++){
                IloNumExpr Sum_Zijtk[][] = new IloNumExpr[inst.N][inst.N];
                for(int i=0; i<inst.N; i++){
                    for(int j=0; j<inst.N; j++){
                        Sum_Zijtk[i][j] = Zijtk[i][j][t][k];
                    }
                }
                cpx.addLe(cpx.Sum(Sum_Zijtk), Qt[t]
                        , "UmaTroca("+t+","+k+")");
            }
        }
        //--------------------------------- sub(11) -----------------------------
        for(int i=0; i<inst.N; i++){
            for(int t=0; t<inst.T; t++){
                for(int k=0; k<inst.K; k++){
                    IloNumExpr Sum_Zjitk_Sjik[] = new IloNumExpr[inst.N];
                    for(int j=0; j<inst.N; j++){
                        Sum_Zjitk_Sjik[j] = cpx.prod(Zijtk[j][i][t][k], inst.Sijk[j][i][k]);
                    }
                    cpx.addGe(cpx.prod(Nitk[i][t][k], inst.Pik[i][k]), cpx.sum(Sum_Zjitk_Sjik)
                         , "Produz("+i+","+t+","+k+")");
                }
            }
        }
    }
    
    @Override
    public void results(LinkerResults link) throws Exception {
        super.results(link); //To change body of generated methods, choose Tools | Templates.
        if(cpx.getStatus() == IloCplex.Status.Optimal || cpx.getStatus() == IloCplex.Status.Feasible){
            link.writeDbl("Swaping", cpx.getValue(ObjSwaping));
            link.writeDbl("ExSwap", cpx.getValue(ObjExSwap));
            link.writeDbl("Idle", cpx.getValue(ObjIdle));
            link.writeDbl("Hold", cpx.getValue(ObjHold));
        }
    }
    
    @Override
    public void print() throws Exception{
        System.out.println("Solution status  = " + cpx.getStatus());
        System.out.println("Solution value   = " + cpx.getObjValue());
        System.out.println("Solution Swaping = " + cpx.getValue(ObjSwaping));
        System.out.println("Solution ExSwap  = " + cpx.getValue(ObjExSwap));
        System.out.println("Solution Idle    = " + cpx.getValue(ObjIdle));
        System.out.println("Solution Hold    = " + cpx.getValue(ObjHold));

        System.out.println("--------------- Y(i,t,k)---------------------");
        double[][][] v_Yitk = cpx.getValues(Yitk);

        System.out.printf(" K\\T  ");
        for(int t=0; t<inst.T; t++){
            System.out.printf((t<9? "  T%d " : " T%d "), t+1);
        }
        System.out.println();
        for(int k=0; k<inst.K; k++){
            System.out.printf(" K%d | ", k+1);
            for(int t=0; t<inst.T; t++){
                int j = -1;
                for(int i=0; i<inst.N; i++){
                    if(v_Yitk[i][t][k] > 0.9){
                        j = i;
                    }
                }
                if(j>=0){
                    System.out.printf("%4d ", j+1);
                }else{
                    System.out.printf("   - ");
                }
            }
            System.out.println();
        }

        System.out.println("--------------- N(i,t,k) ---------------------");
        double[][][] v_Nitk = cpx.getValues(Nitk);
        for(int k=0; k<inst.K; k++){
            System.out.printf(" K%d | ", k+1);
            for(int t=0; t<inst.T; t++){
                int j = -1;
                for(int i=0; i<inst.N; i++){
                    if(v_Yitk[i][t][k] > 0.9){
                        j = i;
                    }
                }
                if(j>=0){
                    System.out.printf("%4.1f ", v_Nitk[j][t][k]);
                }else{
                    System.out.printf("   - ");
                }
            }
            System.out.println();
        }
        System.out.println("----------------- Q(t)----------------------");
        double[] v_Qt = cpx.getValues(Qt);
        System.out.printf("      ");
        for(int t=0; t<inst.T; t++){
            System.out.printf("%4.0f ", v_Qt[t]);
        }
        System.out.println();

        System.out.println("----------------- Id(t)----------------------");
        double[] v_Idt = cpx.getValues(Idt);
        System.out.printf("      ");
        for(int t=0; t<inst.T; t++){
            System.out.printf("%4.0f ", v_Idt[t]);
        }
        System.out.println();

        System.out.println("----------------- I(i,t)---------------------");
        double[][] v_Iit = cpx.getValues(Iit);
        for(int i=0; i<inst.N; i++){
            System.out.printf(" I%d | ", i+1);
            for(int t=0; t<inst.T; t++){
                System.out.printf("%4.0f ", v_Iit[i][t]);
            }
            System.out.println();
        }

        System.out.println("--------------- X(i,t,k) ---------------------");
        double[][][] v_Xitk = new double[inst.N][inst.T][inst.K];
        double[][][][] v_Zijtk = cpx.getValues(Zijtk);
        for(int k=0; k<inst.K; k++){
            System.out.printf(" K%d | ", k+1);
            for(int t=0; t<inst.T; t++){
                int j = -1;
                for(int i=0; i<inst.N; i++){
                    if(v_Yitk[i][t][k] > 0.9){
                        j = i;
                    }
                }
                if(j>=0){
                    v_Xitk[j][t][k] = inst.Pik[j][k] * v_Nitk[j][t][k];
                    for(int p=0; p<inst.N; p++){
                        v_Xitk[j][t][k] -= inst.Sijk[p][j][k] * v_Zijtk[p][j][t][k];
                    }
                    System.out.printf("%4.0f ", v_Xitk[j][t][k]);
                }else{
                    System.out.printf("   - ");
                }

            }
            System.out.println();
        }

        System.out.println("----------------- Y(i,t,k)---------------------");
        for(int k=0; k<inst.K; k++){
            for(int i=0; i<inst.N; i++){
                System.out.printf("(%d,%d)| ", k+1, i+1);
                for(int t=0; t<inst.T; t++){
                    System.out.printf("%4.0f ", v_Yitk[i][t][k]);
                }
                System.out.println();
            }
        }

        System.out.println("----------------- Z(i,j,t,k)---------------------");
        for(int k=0; k<inst.K; k++){
            for(int i=0; i<inst.N; i++){
                for(int j=0; j<inst.N; j++){
                    System.out.printf("(%d,%d,%d)| ", k+1, i+1, j+1);
                    for(int t=0; t<inst.T; t++){
                        System.out.printf("%4.1f ", v_Zijtk[i][j][t][k]);
                    }
                    System.out.println();
                }
            }
        }
    }

    
}
