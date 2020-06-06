/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ProOF.apl.sample1.problem.NSP;

import ProOF.apl.sample1.problem.NSP.*;
import ProOF.com.Linker.LinkerResults;
import ProOF.com.Stream.StreamPrinter;
import ProOF.opt.abst.problem.meta.objective.SingleObjective;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

/**
 *
 * @author dexter
 */
class NSPObjective extends SingleObjective<NSP, cNSP, NSPObjective> {
    int penalty = 100;
    int ant, cons, dem, doff, hour, mhour,mcons, mdoff, ron, roff, shift, wknd;
    public NSPObjective() throws Exception {
        super();
    }

    @Override
    public void copy(NSP prob, NSPObjective source) throws Exception {
        super.copy(prob, source); 
        this.ant  = source.ant;
        this.cons = source.cons;
        this.dem  = source.dem;
        this.doff = source.doff;
        this.hour = source.hour;
        this.mhour = source.mhour;
        this.mcons = source.mcons;
        this.mdoff = source.mdoff;
        this.ron  = source.ron;
        this.roff  = source.roff;
        this.wknd = source.wknd;
        this.shift = source.shift;
    }

    @Override
    public void printer(NSP prob, StreamPrinter stream, cNSP codif) throws Exception {
        super.printer(prob, stream, codif); 
        stream.printInt("a", "%3d", ant);
        stream.printInt("c", "%4d", cons);
        stream.printInt("de", "%6d", dem);
        stream.printInt("do", "%4d", doff);
        stream.printInt("h", "%4d", hour);
        stream.printInt("mh", "%4d", mhour);
        stream.printInt("mC", "%4d", mcons);
        stream.printInt("mD", "%4d", mdoff);
        stream.printInt("rof", "%4d", roff);
        stream.printInt("ron", "%4d", ron);
        stream.printInt("s", "%3d", shift);
        stream.printInt("w", "%3d", wknd);
    }

    @Override
    protected void evaluate(NSP prob, cNSP codif) throws Exception {
        double fitnessModel =0;
        
        //Penalisation for not being assignment in requested shifts 
      /*  roff=requestOff(prob,codif);        
        fitnessModel += roff;
        //Penalisation for being assignment in not requested shifts 
        ron = requestOn(prob,codif);
        fitnessModel += ron;     
        //controlling demand 
        dem = demand(prob, codif);
        fitnessModel +=  dem;     
        //controlling antagonists shifts      
        ant = antagonism(prob, codif);
        fitnessModel += ant*penalty*70;
        //controlling assignment in Days-Off        
        doff = daysOff(prob, codif);
        fitnessModel += doff*penalty;        
        //controlling maximum consecutive days of assignment
        cons=consecutiveness(prob, codif);
        fitnessModel += cons*penalty;
        //controlling minimum consecutive days off
        mdoff = minConsecutiveDaysOff(prob, codif);
        fitnessModel += mdoff*penalty;
        //controlling minimum consecutive days of assignment
        mcons = minConsecutiveness(prob, codif);
        fitnessModel +=mcons*penalty;      
        // controlling weekend assignment 
        wknd = weekend(prob, codif);
        fitnessModel +=wknd*penalty;
        //controlling min hour deviations
        mhour = minHour(prob,codif);
        fitnessModel+=mhour*penalty;
        //controlling max hour deviations
        hour = maxHour(prob,codif);
        fitnessModel+=hour*penalty;
        //controlling shift deviations
        shift = shift(prob,codif);
        fitnessModel+=shift*penalty;*/
        
        
        set(fitnessModel);
   }

    
    @Override
    public void results(NSP prob, LinkerResults link, cNSP codif) throws Exception {
        super.results(prob, link, codif); 
        
        BufferedWriter bw = null;
        FileWriter fw = null;
        
        fw = new FileWriter("rNSP_"+prob.inst.getNOE()+"_"+prob.inst.getNOD()+"_"+prob.inst.getNOS()+"_"+prob.best().ind()+".csv");
        bw = new BufferedWriter(fw);
        
        for(int d=0; d<prob.inst.getNOD(); d++){
                bw.write("\t Dia "+(d+1));
        }
        
        bw.write("\n");
        
        for(Employee e: prob.inst.getEmployees()){
            int p = prob.inst.getEmployees().indexOf(e);
            bw.write(e.getID());
            for(int j=0; j<prob.inst.getNOD(); j++){
                    bw.write(" \t"+(int)codif.schedule[p][j]);
            }
            bw.write("\n");
        }
        
        bw.write("\n");
        bw.write("\n Violations ");
        bw.write("\n Req. Off  \t "+requestOff(prob,codif));
        bw.write("\nReq. On  \t "+requestOn(prob, codif));
        bw.write("\nDemand  \t "+demand(prob, codif));
        bw.write("\nAntagonism  \t "+antagonism(prob, codif));
        bw.write("\nConsecutiveness  \t "+consecutiveness(prob, codif));
        bw.write("\nDays Off  \t "+daysOff(prob, codif));
        bw.write("\nMax Hours \t "+maxHour(prob, codif));
        bw.write("\nmin Hours \t "+minHour(prob, codif));
        bw.write("\nMin. Consecutive days  \t "+minConsecutiveness(prob, codif));
        bw.write("\nMin Days Off  \t "+minConsecutiveDaysOff(prob, codif));
        bw.write("\nshift \t "+shift(prob, codif));
        bw.write("\nWeekends \t "+weekend(prob, codif));
        
        if (bw != null)
            bw.close();

        if (fw != null)
            fw.close();
    }
    
    @Override
    public NSPObjective build(NSP prob) throws Exception {
        return new NSPObjective();
    }

    private int requestOff(NSP prob, cNSP codif) {
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
    
    
    private int requestOn(NSP prob, cNSP codif){
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
    
    public int antagonism(NSP prob, cNSP codif){
    
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
    
    
    public int daysOff(NSP prob, cNSP codif)
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
    
    
    int consecutiveness(NSP prob, cNSP codif){
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
    
    
    int shift(NSP prob, cNSP codif) {
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
    
    int minConsecutiveDaysOff(NSP prob, cNSP codif){
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
    int minConsecutiveness(NSP prob, cNSP codif)
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
    int demand(NSP prob, cNSP codif)
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
 
    int weekend(NSP prob, cNSP codif)
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

    
    private int minHour(NSP prob, cNSP codif) {
        
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
    
    
    private int maxHour(NSP prob, cNSP codif) {
        
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
    
    private int hourDev(NSP prob, cNSP codif) {
        
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
