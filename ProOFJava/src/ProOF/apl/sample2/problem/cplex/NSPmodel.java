/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ProOF.apl.sample2.problem.cplex;

import CplexExtended.CplexExtended;
import ProOF.apl.sample1.problem.NSP.NSPInstance;
import ProOF.apl.sample1.problem.NSP.Staff;
import ProOF.com.Linker.LinkerResults;
import ilog.concert.IloException;
import ilog.concert.IloIntVar;
import ilog.concert.IloNumExpr;
import ilog.concert.IloNumVar;
import ilog.cplex.IloCplex;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Vector;

/**
 *
 * @author dexter
 */
public class NSPmodel {
    public final NSPInstance inst;
    public final CplexExtended cpx;  
    public IloNumVar[][][] X, TS;   //binary relaxed
    public IloNumVar[][] K, Y, Z;    
    public IloNumVar[][] TH;    
    
    IloNumVar[] TW, UW;
    
    private IloNumVar UH[][], US[][][];
    private IloNumExpr ObjF1, ObjF2, ObjF3, ObjF4, ObjF5, ObjF6;    
    private IloNumExpr ObjValue;
    static int calls = 0;
    
    double start[][][];
                   
    public NSPmodel(NSPInstance inst, CplexExtended cpx) throws IloException {
        this.inst = inst;
        this.cpx = cpx;
    }
    
    
      
    public void startSolutionFromHeuristic(NSPInstance inst) throws IloException, Exception{
      
        //String csvFile = "/home/claudio/Documentos/Code/ProOF-master/see.csv";
        String csvFile = "/home/claudio/NetBeansProjects/ProOF-master/ProOFJava/src/ProOF/apl/sample2/problem/cplex/teste.csv";
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";
        
        try {
            br = new BufferedReader(new FileReader(csvFile));
            int a =0;
            while ((line = br.readLine()) != null) {
                String[] phySched = line.split(cvsSplitBy);
                int i=0;
                for(int b=0;b<inst.getHorizon();b++)
                {
                    for(int c=0;c < inst.getNoS();c++)
                    {  
                        int value = Integer.parseInt(phySched[i]);
                        X[a][b][c].setUB(value == 0 ? 0 : 1);
                        X[a][b][c].setLB(value == 0 ? 0 : 1);
                        System.out.println("X["+a+"]["+b+"]["+c+"]: "+(int)X[a][b][c].getLB()+" - "+(int)X[a][b][c].getUB());
                        
                        i++;
                    }
                }
                System.out.println(" ");
                a++;
            }  
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    
    
    public void model(boolean relaxed) throws Exception {
        
        if(relaxed){
            X = cpx.numVarArray(inst.getNoN(), inst.getHorizon(), inst.getNoS(), 0.0, 1.0, "X"); //binary relaxed
            K = cpx.numVarArray(inst.getNoN(), inst.getWeeks(), 0.0, 1.0, "K"); //binary relaxed
        }else{
            X = cpx.boolVarArray(inst.getNoN(), inst.getHorizon(), inst.getNoS(),  "X");
            K = cpx.boolVarArray(inst.getNoN(), inst.getWeeks(),  "K");
        }
       
        Z = cpx.numVarArray(inst.getHorizon(), inst.getNoS(), 0, Double.POSITIVE_INFINITY, "Z"); //binary relaxed
        Y = cpx.numVarArray(inst.getHorizon(), inst.getNoS(), 0, Double.POSITIVE_INFINITY, "Y"); //binary relaxed
        
        UH  = cpx.numVarArray(inst.getNoN(), inst.getMonths(), 0, Double.POSITIVE_INFINITY,"UH");
        TH  = cpx.numVarArray(inst.getNoN(), inst.getMonths(), 0, Double.POSITIVE_INFINITY,"TH");
        
        
        TW  = cpx.numVarArray(inst.getNoN(), 1, Double.POSITIVE_INFINITY,"TW"); //for weekends
        UW  = cpx.numVarArray(inst.getNoN(), 0, Double.POSITIVE_INFINITY,"UW"); //for weekends
        
       //--------------------------Definindo função objetivo ------------------       
       /** shiftoff request. */
        ObjF1  = null;
        for(int i=0; i<inst.getNoN(); i++){
            Staff st = inst.getStaff().get(i);
            int r = 0;
            for(int d=0; d < inst.getHorizon();d++){
                for(int t = 0; t < inst.getNoS(); t++){
                    String str = inst.getShiftByIndex(t);
                        
                    if(st.isNotRequested(d, str)){
                        int pidt = st.getOffReqWeights().get(r).get(str) !=null ? st.getOffReqWeights().get(r).get(str) : 0;
                        ObjF1 = cpx.SumProd(ObjF1, pidt,  X[i][d][t]);  
                        r++;
                    }
                }
            }              
        }
        
        /** shifton request. */
        ObjF2  = null;
        for(int i=0; i<inst.getNoN(); i++){
            Staff st = inst.getStaff().get(i);
            int r = 0;
            
            for(int d=0; d < inst.getHorizon();d++){
                for(int t = 0; t < inst.getNoS(); t++){       

                    String str = inst.getShiftByIndex(t);
                        
                    if(st.isRequired(d, str)){
                        int qidt = st.getReqWeights().get(r).get(str) != null ? st.getReqWeights().get(r).get(str) : 0;
                        ObjF2 = cpx.SumProd(ObjF2, qidt, cpx.sum(1, cpx.prod(-1, X[i][d][t])));   
                        r++;
                    }                 
                }
            }              
        }
        
        /** Demand. */
        ObjF3 = null;
        for(int j=0; j<inst.getHorizon(); j++){
            for(int k=0;k<inst.getNoS();k++){
             //   int weight = inst.getCv().get(j).getWeights().get(k).get(j)[0][0];                   
              //  ObjF3 = cpx.SumNumScalProd(ObjF3, "DemY", 32, inst.getMaxDemand(j, k), weight, Y[j][k]);
               ObjF3 = cpx.SumProd(ObjF3, 100, Y[j][k]);           
            //    ObjF3 = cpx.SumProd(ObjF3, weight,cpx.square(Y[j][k]));     
            }
        }
        
        
        /** Demand. */
        /*ObjF4 = null;
        for(int j=0; j<inst.getHorizon(); j++){
            for(int k=0;k<inst.getNoS();k++){
                int weight = inst.getCv().get(j).getWeights().get(k).get(j)[1][0];      
            //    ObjF4 = cpx.SumNumScalProd(ObjF4, "DemZ", 32, inst.getMaxDemand(j, k), weight, Z[j][k]);
                ObjF4 = cpx.SumProd(ObjF4, weight, Z[j][k]);           
            //    ObjF4 = cpx.SumProd(ObjF4, weight, cpx.square(Z[j][k]));           
            }
        }
        */
         ObjF5  = null;
        for(int p=0; p<inst.getNoN(); p++){
            for(int m=0; m<inst.getMonths(); m++){
                
                int cons = inst.getStaff().get(p).getMaxConsecutiveShifts();
                int mDOff = inst.getStaff().get(p).getMinConsecutiveDaysOff();

                int slotts = inst.getMonthLenght()/(cons+mDOff);
                int mod = inst.getMonthLenght()%(cons+mDOff);
                slotts = slotts*cons + mod;

                int mH = slotts*inst.longestShift();
                
            //    ObjF5 = cpx.SumNumScalProd(ObjF5, "Hours", 32, mH, 100, UH[p][m]);
             //   ObjF5 = cpx.SumProd(ObjF5, 100, cpx.square(UH[p][m]));
                ObjF5 = cpx.SumProd(ObjF5, 1, UH[p][m]);
            }
        }      /* */
        
        //ObjF6  = null;
     //   for(int p=0; p<inst.getNoN(); p++){
                //int mH = slotts*inst.longestShift();
                
       //     ObjF6 = cpx.SumNumScalProd(ObjF6, "Weeks", 32, inst.getWeeks(), 100, UW[p]);
          //  ObjF6 = cpx.SumProd(ObjF6, 100, cpx.square(UW[p]));
            //ObjF6 = cpx.SumProd(ObjF6, 100, UW[p]);
       // }        
        
        ObjValue = cpx.sum(ObjF1, ObjF2, ObjF3, ObjF5);
        
        //ObjValue = cpx.sum(ObjF1, ObjF2, ObjF3, ObjF4, ObjF5, ObjF6);
        
        cpx.addMinimize(ObjValue);
        
          /**********************************************************************
        ******************** SET OF CONSTRAINTS *******************************
        ************************************************************************
        * HC: set of hard constraints in the given order. */
        /**One shift a day. */
        hc1();
        /**Shift rotation (Antagonist shifts). */
        hc2();
        /**Maximum numbers of shifts. */
        hc3();
        /**Minimum and maximum work time. */
     //   hc4();
        /**Maximum consecutive shifts. */
        hc5();
        /**Minimum consecutive shifts. */
        hc6();
        /**Minimum consecutive days off. */
        hc7();
        /**Balanced number of weekends. */
        hc8();
   //     hc8F();
        /** Holidays. */                                                                                                                                                                                                                                                                                                                                                                                                        
        hc9();
        /** Demand. */
        sc2();
        /** balanced hours. */
        fc1();
        /** Shifts balance. */
     //   fc2();        
       /**Cuts on hours considering shift limits and categories of nurses. */
    //    hourGenCut();
     //   shiftGenCut();
     //   hourCut();
     //   shiftCut();
//        startSolutionFromHeuristic(inst);
       
        cpx.exportModel("NSP_"+inst.getNoN()+"_"+inst.getHorizon()+"_"+inst.getNoS()+".lp");
    }
    
    
    
    public void print() throws Exception {
        
        double vXij[][][] = cpx.getValues(X);
        
    /*    double h[][] = cpx.getValues(UH);    
        double th[][] = cpx.getValues(TH);   
        
        double w[] = cpx.getValues(UW);   
        double tw[] = cpx.getValues(TW);   
       /* */
        BufferedWriter bw = null;
        FileWriter fw = null;
        
        fw = new FileWriter("NSP_"+inst.getNoN()+"_"+inst.getHorizon()+"_"+inst.getNoS()+"_"+"="+cpx.getObjValue()+"-"+inst.getID()+".xls");
        bw = new BufferedWriter(fw);
        
        
        bw.write(" [ Xijk ] ");
        for(int d=0; d<inst.getHorizon(); d++){
            for(int t=0; t<inst.getNoS(); t++){
                bw.write("\t Dia "+(d+1)+": "+(inst.getShiftByIndex(t)));
            }
        }
        
        bw.write("\n");
        
        for(int i=0; i<inst.getNoN(); i++){
            bw.write("PHY["+(i+1)+"]");
            for(int j=0; j<inst.getHorizon(); j++){
                for(int k=0;k<inst.getNoS();k++){
                    bw.write(" \t"+(int)vXij[i][j][k]);
                }
            }
            bw.write("\n");
        }
        
        bw.write("\n");
        
        for(int k=0;k<inst.getNoS();k++){
            bw.write("\tDemand : "+inst.getShiftByIndex(k)+"\t Coverage: "+inst.getShiftByIndex(k)+"\tDifference: "+inst.getShiftByIndex(k));
        }
        
        bw.write("\n");
        for(int j=0; j<inst.getHorizon(); j++){
            bw.write("Day "+(j+1));
            for(int k=0;k<inst.getNoS();k++){
                
                int dem = 0;
                for(int i=0; i<inst.getNoN(); i++){
                    dem += (int)vXij[i][j][k];
                }
                bw.write("\t"+inst.getMaxDemand(j, k)+"\t"+dem+"\t"+(inst.getMaxDemand(j, k) - dem));
            }
            bw.write("\n");
        }
        
        
        bw.write("\n");
        bw.write("\t \t Hour deviations \n");

        for(int t=0; t< inst.getMonths(); t++){
             bw.write("\t Dev. "+(t+1)+"\t Anc. Targ \t Diff \t MHours  \t mHours");
        }

        bw.write("\n");
        int TARGET;
        int sum = 0;
        for(int i=0; i<inst.getNoN(); i++){
            bw.write("PHY["+( i+1)+"]");
            for(int m=0; m< inst.getMonths(); m++){
                int hours = 0;
                for(int j=inst.begining(m);j<inst.ending(m);j++){
                    for(int s =0;s<inst.getNoS();s++){
                        
                        hours += ((int)vXij[i][j][s] > 0.5 ? 1 : 0)*inst.getShifts().get(s).getDuration();
                    }
                }
                TARGET =(int) (inst.getStaff().get(i).getMaxTotalMinutes()+inst.getStaff().get(i).getMinTotalMinutes())/(2*inst.getMonths());
                int diff = (TARGET - hours);
                bw.write("\t"+(int)hours+"\t"+TARGET+"\t "+diff+"\t"+inst.getStaff().get(i).getMaxTotalMinutes()/inst.getMonths()+"\t"+inst.getStaff().get(i).getMinTotalMinutes()/inst.getMonths());
                if(diff!=0){
                    sum ++;
                }
            }
            bw.write("\n");
        }
        
        bw.write("DEVIATIONS \t"+sum);
        
        bw.write("\n");
     /*   bw.write("\t \t Hour Deviation \n");

        for(int t=0; t< inst.getMonths(); t++){
             bw.write("Deviation \t Target"+(t+1)+"\t Org. target \t target diff.");
        }

        bw.write("\n");
        int newTarget;
        for(int i=0; i<inst.getNoN(); i++){
            bw.write("PHY["+( i+1)+"]");
            for(int j=0; j< inst.getMonths(); j++){
                newTarget =(int) (inst.getStaff().get(i).getMaxTotalMinutes()+inst.getStaff().get(i).getMinTotalMinutes())/2;
                bw.write("\t"+h[i][j]+"\t"+(int)th[i][j]+"\t"+newTarget/inst.getMonths()+"\t"+((newTarget/inst.getMonths())-th[i][j]));
            }
            bw.write("\n");
        }
        
        
        bw.write("\n");
        bw.write("\t \t Weekend deviations \n");
        bw.write("\t Deviation Weekends  \t Targ Value \t Diff \t Max Weekends ");
        bw.write("\n");
        
        for(int i=0; i<inst.getNoN(); i++){
            bw.write("PHY["+( i+1)+"]");
                
            bw.write("\t"+(int)w[i]+"\t"+(int)tw[i]+"\t "+(int)(tw[i]- w[i])+"\t"+inst.getStaff().get(i).getMaxWeekends());
            
            bw.write("\n");
        }/**/
        if (bw != null)
            bw.close();

        if (fw != null)
            fw.close();
        
    }
    

    /**
    *An employee cannot be assigned more than one shift on a single day.
    **/
    private void hc1() throws Exception {
        
        //System.out.println("Ceil of 9.22"+Math.ceil(9.22));
        
        for(int i=0; i<inst.getNoN(); i++){
            for(int d=0; d<this.inst.getHorizon(); d++){
                IloNumExpr Sum_Xijk[] = new IloNumExpr[inst.getNoS()];
                for(int t=0; t< this.inst.getNoS(); t++){
                    Sum_Xijk[t] = X[i][d][t];
                }
                cpx.addLe(cpx.Sum(Sum_Xijk), 1, "OneShiftADay("+i+","+d+")");
            }
        }
    }
    
    /**
     *Shift rotation. 
     * the shift assignment of nurses on two consecutive 
     * days must comply with the pre-defined set of shift
     * patterns (rotations). The shift patterns prevent forbidden shift
     * sequences
     */
    private void hc2() throws Exception{
    
        for(int i=0; i<this.inst.getNoN();i++){
            for(int d=0; d<this.inst.getHorizon()-1; d++){
                for(int t = 0; t< inst.getNoS(); t++){
                    
                    LinkedList<String> conf = this.inst.listOfConflicts(t);
                    
                    if(!conf.isEmpty())
                    {
                        for(String c:conf){
                            cpx.addLe(cpx.sum(X[i][d][t], X[i][d+1][inst.getShiftIndexByID(c)]),1, "Workload("+i+","+d+")"); 
                        }
                    }
                }
            }
        }
    }
    
    /**
     *** Maximum number of shifts.
     * the maximum number of shift types that can be 
     * assigned to each nurse within the planning period
     */
    private void hc3() throws Exception {
    
        for(int i=0; i<this.inst.getNoN();i++){
            for(int t=0; t<this.inst.getNoS(); t++){
                IloNumExpr Sum_Xijk[] = new IloNumExpr[inst.getHorizon()];
                for(int d=0; d<this.inst.getHorizon(); d++){
                    Sum_Xijk[d] = X[i][d][t];
                }
                cpx.addLe(cpx.Sum(Sum_Xijk), inst.getStaff().get(i).getMaxShifts().get(t), "maxShifts("+i+","+t+")");
            }
        }
    }
    
    /**
     * Maximum total minutes. 
     * the maximum amount of total time in minutes that can be assigned to each nurse within the
        planning period.
     */
    private void hc4() throws Exception {
        for(int i=0; i< inst.getNoN(); i++)
        {   
            IloNumExpr Sum_Xijk[][] = new IloNumExpr[inst.getHorizon()][inst.getNoS()];
            
            for(int d=0; d< inst.getHorizon(); d++){
                for(int s=0; s< inst.getNoS(); s++){
                    Sum_Xijk[d][s] = cpx.prod(X[i][d][s], inst.getShifts().get(s).getDuration());
                }
            }
            
                
            cpx.addGe(cpx.Sum(cpx.Sum(Sum_Xijk)), inst.getStaff().get(i).getMinTotalMinutes(), "minMinutes("+i+")");   
            cpx.addLe(cpx.Sum(cpx.Sum(Sum_Xijk)), inst.getStaff().get(i).getMaxTotalMinutes(), "maxMinutes("+i+")");
        }
    }

    /**
     * Maximum consecutive shifts. The maximum number of
        consecutive shifts, which are allowed to be worked within the
        planning period.
     */
    private void hc5() throws Exception {  
        
        for(int i=0; i<this.inst.getNoN();i++){
            int incr = this.inst.getStaff().get(i).getMaxConsecutiveShifts();
                
            for(int d= 0; d<this.inst.getHorizon()-incr; d++){
                IloNumExpr Sum_Xijk[][]= new IloNumExpr[incr+1][this.inst.getNoS()];
                for(int j=d, a=0; j<=d+incr && a <=incr ;j++,a++){
                    for(int t=0; t<this.inst.getNoS(); t++){
                        Sum_Xijk[a][t] = X[i][j][t];
                    }
                }
                cpx.addLe(cpx.Sum(Sum_Xijk), incr, "Consecutive("+i+","+d+"-"+(d+incr)+")");
            }
        }
    }
    
    
    /**
     * Minimum consecutive shifts. The maximum number of shifts an employee can work without a day 
     * off. For example, part-time employees sometimes do not work as many consecutive shifts as 
     * full-time staff.
     */
    private void hc6() throws Exception {  
        for(int i=0; i < this.inst.getNoN(); i++)
        {  
            for(int c=1; c<= this.inst.getStaff().get(i).getMinConsecutiveShifts()-1; c++){
                
                for(int d = 0; d< this.inst.getHorizon()-(c+1);d++)
                {   
                    IloNumExpr Sum_Xidt[] = new IloNumExpr[inst.getNoS()];
                    for(int t = 0; t < this.inst.getNoS(); t++)
                    {
                        Sum_Xidt[t] = X[i][d][t];
                    }
                    
                    IloNumExpr Sum_Xijt[][] = new IloNumExpr[c][inst.getNoS()];
                    for(int j=d+1, a=0;j<=d+c && a < c;j++, a++)
                    {
                        for(int t = 0; t < this.inst.getNoS(); t++)
                        {
                            Sum_Xijt[a][t] = X[i][j][t];
                        }
                    }

                    IloNumExpr Sum_Xilt[]= new IloNumExpr[inst.getNoS()];
                    for(int t = 0; t < this.inst.getNoS(); t++)
                    {
                        Sum_Xilt[t] = X[i][d+c+1][t];
                    }
                   
                    cpx.addGe(cpx.sum(cpx.Sum(Sum_Xidt), cpx.sum(c, cpx.prod(-1, cpx.Sum(Sum_Xijt))), cpx.Sum(Sum_Xilt)),1, "MinConsecutive("+i+","+c+","+d+","+(d+c+1)+")");
                }
            }
        }
    }
    
       
    /**
     * Maximum consecutive shifts. 
     *  The minimum number of consecutive shifts, 
     * which are allowed to be worked within the planning period
     */
    private void hc7() throws Exception {          
        for(int i=0; i < this.inst.getNoN(); i++)
        { 
            for(int b=1; b<= this.inst.getStaff().get(i).getMinConsecutiveDaysOff()-1; b++){
                for(int d = 0; d< this.inst.getHorizon()-(b+1);d++)
                {   
                    IloNumExpr Sum_Xidt[] = new IloNumExpr[inst.getNoS()];
                    for(int t = 0; t < this.inst.getNoS(); t++)
                    {
                        Sum_Xidt[t] = X[i][d][t];
                    }
                
                    IloNumExpr Sum_Xijt[][] = new IloNumExpr[b][inst.getNoS()];
                    for(int j=d+1, a=0;j<=d+b && a < b;j++, a++)
                    {
                        for(int t = 0; t < this.inst.getNoS(); t++)
                        {
                            Sum_Xijt[a][t] = X[i][j][t];
                        }
                    }

                    IloNumExpr Sum_Xilt[]= new IloNumExpr[inst.getNoS()];
                    for(int t = 0; t < this.inst.getNoS(); t++)
                    {
                        Sum_Xilt[t] = X[i][d+b+1][t];
                    }
                    cpx.addGe(cpx.sum(cpx.sum(1, cpx.prod(-1, cpx.Sum(Sum_Xidt))) , cpx.Sum(Sum_Xijt), cpx.prod(-1, cpx.Sum(Sum_Xilt))), 0, "MinConsDaysOff("+i+","+d+")");
                }
            }
        }
    }
    
    /**
     Maximum number of weekends.
     * The maximum number of worked weekends (a weekend 
     * is defined as being worked if there is a shift on 
     * Saturday or Sunday) within the planning period.
     */
    
    private void hc8() throws Exception {  
        
        for(int i=0; i < this.inst.getNoN(); i++)
        {
            IloNumExpr Sum_Kiw[] = new IloNumExpr[inst.getWeeks()];
            for(int d=5, w =0; d< this.inst.getHorizon() && w < inst.getWeeks(); d+=7, w++){
                   
                IloNumExpr Sum_Xist[] = new IloNumExpr[inst.getNoS()];
                IloNumExpr Sum_Xisd[] = new IloNumExpr[inst.getNoS()];
                for(int t = 0; t < this.inst.getNoS(); t++)
                {
                    Sum_Xist[t] = X[i][d][t];
                    if((d+1)<inst.getHorizon())
                        Sum_Xisd[t] = X[i][d+1][t];
                }
                Sum_Kiw[w] = K[i][w];
                cpx.addLe(cpx.sum(cpx.Sum(Sum_Xist), cpx.Sum(Sum_Xisd)), cpx.prod(2, K[i][w]), "weekends("+i+","+w+")");
                cpx.addGe(cpx.sum(cpx.Sum(Sum_Xist), cpx.Sum(Sum_Xisd)), K[i][w], "weekends("+i+","+w+")");
            }
            
            cpx.addLe(cpx.Sum(Sum_Kiw), inst.getStaff().get(i).getMaxWeekends(), "weeksMax("+i+")");
        }
    }
    
    
    private void hc8F() throws Exception, IloException {  
        
        for(int i=0; i < this.inst.getNoN(); i++)
        {
            IloNumExpr Sum_Kiw[] = new IloNumExpr[inst.getWeeks()];
            for(int d=5, w =0; d< this.inst.getHorizon() && w < inst.getWeeks(); d+=7, w++){
                   
                IloNumExpr Sum_Xist[] = new IloNumExpr[inst.getNoS()];
                IloNumExpr Sum_Xisd[] = new IloNumExpr[inst.getNoS()];
                for(int t = 0; t < this.inst.getNoS(); t++)
                {
                    Sum_Xist[t] = X[i][d][t];
                    if((d+1)<inst.getHorizon())
                        Sum_Xisd[t] = X[i][d+1][t];
                }
                Sum_Kiw[w] = K[i][w];
                cpx.addLe(cpx.sum(cpx.Sum(Sum_Xist), cpx.Sum(Sum_Xisd)), cpx.prod(2, K[i][w]), "weekends("+i+","+w+")");
                cpx.addGe(cpx.sum(cpx.Sum(Sum_Xist), cpx.Sum(Sum_Xisd)), K[i][w], "weekends("+i+","+w+")");
            }
            
                
            cpx.addLe(TW[i],inst.getStaff().get(i).getMaxWeekends());
            cpx.addLe(UW[i], inst.getStaff().get(i).getMaxWeekends());
            
       //     cpx.addLe(TW[i],inst.getWeeks());
       //     cpx.addLe(UW[i], inst.getWeeks());
      //      
            cpx.addGe(cpx.Sum(Sum_Kiw), cpx.sum(cpx.prod(-1, UW[i]), TW[i]), "weekBal["+i+"]");  
            cpx.addLe(cpx.Sum(Sum_Kiw), cpx.sum(UW[i], TW[i]), "weekBal["+i+"]");
            
            //cpx.addLe(super.Sum(Sum_Kiw), inst.getEmployees().get(i).getMaxWeekends(), "weeksMax("+i+")");
        }
    }
    
    
    /**
     Requested days off.
     * shifts must not be assigned to a specified nurse on some specified days
     */
    private void hc9() throws Exception {
    
        for(int i=0; i<this.inst.getNoN();i++){
            for(int j=0; j<this.inst.getHorizon(); j++){
                if(inst.getStaff().get(i).isUnavailable(j)){
                    for(int k=0; k<this.inst.getNoS(); k++){
                        X[i][j][k].setUB(0);
                    }
                }
            }
        }
    }
    
    
    
    private void sc2() throws IloException {
        
         for(int j=0; j<inst.getHorizon(); j++){
            for(int k=0; k<inst.getNoS(); k++){
                IloNumExpr Sum_X[] = new IloNumExpr[inst.getNoN()];
                
                for(int i=0; i<inst.getNoN(); i++){
                       Sum_X[i] = X[i][j][k];
                }
                //cpx.addLe(cpx.sum(cpx.Sum(Sum_X), cpx.prod(-1, Z[j][k])), this.inst.getMaxDemand(j, k), "MaxDemand("+j+","+k+")");
                //cpx.addGe(cpx.sum(cpx.Sum(Sum_X), Y[j][k]), this.inst.getMaxDemand(j, k), "MaxDemand("+j+","+k+")");
                
                
                cpx.addLe(cpx.Sum(Sum_X), cpx.sum(Y[j][k], this.inst.getMaxDemand(j, k) ), "MaxDemand["+j+"]["+k+"]");
                cpx.addGe(cpx.Sum(Sum_X), cpx.sum(cpx.prod(-1, Y[j][k]),this.inst.getMaxDemand(j, k)), "MaxDemand["+j+"]["+k+"]");  
                
           
          }
        }
    }
    
    
    
    private void fc1() throws Exception {
        for(int p=0; p< inst.getNoN(); p++)
        {   
            int value = inst.getStaff().get(p).getMaxConsecutiveShifts()+inst.getStaff().get(p).getMinConsecutiveDaysOff();
            
            int rest = (int) Math.round(inst.getMonthLenght()/value);
            rest = rest*inst.longestShift()*inst.getStaff().get(p).getMaxConsecutiveShifts();
            
            for(int m=0; m<inst.getMonths(); m++){
                IloNumExpr Sum_Xijk[][] = new IloNumExpr[inst.getMonthLenght()][inst.getNoS()];
        
                for(int d=inst.begining(m), a = 0; d< inst.ending(m) && a<inst.getMonthLenght(); d++, a++){
                    for(int s=0; s< inst.getNoS(); s++){
                        Sum_Xijk[a][s] = cpx.prod(X[p][d][s], inst.getShifts().get(s).getDuration());
                    }
                }
                                
                IloNumExpr sum = cpx.Sum(cpx.Sum(Sum_Xijk));
            //   cpx.addLe(sum, cpx.sum(UH[p][m], TARGET ), "Hours["+p+"]["+m+"]");
           //     cpx.addGe(sum, cpx.sum(cpx.prod(-1, UH[p][m]), TARGET), "Hours["+p+"]["+m+"]");                   
             
                cpx.addLe(sum, cpx.sum(UH[p][m], TH[p][m] ), "Hours["+p+"]["+m+"]");
                cpx.addGe(sum, cpx.sum(cpx.prod(-1, UH[p][m]), TH[p][m]), "Hours["+p+"]["+m+"]");  
                
                cpx.addLe(UH[p][m], rest, "TARG["+p+"]["+m+"]");
                cpx.addLe(TH[p][m], rest, "TARG["+p+"]["+m+"]");    
            }
        }
    }
    
    /*
    
    
    private void fc1() throws Exception {
        for(int p=0; p< inst.getNoN(); p++)
        {   
            int cons = inst.getStaff().get(p).getMaxConsecutiveShifts();
            int mDOff = inst.getStaff().get(p).getMinConsecutiveDaysOff();
            
            int slotts = inst.getMonthLenght()/(cons+mDOff);
            int mod = inst.getMonthLenght()%(cons+mDOff);
            slotts = slotts*cons + mod;
            
            int maxHours = slotts*inst.longestShift();
            int TARGET = inst.getStaff().get(p).getMaxTotalMinutes()+inst.getStaff().get(p).getMinTotalMinutes();
             TARGET /= 2;
          //  int TARGET = inst.getStaff().get(p).getMaxTotalMinutes();
            TARGET = TARGET/inst.getMonths();
            
            for(int m=0; m<inst.getMonths(); m++){
                IloNumExpr Sum_Xijk[][] = new IloNumExpr[inst.getMonthLenght()][inst.getNoS()];
        
                for(int d=inst.begining(m), a = 0; d< inst.ending(m) && a<inst.getMonthLenght(); d++, a++){
                    for(int s=0; s< inst.getNoS(); s++){
                        Sum_Xijk[a][s] = cpx.prod(X[p][d][s], inst.getShifts().get(s).getDuration());
                    }
                }
                                
                IloNumExpr sum = cpx.Sum(cpx.Sum(Sum_Xijk));
            //   cpx.addLe(sum, cpx.sum(UH[p][m], TARGET ), "Hours["+p+"]["+m+"]");
           //     cpx.addGe(sum, cpx.sum(cpx.prod(-1, UH[p][m]), TARGET), "Hours["+p+"]["+m+"]");                   
             
                cpx.addLe(sum, cpx.sum(UH[p][m], TH[p][m] ), "Hours["+p+"]["+m+"]");
                cpx.addGe(sum, cpx.sum(cpx.prod(-1, UH[p][m]), TH[p][m]), "Hours["+p+"]["+m+"]");  
                
                cpx.addLe(UH[p][m], TARGET, "TARG["+p+"]["+m+"]");
                cpx.addLe(TH[p][m], TARGET, "TARG["+p+"]["+m+"]");    
            }
        }
    }
    private void fc1() throws Exception {
        for(int p=0; p< inst.getNoN(); p++)
        {
            int value = inst.getStaff().get(p).getMaxConsecutiveShifts()+inst.getStaff().get(p).getMinConsecutiveDaysOff();
            
            int rest = (int) Math.round(inst.getMonthLenght()/value);
            rest = rest*inst.longestShift()*inst.getStaff().get(p).getMaxConsecutiveShifts();
            
            for(int m=0; m<inst.getMonths(); m++){
                IloNumExpr Sum_Xijk[][] = new IloNumExpr[inst.getMonthLenght()][inst.getNoS()];
        
                for(int d=inst.begining(m), a = 0; d< inst.ending(m) && a<inst.getMonthLenght(); d++, a++){
                    for(int s=0; s< inst.getNoS(); s++){
                        Sum_Xijk[a][s] = cpx.prod(X[p][d][s], inst.getShifts().get(s).getDuration());
                    }
                }
                
                IloNumExpr sum = cpx.Sum(cpx.Sum(Sum_Xijk));
                
                cpx.addLe(sum, cpx.sum(UH[p][m], TH[p][m] ), "Hours["+p+"]["+m+"]");
                cpx.addGe(sum, cpx.sum(cpx.prod(-1, UH[p][m]), TH[p][m]), "Hours["+p+"]["+m+"]");  
                cpx.addLe(UH[p][m], rest, "DEV["+p+"]["+m+"]");
                cpx.addLe(TH[p][m], rest, "TARG["+p+"]["+m+"]");    
            }
        }
    }   */
    
    private void shiftCut() throws Exception {
        for(int p=0; p<this.inst.getNoN();p++){
            
            int value = inst.getStaff().get(p).getMaxConsecutiveShifts()+inst.getStaff().get(p).getMinConsecutiveDaysOff();
            int rest = (int) Math.round(inst.getMonthLenght()/value);
            rest = rest*inst.longestShift()*inst.getStaff().get(p).getMaxConsecutiveShifts();
                             
            for(int m=0; m<inst.getMonths(); m++){
                for(int s=0; s< inst.getNoS(); s++){
                    IloNumExpr Sum_Xijk[][] = new IloNumExpr[inst.getMonthLenght()][inst.getNoS()];
                    for(int d=inst.begining(m), a = 0; d< inst.ending(m) && a<inst.getMonthLenght(); d++, a++){
                        Sum_Xijk[a][s] = X[p][d][s];
                    }     
                    
                    int max = inst.getStaff().get(p).getMaxShifts().get(s);
                    int duration = inst.getShifts().get(s).getDuration();
                    int cvr = (int) rest/duration;
                    if(max>0){
                        cpx.addLe(cpx.Sum(cpx.Sum(Sum_Xijk)), cpx.sum(US[p][m][s], Math.min(max,cvr-1)), "genericCutShift["+p+"]["+m+"]["+s+"]");
                    }
                    else{
                        cpx.addLe(cpx.Sum(cpx.Sum(Sum_Xijk)), cpx.sum(US[p][m][s], cvr-1), "genericCutShift["+p+"]["+m+"]["+s+"]");
                    }
                        
                }
            }
        }
    }
    
    
  
    private  void shiftGenCut() throws Exception {
        
        for(int p=0; p<this.inst.getNoN();p++)
        {   
            double beta = 1;

            for(int m=0; m<inst.getMonths(); m++){
                
                for(int s=0; s< inst.getNoS(); s++){
                    
                    int max = inst.getStaff().get(p).getMaxShifts().get(s);
                    
                    if(max>0){
                        
                        int arrDays[] = createArray(inst.begining(m),inst.ending(m));
                        int data[] =new int[max];
                        IloNumExpr Sum_Xijk[][]= new IloNumExpr[data.length][inst.getNoS()];
                        combinationUtil(arrDays, data, 0, arrDays.length-1, 0, max);
                        Sum_Xijk = new IloNumExpr[data.length][inst.getNoS()];
                    
                        for(int j=0; j<data.length;j++){
                            Sum_Xijk[j][s] = X[p][data[j]][s];
                        }     
                        beta = calcBeta2(max-1);
                        cpx.addLe(cpx.Sum(cpx.Sum(Sum_Xijk)), cpx.sum(cpx.prod(beta, UH[p][m]), max-1), "shifGen["+p+"]["+m+"]["+s+"]");
                    }
                }
            }
        }
    }
    
    
    private  void hourGenCut() throws Exception {
        int cat[] = inst.getCategory();
        for(int c=0; c< cat.length; c++)
        {
            for(Integer p:inst.getNursesOfCategory(cat[c]))
            {   int TARGET = inst.getStaff().get(p).getMaxTotalMinutes() + inst.getStaff().get(p).getMinTotalMinutes();
                TARGET = (int) TARGET/2;
                double beta = 1;

                for(int sc=inst.getShiftsByDuration().length-1; sc >= 0; sc--)
                {
                    for(int m=0;m<inst.getMonths();m++)
                    {
                        int nComb = (int) (TARGET/inst.getShiftsByDuration()[sc]) + 1; 
                        int arrDays[] = createArray(inst.begining(m),inst.ending(m));
                        int data[] = new int[nComb];
                        combinationUtil(arrDays, data, 0, arrDays.length-1, 0, nComb);                        
                        IloNumExpr Xms[][] = new IloNumExpr[data.length][inst.getNoS()];

                        for(int j=0; j<data.length;j++)
                        {
                            for(String s: inst.getShiftOfCategory(inst.getShiftsByDuration()[sc]))
                            {   
                                int idx =inst.getShiftIndexByID(s);
                                Xms[j][idx] = cpx.prod(X[p][data[j]][idx], inst.getShifts().get(idx).getDuration());
                                beta = calcBeta(TARGET,inst.getShifts().get(idx).getDuration());
                            }  
                        }                        
                        cpx.addLe(cpx.Sum(cpx.Sum(Xms)), cpx.sum(cpx.prod(beta, UH[p][m]), TARGET), "liftHour["+p+"]["+m+"]");    
                    }                   
                }
            }
        }
    }
    
    private void hourCut() throws Exception {
        int cat[] = inst.getCategory();
        final int STD_CAT = inst.getShifts().get(0).getDuration();
        double beta = 1;
        for(int c=0; c<cat.length;c++)
        {                
            for(Integer p:inst.getNursesOfCategory(cat[c]))
            {   
                boolean NON_STD = false;
                for(int sc=inst.getShiftsByDuration().length-1; sc >= 0; sc--)
                {   
                    int max = 0;
                    int TARGET = inst.getStaff().get(p).getMaxTotalMinutes()+inst.getStaff().get(p).getMinTotalMinutes();
                    
                    TARGET = (int)TARGET/2;
                    TARGET = TARGET/inst.getMonths();
                    for(int m=0;m<inst.getMonths();m++)
                    {
                        IloNumExpr Sum_Xijk[][] = new IloNumExpr[inst.getMonthLenght()][inst.getNoS()];
                    
                        for(int d=inst.begining(m), a = 0; d< inst.ending(m) && a<inst.getMonthLenght(); d++, a++)
                        {
                            for(String s: inst.getShiftOfCategory(inst.getShiftsByDuration()[sc]))
                            {  
                                int idx = inst.getShiftIndexByID(s);
                                Sum_Xijk[a][idx] = cpx.prod(X[p][d][idx], inst.getShifts().get(idx).getDuration());
                                
                                if(inst.getShifts().get(idx).getDuration() != STD_CAT){
                                    NON_STD = true;
                                    if(inst.getStaff().get(p).getMaxShifts().get(idx) > 0)
                                    {    
                                        max =  inst.getStaff().get(p).getMaxShifts().get(idx)*inst.getShifts().get(idx).getDuration();
                                    }
                                }
                                else{
                                    max = TARGET;
                                }
                                beta = calcBeta(TARGET,inst.getShifts().get(idx).getDuration());
                            }
                        }   
                        cpx.addLe(cpx.Sum(cpx.Sum(Sum_Xijk)), cpx.sum(cpx.prod(beta, UH[p][m]) , Math.min(max,TARGET)), "generic["+p+"]["+m+"]");
                    }
                }
            }
        }
    }  
    
    private double calcBeta(int TARGET, int duration) {
        int qtt = (int) TARGET/60;
        int value [] = new int[qtt]; 
        double beta [] = new double[qtt];
        
        double coef = 1;
        if(qtt > 1){
            for(int j = 0; j<qtt; j++)
            {
                value[j] = (int) ((TARGET+ ((j+1)*60))/duration);
                value[j] = value[j]*duration; 
                if(value[j] > TARGET)
                {   int diff = value[j] - TARGET;
                    beta[j] = ((j+1)*60)/(diff);
                } 
                else{
                    beta[j] = 10000000;
                }
            }
            coef = minumum(beta);
        }
        
        return (1/coef);
    }
    
    
    private double calcBeta2(int TARGET) {
         int qtt = 50;
        int value [] = new int[qtt]; 
        double beta [] = new double[qtt];
        
        double coef = 1;
        for(int j = 0; j<qtt; j++)
        {
            value[j] = (int) (TARGET+ (j+1));

            if(value[j] > TARGET)
            {   int diff = value[j] - TARGET;
                beta[j] = (j+1)/(diff);
            } 
            else{
                beta[j] = 10000000;
            }
        }
        coef = minumum(beta);
            
        return (1/coef);
    }
    
    private double calcBeta(int TARGET, int pi, int duration) {
        int qtt = (int) TARGET/60;
        int value [] = new int[qtt]; 
        double beta [] = new double[qtt];
        
        double coef = 1;
        if(qtt > 1){
            for(int j = 0; j<qtt; j++)
            {
                value[j] = (int) ((TARGET+ ((j+1)*60))/duration);
                value[j] = value[j]*duration; 
                
                if(value[j] > pi)
                {   int diff = value[j] - pi;
                    beta[j] = ((j+1)*60)/(diff);
                } 
                else{
                    beta[j] = 10000000;
                }
            }
            coef = minumum(beta);
        }
        coef = minumum(beta);
        
        return (1/coef);
    }
    
    private double calcBetaMIP2(int TARGET, int pi) throws IloException {
        int size = 5;
        double [] neta = new double[size];
        double [] beta = new double[size];
       
        for(int s=1; s<=size;s++){
            CplexExtended cpx2 = new CplexExtended();
            IloNumVar Y[] = cpx2.boolVarArray(5);
            int coef[] = {7,6,5,3,2};
            
            IloNumExpr aux[] = new IloNumExpr[coef.length];
            IloNumExpr ObjFunct = null;
            for(int i=0; i<2; i++){
               ObjFunct = cpx2.SumProd(ObjFunct, 1, Y[i]);
            }
            cpx2.addMaximize(ObjFunct);
            
            for(int i =0; i< coef.length; i++)
            {
                aux[i] = cpx2.prod(coef[i], Y[i]);
            }
            cpx2.addLe(cpx2.sum(aux), TARGET+s, "Col");
                    
            
            
            if(cpx2.solve())
            {
                neta[s-1]  = cpx2.getObjValue();
                beta[s-1] = s/(neta[s-1] - pi);
            }
            
        }
        
        return minumum(beta);
    }
    
    private double calcBetaMIP(int TARGET, int pi, int shiftCat) throws IloException {
        int size = (int) TARGET/60;
        double [] neta = new double[size];
        double [] beta = new double[size];
       
       
        for(int s=60; s<=TARGET;s+=60){
            CplexExtended cpx2 = new CplexExtended();
            IloNumVar O[][] = cpx2.boolVarArray(inst.getHorizon(), inst.getNoS(), "O");
            
             
                IloNumExpr ObjFunct = null;
                for(int m=0;m<inst.getMonths();m++)
                {
                    for(int d=inst.begining(m); d< inst.ending(m); d++)
                    {   
                        for(String sc:inst.getShiftOfCategory(shiftCat))
                        {  
                            int idx = inst.getShiftIndexByID(sc);

                           ObjFunct = cpx2.SumProd(ObjFunct, inst.getShifts().get(idx).getDuration(), O[d][idx]);
                        }
                    }
                }
                cpx2.addMaximize(ObjFunct);
                
                for(int d=0; d<this.inst.getHorizon(); d++){
                    IloNumExpr Sum_Xijk[] = new IloNumExpr[inst.getNoS()];
                    for(int t=0; t< this.inst.getNoS(); t++){
                        Sum_Xijk[t] = O[d][t];
                    }
                    cpx2.addLe(cpx2.Sum(Sum_Xijk), 1, "OneShiftADay("+d+")");
                }
            
                for(int m=0;m<inst.getMonths();m++)
                {
                    IloNumExpr Sum_Xijk[][] = new IloNumExpr[inst.getMonthLenght()][inst.getNoS()];
        
                    for(int d=inst.begining(m), a = 0; d< inst.ending(m) && a<inst.getMonthLenght(); d++, a++){
                        for(int f=0; f< inst.getNoS(); f++){
                            Sum_Xijk[a][f] = cpx2.prod(O[d][f], inst.getShifts().get(f).getDuration());
                        }
                    }
                    IloNumExpr sum = cpx2.Sum(cpx2.Sum(Sum_Xijk));

                    cpx2.addLe(sum, TARGET+s, "CUT["+m+"]");
                }
          


                if(cpx2.solve())
                { int a = s/60 -1;
                    neta[a]  = cpx2.getObjValue();
                    beta[a] = s/(neta[a] - pi);
                }
               // cpx2.exportModel("knapsack"+s+".lp");  
        }
        
        return 1/minumum(beta);
    }
    
    private double minumum(double [] array){
        double min = array[0];

        for(int i =1; i<array.length; i++)
        {
            if(array[i]<min)
            {
                min = array[i];
            }
        }

        return min;
    }
    
    private void combinationUtil(int arr[], int data[], int start, 
                                int end, int index, int r) 
    { 
        // Current combination is ready to be printed, print it 
        if (index == r) 
        {   int aux[] = new int[data.length];
            
            return ;
        } 
        // replace index with all possible elements. The condition 
        // "end-i+1 >= r-index" makes sure that including one element 
        // at index will make a combination with remaining elements 
        // at remaining positions 
        for (int i=start; i<=end && end-i+1 >= r-index; i++) 
        { 
            data[index] = arr[i]; 
            combinationUtil(arr, data, i+1, end, index+1, r);             
        }
        
    } 
     

    private int [] createArray(int begining, int ending) {
        
        int arr[] = new int[ending - begining];
        for(int i =begining, a =0; i< ending && a<arr.length;i++,a++)
        {
            arr[a] = i;
        }
        
        return arr; 
    }

    private int nrCombinations(int n, int r) {
     
        int numerator = 1, denominator = 1;
        if (r > n - r) {
                r = n - r;
        }
        for (int i = (int) 1L; i <= r; ++i) {
                denominator *= i;
        }
        for (int i = (int) (n - r + 1L); i <= n; ++i) {
                numerator *= i;
        }
        return numerator / denominator;
    }
  
    
}