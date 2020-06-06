/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ProOF.apl.sample1.problem.NSP;

import java.util.Vector;

/**
 *
 * @author claudio
 */
public class Scenario {
    private int weeks, nrOfSkills, nrShifts, nrContracts, nrNurses;
    private Vector<String> skills;
    private Vector<Duty> duties;
    
    public Scenario() {
        this.weeks = 0;
        this.nrOfSkills = 0;
        this.nrShifts = 0;
        this.nrContracts = 0;
        this.nrNurses = 0;
        this.skills = new Vector<>();
        this.duties = new Vector<Duty>();
    }

    public Scenario(int weeks, int nrOfSkills, int nrShifts, int nrContracts, int nrNurses, Vector<String> skills, Vector<Duty> duties) {
        this.weeks = weeks;
        this.nrOfSkills = nrOfSkills;
        this.nrShifts = nrShifts;
        this.nrContracts = nrContracts;
        this.nrNurses = nrNurses;
        this.skills = skills;
        this.duties = duties;
    }

    public Vector<Duty> getDuties() {
        return duties;
    }

    public Duty getDutyByID(String name){
        
        for(Duty d:duties)
        {
            if(d.getName().equalsIgnoreCase(name))
            {
                return d;
            }
        }
        return new Duty();
    }
    
    
    public int getWeeks() {
        return weeks;
    }

    public void setWeeks(int weeks) {
        this.weeks = weeks;
    }

    public int getNrOfSkills() {
        return nrOfSkills;
    }

    public void setNrOfSkills(int nrOfSkills) {
        this.nrOfSkills = nrOfSkills;
    }

    public Vector<String> getSkills() {
        return skills;
    }

    public void setSkills(Vector<String> skills) {
        this.skills = skills;
    }

    public int getNrShifts() {
        return nrShifts;
    }

    public void setNrShifts(int nrShifts) {
        this.nrShifts = nrShifts;
    }

    public int getNrContracts() {
        return nrContracts;
    }

    public void setNrContracts(int nrContracts) {
        this.nrContracts = nrContracts;
    }

    public int getNrNurses() {
        return nrNurses;
    }

    public void setNrNurses(int nrNurses) {
        this.nrNurses = nrNurses;
    }
    
}
