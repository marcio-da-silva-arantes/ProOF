/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ProOF.apl.sample1.problem.PSP;

import CplexExtended.CplexExtended;
import ProOF.apl.advanced2.FO_local_search.oFOOperator;
import ProOF.apl.sample1.problem.PSP.*;
import ProOF.apl.sample2.problem.cplex.PSPmodel;
import ProOF.com.language.Factory;
import ProOF.gen.operator.oCrossover;
import ProOF.gen.operator.oMutation;
import ProOF.gen.operator.oInitialization;
import ProOF.gen.operator.oLocalMove;
import ProOF.opt.abst.problem.meta.Solution;
import ProOF.opt.abst.problem.meta.codification.Codification;
import java.util.Random;
import ProOF.opt.abst.problem.meta.codification.Operator;
import ilog.concert.IloException;
import ilog.cplex.IloCplex;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
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
public class PSPOperator extends Factory<Operator> {

    public static final PSPOperator obj = new PSPOperator();

    static LinkedList  <Integer> infeasiblePhy = new LinkedList<>();
    
    public String name() {
        return "PSP-Operator"; //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Operator build(int index) throws Exception {
        switch(index) {
            case 0:
                    return new Greedy();
            case 1: 
                    return new Mut2Exchange();
            case 2:
                    return new Mut3Exchange();
            case 3: 
                    return new MutBlockExchange();
            case 4: 
                    return new MutDoubleExchange();
            case 5: 
                    return new Mov2Exchange();
            case 6:
                    return new Mov3Exchange();
            case 7: 
                    return new MovRandom2();
            case 8: 
                    return new TwoPoints();
            case 9: 
                    return new Uniform();
            case 10: 
                    return new FOPSP();
            case 11: 
                    return new MutRandom();
            
        }
        return null;
    }
    
        
    private class GreedyInitial extends oInitialization<PSP, cPSP> {

        public String name() {
            return "Greedy";
        }

        public void initialize(PSP prob, cPSP codif) throws Exception {
            
            prob.inst.getEmployees().forEach((emp) -> {
                int p = prob.inst.getEmployees().indexOf(emp);
                    
                Integer max = Collections.max(emp.getMaxShifts());
                int index = emp.getMaxShifts().indexOf(max);
                
                for(int d=0; d<prob.inst.getNOD();d++){
                    if((d % 7) != 5 && (d % 7) != 6){
                        codif.schedule[p][d] = index+1;
                    }
                }
                
                codif.schedule[p] = setDaysOff(prob, codif, emp);
                codif.schedule[p] = correctSingleAssignment(prob, codif, emp);
            
                
            });
            finalCod(prob, codif);
        }
            

    }
    
    private class iGreedy extends oInitialization<PSP, cPSP> {

        public String name() {
            return "iGreedy";
        }

        public void initialize(PSP prob, cPSP codif) throws Exception {
                        
            for (Employee emp : prob.inst.getEmployees()) {
                int p = prob.inst.getEmployees().indexOf(emp);
                
                codif.schedule[p] = setDaysOff(prob, codif, emp);
                codif.schedule[p] = opt(prob, codif, emp);   
                
                
                
            //    initRepair(prob, codif);

            }                   
            finalCod(prob, codif);
            
        }
    }
    
    private class Greedy extends oInitialization<PSP, cPSP> {

        public String name() {
            return "iGreedy";
        }

        public void initialize(PSP prob, cPSP codif) throws Exception {
                        
            for (Employee emp : prob.inst.getEmployees()) {
                int p = prob.inst.getEmployees().indexOf(emp);
                
                codif.schedule[p] = setDaysOff(prob, codif, emp);
                
            }                   
            assign(prob, codif);
            finalCod(prob, codif);
            
        }
    }
    
    static int[] setDaysOff(PSP prob, cPSP codif, Employee emp) {
        int p = prob.inst.getEmployees().indexOf(emp);

        if (prob.inst.getDaysOff() != null) {
            if (prob.inst.getDaysOff().get(emp.getID()) != null) {

                Vector<Integer> daysOff = prob.inst.getDaysOff().get(emp.getID());

                for (Integer d : daysOff) {
                    codif.schedule[p][d] = -1;
                }
            }
        }
        return codif.schedule[p];
    }
    
    
    
    
    static int[] setMinDaysOff(PSP prob, cPSP codif, Employee emp) {
        int p = prob.inst.getEmployees().indexOf(emp);

        int start = emp.getMinDaysOff();
        int window = prob.inst.getNOD();

        for (int d = 1; d < window; d++) {
          
            if(codif.schedule[p][d] <=0 && codif.schedule[p][d-1]>0)
            {
                for(int i=d;i<d+start;i++){
                    if(i<window)
                        codif.schedule[p][i]=-1;                    
                }
            }
               
        }
        return codif.schedule[p];
    }
    
    static int[] setOnRequest(PSP prob, cPSP codif, Employee emp) {
        int p = prob.inst.getEmployees().indexOf(emp);

        if (prob.inst.getOn_reqs() != null) {

            if (prob.inst.getOn_reqs().get(emp.getID()) != null) {
                Map<Integer, Map<String, Integer>> map;
                map = prob.inst.getOn_reqs().get(emp.getID());

                for (Integer d : map.keySet()) {
                    Map<String, Integer> map2;
                    map2 = map.get(d);

                    for (String str : map2.keySet()) {
                        int id = prob.inst.getShiftIndexByID(str);
                        codif.schedule[p][d] = id + 1;
                    }
                }
            }
        }
        return codif.schedule[p];
    }
    
    static int[] setOffRequest(PSP prob, cPSP codif, Employee emp) {
        int p = prob.inst.getEmployees().indexOf(emp);

        if (prob.inst.getOff_reqs() != null) {
            if (prob.inst.getOff_reqs().get(emp.getID()) != null) {

                Map<Integer, Map<String, Integer>> map;
                map = prob.inst.getOff_reqs().get(emp.getID());

                for (Integer d : map.keySet()) {
                    Map<String, Integer> map2;
                    map2 = map.get(d);

                    for (String str : map2.keySet()) {
                        int id = prob.inst.getShiftIndexByID(str);
                        if (prob.inst.getShifts().size() > 1) {
                            id = prob.inst.getOtherShift(id);
                            codif.schedule[p][d] = id + 1;
                        } else {
                            codif.schedule[p][d] = -1;
                        }
                    }
                }
            }
        }
        return codif.schedule[p];
    }

    
    static int[] correctSingleAssignment(PSP prob, cPSP codif, Employee emp) {
        int p = prob.inst.getEmployees().indexOf(emp), consec =  emp.getMinCons();
        int window = prob.inst.getNOD();
        for(int d= 0; d<window; d++){
            int sum = 0;//1 1 1 1 0 0 
            int i = d-1;
            
            while(i>=0 && codif.schedule[p][i]>0){
               sum++;
               i--;
            }
            
            
            i = d;
            while(i<window && codif.schedule[p][i]>0){
               sum++;
               i++;
            }
            
            
            if(sum < consec && codif.schedule[p][d]>0){
                codif.schedule[p][d] = -1;
            }
            
            
        }
        return codif.schedule[p];
    }
    
    static int[] consecutive(PSP prob, cPSP codif, Employee emp) {
        int p = prob.inst.getEmployees().indexOf(emp), consec =  emp.getMaxCons(), maxHours = emp.getMaxMinutes();
        int hours = 0;  
        Integer max = Collections.max(emp.getMaxShifts());
        int index = emp.getMaxShifts().indexOf(max);
        int duration = prob.inst.getShifts().get(index).getDuration();
        
        for (int d = 0; d < prob.inst.getNOD(); d++) {      
            int seq = 0;
            
            if(codif.schedule[p][d] > 0){                
                for(int j=d; j<consec+d;j++)
                {
                    if(j<  prob.inst.getNOD())
                    if(codif.schedule[p][j]>-1){
                        seq++;
                    }
                }
                            
                for(int j=d; j<seq+d;j++)
                {
                    hours += duration;
                    if(hours > maxHours)
                    {break;}
                    codif.schedule[p][j] = codif.schedule[p][d];
                }
                d+=seq;
            }
        }
        
        return codif.schedule[p];
    }
    
    
    static int[] minDaysOff(PSP prob, cPSP codif, Employee emp) {
        int p = prob.inst.getEmployees().indexOf(emp), consec = emp.getMinDaysOff(), maxHours = emp.getMaxMinutes();
        
        for (int d = 0; d < prob.inst.getNOD(); d++) {      
            int seq = 0;
            
            if(codif.schedule[p][d] > 0){                
                for(int j=d; j<consec+d;j++)
                {
                    if(j<prob.inst.getNOD()){
                        if(codif.schedule[p][j]>0){
                            seq++;
                        }
                    }
                }
            
                for(int j=d+seq; j<seq+d+consec;j++)
                {
                    if(j<prob.inst.getNOD()){
                        if(codif.schedule[p][j]==0){
                            codif.schedule[p][j] = -1;
                        }
                    }
                }
            }  
        }        
        
        
        return codif.schedule[p];
    }
    
    static int[] markToOptimize(PSP prob, cPSP codif, Employee emp) {
        int p = prob.inst.getEmployees().indexOf(emp);
        
        for (int d = prob.inst.getNOD()-1; d > 0; d--) {      
            
            if(codif.schedule[p][d] == 0){                
                codif.schedule[p][d] = -1;
            }  
        }
        
        return codif.schedule[p];
    }
    
    
    static int[] minDaysOff2(PSP prob, cPSP codif, Employee emp) {
        int p = prob.inst.getEmployees().indexOf(emp), consec = emp.getMinDaysOff(), maxHours = emp.getMaxMinutes();
        
        for (int d = prob.inst.getNOD()-1; d > 0; d--) {      
            int seq = 0;
            
            if(codif.schedule[p][d] > 0){                
                for(int j=d; j>d-consec;j--)
                {
                    if(j>=0)
                    if(codif.schedule[p][j]>0){
                        seq++;
                    }
                }
            
                for(int j=d-seq; j>d-consec-seq;j--)
                {
                    if(j>=0){
                        if(codif.schedule[p][j]==0){
                            codif.schedule[p][j] = -1;
                        }
                    }
                }
            }  
        }
        
        
        
        return codif.schedule[p];
    }
    
    static int[] assignToOptimize(PSP prob, cPSP codif, Employee emp) {
        int p = prob.inst.getEmployees().indexOf(emp);
        
        for (int d = 0; d < prob.inst.getNOD(); d++) {      
            
            if(codif.schedule[p][d] ==0 && !prob.inst.getWeekends().contains(d)){   
                codif.schedule[p][d] = -2;
            }
        }
        
        return codif.schedule[p];
    }
    
    
    private int[] assignMissingDays(PSP prob, cPSP codif, Employee emp) {
        int p = prob.inst.getEmployees().indexOf(emp), consec = emp.getMinCons(), maxHours = emp.getMinMinutes();
        int hours = 0, window = prob.inst.getNOD();  
        Integer max = Collections.max(emp.getMaxShifts());
        int index = emp.getMaxShifts().indexOf(max);
        int duration = prob.inst.getShifts().get(index).getDuration();
        
        for (int d = 0; d < window; d++) {      
            if(codif.schedule[p][d] > 0){
                int dur = prob.inst.getShifts().get(codif.schedule[p][d]-1).getDuration();
                hours+=dur;
            } 
        }        
        
        if(hours > 0){
            for (int d = 0; d < window-1; d++) { 
                int seq = 0;     
               
                  if(codif.schedule[p][d] == 0){
                    for(int j=d; j<consec+d;j++)
                    {
                        if(j<prob.inst.getNOD()){
                            if(codif.schedule[p][j]==0){
                                seq++;
                            }
                            else{
                                seq = 0;
                            }
                        }
                    }     

                    if(seq > 1){
                        for(int j=d; j<seq+d;j++)
                        {
                            if(hours < maxHours)
                            {
                                if(codif.schedule[p][j]==0){
                                    codif.schedule[p][j] = index+1;
                                    hours += duration;
                                }
                            }
                        }               
                    }
                } 
                
            /* */
            }
        }
        return codif.schedule[p];
    }
    
    
    private int[] opt(PSP prob, cPSP codif, Employee emp) {
        int p = prob.inst.getEmployees().indexOf(emp);
        
        for (int d = 0; d < prob.inst.getNOD(); d++) {      
           
            if(prob.rnd.nextDouble()> 0.5) 
            {
                codif.schedule[p][d] = 0;
            }
            else {
                    if(codif.schedule[p][d] != -1)
                        codif.schedule[p][d] = prob.rnd.nextInt(1,prob.inst.getNOS());
                    else
                        codif.schedule[p][d] = 0;
                    
            }
        }        
        
        return codif.schedule[p];
    }
    
    
    
    private void assign(PSP prob, cPSP codif) {
        
       
        
        for(int d = 0; d<prob.inst.getNOD();d++){
            for(int s=0;s<prob.inst.getNOS();s++){
                for(Integer demand:prob.inst.getCovers().get(d).get(prob.inst.getShifts().get(s).getID()).keySet())
                {   int dem = 0;
                    while(dem<demand){
                        int p = prob.rnd.nextInt(0, prob.inst.getNOE()-1);
                      
                        if(codif.schedule[p][d]==0){
                            codif.schedule[p][d] = s+1;
                            dem++;
                        }
                    }
                }
            }
       }
    }
    
    private static void finalCod(PSP prob, cPSP codif) {

        for(Employee emp:prob.inst.getEmployees()){
            int window = prob.inst.getNOD();
            int p = prob.inst.getEmployees().indexOf(emp);

            for (int d = 0; d < window; d++) {
                if (codif.schedule[p][d] <= -1) {
                    codif.schedule[p][d] = 0;
                }
            }
        }
    }
    
    private int [] setAntagonist(PSP prob, cPSP codif, Employee emp){
    
        int p = prob.inst.getEmployees().indexOf(emp);
        
        for(int d=0; d< prob.inst.getNOD()-1; d++)
        {
            int sft2 = codif.schedule[p][d+1] -1, sft = codif.schedule[p][d] -1;
            Shift shift; 
            String sftStr;
            if(sft> -1 && sft2 > -1){

                shift = prob.inst.getShifts().get(sft);

                sftStr = prob.inst.getShifts().get(sft2).getID();

                if(shift.getForbiddenShifts().contains(sftStr))
                { 
                    codif.schedule[p][d+1] = codif.schedule[p][d];
                }
            }
        }
        return codif.schedule[p];
    }
        
    
    private int[] setConsecHours2(PSP prob, cPSP codif, Employee emp) {

        int p = prob.inst.getEmployees().indexOf(emp);
        int window = prob.inst.getNOD();
        int cons = emp.getMaxCons();
        
        
        Object obj = Collections.max(emp.getMaxShifts());
        int s = emp.getMaxShifts().indexOf(obj);
        
        for(int j=0;j< window; j++){
                
            if(!prob.inst.getWeekends().contains(j)){
                if(codif.schedule[p][j] == 0){
                    codif.schedule[p][j] = s+1;
                }
            }
            
        }
        
        return codif.schedule[p];
    }
    
    
    private int[] setConsecHours(PSP prob, cPSP codif, Employee emp) {

        int p = prob.inst.getEmployees().indexOf(emp);
        int window = prob.inst.getNOD();
        int cons = emp.getMaxCons();
        
        
        Object obj = Collections.max(emp.getMaxShifts());
        int s = emp.getMaxShifts().indexOf(obj);
        
        for(int j=0;j< window - cons; j+=(cons+2)){
                
            for(int k=j; k<j+cons;k++)
            {   
                
                if(codif.schedule[p][k]==0){
                    codif.schedule[p][k] = s+1;
                }
            }
        }
               
        return codif.schedule[p];
    }
       
    
    private int[] setMaxHours(PSP prob, cPSP codif, Employee emp) {

        int hDemand = 0;
        int p = prob.inst.getEmployees().indexOf(emp);
        
        Object obj = Collections.max(emp.getMaxShifts());
        int s = emp.getMaxShifts().indexOf(obj);
        int d = 0; 
        
        while(hDemand < emp.getMaxMinutes()){
            if(codif.schedule[p][d] == 0)
            {    
                codif.schedule[p][d] = s+1;
                hDemand+= prob.inst.getShifts().get(s).getDuration();
                d++;
            }
            else{
                d++;
            }
        }
        return codif.schedule[p];
    }
    
    private int[] setMinHours(PSP prob, cPSP codif, Employee emp) {

        int hDemand = 0;
        int p = prob.inst.getEmployees().indexOf(emp);
        
        int window = prob.inst.getNOD();
        
        for (int d = 0; d < window; d++) {
            if(codif.schedule[p][d]>0)
            {   
                int s = codif.schedule[p][d];
                hDemand+= prob.inst.getShifts().get(s-1).getDuration();
            }
        }
        
        int target = emp.getMinMinutes() + emp.getMaxMinutes();
        target = target/2;
        
        while(hDemand<target)
        {
            int i = 0;
            do{
                i = prob.rnd.nextInt(0,window-1);
                codif.schedule[p][i] = prob.rnd.nextInt(1,prob.inst.getNOS());
            
            }while(codif.schedule[p][i]==-1);
            hDemand+= prob.inst.getShifts().get(codif.schedule[p][i]-1).getDuration();
        }
        return codif.schedule[p];
    }   
    
    
    
    private int[] setConsDays2(PSP prob, cPSP codif, Employee emp) {

        int incr = emp.getMaxCons();
        int window = prob.inst.getNOD() - 1;
        int p = prob.inst.getEmployees().indexOf(emp), aux = 0;

        for (int d = window; d > incr; d--) {
            Object obj = Collections.max(emp.getMaxShifts());
            int electedShift = emp.getMaxShifts().indexOf(obj)+1;
            int sum = 0;  
            
            if (codif.schedule[p][d] > -1) {
                for (int j = d; j > d - incr; j--) {
                    if (codif.schedule[p][j] != -1) {
                        codif.schedule[p][j] = electedShift;
                    }
                }

                if (sum > incr) {
                    aux = sum;
                }
                while (aux > incr) {
                    if (codif.schedule[p][d] > 0) {
                        codif.schedule[p][d] = 0;
                    }
                    aux--;
                }
            }
        }
        return codif.schedule[p];
    }
    
    
    private int[] setConsDays(PSP prob, cPSP codif, Employee emp) {

        int incr = emp.getMaxCons();
        int window = prob.inst.getNOD() - 1;
        int p = prob.inst.getEmployees().indexOf(emp), aux = 0;

        for (int d = window; d > incr; d--) {
            int sum = 0;
            if (codif.schedule[p][d] > 0) {
                for (int j = d; j >= d - incr; j--) {
                    if (codif.schedule[p][j] > 0) {
                        sum++;
                    } else {
                        sum = 0;
                    }
                }

                if (sum > incr) {
                    aux = sum;
                }
                while (aux > incr) {
                    if (codif.schedule[p][d] > 0) {
                        codif.schedule[p][d] = 0;
                    }
                    aux--;
                }
            }
        }
        return codif.schedule[p];
    }

    private int[] setFinalCod(PSP prob, cPSP codif, Employee emp) {

        int window = prob.inst.getNOD();
        int p = prob.inst.getEmployees().indexOf(emp);

        for (int d = 0; d < window; d++) {
            if (codif.schedule[p][d] == -1) {
                codif.schedule[p][d] = 0;
            }
        }
        return codif.schedule[p];
    }
    
    private int[] assignWorkDays(PSP prob, cPSP codif, Employee emp) {
        int p = prob.inst.getEmployees().indexOf(emp);
        
        //int num = prob.rnd.nextInt(prob.inst.getWLenght(), prob.inst.getNOD());
        
        for(int d = 0; d< prob.inst.getNOD(); d++){
            if((d % 7) != 5 && (d % 7) != 6 && codif.schedule[p][d] != -1){
                if(Math.random() > 0.20){
                    codif.schedule[p][d] = -2;
                }
            }
        }
        
        return codif.schedule[p];
    }

    private int[] assignShifts(PSP prob, cPSP codif, Employee emp) {
        int p = prob.inst.getEmployees().indexOf(emp);
        int s = prob.rnd.nextInt(1, prob.inst.getNOS());
        
        for(int d = 0; d< prob.inst.getNOD(); d++){
            if(codif.schedule[p][d] == -2){
                codif.schedule[p][d] = s;           
            }
        }
        
        return codif.schedule[p];
    }    
    
    private int[] setMinDaysOff2(PSP prob, cPSP codif, Employee emp) {
        int p = prob.inst.getEmployees().indexOf(emp);

        int start = emp.getMinDaysOff();
        int window = prob.inst.getNOD();

        for (int d = 0; d < window; d++) {
            if (codif.schedule[p][d] == -1) {
                for (int i = 1; i < start; i++) {
                    if((d+i) < window){
                        if (codif.schedule[p][d + i] < 1) {
                            codif.schedule[p][d + i] = -1;
                        } else {
                            if (codif.schedule[p][d + i] > 0) {
                                if (codif.schedule[p][d + 1] == -1 && i > 1) {
                                    codif.schedule[p][d - 1] = -1;
                                } else {
                                    if((d-i)>-1){
                                        codif.schedule[p][d - i] = -1;
                                    }
                                }
                            }
                        }
                    }
                }
                d += start;
            }
        }
        return codif.schedule[p];
    }
    
    private int[] setMinConsec(PSP prob, cPSP codif, Employee emp) {
        int incr = emp.getMinCons();
        int window = prob.inst.getNOD();
        int p = prob.inst.getEmployees().indexOf(emp);

        
        for (int d = window-1; d >= incr; d--) {
            if (codif.schedule[p][d] > 0) {
                int i = 1;
                while (i < incr) {
                    if (codif.schedule[p][d - i] < 1) {
                        codif.schedule[p][d - i] = codif.schedule[p][d];
                    } 
                    else{
                        i++;
                    }
                    i++;
                }
                d -= incr;
            }
        }   
        return codif.schedule[p];
    }

    private int[] setWeekends(PSP prob, cPSP codif, Employee emp) {
        int p = prob.inst.getEmployees().indexOf(emp);
        int window = prob.inst.getNOD() - 1, count = 0;
        int wknds = emp.getMaxWeekends();

        for (int d = window; d > 5; d -= 7) {
            if (codif.schedule[p][d] > 0 || codif.schedule[p][d - 1] > 0) {
                count++;
            }
        }

        while (count < wknds) {
            int d = prob.rnd.nextInt(prob.inst.getWeekends());
            codif.schedule[p][d] = prob.rnd.nextInt(1, prob.inst.getNOS());
            count++;
        }

        for (int d = 5; d < window; d += 7) {
            if (codif.schedule[p][d] < 1 && codif.schedule[p][d + 1] < 1) {
                codif.schedule[p][d] = -1;
                codif.schedule[p][d + 1] = -1;
            }
        }

        return codif.schedule[p];
    }
    
    private static int antagonist(PSP prob, cPSP codif, Employee emp){
      
        int fitness = 0;
        int p = prob.inst.getEmployees().indexOf(emp);
        
        for(int d=0; d< prob.inst.getNOD()-1; d++)
        {
            int sft2 = codif.schedule[p][d+1] -1, sft = codif.schedule[p][d] -1;
            Shift shift; 
            String sftStr;
            if(sft> -1 && sft2 > -1){

                shift = prob.inst.getShifts().get(sft);

                sftStr = prob.inst.getShifts().get(sft2).getID();

                if(shift.getForbiddenShifts().contains(sftStr))
                { 
                    fitness++;
                }
            }
        }            
        return fitness;       
    }
    
    private static int evaluateMinHours(PSP prob, cPSP codif, Employee emp){
    
        int hDemand = 0, hours[] = new int[prob.inst.getShifts().size()];
        int p = prob.inst.getEmployees().indexOf(emp), fitness = 0;
        Arrays.fill(hours,0);

        for(Shift sft: prob.inst.getShifts())
        {   int duration = sft.getDuration();
            int s = prob.inst.getShifts().indexOf(sft);

            for(int d=0;d< prob.inst.getNOD();d++)
            {   
                if(codif.schedule[p][d] == (s+1))
                {
                    hours[s] += duration; 
                }
            }
        }

        for(int i=0;i<hours.length;i++)
        {
            hDemand += hours[i];
        }

        if(hDemand < emp.getMinMinutes() )
        {
            fitness += (emp.getMinMinutes()-hDemand);
        }
        
        return fitness;
    }
    
    private static int evaluateDaysOff(PSP prob, cPSP codif, Employee emp){
    
        int incr = emp.getMinDaysOff(), fitness = 0;
        int window = prob.inst.getNOD()-1;
        int p = prob.inst.getEmployees().indexOf(emp);
        
        for(int d =window; d>0;) {

            int sum = 0;

            if(codif.schedule[p][d] == 0)
            {       
                sum++;
                for(int j=1;j<incr; j++)
                {
                    if((d-j) >= 0){
                        if(codif.schedule[p][d-j]==0)
                        {
                            sum++;
                        }
                        else{
                            sum =0;
                        }
                    }
                }

                if(sum < incr )
                {
                    fitness+= 1;
                    d--;
                }
                else{
                    d-=incr;
                }
            }
            else{
                d--;
            }
        }
        return fitness;
    }    
    
    static int evaluateMinConsec(PSP prob, cPSP codif, Employee e)
    {
        int fitnessModel = 0;
        
        
        int incr = e.getMinCons();
        int window = prob.inst.getNOD()-1;
        int p = prob.inst.getEmployees().indexOf(e);

        for(int d =1; d<window;d++) {

            if(codif.schedule[p][d] > 0)
            {     boolean  flag = false;
                for(int i=d;i<d+incr;i++){
                    if(codif.schedule[p][d]<0)
                        flag = true;
                }

                if(flag){
                    fitnessModel+=1;
                }
                
                d+=incr;
            }
        }            
            
        
        return fitnessModel;
    }
    
    int evaluateMinConsecutiveDaysOff(PSP prob, cPSP codif, Employee e){
        int fitnessModel = 0;
        
        int incr = e.getMinDaysOff();
        int window = prob.inst.getNOD()-1;
        int p = prob.inst.getEmployees().indexOf(e);
        int i = 0;    
        for(int d =window; d>-1;) 
        {
            if(codif.schedule[p][d] == 0)
            {   i = 1;
                if(d==0){
                    i = 2;
                }
                while((d-i)>-1 && codif.schedule[p][d-i]==0){
                    if((d-i)==0)
                    {
                        i+=2;
                    }
                    else{
                     i++;
                    }
                }

                if(i < incr )
                {
                    fitnessModel+= 1;
                    d-=i;
                }
                else{
                    d-=i;
                }
            }
            else{d--;}
        }
        
        return fitnessModel;
    }
    
    private static int evaluateWeekends(PSP prob, cPSP codif, Employee emp){
    
        int wknds = emp.getMaxWeekends(), fitness =0;
        int sum =0, p = prob.inst.getEmployees().indexOf(emp);

        for(int d=5;d< prob.inst.getNOD();d+=7)
        {   
            if(codif.schedule[p][d]>0 || codif.schedule[p][d+1]>0)
            {
                sum += 1; 
            }
        }

        if(sum>wknds){
            fitness+=(sum-wknds);
        }
        
        return fitness;
    }
    
    public int evaluateAntagonism(PSP prob, cPSP codif, Employee emp){
    
        int fitnessModel = 0;        
        
        int p = prob.inst.getEmployees().indexOf(emp);
        
        for(int d=0; d< prob.inst.getNOD()-1; d++)
        {
            int sft2 = codif.schedule[p][d+1] -1, sft = codif.schedule[p][d] -1;
            Shift shift; 
            String sftStr;
            if(sft> -1 && sft2 > -1){

                shift = prob.inst.getShifts().get(sft);

                sftStr = prob.inst.getShifts().get(sft2).getID();

                if(shift.getForbiddenShifts().contains(sftStr))
                { 
                    fitnessModel+=1;
                }
            }
        }
        return fitnessModel;
    }    

    private static int evaluateConsecutiveness(PSP prob, cPSP codif, Employee emp){
    
        int incr = emp.getMaxCons(), fitness=0;
        int window = prob.inst.getNOD()-incr;
        int p = prob.inst.getEmployees().indexOf(emp);

        for(int d= 0; d<window; d++){
            int sum = 0;
            for(int j=d; j<=d+incr;j++){
                if(codif.schedule[p][j]!=0)
                {
                    sum++;
                }
                else{
                    sum = 0;
                }
            }
            if(sum > incr)
            {
                fitness+= 1;
            }
        }
        return fitness;
    }
    
    private static int evaluateMaxHours(PSP prob, cPSP codif, Employee emp){
    
        int hDemand = 0, hours[] = new int[prob.inst.getShifts().size()];
        int p = prob.inst.getEmployees().indexOf(emp), fitness = 0;
        Arrays.fill(hours,0);

        for(Shift sft: prob.inst.getShifts())
        {   int duration = sft.getDuration();
            int s = prob.inst.getShifts().indexOf(sft);

            for(int d=0;d< prob.inst.getNOD();d++)
            {   
                if(codif.schedule[p][d] == (s+1))
                {
                    hours[s] += duration; 
                }
            }
        }

        for(int i=0;i<hours.length;i++)
        {
            hDemand += hours[i];
        }

        if(hDemand > emp.getMaxMinutes() )
        {
            fitness += (hDemand-emp.getMaxMinutes());
        }
        
        return fitness;
    }
    
    private class Uniform extends oCrossover<PSP, cPSP> {

        @Override
        public String name() {
            return "Uniform";
        }

        @Override
        public cPSP crossover(PSP prob, cPSP ind1, cPSP ind2) throws Exception {
            cPSP child = ind1.build(prob);

          //  System.out.println("Uniform Crossover");
            for (int i = 0; i < prob.inst.getNOE(); i++) {
                for (int j = 0; j < prob.inst.getNOD(); j++) {
                    if (prob.rnd.nextBoolean()) {
                        child.schedule[i][j] = ind1.schedule[i][j];

                    } else {
                        child.schedule[i][j] = ind2.schedule[i][j];
                    }
                }
            }
       //     repair(prob, child);
       //     repair2(prob, child);
            
     //       setDemand(prob, child);
          //  repair3(prob, child);
            return child;
        }
        
    }
    
    
    private class MutRandom2 extends oMutation<PSP, cPSP> {

        @Override
        public String name() {
            return "Mut-random2";
        }

        @Override
        public void mutation(PSP prob, cPSP ind) throws Exception {
            random_swap2(prob, ind);
            
       //     setDemand(prob, ind);
            repair3(prob, ind);
        }
    }
    
    
    private class MutRandom extends oMutation<PSP, cPSP> {

        @Override
        public String name() {
            return "Mut-random";
        }

        @Override
        public void mutation(PSP prob, cPSP ind) throws Exception {
            random_swap(prob, ind);
            
       //     setDemand(prob, ind);
            repair3(prob, ind);
        }
    }
    
    
    private class MutBlockExchange extends oMutation<PSP, cPSP>{

        @Override
        public String name(){
            return "Mut-Block-Exchange";
        }
        
        @Override
        public void mutation(PSP prob, cPSP codif) throws Exception {
            block_exchange(prob, codif);
        
       //     repair(prob, codif);
        //    repair2(prob, codif);
            //setDemand(prob, codif);
        //    repair3(prob, codif);
        }
    }
    
    private class Mut2Exchange extends oMutation<PSP, cPSP>{

        @Override
        public String name(){
            return "Mut-2-Exchange";
        }
        
        @Override
        public void mutation(PSP prob, cPSP codif) throws Exception {
        //    System.out.println("2-Exchange mutation.");
            exchange_2(prob, codif);
            //repair(prob, codif);
        //    repair2(prob, codif);
            
            //setDemand(prob, codif);
         //   repair3(prob, codif);
        //    System.out.println("2-Exchange mutation end");
        }
       
    }
    
    
    
    
    private class Mut3Exchange extends oMutation<PSP, cPSP>{

        @Override
        public String name(){
            return "Mut-3-Exchange";
        }
        
        @Override
        public void mutation(PSP prob, cPSP codif) throws Exception {
        
            exchange_3(prob, codif);
      //      setDemand(prob, codif);
           // repair3(prob, codif);
        }
    }
    
    
    
    
    private class MutDoubleExchange extends oMutation<PSP, cPSP>{

        @Override
        public String name(){
            return "Mut-Double-Exchange";
        }
        
        @Override
        public void mutation(PSP prob, cPSP codif) throws Exception {
        ///    System.out.println("Double-Exchange mutation.");
            double_exchange(prob, codif);
       //     setDemand(prob, codif);
      //      repair3(prob, codif);
           // repair(prob, codif);
           // repair2(prob, codif);
        //    System.out.println("Double-Exchange mutation end.");
        }
    }
    
    private static void repair3(PSP prob, cPSP codif){        
        
       /**/ for(Employee emp: prob.inst.getEmployees()){
            int fitnessA = 0, fitnessC = 0,
            fitnessDO = 0, fitnessH = 0, fitnessMC = 0, fitnessMH = 0, fitnessW =0;  
            
                fitnessA = antagonist(prob,codif,emp);
                if(fitnessA>0){
                    empAntMove(prob, codif,emp);
                }

                fitnessC = evaluateConsecutiveness(prob,codif,emp);
                if(fitnessC > 0){
                    empConsecMove(prob, codif,emp); 
                }


                fitnessDO = evaluateDaysOff(prob, codif, emp);
                if(fitnessDO> 0){
                    empDaysOffMove(prob, codif,emp); 
                }


                fitnessMH = evaluateMaxHours(prob, codif, emp);
                if(fitnessMH>0)
                {
                    empMaxHoursMove(prob, codif,emp);
                }

               fitnessH = evaluateMinHours(prob, codif, emp);
                if(fitnessH>0)
                {
                    empMinHoursMove(prob, codif,emp);
                }


                fitnessMC = evaluateMinConsec(prob, codif, emp);
                if(fitnessMC>0)
                {
                    empMinConsecMove(prob, codif,emp);  
                }


                fitnessW  = evaluateWeekends(prob, codif, emp);
                if(fitnessW>0){
                    empWkndsMove(prob, codif,emp);
                }
                    
                //    empShiftMove(prob, codif,emp);
                //    empMinConsecMove(prob, codif,emp);
                //    empMDaysOffMove(prob, codif, emp);
            }
            daysOffMove(prob, codif);
            mDaysOffMove2(prob, codif);
            finalCod(prob, codif);
            //setDemand(prob, codif);
    }
        
    
    private static void initRepair(PSP prob, cPSP codif){
    
        for(Employee emp: prob.inst.getEmployees()){
            int fitnessDO = 0, fitnessW = 0, fitnessMC = 0, fitnessMH = 0, fitnessMD =0;  
            
                    
            fitnessDO = evaluateDaysOff(prob, codif, emp);
            if(fitnessDO> 0){
                empDaysOffMove(prob, codif,emp); 
            }                    

            fitnessMH = evaluateMaxHours(prob, codif, emp);
            if(fitnessMH>0)
            {
                empMaxHoursMove(prob, codif,emp);
            }

            fitnessMC = evaluateMinConsec(prob, codif, emp);
            if(fitnessMC>0)
            {
                minConsecMove(prob, codif, emp);
            }

            fitnessW = evaluateWeekends(prob, codif, emp);
            if(fitnessW > 0)
            {
                wkndsMoveEmp(prob, codif, emp);
            }
                    
        }
    
    }
    
    private static void random_swap(PSP prob, cPSP ind) {
        int emp = prob.rnd.nextInt(ind.schedule.length);
        int day = prob.rnd.nextInt(ind.schedule[emp].length);

        ind.schedule[emp][day] = 0;
    }
    
    
    
    private static void random_swap2(PSP prob, cPSP ind) {
        int emp = prob.rnd.nextInt(ind.schedule.length);
        int day = prob.rnd.nextInt(ind.schedule[emp].length);

        ind.schedule[emp][day] = prob.rnd.nextInt(1, prob.inst.getNOS());
    }
    
    private static void restart_swap(PSP prob, cPSP codif) {
        
        int shiftCounter[] = new int[prob.inst.getNOS()];
        for(int i=0;i<shiftCounter.length;i++){
            shiftCounter[i] = 0;
        }
        
        for(int d = 0; d<prob.inst.getNOD();d++){
           for(int p=0;p<prob.inst.getNOE();p++){
                for(int s=0;s<prob.inst.getNOS();s++)
                { 
                   if(codif.schedule[p][d] == s)
                    shiftCounter[s] +=1;
                
                }
                
                for(int s=0;s<prob.inst.getNOS();s++){
                    for(Integer dem:prob.inst.getCovers().get(d).get(prob.inst.getShifts().get(s).getID()).keySet())
                    {
                        while(shiftCounter[s] > dem){
                            codif.schedule[p][d] = 0;
                            shiftCounter[s] --;
                        }
                    }
                }
           }
       }
        
        
    }
    
    private static void exchange_2(PSP prob, cPSP ind) {
        
        int emp1 =  prob.rnd.nextInt(ind.schedule.length);
        int emp2 =  prob.rnd.nextInt(ind.schedule.length);
        int day = prob.rnd.nextInt(ind.schedule[emp1].length);
        
        int aux = ind.schedule[emp1][day];
        
        ind.schedule[emp1][day] = ind.schedule[emp2][day];
        ind.schedule[emp2][day] = aux;
    }
        
    private static void exchange_3(PSP prob, cPSP ind) {
        
        int emp1 =  prob.rnd.nextInt(ind.schedule.length);
        int emp2 =  prob.rnd.nextInt(ind.schedule.length);
        int emp3 =  prob.rnd.nextInt(ind.schedule.length);
        
        int day = prob.rnd.nextInt(ind.schedule[emp1].length);
        
        int aux = ind.schedule[emp1][day];
        int aux2 = ind.schedule[emp2][day];
        
        ind.schedule[emp1][day] = aux2;
        ind.schedule[emp2][day] = ind.schedule[emp3][day];
        ind.schedule[emp3][day] = aux;
    } 
        
    private static void double_exchange(PSP prob, cPSP ind) {
        
        int emp1 =  prob.rnd.nextInt(ind.schedule.length);
        int emp2 =  prob.rnd.nextInt(ind.schedule.length);
        
        int day1 = prob.rnd.nextInt(ind.schedule[emp1].length);
        int day2 = prob.rnd.nextInt(ind.schedule[emp1].length);
        
        int aux = ind.schedule[emp1][day1];
        int aux2 = ind.schedule[emp1][day2];
        
        ind.schedule[emp1][day1] = ind.schedule[emp2][day1];
        ind.schedule[emp1][day1] = ind.schedule[emp2][day2];
        
        ind.schedule[emp2][day1]= aux;
        ind.schedule[emp2][day2]= aux2;
    } 
    
    private static void block_exchange(PSP prob, cPSP ind) {
        
        int emp1 =  prob.rnd.nextInt(ind.schedule.length);
        int emp2 =  prob.rnd.nextInt(ind.schedule.length);
        
        int d[] = prob.rnd.cuts_points(prob.inst.getNOD(), 2);
        int aux = 0;
        
        for(int i=d[0];i<d[1];i++){
            aux = ind.schedule[emp1][i];
            ind.schedule[emp1][i] = ind.schedule[emp2][i];
            ind.schedule[emp2][i] = aux;
        }
    } 
    
    //LOCAL MOVES 
    private static void minConsecMove(PSP prob, cPSP codif, Employee emp) {
    
        int incr = emp.getMinCons();
        int window = prob.inst.getNOD();
        int p = prob.inst.getEmployees().indexOf(emp);


        for (int d = window-1; d >= incr; d--) {
            if (codif.schedule[p][d] > 0) {
                int i = 1;
                while (i < incr) {
                    if (codif.schedule[p][d - i] < 1) {
                        codif.schedule[p][d - i] = codif.schedule[p][d];
                    } 
                    else{
                        i++;
                    }
                    i++;
                }
                d -= incr;
            }
        }   
    }

    private static void empMinConsecMove(PSP prob, cPSP codif, Employee emp) {
    
        int incr = emp.getMinCons();
        int window = prob.inst.getNOD();
        int p = prob.inst.getEmployees().indexOf(emp);

        for (int d = window-1; d >= incr; d--) {
            if (codif.schedule[p][d] > 0) {
                int i = 1;
                while (i < incr) {
                    if (codif.schedule[p][d - i] < 1) {
                        codif.schedule[p][d - i] = codif.schedule[p][d];
                    } 
                    else{
                        i++;
                    }
                    i++;
                }
                d -= incr;
            }
        }   
    }

    private static void empAntMove(PSP prob, cPSP codif, Employee emp){
    
        int p = prob.inst.getEmployees().indexOf(emp);

        for(int d=0; d< prob.inst.getNOD()-1; d++)
        {
            int sft2 = codif.schedule[p][d+1] -1, sft = codif.schedule[p][d] -1;
            Shift shift; 
            String sftStr;
            if(sft> -1 && sft2 > -1){

                shift = prob.inst.getShifts().get(sft);

                sftStr = prob.inst.getShifts().get(sft2).getID();

                if(shift.getForbiddenShifts().contains(sftStr))
                { 
                    codif.schedule[p][d+1] = codif.schedule[p][d];
                }
            }
        }

    }
    
    private static void antMove(PSP prob, cPSP codif){
    
        for (Employee emp:prob.inst.getEmployees()) {
            int p = prob.inst.getEmployees().indexOf(emp);

            for(int d=0; d< prob.inst.getNOD()-1; d++)
            {
                int sft2 = codif.schedule[p][d+1] -1, sft = codif.schedule[p][d] -1;
                Shift shift; 
                String sftStr;
                if(sft> -1 && sft2 > -1){

                    shift = prob.inst.getShifts().get(sft);

                    sftStr = prob.inst.getShifts().get(sft2).getID();

                    if(shift.getForbiddenShifts().contains(sftStr))
                    { 
                        codif.schedule[p][d+1] = codif.schedule[p][d];
                    }
                }
            }
        }
    }
    
    
    private static void empConsecMove(PSP prob, cPSP codif, Employee emp){
    
        int p = prob.inst.getEmployees().indexOf(emp);
        int cons = emp.getMaxCons();
        int aux=0, count = 0, window = prob.inst.getNOD()-cons;

        for(int d = 0; d<window;d++){

            if(codif.schedule[p][d]>-1){
                count = 1;
                for(int i=d;i<=d+cons;i++){
                    if(codif.schedule[p][i]>0)
                    {
                      count++;  
                    }
                }

                if (count > cons) {
                    aux = count;
                }
                while (aux > cons) {
                    if (codif.schedule[p][d] > 0) {
                        codif.schedule[p][d] = 0;
                    }
                    aux--;
                }
            }
        }
    
    }
    
    private static void consecMove(PSP prob, cPSP codif){
    
        for (Employee emp:prob.inst.getEmployees()) {
            int p = prob.inst.getEmployees().indexOf(emp);
            int cons = emp.getMaxCons();
            int aux=0, count = 0, window = prob.inst.getNOD()-cons;

            for(int d = 0; d<window;d++){

                if(codif.schedule[p][d]>0){
                    count = 1;
                    for(int i=d;i<=d+cons;i++){
                        if(codif.schedule[p][i]>0)
                        {
                          count++;  
                        }
                    }

                    if (count > cons) {
                        aux = count;
                    }
                    while (aux > cons) {
                        if (codif.schedule[p][d] > 0) {
                            codif.schedule[p][d] = 0;
                        }
                        aux--;
                    }
                }
            }
        }
    }
    
    private static void empDaysOffMove(PSP prob, cPSP codif, Employee emp) {
        int p = prob.inst.getEmployees().indexOf(emp);

        if (prob.inst.getDaysOff() != null) {
            if (prob.inst.getDaysOff().get(emp.getID()) != null) {

                Vector<Integer> daysOff = prob.inst.getDaysOff().get(emp.getID());

                for (Integer d : daysOff) {
                    codif.schedule[p][d] = -1;
                }
            }
        }
    
    }
    
    
    private static void daysOffMove(PSP prob, cPSP codif) {
        for(Employee emp : prob.inst.getEmployees())
        {
            int p = prob.inst.getEmployees().indexOf(emp);
            
            if (prob.inst.getDaysOff() != null) {
                if (prob.inst.getDaysOff().get(emp.getID()) != null) {

                    Vector<Integer> daysOff = prob.inst.getDaysOff().get(emp.getID());

                    for (Integer d : daysOff) {
                        codif.schedule[p][d] = -1;
                    }
                }
            }
        }
    }
    
    private static void empWkndsMove(PSP prob, cPSP codif, Employee emp){
    
        int window = prob.inst.getNOD() - 1, count = 0;
        int p = prob.inst.getEmployees().indexOf(emp);

        int wknds = emp.getMaxWeekends();

        for (int d = window; d > 5; d -= 7) {
            if (codif.schedule[p][d] > 0 || codif.schedule[p][d - 1] > 0) {
                count++;
            }
        }

        while (count > wknds) {

            for(int d=window;d>5; d-=7){
                if(codif.schedule[p][d]> 0 || codif.schedule[p][d-1]> 0 ){
                    codif.schedule[p][d] = 0;
                    codif.schedule[p][d-1] = 0;
                    count --;
                }
            }
        }
    }
    
    private static void wkndsMove(PSP prob, cPSP codif){
    
        for(Employee emp : prob.inst.getEmployees())
        {
            int window = prob.inst.getNOD() - 1, count = 0;
            int p = prob.inst.getEmployees().indexOf(emp);

            int wknds = emp.getMaxWeekends();

            for (int d = window; d > 5; d -= 7) {
                if (codif.schedule[p][d] > 0 || codif.schedule[p][d - 1] > 0) {
                    count++;
                }
            }

            while (count > wknds) {
            
                for(int d=window;d>5; d-=7){
                    if(codif.schedule[p][d]> 0 || codif.schedule[p][d-1]> 0 ){
                        codif.schedule[p][d] = 0;
                        codif.schedule[p][d-1] = 0;
                        count --;
                    }
                }
            }
        }
    }
    
    private static void wkndsMoveEmp(PSP prob, cPSP codif, Employee emp){
    
        int window = prob.inst.getNOD() - 1, count = 0;
        int p = prob.inst.getEmployees().indexOf(emp);

        int wknds = emp.getMaxWeekends();

        for (int d = window; d > 5; d -= 7) {
            if (codif.schedule[p][d] > 0 || codif.schedule[p][d - 1] > 0) {
                count++;
            }
        }

        while (count > wknds) {

            for(int d=window;d>5; d-=7){
                if(codif.schedule[p][d]> 0 || codif.schedule[p][d-1]> 0 ){
                    codif.schedule[p][d] = 0;
                    codif.schedule[p][d-1] = 0;
                    count --;
                }
            }
        }
    }
    
    
    private static void empMDaysOffMove(PSP prob, cPSP codif, Employee emp){
        

        int p = prob.inst.getEmployees().indexOf(emp);
        int start = emp.getMinDaysOff();
        int window = prob.inst.getNOD();

        for (int d = start; d < window; d++) {
            if (codif.schedule[p][d] == 0 ) {
                for (int i = 1; i < start; i++) {
                    if((d + i) < window){
                        if (codif.schedule[p][d + i] < 1) {
                            codif.schedule[p][d + i] = 0;
                        } else {
                            if (codif.schedule[p][d + i] > 0) {
                                if (codif.schedule[p][d + 1] == 0 && i > 1) {
                                    codif.schedule[p][d - 1] = 0;
                                } else {
                                    codif.schedule[p][d - i] = 0;
                                }
                            }
                        }
                    }
                }
                d += start;
            }
        }
    }
    
    private static void mDaysOffMove2(PSP prob, cPSP codif){
        
        for(Employee emp:prob.inst.getEmployees()){

            int p = prob.inst.getEmployees().indexOf(emp);
            int start = emp.getMinDaysOff();
            int window = prob.inst.getNOD();

            for (int d = start; d < window; d++) {
                if (codif.schedule[p][d] == 0 ) {
                    for (int i = 1; i < start; i++) {
                        if((d + i) < window){
                            if (codif.schedule[p][d + i] < 1) {
                                codif.schedule[p][d + i] = 0;
                            } else {
                                if (codif.schedule[p][d + i] > 0) {
                                    if (codif.schedule[p][d + 1] == 0 && i > 1) {
                                        codif.schedule[p][d - 1] = 0;
                                    } else {
                                        codif.schedule[p][d - i] = 0;
                                    }
                                }
                            }
                        }
                    }
                    d += start;
                }
            }
        }
    }
    
    private static void mDaysOffMove(PSP prob, cPSP codif){
        
        for(Employee emp:prob.inst.getEmployees()){

            int p = prob.inst.getEmployees().indexOf(emp);
            int start = emp.getMinDaysOff();
            int window = prob.inst.getNOD();

           for (int d = 0; d < window; d++) {
                if (codif.schedule[p][d] == -1 ) {
                    for (int i = 1; i < start; i++) {
                        if((d + i) < window){
                            if (codif.schedule[p][d + i] < 1) {
                                codif.schedule[p][d + i] = -1;
                            } else {
                                if (codif.schedule[p][d + i] > 0) {
                                    if (codif.schedule[p][d + 1] == -1 && i > 1) {
                                        if(d-1>0)
                                        {codif.schedule[p][d - 1] = -1;}
                                    } else {
                                        if(d>=i){
                                            codif.schedule[p][d - i] = -1;
                                        }
                                    }
                                }
                            }
                        }
                    }
                    d += start;
                }
            }
        }
    }
    
    
    private static void empFinalCod(PSP prob, cPSP codif, Employee emp) {

        int window = prob.inst.getNOD();
        int p = prob.inst.getEmployees().indexOf(emp);

        for (int d = 0; d < window; d++) {
            if (codif.schedule[p][d] == -1) {
                codif.schedule[p][d] = 0;
            }
        }
    }
    

    private static void empShiftMove(PSP prob,cPSP codif, Employee emp){
        int p = prob.inst.getEmployees().indexOf(emp);
        int sfts[] = new int[prob.inst.getNOS()];
        Arrays.fill(sfts, 0);
        int window = prob.inst.getNOD();
        Object maxShift = Collections.max(emp.getMaxShifts());
        int newShift = emp.getMaxShifts().indexOf(maxShift);

        for(int s=0;s<prob.inst.getNOS();s++){
            for (int d = 0; d < window; d++) {
                if(codif.schedule[p][d] == (s+1))
                {
                    sfts[s]++; 
                }
            }

            int d= 0;
            while(sfts[s] > emp.getMaxShifts().get(s) && d < window){

                if(codif.schedule[p][d] == (s+1)){
                    codif.schedule[p][d] = newShift+1;
                    sfts[s]--;
                }
                d++;
            }

        }   
    }
    
    
    private static void shiftMove(PSP prob,cPSP codif){
        for(Employee emp:prob.inst.getEmployees()){
            int p = prob.inst.getEmployees().indexOf(emp);
            int sfts[] = new int[prob.inst.getNOS()];
            Arrays.fill(sfts, 0);
            int window = prob.inst.getNOD();
            Object maxShift = Collections.max(emp.getMaxShifts());
            int newShift = emp.getMaxShifts().indexOf(maxShift);
            
            for(int s=0;s<prob.inst.getNOS();s++){
                for (int d = 0; d < window; d++) {
                    if(codif.schedule[p][d] == (s+1))
                    {
                        sfts[s]++; 
                    }
                }
                
                int d= 0;
                while(sfts[s] > emp.getMaxShifts().get(s) && d < window){
                    
                    if(codif.schedule[p][d] == (s+1)){
                        codif.schedule[p][d] = newShift+1;
                        sfts[s]--;
                    }
                    d++;
                }                
            }   
        }    
    }
    
    
    private static void setDemand(PSP prob, cPSP codif) {
        int window = prob.inst.getNOD();
        LinkedList<Integer> list;

        for(int d = 0; d < window;d++)
        {   
       
            list = prob.inst.getAvailable(codif,d);

            for(Shift shift: prob.inst.getShifts()){
                int s = prob.inst.getShifts().indexOf(shift)+1;

                Integer value= prob.inst.getCovers().get(d).get(shift.getID()).keySet().iterator().next();
                int  dem = Math.min(value, list.size());
                LinkedList<Integer> remList = new LinkedList<>();
                for(Integer p:list){
                    if(codif.schedule[p][d] == s){
                        dem--;
                        remList.add(p);
                    }
                }
                
                list.removeAll(remList);
                
                while(dem > 0 && list.size() > 0)
                { 
                    int p = list.get((int) (Math.random()*(list.size()-1)));

                    if(codif.schedule[p][d] == 0){
                        codif.schedule[p][d] = s;
                        list.removeFirstOccurrence(p);
                        dem--;
                    }

                    else{
                            if(codif.schedule[p][d] !=s){
                                list.removeFirstOccurrence(p);
                            }                                   
                        }
                    if(list.isEmpty()){
                        dem = 0;
                    }
                }
            }            
        }  
    }
    
    private  static void empMaxHoursMove(PSP prob, cPSP codif, Employee emp) {
            
        int completedHours = 0;
        int p = prob.inst.getEmployees().indexOf(emp);
        int consec = emp.getMaxCons();
        
        int window = prob.inst.getNOD();

        for (int d = 0; d < window; d++) {
            if(codif.schedule[p][d]>0)
            {   
                int s = codif.schedule[p][d];
                completedHours+= prob.inst.getShifts().get(s-1).getDuration();
            }
        }
        
        int j = prob.rnd.nextInt(prob.inst.getNOD());
        
        
        while(completedHours > emp.getMaxMinutes()){
        
            while((j+1)<window){
                if(codif.schedule[p][j]>0 && codif.schedule[p][j+1]<=0)
                {
                    int s = codif.schedule[p][j];
                    codif.schedule[p][j] = -1;
                    completedHours -= prob.inst.getShifts().get(s-1).getDuration();
                    break;
                }
                else{
                        j++;
                }
            }          
            j = prob.rnd.nextInt(prob.inst.getNOD());
        }   
        
       /*  if(p%2==1){
            for (int d = 0; d < window; d++) {
            
                for(int j=d; j<consec+d;j++)
                {
                    if(j< window){
                        while(codif.schedule[p][j]>0 && completedHours > emp.getMaxMinutes()){
                            int s = codif.schedule[p][j];
                            codif.schedule[p][j] = -1;
                            completedHours -= prob.inst.getShifts().get(s-1).getDuration();
                        }    
                    }
                }        
            }
        }
        else{
            for (int d = window-1; d >=0 ; d--) {
            
                for(int j=d; j>d-consec;j--)
                {
                    if(j>-1){
                        while(codif.schedule[p][j]>0 && completedHours > emp.getMaxMinutes()){
                            int s = codif.schedule[p][j];
                            codif.schedule[p][j] = -1;
                            completedHours -= prob.inst.getShifts().get(s-1).getDuration();
                        }    
                    }
                }        
            }
        } */
        
    }    
    
    
    private  static void empMinHoursMove(PSP prob, cPSP codif, Employee emp) {
    
        
        int hDemand = 0;
        int p = prob.inst.getEmployees().indexOf(emp);

        int window = prob.inst.getNOD();

        Integer shift = Collections.max(emp.getMaxShifts());
        int index = emp.getMaxShifts().indexOf(shift);

            
        for (int d = 0; d < window; d++) {
            if(codif.schedule[p][d]>0)
            {   
                int s = codif.schedule[p][d];
                hDemand+= prob.inst.getShifts().get(s-1).getDuration();
            }
        }

            
        while(hDemand<emp.getMinMinutes())
        {
            int i = 0;
            do{ i = prob.rnd.nextInt(0,window-1);
                codif.schedule[p][i] = index+1;
                //System.out.println("codif.schedule["+p+"]["+i+"]: "+codif.schedule[p][i]);
            hDemand+= prob.inst.getShifts().get(codif.schedule[p][i]-1).getDuration(); 
            }while(codif.schedule[p][i] == -1);
        }   
       
    }    
    
    private class MovWknd extends oLocalMove<PSP, cPSP>{
        @Override
        public String name(){
            return "Mov-Wknds";
        }
    
        @Override
        public void local_search(PSP prob, cPSP codif) throws Exception{
            wkndsMove(prob, codif);
        }
    }
    
    private class MovRandom extends oLocalMove<PSP, cPSP> {

        @Override
        public String name() {
            return "Mov-Exchange";
        }

        @Override
        public void local_search(PSP prob, cPSP ind) throws Exception {
            random_swap(prob, ind);
          
        //    repair(prob, ind);
        //    repair2(prob, ind);
       /**     for(Employee emp: prob.inst.getEmployees()){
                setOffRequest(prob, ind, emp);
                setOnRequest(prob, ind, emp);
                
            }
        
            setDemand(prob, ind);*/
            daysOffMove(prob, ind);
        }
    }
    
    
    private class MovRandom2 extends oLocalMove<PSP, cPSP> {

        @Override
        public String name() {
            return "Mov-Demand";
        }

        @Override
        public void local_search(PSP prob, cPSP ind) throws Exception {
            restart_swap(prob, ind);
          
        //    repair(prob, ind);
        //    repair2(prob, ind);
       /**     for(Employee emp: prob.inst.getEmployees()){
                setOffRequest(prob, ind, emp);
                setOnRequest(prob, ind, emp);
                
            }
        
            setDemand(prob, ind);*/
         //   repair3(prob, ind);
        }
    }
    
    private class Mov2Exchange extends oLocalMove<PSP, cPSP>{

        @Override
        public String name(){
            return "Mov-2-Exchange";
        }
        
        @Override
        public void local_search(PSP prob, cPSP codif) throws Exception {
            exchange_2(prob, codif);
        //    repair3(prob, codif);
        }
    }
    
    
    private class Mov3Exchange extends oLocalMove<PSP, cPSP>{

        @Override
        public String name(){
            return "Mov-3-Exchange";
        }
        
        @Override
        public void local_search(PSP prob, cPSP codif) throws Exception {
            exchange_3(prob, codif);
        //    repair3(prob, codif);
        }
    }
    
    
    private class MovDoubleExchange extends oLocalMove<PSP, cPSP>{

        @Override
        public String name(){
            return "Mov-Double-Exchange";
        }
        
        @Override
        public void local_search(PSP prob, cPSP codif) throws Exception {
            double_exchange(prob, codif);
        //    repair3(prob, codif);
            
        }
    }
    
    private class MovImpIndSched extends oLocalMove<PSP, cPSP>{

        @Override
        public String name(){
            return "Mov-Impr-Ind-Sched";
        }
        
        @Override
        public void local_search(PSP prob, cPSP codif) throws Exception {
            for(Employee emp: prob.inst.getEmployees()){
                setOffRequest(prob, codif, emp);
                setOnRequest(prob, codif, emp);                
            }
            
            setDemand(prob, codif);
           // repair3(prob, codif);
        }
    }
    
    
    
    private class MovBlockExchange extends oLocalMove<PSP, cPSP>{

        @Override
        public String name(){
            return "Mov-Block-Exchange";
        }
        
        @Override
        public void local_search(PSP prob, cPSP codif) throws Exception {
            block_exchange(prob, codif);
        //    repair3(prob, codif);
        }
    }
    
    
    private class OnePoint extends oCrossover<PSP, cPSP> {

        @Override
        public String name() {
            return "OnePoint";
        }

        @Override
        public cPSP crossover(PSP prob, cPSP ind1, cPSP ind2) throws Exception {
            cPSP child = ind1.build(prob);
            int p = prob.rnd.nextInt(prob.inst.getNOE());
            
            for (int i = 0; i < p; i++) {
                for (int j = 0; j < prob.inst.getNOD(); j++) {
                    child.schedule[i][j] = ind1.schedule[i][j];
                }
            }

            for (int i = p; i < prob.inst.getNOE(); i++) {
                for (int j = 0; j < prob.inst.getNOD(); j++) {
                    child.schedule[i][j] = ind2.schedule[i][j];
                }
            }
        //    repair3(prob, child);
            return child;
        }
    }
    
    
    private class TwoPoints extends oCrossover<PSP, cPSP> {

        @Override
        public String name() {
            return "TwoPoints";
        }

        @Override
        public cPSP crossover(PSP prob, cPSP ind1, cPSP ind2) throws Exception {
            cPSP child = ind1.build(prob);
            int p[] = prob.rnd.cuts_points(prob.inst.getNOE(), 2);
            
            for (int i = 0; i < p[0]; i++) {
                for (int j = 0; j < prob.inst.getNOD(); j++) {
                    child.schedule[i][j] = ind1.schedule[i][j];
                }
            }
            for (int i = p[0]; i < p[1]; i++) {
                for (int j = 0; j < prob.inst.getNOD(); j++) {
                    child.schedule[i][j] = ind2.schedule[i][j];
                }
            }

            for (int i = p[1]; i < prob.inst.getNOE(); i++) {
                for (int j = 0; j < prob.inst.getNOD(); j++) {

                    child.schedule[i][j] = ind1.schedule[i][j];
                }
            }
            
        //    repair3(prob, child);
            
            return child;
        }
    }
    
    int weekend(PSP prob, cPSP codif)
    {   
        int fitnessModel = 0;
    
        for(Employee e : prob.inst.getEmployees()){
            int wknds = e.getMaxWeekends();
            int sum =0, p = prob.inst.getEmployees().indexOf(e);
                     
            for(int d=5;d< prob.inst.getNOD();d+=7)
            {   
                if(codif.schedule[p][d]>0 || codif.schedule[p][d+1]>0)
                {
                    sum += 1; 
                }
            }
        
            if(sum>wknds){
                fitnessModel+=(sum-wknds);
            }
        }
        return fitnessModel;
    }

    
    
    
    int shift(PSP prob, cPSP codif) {
        int fitnessModel = 0;
        for(Employee e : prob.inst.getEmployees()){
            int p = prob.inst.getEmployees().indexOf(e);
            int window = prob.inst.getNOD();
            int counter [] = new int[prob.inst.getNOS()];
            
            for(int d= 0; d<window; d++){
                for(int s = 1; s<=prob.inst.getNOS();s++){
                    if(codif.schedule[p][d] == s){
                        counter[s-1] +=1;
                    }
                }
            }
            
            for(int i = 0; i< e.getMaxShifts().size();i++){
                if(counter[i]>e.getMaxShifts().elementAt(i)){
                    fitnessModel +=1;
                }
            }
            
        }
        return fitnessModel;
    }
    int consecutiveness(PSP prob, cPSP codif){
        int fitnessModel = 0;
        
        for(Employee e : prob.inst.getEmployees()){
            int incr = e.getMaxCons();
            int window = prob.inst.getNOD()-incr;
            int p = prob.inst.getEmployees().indexOf(e);
                
            for(int d= 0; d<window; d++){
               int sum = 0;
                for(int j=d; j<=d+incr;j++){
                    if(codif.schedule[p][j]!=0)
                    {
                        sum++;
                    }
                    else{
                        sum = 0;
                    }
                }
                if(sum > incr)
                {
                    fitnessModel+= 1;
                }
            }
        }
        return fitnessModel;
    }
    
    
    
    private int minHour(PSP prob, cPSP codif) {
        
        int fitnessModel = 0;
        
        for(Employee e : prob.inst.getEmployees()){
            int hDemand = 0, hours[] = new int[prob.inst.getShifts().size()];
            int p = prob.inst.getEmployees().indexOf(e);
            Arrays.fill(hours,0);
            
            for(Shift sft: prob.inst.getShifts())
            {   int duration = sft.getDuration();
                int s = prob.inst.getShifts().indexOf(sft);
                         
                for(int d=0;d< prob.inst.getNOD();d++)
                {   
                    if(codif.schedule[p][d] == (s+1))
                    {
                        hours[s] += duration; 
                    }
                }
            }
            
            for(int i=0;i<hours.length;i++)
            {
                hDemand += hours[i];
            }
            
            if(hDemand < e.getMinMinutes())
            {
                fitnessModel += 1;
            }
        }
        
        return fitnessModel;
    }
    
    int minConsecutiveDaysOff(PSP prob, cPSP codif){
        int fitnessModel = 0;
        
        for(Employee e : prob.inst.getEmployees()){
            int incr = e.getMinDaysOff();
            int window = prob.inst.getNOD()-1;
            int p = prob.inst.getEmployees().indexOf(e);
            int i = 0;    
            for(int d =window; d>-1;) 
            {
                if(codif.schedule[p][d] == 0)
                {   i = 1;
                    if(d==0){
                        i = 2;
                    }
                    while((d-i)>-1 && codif.schedule[p][d-i]==0){
                        if((d-i)==0)
                        {
                            i+=2;
                        }
                        else{
                         i++;
                        }
                    }
                    
                    if(i < incr )
                    {
                        infeasiblePhy.add(p);
                        fitnessModel+= 1;
                        d-=i;
                    }
                    else{
                        d-=i;
                    }
                }
                else{d--;}
            }
        }
        return fitnessModel;
    }
    
    
    private class FOPSP extends oFOOperator<PSP, cPSP>{
        private CplexExtended cpx;
        private PSPmodel model;
        private final int NodesLim = 100000;
        @Override
        public String name() {
            return "Rows statrategy";
        }

        @Override
        public void start() throws Exception {
            super.start(); //To change body of generated methods, choose Tools | Templates.
            cpx = new CplexExtended();
            cpx.setParam(IloCplex.LongParam.NodeLim, NodesLim);
            
        }
        
        
        private int [][][] getSolFromCodif(PSP prob, cPSP codif){
            //getting the solution
            int Xijk[][][] = new int[prob.inst.getNOE()][prob.inst.getNOD()][prob.inst.getNOS()];
            
            if(codif.schedule!=null)
            {
                for(int i=0;i<prob.inst.getNOE();i++)
                {
                    for(int j=0;j<prob.inst.getNOD(); j++)
                    {

                        for(int k = 0; k<prob.inst.getNOS();k++){
                            if(codif.schedule[i][j]== k+1)
                            {    
                                Xijk[i][j][k] = 1;
                            }                    
                            else{
                                    Xijk[i][j][k] = 0;                         
                            }
                        }
                    }                
                }
            } 
            
            
            return Xijk;
        }
        
        private int [][][] getSolFromModel(PSP prob) throws IloException{
            //getting the solution
            int Xijk[][][] = new int[prob.inst.getNOE()][prob.inst.getNOD()][prob.inst.getNOS()];
            for(int i=0; i<prob.inst.getNOE(); i++){
                for(int j=0; j<prob.inst.getNOD(); j++){
                    for(int k=0; k<prob.inst.getNOS(); k++){
                        Xijk[i][j][k] = cpx.getValue(model.X[i][j][k])>0.5 ? 1 : 0;
                    }
                }
            }
            return Xijk;
        }
        
        private void fixAll(PSP prob, int Xijk[][][]) throws IloException{
            for(int i=0; i<prob.inst.getNOE(); i++){
                for(int j=0; j<prob.inst.getNOD(); j++){
                    for(int k=0; k<prob.inst.getNOS(); k++){
                        model.X[i][j][k].setLB(Xijk[i][j][k]);
                        model.X[i][j][k].setUB(Xijk[i][j][k]);
                    }
                }
            }
        }
        
        private void freeWindows(int indexBeg, int incr, int j, int k) throws IloException {
            //free all cities form i to anywhere
            //free all cities from anywhere to j
            for(int i=indexBeg;i<(indexBeg+incr);i++){
                model.X[i][j][k].setLB(0.0);
                model.X[i][j][k].setUB(1.0);
            }

        }
        
        private cPSP getCodifFromModel(PSP prob) throws Exception{
            cPSP codif = (cPSP) prob.build_codif();
            
            
            for(int i=0;i<prob.inst.getNOE();i++)
            {
                for(int j=0;j<prob.inst.getNOD(); j++)
                { 
                    boolean flag = false;
                    for(int k = 0; k<prob.inst.getNOS();k++){

                        if(this.cpx.getValue(model.X[i][j][k])  > 0.5)
                        {
                            flag = true;
                            codif.schedule[i][j]= k+1;
                        }                    
                    }

                    if(!flag)
                        codif.schedule[i][j]= 0;

                }                
            }
            
            
      /**      
            double vXij[][][] = cpx.getValues(model.X);
        
            
            for(int i=0; i<prob.inst.getNOE(); i++){
                System.out.print("PHY["+(i+1)+"]");
                for(int j=0; j<prob.inst.getNOD(); j++){
                    for(int k=0;k<prob.inst.getNOS();k++){
                        System.out.print(" \t"+(int)vXij[i][j][k]);
                    }
                }
                System.out.print("\n");
            }
        
        
            System.out.print("\n");
            System.out.print("\n");
            System.out.print("\n");
            System.out.print("\n__________________________\n");
        
            
            for(int i=0; i<prob.inst.getNOE(); i++){
                System.out.print("PHY["+(i+1)+"]");
                for(int j=0; j<prob.inst.getNOD(); j++){

                        System.out.print(" \t"+(int)codif.schedule[i][j]);
                }
                System.out.print("\n");
            }*/
            
            return codif;
        }
        
        
        @Override
        public cPSP execute_FO_strategy(PSP prob, cPSP ind) throws Exception {
            if(model==null){
                model = new PSPmodel(prob.inst, cpx);
                model.model(false);
                model.cpx.setOut(null);
                model.cpx.setWarning(null);
            }
            
            //-------------- fixing the start solution ---------------------
            //getting the solution
            int Xijk[][][] = getSolFromCodif(prob, ind);
            
            
            //now fixing
           fixAll(prob, Xijk);
            
            Solution sol1 = prob.build_sol(ind);
            prob.evaluate(sol1);
            
            PSPObjective obj1 =  (PSPObjective) sol1.obj();
             
            //solving to cplex get this solution
            if(cpx.solve()){
                

            }else{
                throw new Exception("this is not expected");
            }
            
            /**/
            //-------------------- the fix and optimize will be started hehe -----------
            //for each window on FO         //the window will be a subset of all variables
                //fix take the current solution
                //fix fix all variables using the current solution
                //free the window variables 
                //solve the model 
           
            int dayInc = prob.inst.getWLenght();
            for(int w=0; w<prob.inst.getWeeks(); w++){   

                //fix fix all variables using the current solution

                //for each window on FO
                Xijk = getSolFromModel(prob);        //fix take the current solution
                //    cPSP current = getCodifFromModel(prob);
                fixAll(prob, Xijk);      
                //    int size = prob.rnd.nextInt(1, prob.inst.getNOE()); //selecting a window with random size

                //  for(int i=0; i<size; i++){ //selecting randomly the variables on window
                //      System.out.println("HERE!");  
                
                for(int i=0;i<prob.inst.getNOE();i++){
                    int incr = 2;
                    for(int j=prob.inst.begining(w); j<prob.inst.ending(w);j++){
                        for(int k=0;k<prob.inst.getNOS();k++){
                            if((i+incr)<prob.inst.getNOE())
                                freeWindows(i,incr,j,k);
                        }
                    }
                        //solving and try improve solution
                    if(cpx.solve()){
                          System.out.println("FO Obj: "+cpx.getObjValue());
                    }else{
                        throw new Exception("this is not expected");
                    }
                    
                }      
            }
                
            //-------------------- recovering the best solution from cplex -----------
            //creating a new codification and copping to it
            cPSP improved = getCodifFromModel(prob);
             
        
            return improved;//returning the improvement
        }

    }/***/
}