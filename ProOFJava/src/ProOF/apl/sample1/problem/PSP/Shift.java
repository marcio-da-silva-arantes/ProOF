/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ProOF.apl.sample1.problem.PSP;


import ProOF.apl.sample1.problem.PSP.*;
import java.util.Vector;

/**
 *
 * @author valdemar
 */
public class Shift {
    private String ID;
    private int duration;
    private Vector<String> forbiddenShifts;

    public Shift(String ID, int duration, Vector<String> forbiddenShifts) {
        setID(ID);
        setForbiddenShifts(forbiddenShifts);
        setDuration(duration);
    }
    
    
    
    public Shift() {
        setDuration(0);
        setID("");
        setForbiddenShifts(new Vector<>());
    }
    
    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public Vector<String> getForbiddenShifts() {
        return forbiddenShifts;
    }

    public void setForbiddenShifts(Vector<String> forbiddenShifts) {
        this.forbiddenShifts = forbiddenShifts;
    }
    
    
    
}