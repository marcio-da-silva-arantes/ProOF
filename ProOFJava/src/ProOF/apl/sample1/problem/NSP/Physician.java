/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ProOF.apl.sample1.problem.NSP;

import java.util.Map;
import java.util.Vector;

/**
 *
 * @author claudio
 */
public class Physician {
    private String name;
    private String contractID;
    private int nrSkill;
    private Vector<String> skills;

    public Physician(String name, String contract, int nrSkill, Vector<String> skills) {
        this.name = name;
        this.contractID = contractID;
        this.nrSkill = nrSkill;
        this.skills = skills;
    }

    
    public Physician() {
        this.name = "";
        this.contractID = "";
        this.nrSkill = 0;
        this.skills = new Vector<>();
    }

    
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContractID() {
        return contractID;
    }

    public void setContract(String contractID) {
        this.contractID = contractID;
    }

    public int getNrSkill() {
        return nrSkill;
    }

    public void setNrSkill(int nrSkill) {
        this.nrSkill = nrSkill;
    }

    public Vector<String> getSkills() {
        return skills;
    }

    public void setSkills(Vector<String> skills) {
        this.skills = skills;
    }
    
    
    
}
