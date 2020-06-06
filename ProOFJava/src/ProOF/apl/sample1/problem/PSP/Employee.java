/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ProOF.apl.sample1.problem.PSP;

import ProOF.apl.sample1.problem.PSP.*;
import ProOF.opt.abst.problem.Instance;
import java.util.Map;
import java.util.Vector;

/**
 *
 * @author valdemar
 */
public class Employee {
    private String ID;
    private int maxMinutes; //stores the maximum number of minutes
    private int minMinutes; //stores the minimum number of minutes
    private int maxCons; //stores the maximum number of consecutive shifts
    private Vector <Integer> maxShifts; //stores the maximum number of shifts
    private int minCons; //stores the minimum number of consecutive shifts
    private int minDaysOff; //stores the minimum number of days off
    private int maxWeekends;//stores the maximum number of weekends
    
    
    public Employee(){
   
    }

    /**
     * @return the ID
     */
    public String getID() {
        return ID;
    }

    /**
     * @param ID the ID to set
     */
    public void setID(String ID) {
        this.ID = ID;
    }

    /**
     * @return the maxMinutes
     */
    public int getMaxMinutes() {
        return maxMinutes;
    }

    /**
     * @param maxMinutes the maxMinutes to set
     */
    public void setMaxMinutes(int maxMinutes) {
        this.maxMinutes = maxMinutes;
    }

    /**
     * @return the minMinutes
     */
    public int getMinMinutes() {
        return minMinutes;
    }

    /**
     * @param minMinutes the minMinutes to set
     */
    public void setMinMinutes(int minMinutes) {
        this.minMinutes = minMinutes;
    }

    /**
     * @return the maxCons
     */
    public int getMaxCons() {
        return maxCons;
    }

    /**
     * @param maxCons the maxCons to set
     */
    public void setMaxCons(int maxCons) {
        this.maxCons = maxCons;
    }

    /**
     * @return the maxShifts
     */
    public Vector <Integer> getMaxShifts() {
        return maxShifts;
    }

    /**
     * @param maxShifts the maxShifts to set
     */
    public void setMaxShifts(Vector <Integer> maxShifts) {
        this.maxShifts = maxShifts;
    }

    /**
     * @return the minCons
     */
    public int getMinCons() {
        return minCons;
    }

    /**
     * @param minCons the minCons to set
     */
    public void setMinCons(int minCons) {
        this.minCons = minCons;
    }

    /**
     * @return the minDaysOff
     */
    public int getMinDaysOff() {
        return minDaysOff;
    }

    /**
     * @param minDaysOff the minDaysOff to set
     */
    public void setMinDaysOff(int minDaysOff) {
        this.minDaysOff = minDaysOff;
    }

    /**
     * @return the maxWeekends
     */
    public int getMaxWeekends() {
        return maxWeekends;
    }

    /**
     * @param maxWeekends the maxWeekends to set
     */
    public void setMaxWeekends(int maxWeekends) {
        this.maxWeekends = maxWeekends;
    }
    
    
    
}
