/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ProOF.apl.sample1.problem.NSP;

import java.util.Map;

/**
 *
 * @author claudio
 */
public class Requirements {
    private String shiftID;
    private String skill;
    private Map<Integer, Map< Integer, Integer> > demands;

    public String getShift() {
        return shiftID;
    }

    public void setShift(String shiftID) {
        this.shiftID = shiftID;
    }

    public String getSkill() {
        return skill;
    }

    public void setSkill(String skill) {
        this.skill = skill;
    }

    public Map<Integer, Map<Integer, Integer>> getDemands() {
        return demands;
    }

    public void setDemands(Map<Integer, Map<Integer, Integer>> demands) {
        this.demands = demands;
    }

    
    /****
      private String shiftType;
    private String skill;
    
    public Requirements(String shiftType, String skill, Map<Integer, Map<Integer, Integer>> demands) {
       
        setShiftType(shiftType);
        setSkill(skill);
    }

    public Requirements() {
        
    }
    
    public void setShiftType(String shiftType) {
        this.shiftType = shiftType;
    }

    public void setSkill(String skill) {
        this.skill = skill;
    }
    
    
    public String getShiftType() {
        return shiftType;
    }

    public String getSkill() {
        return skill;
    }

     
     */
}
