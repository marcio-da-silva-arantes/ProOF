/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ProOF.apl.sample1.problem.NSP;

import ProOF.com.language.Factory;
import ProOF.gen.operator.oCrossover;
import ProOF.gen.operator.oMutation;
import ProOF.gen.operator.oInitialization;
import ProOF.gen.operator.oLocalMove;
import ProOF.opt.abst.problem.meta.codification.Codification;
import java.util.Random;
import ProOF.opt.abst.problem.meta.codification.Operator;
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
class NSPOperator_v2 extends Factory<Operator> {

    public static final NSPOperator_v2 obj = new NSPOperator_v2();

    public String name() {
        return "NSP-Operator"; //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Operator build(int index) throws Exception {
        switch(index) {
            case 0:
                return new SmartInitial(); 
            case 1:
                return new MutRandom(); 
            case 2:
                return new Uniform(); 
            case 3:
                return new UniformWknd(); 
            case 4:
                return new TwoPoints(); 
            case 5:
                return new TwoPointsWknd(); 
            case 6:
                return new MovRandom();
            case 7:
                return new MovImpIndSched();
            case 8:
                return new Mov2Exchange(); 
            case 9:
                return new MovMDOff(); 
            case 10:
                return new MovWknd(); 
            case 11:
                return new MovDaysOff();
            case 12:
                return new MovAntagonist(); 
            case 13:
                return new MovGlobal(); 
            case 14:
                return new PlagueInitial(); 
            case 15:
                return new MutRepair(); 
            case 16:
                return new MutDoubleExchange(); 
            case 17:
                return new Mut2Exchange(); 
            case 18:
                return new Mut3Exchange(); 
            case 19:
                return new MovConsec(); 
                
            case 20:
                return new MovDoubleExchange(); 
            case 21:
                return new TwoPointsV2(); 
            case 22: 
                return new ThreePoints(); 
            case 23: 
                return new Mov3Exchange(); 
            case 24: 
                return new GreedyInitial(); 
            case 25: 
                return new MutBlockExchange(); 
            case 26: 
                return new MovBlockExchange(); 
            case 27: 
                return new MutMinDaysOff(); 
            case 28: 
                return new MutShifts(); 
            case 29:
                return new MutImpIndSched(); 
            case 30:
                return new MutHoursAdj(); 
            case 31: 
                return new RandomInitial(); 
        }
        return null;
    }

    private class GreedyInitial extends oInitialization<NSP, cNSP> {

        public String name() {
            return "Greedy Initialization";
        }

        public void initialize(NSP prob, cNSP codif) throws Exception {
            for (Employee emp : prob.inst.getEmployees()) {
                int p = prob.inst.getEmployees().indexOf(emp);
                    
                codif.schedule[p] = setDaysOff(prob, codif, emp);
                codif.schedule[p] = setMinDaysOff(prob, codif, emp);
                codif.schedule[p] = setConsecHours2(prob, codif, emp);
                  
          /**      for (int d = 0; d < prob.inst.getNOD(); d++) {
                    System.out.print(codif.schedule[p][d] + "\t");
                }
                System.out.println("");*/
            }
            //maxHoursMove(prob, codif);
            //minConsecMove(prob, codif);
        //    mDaysOffMove(prob, codif);
        //    mDaysOffMove2(prob, codif);
         //   System.exit(0);
        //    repair(prob, codif);
        //    repair2(prob, codif);
        }
            

    }
    
    
    private class SmartInitial extends oInitialization<NSP, cNSP> {

        public String name() {
            return "Smart Initialization";
        }

        public void initialize(NSP prob, cNSP codif) throws Exception {
            for (Employee emp : prob.inst.getEmployees()) {
                int p = prob.inst.getEmployees().indexOf(emp);
                
                codif.schedule[p] = setDaysOff(prob, codif, emp);
                codif.schedule[p] = setMinDaysOff(prob, codif, emp);
                codif.schedule[p] = setOnRequest(prob, codif, emp);
                codif.schedule[p] = setOffRequest(prob, codif, emp);
                codif.schedule[p] = setConsDays(prob, codif, emp);
                codif.schedule[p] = setMinHours(prob,codif,emp);
            //    codif.schedule[p] = setWeekends(prob, codif, emp);  
                codif.schedule[p] = setMinDaysOff(prob, codif, emp);
                codif.schedule[p] = setMinConsec(prob, codif, emp); 
                codif.schedule[p] = setFinalCod(prob, codif, emp);
          /**      for (int d = 0; d < prob.inst.getNOD(); d++) {
                    System.out.print(codif.schedule[p][d] + "\t");
                }
                System.out.println("");*/
            }
        //    System.exit(0);
        //    setDemand(prob, codif);
        //    repair(prob, codif);
        //    repair2(prob, codif);
        }

    }

    private class PlagueInitial extends oInitialization<NSP, cNSP> {

        public String name() {
            return "Plague Initialization";
        }

        public void initialize(NSP prob, cNSP codif) throws Exception {
            for (Employee emp : prob.inst.getEmployees()) {
                int p = prob.inst.getEmployees().indexOf(emp);
                    
                codif.schedule[p] = setDaysOff(prob, codif, emp);
                codif.schedule[p] = setMinDaysOff(prob, codif, emp);
                codif.schedule[p] = setConsDays2(prob, codif, emp);
                codif.schedule[p] = setFinalCod(prob, codif, emp);
            /**    for (int d = 0; d < prob.inst.getNOD(); d++) {
                    System.out.print(codif.schedule[p][d] + "\t");
                }
                System.out.println("");
            }*/
        //    setDemand(prob, codif);
        //    repair(prob, codif);
        //    repair2(prob, codif);
    //        System.out.println(" ending of plague initialization");
          //  repair(prob, codif);
            }
        }
    }
        
    
    private class RandomInitial extends oInitialization<NSP, cNSP> {

        public String name() {
            return "Random Initialization";
        }

        public void initialize(NSP prob, cNSP codif) throws Exception {
            for (Employee emp : prob.inst.getEmployees()) {
                int p = prob.inst.getEmployees().indexOf(emp);
                    
                codif.schedule[p] = setDaysOff(prob, codif, emp);
                codif.schedule[p] = setMinDaysOff(prob, codif, emp);
                
                for (int d = 0; d < prob.inst.getNOD(); d++) {
                    
                    if(codif.schedule[p][d]!=-1){
                        codif.schedule[p][d] = prob.rnd.nextInt(0, prob.inst.getNOS());
                    }
                }
                
                codif.schedule[p] = setFinalCod(prob, codif, emp);
            
            /**    for (int d = 0; d < prob.inst.getNOD(); d++) {
                    System.out.print(codif.schedule[p][d] + "\t");
                }
                System.out.println("");*/
            }
            setDemand(prob, codif);
            repair(prob, codif);
            repair2(prob, codif);
        //    System.out.println(" ending of random initialization");
            repair(prob, codif);
        //    System.exit(0);
        }

    }
    
    private static int antagonist(NSP prob, cNSP codif, Employee emp){
    
       
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
    
    
    private int [] setAntagonist(NSP prob, cNSP codif, Employee emp){
    
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
        
    private static int minHours(NSP prob, cNSP codif, Employee emp){
    
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
            fitness += 1;
        }
        
        return fitness;
    }
    
    
    private int[] setConsecHours2(NSP prob, cNSP codif, Employee emp) {

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
    
    
    private int[] setConsecHours(NSP prob, cNSP codif, Employee emp) {

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
        
        //minConsec(prob, codif, emp);
     //   mDaysOffMove2(prob, codif);
        
        return codif.schedule[p];
    }
    
    
    
    private int[] setMaxHours(NSP prob, cNSP codif, Employee emp) {

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
    
    private int[] setMinHours(NSP prob, cNSP codif, Employee emp) {

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
    
    
    private static int maxHours(NSP prob, cNSP codif, Employee emp){
    
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
            fitness += 1;
        }
        
        return fitness;
    }
    
    
    private int[] setConsDays2(NSP prob, cNSP codif, Employee emp) {

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
    
    private static int consec(NSP prob, cNSP codif, Employee emp){
    
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
    
    
    private int[] setConsDays(NSP prob, cNSP codif, Employee emp) {

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

    private int[] setFinalCod(NSP prob, cNSP codif, Employee emp) {

        int window = prob.inst.getNOD();
        int p = prob.inst.getEmployees().indexOf(emp);

        for (int d = 0; d < window; d++) {
            if (codif.schedule[p][d] == -1) {
                codif.schedule[p][d] = 0;
            }
        }
        return codif.schedule[p];
    }

    
    private static int weekends(NSP prob, cNSP codif, Employee emp){
    
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
    
    
    private int[] setWeekends(NSP prob, cNSP codif, Employee emp) {
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
    
    private static int daysOff(NSP prob, cNSP codif, Employee emp){
    
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
    
    
    private int[] setDaysOff(NSP prob, cNSP codif, Employee emp) {
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

    private int[] setOnRequest(NSP prob, cNSP codif, Employee emp) {
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

    private int[] setOffRequest(NSP prob, cNSP codif, Employee emp) {
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

    private int[] setMinDaysOff(NSP prob, cNSP codif, Employee emp) {
        int p = prob.inst.getEmployees().indexOf(emp);

        int start = emp.getMinDaysOff();
        int window = prob.inst.getNOD();

        for (int d = 0; d < window; d++) {
            if (codif.schedule[p][d] == -1) {
                for (int i = 1; i < start; i++) {
                    if((d+i) < window){
                        if (codif.schedule[p][d + i] < 1) {
                            codif.schedule[p][d + i] = -1;
                        } 
                    }
                }
                d += start;
            }
        }
        return codif.schedule[p];
    }

    
    
    private int[] setMinDaysOff2(NSP prob, cNSP codif, Employee emp) {
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

    
    
    private static int minConsec(NSP prob, cNSP codif, Employee emp) {
        int incr = emp.getMinCons();
        int window = prob.inst.getNOD(), fitness = 0;
        int p = prob.inst.getEmployees().indexOf(emp);

        for(int d =0; d<window;d++) {
            int i = 0;
            if(codif.schedule[p][d] > 0)
            {       
                i=1;
                while((d+i)<= window-1 && codif.schedule[p][d+i]>0){
                    i++;
                }

                if(i<incr){
                    fitness+= 1;
                }else{
                    d+=i;
                }
            }
            if(d == 0)
            {
                if((codif.schedule[p][d] > 0) && codif.schedule[p][d+1] < 1)
                {
                    codif.schedule[p][d] = 0;
                }
            }  
        
        }
        return fitness;
    }
    
    private int[] setMinConsec(NSP prob, cNSP codif, Employee emp) {
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

    private class Uniform extends oCrossover<NSP, cNSP> {

        @Override
        public String name() {
            return "Uniform";
        }

        @Override
        public cNSP crossover(NSP prob, cNSP ind1, cNSP ind2) throws Exception {
            cNSP child = ind1.build(prob);

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
            repair(prob, child);
            repair2(prob, child);
        //    System.out.println("Uniform Crossover End");
            return child;
        }
        
    }

    
    
    private class TwoPoints extends oCrossover<NSP, cNSP> {

        @Override
        public String name() {
            return "TwoPoints";
        }

        @Override
        public cNSP crossover(NSP prob, cNSP ind1, cNSP ind2) throws Exception {
            cNSP child = ind1.build(prob);
            int p[] = prob.rnd.cuts_points(prob.inst.getNOE(), 2);
      //      System.out.println("2 Pts");
            
            if(p[0]>0){
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
            }
            else{
                
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
            }
            repair(prob, child);
            repair2(prob, child);
        //    System.out.println("2 Pts xover End");
            return child;
        }
    }

    
    private class TwoPointsWknd extends oCrossover<NSP, cNSP> {

        @Override
        public String name() {
            return "2 Pts wknd";
        }

        @Override
        public cNSP crossover(NSP prob, cNSP ind1, cNSP ind2) throws Exception {
            cNSP child = ind1.build(prob);
            int p[] = prob.rnd.cuts_points(prob.inst.getNOE(), 2);
        //    System.out.println("2 Pts wknd");
            
            
            if (p[0] > 0) {
                for (int i = 0; i < p[0]; i++) {
                    for (Integer j : prob.inst.getWeekends()) {
                        child.schedule[i][j] = ind1.schedule[i][j];
                    }
                }

                for (int i = p[0]; i < p[1]; i++) {
                    for (Integer j : prob.inst.getWeekends()) {
                        child.schedule[i][j] = ind1.schedule[i][j];
                    }
                }


                for (int i = p[1]; i < prob.inst.getNOE(); i++) {
                    for (Integer j : prob.inst.getWeekends()) {
                        child.schedule[i][j] = ind2.schedule[i][j];
                    }
                }
        //        System.out.println(" First condition");
            }
            else{
                
                    for (int i = p[0]; i < p[1]; i++) {
                        for (Integer j : prob.inst.getWeekends()) {
                            child.schedule[i][j] = ind1.schedule[i][j];
                        }
                    }


                    for (int i = p[1]; i < prob.inst.getNOE(); i++) {
                        for (Integer j : prob.inst.getWeekends()) {
                            child.schedule[i][j] = ind2.schedule[i][j];
                        }
                    }
          //      System.out.println(" Scnd condition");
            }
          //  System.out.println("Here!");
            repair(prob, child);
            repair2(prob, child);
        //    System.out.println("2 Pts wknd End");
            return child;
        }
    }

    private class ThreePoints extends oCrossover<NSP, cNSP> {

        @Override
        public String name() {
            return "3 Pts";
        }

        @Override
        public cNSP crossover(NSP prob, cNSP ind1, cNSP ind2) throws Exception {
            cNSP child = ind1.build(prob);
            int p[] = prob.rnd.cuts_points(prob.inst.getNOE(), 3);
        //    System.out.println("3 Pts crossover");
            
            if(p[0]>0){
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

                for (int i = p[1]; i < p[2]; i++) {
                    for (int j = 0; j < prob.inst.getNOD(); j++) {
                        child.schedule[i][j] = ind1.schedule[i][j];
                    }
                }

                for (int i = p[2]; i < prob.inst.getNOE(); i++) {
                    for (int j = 0; j < prob.inst.getNOD(); j++) {
                        child.schedule[i][j] = ind2.schedule[i][j];
                    }
                }
            }
            else{
                for (int i = p[0]; i < p[1]; i++) {
                    for (int j = 0; j < prob.inst.getNOD(); j++) {
                        child.schedule[i][j] = ind2.schedule[i][j];
                    }
                }

                for (int i = p[1]; i < p[2]; i++) {
                    for (int j = 0; j < prob.inst.getNOD(); j++) {
                        child.schedule[i][j] = ind1.schedule[i][j];
                    }
                }

                for (int i = p[2]; i < prob.inst.getNOE(); i++) {
                    for (int j = 0; j < prob.inst.getNOD(); j++) {
                        child.schedule[i][j] = ind2.schedule[i][j];
                    }
                }
            
            }
            repair(prob, child);
            repair2(prob, child);
        //    System.out.println("3 Pts End");
            return child;
        }
    }

    private class TwoPointsV2 extends oCrossover<NSP, cNSP> {

        @Override
        public String name() {
            return "ThreePoints-v2";
        }

        @Override
        public cNSP crossover(NSP prob, cNSP ind1, cNSP ind2) throws Exception {
            cNSP child = ind1.build(prob);
        //    System.out.println("ThreePoints-v2");
            int p[] = prob.rnd.cuts_points(prob.inst.getNOE(), 3);
            int d[] = prob.rnd.cuts_points(prob.inst.getNOD(), 3);
            
          //  System.out.println(" p[0]:"+p[0]+" p[1]:"+p[1]+" p[2]:"+p[2]);
            for (int i = p[0]; i < p[1]; i++) {
                for (int j = d[0]; j < d[1]; j++) {
                    child.schedule[i][j] = ind1.schedule[i][j];
                }
            }

            for (int i = p[1]; i <  p[2]; i++) {
                for (int j = d[1]; j < d[2]; j++) {
                    child.schedule[i][j] = ind2.schedule[i][j];
                }
            }
            
            for (int i = p[2]; i < prob.inst.getNOE(); i++) {
                for (int j = d[2]; j < prob.inst.getNOD(); j++) {
                    child.schedule[i][j] = ind1.schedule[i][j];
                }
            }

            repair(prob, child);
            repair2(prob, child);
        //    System.out.println("TwoPoints-v2 End");
            return child;
        }
    }

    private class UniformWknd extends oCrossover<NSP, cNSP> {

        @Override
        public String name() {
            return "Uniform-Wknd";
        }

        @Override
        public cNSP crossover(NSP prob, cNSP ind1, cNSP ind2) throws Exception {
            cNSP child = ind1.build(prob);
        //    System.out.println("Uniform-Wknd x-over");

            for (int i = 0; i < prob.inst.getNOE(); i++) {
                for (Integer j : prob.inst.getWeekends()) {
                //    System.out.println(" Day "+j);
                    if (prob.rnd.nextBoolean()) {
                        child.schedule[i][j] = ind1.schedule[i][j];
                    } else {
                        child.schedule[i][j] = ind2.schedule[i][j];
                    }
                }
            }
           
            repair(prob, child);
            repair2(prob, child);
        //    System.out.println("Uniform-Wknd x-over End");
            return child;
        }
    }
    
    private class MutRandom extends oMutation<NSP, cNSP> {

        @Override
        public String name() {
            return "Mut-random";
        }

        @Override
        public void mutation(NSP prob, cNSP ind) throws Exception {
        //    System.out.println(" Random Mutation");
            random_swap(prob, ind);
        //    System.out.println("Random Mutation End");
        }
    }
    
    
    
    private class MutBlockExchange extends oMutation<NSP, cNSP>{

        @Override
        public String name(){
            return "Mut-Block-Exchange";
        }
        
        @Override
        public void mutation(NSP prob, cNSP codif) throws Exception {
        //    System.out.println("Block Mutation ");
            block_exchange(prob, codif);
        //    System.out.println("Block Mutation End");
        }
    }
    
    
    private class MutShifts extends oMutation<NSP, cNSP> {
        
        @Override
        public String name(){
            return "Mut-Shifts-Exchange";
        }
    
        @Override
        public void mutation(NSP prob, cNSP codif) throws Exception {
        //    System.out.println("shift exchange mut.");
            shiftMove(prob, codif);
        //    System.out.println("shift exchange mut. end");
        }
    }
    
    
    private class MutImpIndSched extends oMutation<NSP, cNSP>{

        @Override
        public String name(){
            return "Mut-Impr-Ind-Sched";
        }
        
        @Override
        public void mutation(NSP prob, cNSP codif) throws Exception {
        //    System.out.println("Improvement of Individual schedule.");
            setDemand(prob, codif);
        //    System.out.println("Improvement of Individual schedule mut. end");
        }
    }
    
    private class MutMinDaysOff extends oMutation<NSP, cNSP> {
        
        @Override
        public String name(){
            return "Mut-MinDaysOff-Exchange";
        }
    
        @Override
        public void mutation(NSP prob, cNSP codif) throws Exception {
        //    System.out.println("Mut-MinDaysOff schedule.");
            mDaysOffMove2(prob, codif);
        //    System.out.println("Mut-MinDaysOff mut. end");
        }
    }
    
    private class MutRepair extends oMutation<NSP, cNSP> {

        @Override
        public String name() {
            return "Mut-Repair";
        }

        @Override
        public void mutation(NSP prob, cNSP ind) throws Exception {
        //    System.out.println("Repair mutation.");
          //  setDemand(prob, ind);
            repair(prob, ind);
            repair2(prob, ind);
         //   System.out.println("Repair mutation end");
        }
    }
    
    private class Mut2Exchange extends oMutation<NSP, cNSP>{

        @Override
        public String name(){
            return "Mut-2-Exchange";
        }
        
        @Override
        public void mutation(NSP prob, cNSP codif) throws Exception {
        //    System.out.println("2-Exchange mutation.");
            exchange_2(prob, codif);
        //    System.out.println("2-Exchange mutation end");
        }
    }
    
    
    private class Mut3Exchange extends oMutation<NSP, cNSP>{

        @Override
        public String name(){
            return "Mut-3-Exchange";
        }
        
        @Override
        public void mutation(NSP prob, cNSP codif) throws Exception {
        //    System.out.println("3-Exchange mutation.");
            exchange_3(prob, codif);
        //    System.out.println("3-Exchange mutation end");
        }
    }
    
    
    private class MutDoubleExchange extends oMutation<NSP, cNSP>{

        @Override
        public String name(){
            return "Mut-Double-Exchange";
        }
        
        @Override
        public void mutation(NSP prob, cNSP codif) throws Exception {
        ///    System.out.println("Double-Exchange mutation.");
            double_exchange(prob, codif);
        //    System.out.println("Double-Exchange mutation end.");
        }
    }
    
    
    private class MutHoursAdj extends oMutation<NSP, cNSP>{

        @Override
        public String name(){
            return "Mut-Min-Hours";
        }
        
        @Override
        public void mutation(NSP prob, cNSP codif) throws Exception {
        ///    System.out.println("Double-Exchange mutation.");
            hoursMove(prob, codif); ;
        //    System.out.println("Double-Exchange mutation end.");
        }
    }
    
    private static void repair2(NSP prob, cNSP codif){
    //    System.out.println("Repair function #2");
        
        for(Employee emp: prob.inst.getEmployees()){
            int fitnessA = 0, fitnessC = 0,
            fitnessDO = 0, fitnessH = 0, fitnessMC = 0, fitnessMH = 0, fitnessW =0;  
           //     do{
                //    System.out.println("");
                    fitnessA = antagonist(prob,codif,emp);
                    if(fitnessA>0){
                        antMove(prob, codif);
                    }
                //    System.out.println("Antagonism :"+antagonist(prob,codif,emp));
                    
                    fitnessC = consec(prob,codif,emp);
                    if(fitnessC > 0){
                        consecMove(prob, codif); 
                    }
               //     System.out.println("Consec :"+consec(prob,codif,emp));
                    
                    fitnessDO = daysOff(prob, codif, emp);
                    if(fitnessDO> 0){
                        daysOffMove(prob, codif); 
                    }
                //    System.out.println("Days Off :"+daysOff(prob,codif,emp));

                    fitnessH = minHours(prob, codif, emp);
                    if(fitnessH>0)
                    {
                        hoursMove(prob, codif);
                    }
                    fitnessMH = maxHours(prob, codif, emp);
                    if(fitnessMH>0)
                    {
                        maxHoursMove(prob, codif);
                    }
               //     System.out.println("Hours :"+minHours(prob,codif,emp));

                    fitnessMC = minConsec(prob, codif, emp);
                    if(fitnessMC>0)
                    {
                        minConsecMove(prob, codif);  
                    }
                //    System.out.println("MinCons :"+minConsec(prob,codif,emp));

                    fitnessW  = weekends(prob, codif, emp);
                    if(fitnessW>0){
                        wkndsMove(prob, codif);
                    }
                    
                    shiftMove(prob, codif);
                    minConsecMove(prob, codif);
                    mDaysOffMove(prob, codif);
                //    System.out.println("weekends :"+weekends(prob,codif,emp));
                    
                //    fitness = fitnessA+fitnessC+fitnessDO+fitnessH+fitnessMC+fitnessW;
             //   }while(fitness > 15);
            }
    //        System.out.println("End of repair function #2");
            finalCod(prob, codif);
            mDaysOffMove2(prob, codif);
    }
    
    private static void repair(NSP prob, cNSP codif){
        //System.out.println("Repair function");
         LinkedList<Integer> list = new LinkedList<>();
            list.addLast(1);
            list.addLast(2);
            list.addLast(3);
            list.addLast(4);
            list.addLast(5);
            list.addLast(6);
            list.addLast(6);
            list.addLast(7);
            list.addLast(8);
            list.addLast(9);
            Collections.shuffle(list);
            int n = prob.rnd.nextInt(0,list.size());
            
            for(int i =0;i<n;i++){
                list.get(i);
                switch(i){
                    case 1: wkndsMove(prob, codif); break;
                    case 2: consecMove(prob, codif); break;
//                    case 3: hoursMove(prob, codif); break;
                    case 3: minConsecMove(prob, codif); break;
                    case 4: daysOffMove(prob, codif); break;
                    case 5: mDaysOffMove(prob, codif); break;
                    case 6: antMove(prob, codif); break;
                    case 7: shiftMove(prob, codif); break;
                    case 8: maxHoursMove(prob, codif); break;
                }
            }
            finalCod(prob, codif);
            mDaysOffMove2(prob, codif);
         //   minConsecMove(prob, codif);
           // mDaysOffMove2(prob, codif);
            //System.out.println("End of repair function #1");
    }
    
    private static void random_swap(NSP prob, cNSP ind) {
        int emp = prob.rnd.nextInt(ind.schedule.length);
        int day = prob.rnd.nextInt(ind.schedule[emp].length);

        ind.schedule[emp][day] = prob.rnd.nextInt(1, prob.inst.getNOS());
    }
    
    private static void exchange_2(NSP prob, cNSP ind) {
        
        int emp1 =  prob.rnd.nextInt(ind.schedule.length);
        int emp2 =  prob.rnd.nextInt(ind.schedule.length);
        int day = prob.rnd.nextInt(ind.schedule[emp1].length);
        
        int aux = ind.schedule[emp1][day];
        
        ind.schedule[emp1][day] = ind.schedule[emp2][day];
        ind.schedule[emp2][day] = aux;
    }
        
    private static void exchange_3(NSP prob, cNSP ind) {
        
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
        
    private static void double_exchange(NSP prob, cNSP ind) {
        
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
    
    private static void block_exchange(NSP prob, cNSP ind) {
        
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
    
    private static void minConsecMove(NSP prob, cNSP codif) {
    
        for (Employee emp:prob.inst.getEmployees()) {
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
    }

    private static void antMove(NSP prob, cNSP codif){
    
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
    
    private static void consecMove(NSP prob, cNSP codif){
    
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
    
    
    private static void daysOffMove(NSP prob, cNSP codif) {
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
    
    private static void wkndsMove(NSP prob, cNSP codif){
    
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
    
    
    private static void mDaysOffMove2(NSP prob, cNSP codif){
        
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
    
    private static void mDaysOffMove(NSP prob, cNSP codif){
        
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
    
    private static void finalCod(NSP prob, cNSP codif) {

        for(Employee emp:prob.inst.getEmployees()){
            int window = prob.inst.getNOD();
            int p = prob.inst.getEmployees().indexOf(emp);

            for (int d = 0; d < window; d++) {
                if (codif.schedule[p][d] == -1) {
                    codif.schedule[p][d] = 0;
                }
            }
        }
    }

    private static void shiftMove(NSP prob,cNSP codif){
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
    
    private static void hoursMove(NSP prob,cNSP codif){
        for(Employee emp:prob.inst.getEmployees()){
                
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
        }    
    }
         
    private static void setDemand(NSP prob, cNSP codif) {
        int window = prob.inst.getNOD();
        LinkedList<Integer> list;

        for(int d = 0; d < window;d++)
        {   
            list = prob.inst.getAvailable(codif,d);

            for(Shift shift: prob.inst.getShifts()){
                int s = prob.inst.getShifts().indexOf(shift)+1;

                for(Integer value: prob.inst.getCovers().get(d).get(shift.getID()).keySet())
                {   int  dem = (value > list.size()) ? list.size(): value ;

                    while(dem > 0)
                    { 
                        int p = list.get((int) (Math.random()*(list.size()-1)));

                        if(codif.schedule[p][d]== s)
                        {
                            list.removeFirstOccurrence(p);
                            dem--;
                        }
                        else{
                                if(codif.schedule[p][d] == 0 ){
                                    codif.schedule[p][d] = s;
                                    list.removeFirstOccurrence(p);
                                    dem--;
                                }

                                else{
                                    if(codif.schedule[p][d] != 0 && codif.schedule[p][d] !=s){
                                            list.removeFirstOccurrence(p);
                                    }                                   
                                }
                            }

                        if(list.isEmpty()){
                            dem = 0;
                        }
                    }
                }                    
            }
        }  
    }
    
    private  static void maxHoursMove(NSP prob, cNSP codif) {
    
        for(Employee emp:prob.inst.getEmployees()){
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
            System.out.println("Begin");
            System.out.println(+emp.getMinMinutes()+"<="+hDemand+"<="+emp.getMaxMinutes());
            while(hDemand>emp.getMaxMinutes())
            {
                int i = 0, aux = 0;
                do{
                    i = prob.rnd.nextInt(0,window-1);
                    aux = codif.schedule[p][i];
                    codif.schedule[p][i] = 0;
                }while(aux==0);
                if(aux > 0){
                    hDemand -= prob.inst.getShifts().get(aux-1).getDuration();
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
            
            System.out.println(+emp.getMinMinutes()+"<="+hDemand+"<="+emp.getMaxMinutes());
        }
        
    }    
    
    
    // local move OPERATORS
     
    
    private class MovGlobal extends oLocalMove<NSP, cNSP>{

        @Override
        public String name(){
            return "Mov-global";
        }
        
        @Override
        public void local_search(NSP prob, cNSP codif) throws Exception {
            repair2(prob, codif);
            repair(prob, codif);
        }
    }
    
    private class MovAntagonist extends oLocalMove<NSP, cNSP>{

        @Override
        public String name(){
            return "Mov-Antagonist";
        }
        
        @Override
        public void local_search(NSP prob, cNSP codif) throws Exception {
            antMove(prob,codif);
        }
    }
    
    private class MovDaysOff extends oLocalMove<NSP, cNSP>{

        @Override
        public String name(){
            return "Mov-DaysOff";
        }
        
        @Override
        public void local_search(NSP prob, cNSP codif) throws Exception {
            daysOffMove(prob,codif);
            finalCod(prob, codif);
        }
    }
    
    private class MovWknd extends oLocalMove<NSP, cNSP>{
        @Override
        public String name(){
            return "Mov-Wknds";
        }
    
        @Override
        public void local_search(NSP prob, cNSP codif) throws Exception{
            wkndsMove(prob, codif);
        }
    }
    
    private class MovMDOff extends oLocalMove<NSP, cNSP>{

        @Override
        public String name(){
            return "Mov-mDaysOff";
        }
        
        @Override
        public void local_search(NSP prob, cNSP codif) throws Exception {
            mDaysOffMove2(prob, codif);
        }
    }
    private class MovConsec extends oLocalMove<NSP, cNSP> {

        @Override
        public String name() {
            return "Mov-Consec";
        }

        @Override
        public void local_search(NSP prob, cNSP codif) throws Exception {           
            consecMove(prob,codif);
        }
    }

    private class MovRandom extends oLocalMove<NSP, cNSP> {

        @Override
        public String name() {
            return "Mov-Exchange";
        }

        @Override
        public void local_search(NSP prob, cNSP ind) throws Exception {
            random_swap(prob, ind);
        }
    }
    
    
    private class Mov2Exchange extends oLocalMove<NSP, cNSP>{

        @Override
        public String name(){
            return "Mov-2-Exchange";
        }
        
        @Override
        public void local_search(NSP prob, cNSP codif) throws Exception {
            exchange_2(prob, codif);
        }
    }
    
    
    private class Mov3Exchange extends oLocalMove<NSP, cNSP>{

        @Override
        public String name(){
            return "Mov-3-Exchange";
        }
        
        @Override
        public void local_search(NSP prob, cNSP codif) throws Exception {
            exchange_3(prob, codif);
        }
    }
    
    
    private class MovDoubleExchange extends oLocalMove<NSP, cNSP>{

        @Override
        public String name(){
            return "Mov-Double-Exchange";
        }
        
        @Override
        public void local_search(NSP prob, cNSP codif) throws Exception {
            double_exchange(prob, codif);
        }
    }
    
    private class MovImpIndSched extends oLocalMove<NSP, cNSP>{

        @Override
        public String name(){
            return "Mov-Impr-Ind-Sched";
        }
        
        @Override
        public void local_search(NSP prob, cNSP codif) throws Exception {
            for(Employee emp: prob.inst.getEmployees()){
                setOffRequest(prob, codif, emp);
                setOnRequest(prob, codif, emp);
            }
            setDemand(prob, codif);
        }
    }
    
    
    
    private class MovBlockExchange extends oLocalMove<NSP, cNSP>{

        @Override
        public String name(){
            return "Mov-Block-Exchange";
        }
        
        @Override
        public void local_search(NSP prob, cNSP codif) throws Exception {
            block_exchange(prob, codif);
        }
    }
}