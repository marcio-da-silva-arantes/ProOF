/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ProOF.apl.sample1.problem.NSP;

import java.util.Vector;

/**
 *
 * @author valdemar
 */
public class Assignment {
    public final Vector<Integer> [] assignment;
    public final int [] schedule;
        
    public Assignment(int nDays, int nShifts){   
        assignment = new Vector[nDays];
        for(int d=0; d<nDays; d++){
            assignment[d] = new Vector<>(nShifts);
        }
        
        schedule = new int[nDays];
    }
    
    public void assignShift(int day, int shift){
        schedule[day] = shift;
    }
    
    public int nDays(){
        return assignment.length;
    }
    
    public boolean add(int day, Integer shift){
        
        if(!assignment[day].contains(shift)){
            return assignment[day].add(shift);
        }else{
            return false;
        }
    }
    
    @Override
    public String toString() {
        String str = "";
        for(int i =0; i < assignment.length; i++){
            str += "day["+(i+1)+"] { "+assignment[i]+" }, ";
        }
        return str; //To change body of generated methods, choose Tools | Templates.
    }
}
