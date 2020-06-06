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
 * @author dexter
 */
public class Cover {
    private int day;
    private Vector<Integer> demands;
    private Vector< Map<Integer, int [][]> > weights;

    public Cover(int day, HashMap<String, Integer> coverage, Vector<Integer> demands,Vector< Map<Integer, int [][]> > weights ) {
        this.day = day;
        this.demands = demands;
        this.weights = weights;
    }

    public Cover() {
        this.day = 0;
        demands = new Vector<>();
        weights = new Vector< Map<Integer, int [][]> >();
    }
    
    public Vector<Integer> getDemands() {
        return demands;
    }

    public void setDemands(Vector<Integer> demands) {
        this.demands = demands;
    }
    
    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public Vector< Map<Integer, int [][]> > getWeights() {
        return weights;
    }

    public void setWeights(Vector< Map<Integer, int [][]> > weights) {
        this.weights = weights;
    }
    
}
