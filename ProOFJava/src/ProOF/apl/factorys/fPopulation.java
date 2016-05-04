package ProOF.apl.factorys;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import ProOF.apl.advanced1.FMS.population.Convergence;
import ProOF.apl.advanced1.FMS.population.NumberOfGenerations;
import ProOF.apl.advanced1.FMS.population.Population;
import ProOF.com.language.Factory;

/**
 *
 * @author marcio
 */
public final class fPopulation extends Factory<Population>{
    public static final fPopulation obj = new fPopulation();
    
    @Override
    public String name() {
        return "Population";
    }
    
    @Override
    public Population build(int index) {
        switch(index){
            case 0: return new Convergence(fStructure.obj);
            case 1: return new NumberOfGenerations();
        }
        return null;
    }
}
