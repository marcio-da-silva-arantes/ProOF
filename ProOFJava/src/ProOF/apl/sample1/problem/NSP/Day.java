/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ProOF.apl.sample1.problem.NSP;

import java.util.Collections;
import java.util.LinkedList;
import java.util.Vector;

/**
 *
 * @author dexter
 */
public class Day {
   
    private final Vector<Integer>[] shifts;
    
    public Day(int nShifts){   
        shifts = new Vector[nShifts];
        for(int k=0; k<nShifts; k++){
            shifts[k] = new Vector<>();
        }
    }
    public Day(int demand, int nShifts){   
        shifts = new Vector[nShifts];
        for(int k=0; k<nShifts; k++){
            shifts[k] = new Vector<>(demand);
        }
    }
    
    public int nShifts(){
        return shifts.length;
    }
    
    public boolean contains(Integer phy){
        for(int k=0; k<shifts.length; k++){
            if(shifts[k].contains(phy)){
                return true;
            }
        }
        return false;
    }
    
    public int phyShift(Integer phy){
        for(int k=0; k<shifts.length; k++){
            if(shifts[k].contains(phy)){
                return k;
            }
        }
        return -1;
    }
    
    public boolean add(int shift, Integer phy){
        if(!contains(phy)){
            return shifts[shift].add(phy);
        }else{
            return false;
        }
    }
    
    
    
    public Vector<Integer> shift(int shift){
        return shifts[shift];
    }
    
    public Vector<Integer>[] shifts(){
        return shifts;
    }
            
    public void setShifts(Vector <Integer> shifts, int k){
        if(shifts!=null)
            this.shifts[k].addAll(shifts);
        else 
            throw  new NullPointerException("Your 'to be' copied vector is null");
    }
    
//    
//    public void addVector(Day vec, HashMap hashm, int day){
//        Integer a = day;
//        hashm.put(a, vec);  
//    }
//    
//    public void setDailyAssignmentVector(int shift, DailyAssignment auxAss)
//    {   
//        shifts.setElementAt(auxAss,shift);
//    }
//    
//    public void setSAss(Vector <DailyAssignment> auxAss)
//    {
//        this.shifts = auxAss;
//    }
//    
//    public  DailyAssignment getDailyAssignmentVector(int shift)
//    {   
//        return (DailyAssignment) shifts.elementAt(shift);
//    }
//    
//    public  Vector <DailyAssignment> getAssignment()
//    {
//        return shifts;
//    }
//    
//    public void addSAss(int index, DailyAssignment shift)
//    {
//        this.getAssignment().insertElementAt(shift,index);
//    }
    
//    public String toString(int day,int shift, MDRPInstance instance){
//        
//        String strConc = "";
//       
//        for(int i =0; i < shift; i++)
//        {   
//            strConc += " "+this.getDailyAssignmentVector(shift).getPhysicianVec().get(i)+" ";
//        }
//        
//        return " shift "+shift+" -> phy "+strConc;
//    
//    
//    }

    @Override
    public String toString() {
        String str = "";
        for(int i =0; i < shifts.length; i++){
            str += "shift["+(i+1)+"] { "+shifts[i]+" }, ";
        }
        return str; //To change body of generated methods, choose Tools | Templates.
    }

    public void copy(Day day) {
        for(int k=0; k<shifts.length; k++){
            shifts[k].clear();
            shifts[k].addAll(day.shifts[k]);            
        }
    }


}