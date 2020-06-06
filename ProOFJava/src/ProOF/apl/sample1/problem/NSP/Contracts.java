/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ProOF.apl.sample1.problem.NSP;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author claudio
 */
public class Contracts {
    private String type;
    private Map<Integer, Integer> totalAssignments;
    private Map<Integer, Integer> consecWorkDays;
    private Map<Integer, Integer> consecDaysOff;
    private int maxWeekends;
    private boolean fullWeekend;

    public Contracts(String type, Map<Integer, Integer> totalAssignments, Map<Integer, Integer> consecWorkDays, Map<Integer, Integer> consecDaysOff, int maxWeekends, boolean fullWeekend) {
        this.type = type;
        this.totalAssignments = totalAssignments;
        this.consecWorkDays = consecWorkDays;
        this.consecDaysOff = consecDaysOff;
        this.maxWeekends = maxWeekends;
        this.fullWeekend = fullWeekend;
    }

    public Contracts() {
        this.type = "";
        this.totalAssignments = new HashMap<>();
        this.consecWorkDays = new HashMap<>();
        this.consecDaysOff = new HashMap<>();
        this.maxWeekends = 0;
        this.fullWeekend = false;
    }

    
    
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Map<Integer, Integer> getTotalAssignments() {
        return totalAssignments;
    }

    public void setTotalAssignments(Map<Integer, Integer> totalAssignments) {
        this.totalAssignments = totalAssignments;
    }

    public Map<Integer, Integer> getConsecWorkDays() {
        return consecWorkDays;
    }

    public void setConsecWorkDays(Map<Integer, Integer> consecWorkDays) {
        this.consecWorkDays = consecWorkDays;
    }

    public Map<Integer, Integer> getConsecDaysOff() {
        return consecDaysOff;
    }

    public void setConsecDaysOff(Map<Integer, Integer> consecDaysOff) {
        this.consecDaysOff = consecDaysOff;
    }

    public int getMaxWeekends() {
        return maxWeekends;
    }

    public void setMaxWeekends(int maxWeekends) {
        this.maxWeekends = maxWeekends;
    }

    public boolean isFullWeekend() {
        return fullWeekend;
    }

    public void setFullWeekend(boolean fullWeekend) {
        this.fullWeekend = fullWeekend;
    }
    
    
}
