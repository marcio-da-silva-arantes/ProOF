/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ProOF.apl.sample1.problem.NSP;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

/**
 *
 * @author claudio
 */
public class Duty {
    private String name;
    private Map<Integer, Integer> consecLimit;
    private int numberOfForbidden;
    private Vector<String> forbiddenShifts;

    

    public Duty(String name, Map<Integer, Integer> consecLimit, int numberOfForbidden, Vector<String> forbiddenShifts) {
        this.name = name;
        this.consecLimit = consecLimit;
        this.numberOfForbidden = numberOfForbidden;
        this.forbiddenShifts = forbiddenShifts;
    }

    
    public Duty() {
        this.name = "";
        this.consecLimit = new HashMap<Integer, Integer>();
        this.numberOfForbidden = 0;
        this.forbiddenShifts = new Vector<>();
    }

    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    public Map<Integer, Integer> getConsecLimit() {
        return consecLimit;
    }

    public void setConsecLimit(Map<Integer, Integer> consecLimit) {
        this.consecLimit = consecLimit;
    }

    public int getNumberOfForbidden() {
        return numberOfForbidden;
    }

    public void setNumberOfForbidden(int numberOfForbidden) {
        this.numberOfForbidden = numberOfForbidden;
    }

    public Vector<String> getForbiddenShifts() {
        return forbiddenShifts;
    }

    public void setForbiddenShifts(Vector<String> forbiddenShifts) {
        this.forbiddenShifts = forbiddenShifts;
    }
   
    
    
}
