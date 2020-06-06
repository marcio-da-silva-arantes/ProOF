/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ProOF.apl.sample1.problem.NSP;


import ProOF.apl.sample1.problem.NSP.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

/**
 *
 * @author valdemar
 */
public class Staff {
    private String ID;
    private int maxTotalMinutes;
    private int minTotalMinutes; 
    private int maxConsecutiveShifts;
    private Vector<Integer> daysOff;
    private Vector<Map<Integer, String>> reqShift;
    private Vector <Map<Integer, String>> unavailability;
    private Vector <Integer> maxShifts;
    private int minConsecutiveShifts;
    private int minConsecutiveDaysOff;
    private int maxWeekends;
    private Vector<Map<String, Integer>> reqWeights;
    private Vector<Map<String, Integer>> offReqWeights;
    
    
    public Staff(String ID, int maxTotalMinutes, int minTotalMinutes, int maxConsecutiveShifts, int minConsecutiveShifts, 
            Vector<Integer> daysOff, Vector<Map<Integer, String>> reqShift, Vector<Map<Integer, String>> unavailability, 
            Vector<Integer> maxShifts, int minConsecutiveDaysOff, int maxWeekends, Vector<Map<String, Integer> > reqWeights, Vector<Map<String, Integer> > offReqWeights) {
        setID(ID);
        setMaxTotalMinutes(maxTotalMinutes);
        setMinTotalMinutes(minTotalMinutes);
        setDaysOff(daysOff);
        setUnavailability(unavailability);
        setReqShift(reqShift);
        setMaxConsecutiveShifts(maxConsecutiveShifts);
        setMinConsecutiveShifts(minConsecutiveShifts);
        setMinConsecutiveDaysOff(minConsecutiveDaysOff);
        setMaxShifts(maxShifts);
        setMaxWeekends(maxWeekends);
        setReqWeights(reqWeights);
        setOffReqWeights(offReqWeights);
    }
    public Staff() {
        setID("");
        setMaxTotalMinutes(0);
        setMinTotalMinutes(0);
        setMaxConsecutiveShifts(0);
        setMinConsecutiveShifts(0);
        setMinConsecutiveDaysOff(0);
        setDaysOff(new Vector<Integer>());
        setReqShift(new Vector<Map<Integer, String>>());
        setUnavailability(new Vector<Map<Integer, String> >());
        setMaxShifts(new Vector<Integer> ());
        setMaxWeekends(0);
        setReqWeights(new Vector<Map<String, Integer>>());
        setOffReqWeights(new Vector<Map<String, Integer> >());
    }

    public Vector<Map<String,Integer>> getOffReqWeights() {
        return offReqWeights;
    }

    public void setOffReqWeights(Vector<Map<String, Integer>> offReqWeights) {
        this.offReqWeights = offReqWeights;
    }
    
    public Vector<Map<String, Integer>> getReqWeights() {
        return reqWeights;
    }

    public void setReqWeights(Vector<Map<String, Integer>> reqWeights) {
        this.reqWeights = reqWeights;
    }
    
    public int getMaxWeekends() {
        return maxWeekends;
    }

    public void setMaxWeekends(int maxWeekends) {
        this.maxWeekends = maxWeekends;
    }

    public Vector<Integer> getMaxShifts() {
        return maxShifts;
    }

    public void setMaxShifts(Vector<Integer> maxShifts) {
        this.maxShifts = maxShifts;
    }

    
    public Vector<Map<Integer, String>> getUnavailability() {
        return unavailability;
    }
    
    public boolean isNotRequested(int day, String shift){
        if(!unavailability.isEmpty())
            for(Map<Integer, String> map: unavailability)
            {   
                if(map.get(day) != null)
                    if(map.get(day).contains(shift))
                        return true;
            }
        
    return false;
    }

    public boolean isUnavailable(int day, String shift) {
        
        if(!daysOff.isEmpty()){
            for(Integer e: daysOff)
            {
                if(e.compareTo(day)==0)
                    return true;
            }
        }
        
        return false;
    }
    
    
    public boolean isUnavailable(int day) {
        
        if(!daysOff.isEmpty()){
            for(Integer e: daysOff)
            {
                if(e.compareTo(day)==0)
                    return true;
            }
        }
        
        return false;
    }


    
    public boolean isRequired(int day, String shift) {
        if(!reqShift.isEmpty())
        for(Map<Integer, String> map: reqShift)
        {   
            if(map.get(day) != null)
                if(map.get(day).contains(shift))
                    return true;
        }
        
        return false;
    }
    
    public void setUnavailability(Vector <Map<Integer, String>> unavailability) {
        this.unavailability = unavailability;
    }
    
    public Vector <Map<Integer, String>> getReqShift() {
        return reqShift;
    }

    public void setReqShift(Vector <Map<Integer, String>> reqShift) {
        this.reqShift = reqShift;
    }

    public int getMaxConsecutiveShifts() {
        return maxConsecutiveShifts;
    }

    public void setMaxConsecutiveShifts(int maxConsecutiveShifts) {
        this.maxConsecutiveShifts = maxConsecutiveShifts;
    }

    public int getMinConsecutiveShifts() {
        return minConsecutiveShifts;
    }

    public void setMinConsecutiveShifts(int minConsecutiveShifts) {
        this.minConsecutiveShifts = minConsecutiveShifts;
    }

    public int getMinConsecutiveDaysOff() {
        return minConsecutiveDaysOff;
    }

    public void setMinConsecutiveDaysOff(int minConsecutiveDaysOff) {
        this.minConsecutiveDaysOff = minConsecutiveDaysOff;
    }

    public Vector<Integer> getDaysOff() {
        return daysOff;
    }

    public void setDaysOff(Vector<Integer> daysOff) {
        this.daysOff = daysOff;
    }

    
    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public int getMaxTotalMinutes() {
        return maxTotalMinutes;
    }

    public void setMaxTotalMinutes(int maxTotalMinutes) {
        this.maxTotalMinutes = maxTotalMinutes;
    }

    public int getMinTotalMinutes() {
        return minTotalMinutes;
    }

    public void setMinTotalMinutes(int minTotalMinutes) {
        this.minTotalMinutes = minTotalMinutes;
    }
    
    public static int getDifferentOcurrences(int oc, Vector <Integer> vect)
    {
        int count = 0;

        for (Integer i : vect)
        {
            if (oc == i)
            {
                count++;
            }
        }

        return count;
    }

    int getReqWeight(int d, Shift s) {
        
//        this.get(r).get(s.getID()) != null ? st.getReqWeights().get(r).get(s.getID()) : 0
        
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}