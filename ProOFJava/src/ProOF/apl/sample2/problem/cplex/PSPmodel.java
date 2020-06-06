/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ProOF.apl.sample2.problem.cplex;


import CplexExtended.CplexExtended;
import ProOF.apl.sample1.problem.PSP.*;
import ProOF.com.Linker.LinkerResults;
import ilog.concert.IloException;
import ilog.concert.IloIntVar;
import ilog.concert.IloLPMatrix;
import ilog.concert.IloNumExpr;
import ilog.concert.IloNumVar;
import ilog.concert.IloNumVarType;
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
public class PSPmodel {
    public final EasyInstance inst;
    public final CplexExtended cpx;  
    public IloNumVar[][][] X, TS;   //binary relaxed
    public IloNumVar[][] K, Y, Z, Dem;    
    public IloNumVar[][] TH;    
    
    IloNumVar[] TW, UW, L1W;
    
    
    private IloNumVar[][] UH;
    
    public IloNumExpr ObjF1, ObjF2, ObjF3, ObjF4, ObjF5, ObjF6;    
    private IloNumExpr ObjValue;
    
       
    
    double start[][][];
                   
    public PSPmodel(EasyInstance inst, CplexExtended cpx) throws IloException {
        this.inst = inst;
        this.cpx = cpx;
    }
    
    public void getFromModel(PSP prob, cPSP codif) throws IloException, Exception{
      
        
        for(int i=0;i<inst.getNOE();i++)
        {
            for(int j=0;j<inst.getNOD(); j++)
            { 
                boolean flag = false;
                for(int k = 0; k<inst.getNOS();k++){
                    
                    if(this.cpx.getValue(X[i][j][k])  > 0.5)
                    {
                        flag = true;
                        codif.schedule[i][j]= k+1;
                    }                    
                }
                
                if(!flag)
                    codif.schedule[i][j]= 0;
                
            }                
        }
    }
     
    public void startSolutionFromHeuristic(EasyInstance inst, cPSP codif) throws IloException, Exception{
           
        
        if(codif.schedule!=null)
        {
            for(int i=0;i<inst.getNOE();i++)
            {
                for(int j=0;j<inst.getNOD(); j++)
                {
                  //  int k = (int) Math.random()*inst.getNOS();
                    for(int k = 0; k<inst.getNOS();k++){
                            if(codif.schedule[i][j]== k+1)
                            {   
                                X[i][j][k].setLB(1);
                                X[i][j][k].setUB(1);
                            }                    
                            else{
                                X[i][j][k].setLB(0);
                                X[i][j][k].setUB(0);
                            }

                    }
                }                
            }
        } 
    }

    
     public void startSolutionFromFile(EasyInstance inst) throws IloException, Exception{
      
        //String csvFile = "/home/claudio/Documentos/Code/ProOF-master/see.csv";
        String csvFile = "/home/claudio/NetBeansProjects/ProOF-master/ProOFJava/src/ProOF/apl/sample2/problem/cplex/Teste.csv";
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";
        
        try {
            br = new BufferedReader(new FileReader(csvFile));
            int a =0;
            while ((line = br.readLine()) != null) {
                String[] phySched = line.split(cvsSplitBy);
                int i=0;
            //    System.out.println(" "+line);
            //    System.out.println(" "+inst.getNOD());
                
                for(int b=0;b<inst.getNOD();b++)
                {
                    for(int c=0;c < inst.getNOS();c++)
                    {  
                        int value = Integer.parseInt(phySched[i]);
                   //     System.out.println("X["+a+"]["+b+"]["+c+"]: "+value); 
                        
                        if( value == c+1)
                        {
                            X[a][b][c].setUB(1);
                            X[a][b][c].setLB(1);
                        }
                        else{
                            X[a][b][c].setUB(0);
                            X[a][b][c].setLB(0);                        
                        }
                       // System.out.println("X["+a+"]["+b+"]["+c+"]: "+ cpx.getValue(X[a][b][c])); 
                //        System.out.println("X["+a+"]["+b+"]["+c+"]: "+(int)X[a][b][c].getLB()+" - "+(int)X[a][b][c].getUB());                      
                    }    
                    i++;
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
            X = cpx.numVarArray(inst.getNOE(), inst.getNOD(), inst.getNOS(), 0.0, 1.0, "X"); //binary relaxed
            K = cpx.numVarArray(inst.getNOE(), inst.getWeeks(), 0.0, 1.0,  "K");
        }else{
            X = cpx.boolVarArray(inst.getNOE(), inst.getNOD(), inst.getNOS(),  "X");
            K = cpx.boolVarArray(inst.getNOE(), inst.getWeeks(),  "K");
        }
       
        Z = cpx.numVarArray(inst.getNOD(), inst.getNOS(), 0, 100, "Z"); //demand surplass
        Y = cpx.numVarArray(inst.getNOD(), inst.getNOS(), 0, 100, "Y"); //demand 
            
        
        UH  = cpx.numVarArray(inst.getNOE(), inst.getMonths(), 0, Double.POSITIVE_INFINITY,"UH");
        
        
        /*---------------------------------------------------------------------
         *---------------------------------------------------------------------   
        */
        
       
       //--------------------------Definindo função objetivo ------------------       
       /** shiftoff request. */
        ObjF1  = null;
        for(int i=0; i<inst.getNOE(); i++){
            Employee st = inst.getEmployees().get(i);
            
            for(int d=0; d < inst.getNOD();d++){
                for(Shift sft: inst.getShifts()){
                    int s = inst.getShifts().indexOf(sft);
                    
                    if(inst.isOffRequested(st, d, sft.getID())){
                        int pidt = inst.getOff_reqs().get(st.getID()).get(d).get(sft.getID()) !=null ? inst.getOff_reqs().get(st.getID()).get(d).get(sft.getID()): 0;
                        ObjF1 = cpx.SumProd(ObjF1, pidt,  X[i][d][s]);  
                    }
                }
            }              
        }
        
        /** shifton request. */
        ObjF2  = null;
        for(int i=0; i<inst.getNOE(); i++){
            Employee st = inst.getEmployees().get(i);
            
            for(int d=0; d < inst.getNOD();d++){
                for(Shift sft: inst.getShifts()){
                    int s = inst.getShifts().indexOf(sft);
                    
                    if(inst.isRequired(st, d, sft.getID())){
                        int qidt = inst.getOn_reqs().get(st.getID()).get(d).get(sft.getID()) !=null ? inst.getOn_reqs().get(st.getID()).get(d).get(sft.getID()): 0;
                        ObjF2 = cpx.SumProd(ObjF2, qidt,  cpx.sum(1, cpx.prod(-1, X[i][d][s])));  
                    }
                }
            }              
        }
        
        
        
        /** Demand. */
        ObjF3 = null;
        ObjF4 = null;
        for(int j=0; j<inst.getNOD(); j++){
                
            for(Shift sft: inst.getShifts()){
                int k = inst.getShifts().indexOf(sft);
                for (Integer dem: inst.getCovers().get(j).get(sft.getID()).keySet())
                {
                    Map<Integer, Integer> weights = inst.getCovers().get(j).get(sft.getID()).get(dem);
                    
                    for(Integer over: weights.keySet())
                    {
                        ObjF3 =  cpx.SumProd(ObjF3, over, Y[j][k]);     
                        int under = inst.getCovers().get(j).get(sft.getID()).get(dem).get(over); 
                        ObjF4 = cpx.SumProd(ObjF4, under,Z[j][k]);    
                    }                    
                }
            }
        }/**/
        
        ObjF5  = null;
        for(int p=0; p<inst.getNOE(); p++){
            for(int m=0; m<inst.getMonths(); m++){
                
                int cons = inst.getEmployees().get(p).getMaxCons();
                int mDOff = inst.getEmployees().get(p).getMinDaysOff();

                int slotts = inst.getMLenght()/(cons+mDOff);
                int mod = inst.getMLenght()%(cons+mDOff);
                slotts = slotts*cons + mod;

              //  ObjF5 = cpx.SumProd(ObjF5, 100, cpx.square(UH[p][m]));
                ObjF5 = cpx.SumProd(ObjF5, 1, UH[p][m]); 
                
                //to test - 80 and 100.
            }
        }        
        ObjValue = cpx.sum(ObjF1, ObjF2, ObjF3, ObjF4, ObjF5);
        
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
        /**Maximum consecutive shifts. */
        hc5();
        /**Minimum consecutive shifts. */
        hc6();
        /**Minimum consecutive days off. */
        hc7();
        /**Balanced number of weekends. */        
        hc8();
        /** Holidays. */                                                                                                                                                                                                                                                                                                                                                                                                        
        hc9();
        /** Demand. */
       
        /** balanced hours. */
        fc1();
        /** Shifts balance. */
        sc1();        
        
        
        cpx.exportModel("PSP_"+inst.getNOE()+"_"+inst.getNOD()+"_"+inst.getNOS()+".lp");
    }
    
    
    
    public void print() throws Exception {
        
        double vXij[][][] = cpx.getValues(X);
        double h[][] = cpx.getValues(UH);    
 //       double Demand[][] = cpx.getValues(Dem);
        
        BufferedWriter bw = null;
        FileWriter fw = null;
        
        fw = new FileWriter("PSP_"+inst.getNOE()+"_"+inst.getNOD()+"_"+inst.getNOS()+".xls");
        bw = new BufferedWriter(fw);
        
        
        bw.write(" [ Xijk ] ");
        for(int d=0; d<inst.getNOD(); d++){
            bw.write("\t Day "+(d+1));
        }
        
        bw.write("\n");
        
        for(int i=0; i<inst.getNOE(); i++){
            bw.write("PHY["+(i+1)+"]");
            for(int j=0; j<inst.getNOD(); j++){
                for(int k=0;k<inst.getNOS();k++){
                    bw.write(" \t"+(int)vXij[i][j][k]);
                }
            }
            bw.write("\n");
        }
       
        bw.write("\n");
        
        
        BufferedWriter statsBW = null;
        FileWriter statsFW = null;
        
        statsFW = new FileWriter("statsPSP_"+inst.getNOE()+"_"+inst.getNOD()+"_"+inst.getNOS()+".xls");
        statsBW = new BufferedWriter(statsFW);
        
        
        for(int k=0;k<inst.getNOS();k++){
            statsBW.write("\tShift: "+inst.getShifts().get(k).getID()+"\t\t\t");
        }
        statsBW.write("\n");
        statsBW.write("\tCoverage\tDemand\tCoverage\tBalance\t");
        statsBW.write("\n");
        
        
        int demBal = 0;
        for(int j=0; j<inst.getNOD(); j++){
            statsBW.write("Day "+(j+1));
            for(int k=0;k<inst.getNOS();k++){
                
                int dem = 0;
                for(int i=0; i<inst.getNOE(); i++){
                    dem += (int)vXij[i][j][k];
                }
                for(Integer value:inst.getCovers().get(j).get(inst.getShifts().get(k).getID()).keySet())
                {
            //         int cov =  (int) Demand[j][k];
                    int demDiff = (value- dem);
              //      statsBW.write("\t"+cov+"\t"+value+"\t"+dem+"\t"+(cov- dem));
                
                    statsBW.write("\t"+value+"\t"+dem+"\t"+demDiff);
                    if(demDiff > 0 ){
                        demBal ++;
                    }
                }
            }
            statsBW.write("\n");
        }
        double demRate = (double)demBal/(inst.getNOD()*inst.getNOS());
        statsBW.write("Dem Rate: \t"+demRate);
        
        
        statsBW.write("\n");
        
        statsBW.write("\n");
        statsBW.write("\t \t Hour deviations \n");

        for(int t=0; t< inst.getMonths(); t++){
            statsBW.write("\t Target "+(t+1)+"\t Hours \t Diff \t Dev \t");
        }

        statsBW.write("\n");
        int TARGET;
        int sum = 0;
        for(int i=0; i<inst.getNOE(); i++){
            statsBW.write("PHY["+( i+1)+"]");
            for(int m=0; m< inst.getMonths(); m++){
                int hours = 0;
                for(int j=inst.beginingM(m);j<inst.endingM(m);j++){
                    for(int s =0;s<inst.getNOS();s++){
                        
                        hours += ((int)(vXij[i][j][s]) > 0.5 ? 1 : 0)*inst.getShifts().get(s).getDuration();
                    }
                }
                TARGET =(int) (inst.getEmployees().get(i).getMaxMinutes()+inst.getEmployees().get(i).getMinMinutes())/(2*inst.getMonths());
                int diff = (TARGET - hours);
                statsBW.write("\t"+TARGET+"\t"+(int)hours+"\t "+diff+"\t"+h[i][m]);
                if(diff!=0){
                    sum ++;
                }
            }
            statsBW.write("\n");
        }
        
        statsBW.write("DEVIATIONS \t"+sum);
        
        if (statsBW != null)
            statsBW.close();

        if (statsFW != null)
            statsFW.close();
        
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
        
        for(int i=0; i<inst.getNOE(); i++){
            for(int d=0; d<this.inst.getNOD(); d++){
                IloNumExpr Sum_Xijk[] = new IloNumExpr[inst.getNOS()];
                for(int t=0; t< this.inst.getNOS(); t++){
                    Sum_Xijk[t] = X[i][d][t];
                }
                cpx.addLe(cpx.Sum(Sum_Xijk) , 1, "OneShiftADay("+i+","+d+")");
             //   cpx.addLe(cpx.sum(cpx.Sum(Sum_Xijk), cpx.prod(-1, svH1[i][d])) , 1, "OneShiftADay("+i+","+d+")");
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
    
        for(int i=0; i<this.inst.getNOE();i++){
            for(int d=0; d<this.inst.getNOD()-1; d++){
                for(int t = 0; t< inst.getNOS(); t++){
                    
                    Vector<String> conf = this.inst.getShifts().get(t).getForbiddenShifts();
                    
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
    
        for(int i=0; i<this.inst.getNOE();i++){
            for(int t=0; t<this.inst.getNOS(); t++){
                IloNumExpr Sum_Xijk[] = new IloNumExpr[inst.getNOD()];
                for(int d=0; d<this.inst.getNOD(); d++){
                    Sum_Xijk[d] = X[i][d][t];
                }
                cpx.addLe(cpx.Sum(Sum_Xijk), inst.getEmployees().get(i).getMaxShifts().get(t), "maxShifts("+i+","+t+")");
            }
        }
    }
    
    /**
     * Maximum consecutive shifts. The maximum number of
        consecutive shifts, which are allowed to be worked within the
        planning period.
     */
    private void hc5() throws Exception {  
        
        for(int i=0; i<this.inst.getNOE();i++){
            int incr = this.inst.getEmployees().get(i).getMaxCons();
                
            for(int d= 0; d<this.inst.getNOD()-incr; d++){
                IloNumExpr Sum_Xijk[][]= new IloNumExpr[incr+1][this.inst.getNOS()];
                for(int j=d, a=0; j<=d+incr && a <=incr ;j++,a++){
                    for(int t=0; t<this.inst.getNOS(); t++){
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
        for(int i=0; i < this.inst.getNOE(); i++)
        {  
            for(int c=1; c<= this.inst.getEmployees().get(i).getMinCons()-1; c++){
                
                for(int d = 0; d< this.inst.getNOD()-(c+1);d++)
                {   
                    IloNumExpr Sum_Xidt[] = new IloNumExpr[inst.getNOS()];
                    for(int t = 0; t < this.inst.getNOS(); t++)
                    {
                        Sum_Xidt[t] = X[i][d][t];
                    }
                    
                    IloNumExpr Sum_Xijt[][] = new IloNumExpr[c][inst.getNOS()];
                    for(int j=d+1, a=0;j<=d+c && a < c;j++, a++)
                    {
                        for(int t = 0; t < this.inst.getNOS(); t++)
                        {
                            Sum_Xijt[a][t] = X[i][j][t];
                        }
                    }

                    IloNumExpr Sum_Xilt[]= new IloNumExpr[inst.getNOS()];
                    for(int t = 0; t < this.inst.getNOS(); t++)
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
        for(int i=0; i < this.inst.getNOE(); i++)
        { 
            for(int b=1; b<= this.inst.getEmployees().get(i).getMinDaysOff()-1; b++){
                for(int d = 0; d< this.inst.getNOD()-(b+1);d++)
                {   
                    IloNumExpr Sum_Xidt[] = new IloNumExpr[inst.getNOS()];
                    for(int t = 0; t < this.inst.getNOS(); t++)
                    {
                        Sum_Xidt[t] = X[i][d][t];
                    }
                
                    IloNumExpr Sum_Xijt[][] = new IloNumExpr[b][inst.getNOS()];
                    for(int j=d+1, a=0;j<=d+b && a < b;j++, a++)
                    {
                        for(int t = 0; t < this.inst.getNOS(); t++)
                        {
                            Sum_Xijt[a][t] = X[i][j][t];
                        }
                    }

                    IloNumExpr Sum_Xilt[]= new IloNumExpr[inst.getNOS()];
                    for(int t = 0; t < this.inst.getNOS(); t++)
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
        
        for(int i=0; i < this.inst.getNOE(); i++)
        {
            IloNumExpr Sum_Kiw[] = new IloNumExpr[inst.getWeeks()];
            for(int d=5, w =0; d< this.inst.getNOD() && w < inst.getWeeks(); d+=7, w++){
                   
                IloNumExpr Sum_Xist[] = new IloNumExpr[inst.getNOS()];
                IloNumExpr Sum_Xisd[] = new IloNumExpr[inst.getNOS()];
                for(int t = 0; t < this.inst.getNOS(); t++)
                {
                    Sum_Xist[t] = X[i][d][t];
                    if((d+1)<inst.getNOD())
                        Sum_Xisd[t] = X[i][d+1][t];
                }
                Sum_Kiw[w] = K[i][w];
                cpx.addLe(cpx.sum(cpx.Sum(Sum_Xist), cpx.Sum(Sum_Xisd)), cpx.prod(2, K[i][w]), "weekends("+i+","+w+")");
                cpx.addGe(cpx.sum(cpx.Sum(Sum_Xist), cpx.Sum(Sum_Xisd)), K[i][w], "weekends("+i+","+w+")");
            }
            
            cpx.addLe(cpx.Sum(Sum_Kiw) , inst.getEmployees().get(i).getMaxWeekends(), "weeksMax("+i+")");
           
        }
    }
    /**
     Requested days off.
     * shifts must not be assigned to a specified nurse on some specified days
     */
    private void hc9() throws Exception {
    
        for(Employee emp: inst.getEmployees()){
            int i = inst.getEmployees().indexOf(emp);
            for(Integer day: inst.getDaysOff().get(emp.getID()))
            {
                for(int k=0;k<inst.getNOS();k++)
                {
                    cpx.addEq(X[i][day][k], 0);
                }                
            }
        }        
    }
    
    private void sc1() throws IloException {
        
        for(int j=0; j<inst.getNOD(); j++){
            for(Shift sft: inst.getShifts()){
                int k = inst.getShifts().indexOf(sft);
                IloNumExpr Sum_X[] = new IloNumExpr[inst.getNOE()];
                
                for(int i=0; i<inst.getNOE(); i++){
                       Sum_X[i] = X[i][j][k];
                }
                
                for (Integer dem: inst.getCovers().get(j).get(sft.getID()).keySet())
                {   
                    cpx.addLe(cpx.sum(cpx.Sum(Sum_X), cpx.prod(-1, Z[j][k])), dem, "demand("+j+","+k+")");
                    cpx.addGe(cpx.sum(cpx.Sum(Sum_X), Y[j][k]), dem, "demand("+j+","+k+")");
                } 
            }
        }        
    }
    
    private void fc1() throws Exception {
        for(int p=0; p< inst.getNOE(); p++)
        {
            
            int TARGET = inst.getEmployees().get(p).getMaxMinutes()+inst.getEmployees().get(p).getMinMinutes();
            TARGET /= 2;
            TARGET = TARGET/inst.getMonths();
            
            for(int m=0; m<inst.getMonths(); m++){
                IloNumExpr Sum_Xijk[][] = new IloNumExpr[inst.getMLenght()][inst.getNOS()];
        
                for(int d=inst.beginingM(m), a = 0; d< inst.endingM(m) && a<inst.getMLenght(); d++, a++){
                    for(int s=0; s< inst.getNOS(); s++){
                        Sum_Xijk[a][s] = cpx.prod(X[p][d][s], inst.getShifts().get(s).getDuration());
                    }
                }
                
                IloNumExpr sum = cpx.Sum(cpx.Sum(Sum_Xijk));
                                
                
                cpx.addLe(sum, cpx.sum(UH[p][m], TARGET ), "Hours["+p+"]["+m+"]");
                cpx.addGe(sum, cpx.sum(cpx.prod(-1, UH[p][m]), TARGET), "Hours["+p+"]["+m+"]");  
                cpx.addLe(UH[p][m], TARGET, "DEV["+p+"]["+m+"]");
           
            }
        }
    }  
    
}