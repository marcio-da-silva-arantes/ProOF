/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ProOF.apl.sample1.problem.PSP;

import ProOF.apl.sample1.problem.PSP.*;
import ProOF.com.Linker.LinkerResults;
import ProOF.com.Stream.StreamPrinter;
import ProOF.opt.abst.problem.meta.objective.SingleObjective;
import ilog.concert.IloNumExpr;
import ilog.cplex.IloCplex;
import ilog.cplex.IloCplex.DoubleParam;
import ilog.cplex.IloCplex.IntParam;
import ilog.cplex.IloCplex.Status;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

/**
 *
 * @author dexter
 */
class PSPObjective extends SingleObjective<PSP, cPSP, PSPObjective> {
    int penalty = 5;
    int ant, cons, dem, doff, hour, mhour,mcons, mdoff, ron, roff, shift, unq,wknd;
    
    LinkedList <Integer> infeasiblePhy = new LinkedList<>();
    
    public PSPObjective() throws Exception {
        super();
    }

    @Override
    public void copy(PSP prob, PSPObjective source) throws Exception {
        super.copy(prob, source); 
        this.ant  = source.ant;
        this.cons = source.cons;
        this.dem  = source.dem;
        this.doff = source.doff;
        this.mcons = source.mcons;
        this.mdoff = source.mdoff;
        this.ron  = source.ron;
        this.roff  = source.roff;
        this.wknd = source.wknd;
        this.shift = source.shift;
        this.unq = source.unq;
    }

    @Override
    public void printer(PSP prob, StreamPrinter stream, cPSP codif) throws Exception {
        super.printer(prob, stream, codif);
        
        
        stream.printInt("H1", "%3d", unq);
        stream.printInt("H2", "%3d", ant);
        stream.printInt("H3", "%3d", shift);
        stream.printInt("H5", "%5d", cons);
        stream.printInt("H6", "%5d", mcons);
        stream.printInt("H7", "%5d", mdoff);
        stream.printInt("H8", "%5d", wknd); 
        stream.printInt("H9", "%5d", doff);
        stream.printInt("F1", "%6d", roff);
        stream.printInt("F2", "%6d", ron);
        stream.printInt("F3", "%6d", dem);
    }

    @Override
    protected void evaluate(PSP prob, cPSP codif) throws Exception {
        double fitnessModel =0;
      
             
            
            
        prob.model.startSolutionFromHeuristic(prob.inst, codif);

        if(prob.cpx.solve())
        {
            if(prob.cpx.getStatus() == Status.Feasible || prob.cpx.getStatus() == Status.Optimal){
                fitnessModel = prob.cpx.getObjValue(); 

                roff = (int) prob.cpx.getValue(prob.model.ObjF1);   
                ron =  (int) prob.cpx.getValue(prob.model.ObjF2);   
                dem = (int) prob.cpx.getValue(prob.model.ObjF3) + (int) prob.cpx.getValue(prob.model.ObjF4); 


                prob.model.getFromModel(prob, codif);             
            }
            else{
                System.err.println("ERROR!");
                System.exit(0);
            }
        }
        
        set(fitnessModel);
    }

        
    @Override
    public void results(PSP prob, LinkerResults link, cPSP codif) throws Exception {
        super.results(prob, link, codif); 
        
        BufferedWriter bw = null;
        FileWriter fw = null;
        
        fw = new FileWriter("rPSP_"+prob.inst.getNOE()+"_"+prob.inst.getNOD()+"_"+prob.inst.getNOS()+"_"+prob.best().id()+"_"+prob.inst.getFile().getName().substring(0, 4)+".csv");
        bw = new BufferedWriter(fw);
        
        for(int d=0; d<prob.inst.getNOD(); d++){
                bw.write("\t Dia "+(d+1));
        }
        
        bw.write("\n");
        
        for(Employee e: prob.inst.getEmployees()){
            int p = prob.inst.getEmployees().indexOf(e);
            bw.write(e.getID());
            for(int j=0; j<prob.inst.getNOD(); j++){
                    bw.write(" \t"+codif.schedule[p][j]);
            }
            bw.write("\n");
        }
        
        //608.000 1533.0
        
        if (bw != null)
            bw.close();

        if (fw != null)
            fw.close();
        
    }
    
    @Override
    public PSPObjective build(PSP prob) throws Exception {
        return new PSPObjective();
    }

    private int requestOff(PSP prob, cPSP codif) {
        int fitnessModel = 0;
        
        for(int p = 0; p < prob.inst.getNOE(); p++) 
        {
            
            Employee emp = prob.inst.getEmployees().get(p);
            
            for(int d = 0; d< prob.inst.getNOD(); d++)
            {   int sch = codif.schedule[p][d]-1;
                
                for(Shift s: prob.inst.getShifts())
                {   int idx = prob.inst.getShiftIndexByID(s.getID());
                    
                    if(prob.inst.getOff_reqs().get(emp.getID())!=null)
                    {   
                            
                        if(prob.inst.getOff_reqs().get(emp.getID()).containsKey(d))
                        {   
                           
                            if(sch==idx)
                            {        
                                if(prob.inst.getOff_reqs().get(emp.getID()).get(d).containsKey(s.getID()))
                                {
                                    int pidt = prob.inst.getOff_reqs().get(emp.getID()).get(d).get(s.getID());
                                    fitnessModel+=pidt;
                                }                                
                            }
                        }
                    }
                }
                
            }
        }
        return fitnessModel;        
    }
    
    
    private int requestOn(PSP prob, cPSP codif){
        int fitnessModel = 0;
        
        for(int p = 0; p < prob.inst.getNOE(); p++) 
        {
            Employee emp = prob.inst.getEmployees().get(p);
            for(int d = 0; d< prob.inst.getNOD(); d++)
            {   int sch = codif.schedule[p][d]-1;
                
                for(Shift s: prob.inst.getShifts())
                {   int idx = prob.inst.getShiftIndexByID(s.getID());
                    
                    if(prob.inst.getOn_reqs().get(emp.getID())!=null)
                    {
                        if(prob.inst.getOn_reqs().get(emp.getID()).containsKey(d)){
                            if(sch!=idx){
                                if(prob.inst.getOn_reqs().get(emp.getID()).get(d).containsKey(s.getID())){
                                    int qidt = prob.inst.getOn_reqs().get(emp.getID()).get(d).get(s.getID());
                                    fitnessModel += qidt;
                                }
                            }
                        }                    
                    }
                }
            }
        }    
        return fitnessModel;        
    }
    
    public int antagonism(PSP prob, cPSP codif){
    
        int fitnessModel = 0;        
        
        for(int p=0; p< prob.inst.getNOE(); p++)
        { 
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
        }
        return fitnessModel;
    }
    
    
    public int daysOff(PSP prob, cPSP codif)
    {
        
        int fitnessModel = 0;
        
        for(int d=0; d< prob.inst.getNOD(); d++)
        {
            for(Employee e : prob.inst.getEmployees()){
                if(prob.inst.getDaysOff().get(e.getID())!=null){
                    Vector <Integer> daysOff = prob.inst.getDaysOff().get(e.getID());
                    int p = prob.inst.getEmployees().indexOf(e);
                    
                    if(codif.schedule[p][d]!=0 && daysOff.contains(d))
                    {
                        fitnessModel+= 1;
                    }
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
                    if(!infeasiblePhy.contains(p))
                        infeasiblePhy.add(p);
                }
                
                
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
    /**@param prob
     * @param codif
     * @return fitness
     */
    int minConsecutiveness(PSP prob, cPSP codif)
    {
        int fitnessModel = 0;
        
        for(Employee e : prob.inst.getEmployees()){
            int incr = e.getMinCons();
            int window = prob.inst.getNOD();
            int p = prob.inst.getEmployees().indexOf(e);
             
            for(int d =0; d<window;d++) {
                int i = 0;
                if(codif.schedule[p][d] > 0)
                {       
                    i=1;
                    while((d+i)<= window-1 && codif.schedule[p][d+i]>0){
                        i++;
                    }

                    if(i<incr){
                        fitnessModel+= 1;
                        if(!infeasiblePhy.contains(p))
                            infeasiblePhy.add(p);
                    }else{
                        d+=i;
                    }
                }
            }            
        }
        
        return fitnessModel;
    }
    
    
    /***
     * 
     * @param prob
     * @param codif
     * @return 
     * If the number assigned (x) is below the required number then the 
         * solution's penalty is (requirement - x) * weight for under 
         * 
         * If the total number assigned is more than the required number then 
         * the solution's penalty is:  (x - requirement) * weight for over
         */
    int demand(PSP prob, cPSP codif)
    {
        int fitnessModel = 0;
        
        for(int d=0;d< prob.inst.getNOD();d++)
        {   int count[] = new int[prob.inst.getNOS()];
            for(int i=0;i<count.length;i++){
                count[i]= 0;
            }
        
            for(Shift sft:prob.inst.getShifts())
            {
                int s = prob.inst.getShifts().indexOf(sft);
                
                for(Employee e: prob.inst.getEmployees())
                {   int p = prob.inst.getEmployees().indexOf(e);
                    
                    if(codif.schedule[p][d] == s+1)
                    {
                        count[s]++;
                    }
                }
                
                Set<Integer> demands = prob.inst.getCovers().get(d).get(sft.getID()).keySet();
                
                for(Integer dem:demands)
                {
                    if(count[s]< dem)
                    {
                        int under = (dem - count[s]);
                        Set<Integer> weights= prob.inst.getCovers().get(d).get(sft.getID()).get(dem).keySet();
                        
                        for(Integer weight: weights)
                        {   
                            under = under*weight;
                            fitnessModel+= under;
                        }
                    }
                    else{
                            int over = (count[s] - dem);
                            Set<Integer> weights= prob.inst.getCovers().get(d).get(sft.getID()).get(dem).keySet();
                        
                            for(Integer weight:weights)
                            {   
                                int w = prob.inst.getCovers().get(d).get(sft.getID()).get(dem).get(weight);
                                over = over*w;
                                fitnessModel+= over;
                            }
                    }
                }            
            }        
        }
        return fitnessModel; 
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
                
                if(!infeasiblePhy.contains(p))
                    infeasiblePhy.add(p);
            }
        }
        
        return fitnessModel;
    }
    
    
    private int maxHour(PSP prob, cPSP codif) {
        
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
            
            if(hDemand > e.getMaxMinutes())
            {
                fitnessModel += 1;
                
            }
        }
        
        return fitnessModel;
    }
    
    private int hourDev(PSP prob, cPSP codif) {
        
        int fitnessModel = 0;
        
        for(Employee e : prob.inst.getEmployees()){
            int hDemand = 0, dDev=0, uDev = 0, hours[] = new int[prob.inst.getShifts().size()];
            int p = prob.inst.getEmployees().indexOf(e);
            Arrays.fill(hours,0);
            int target = (e.getMaxMinutes() + e.getMinMinutes())/2;
            
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
            
            if(target-hDemand > 0)
            {
                dDev += (target-hDemand);
            }
            else{
                    uDev += (hDemand-target);
            }
            
            fitnessModel+= (Math.pow(dDev, 2) + Math.pow(uDev, 2));
        }
        
        return fitnessModel;
    }

    
}