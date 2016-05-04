/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ProOF.apl.factorys;

import ProOF.apl.advanced1.FMS.local_search.BestImprovement;
import ProOF.apl.advanced1.FMS.local_search.FristImprovement;
import ProOF.apl.advanced1.FMS.local_search.LocalImprovement;
import ProOF.apl.advanced1.FMS.local_search.Nothing;
import ProOF.apl.advanced1.FMS.local_search.SimulatedAnnealing;
import ProOF.apl.advanced1.FMS.local_search.TabuSearch;
import ProOF.apl.advanced1.FMS.local_search.ThresholdAccepting;
import ProOF.com.language.Factory;

/**
 *
 * @author marcio
 */
public final class fLocalImprovement extends Factory<LocalImprovement>{
    public static final fLocalImprovement obj = new fLocalImprovement();
    
    @Override
    public String name() {
        return "Local Improvement";
    }
    
    @Override
    public LocalImprovement build(int index) {
        switch(index){
            case 0: return new Nothing();
            case 1: return new FristImprovement();
            case 2: return new BestImprovement();
            case 3: return new ThresholdAccepting();
            case 4: return new SimulatedAnnealing(fTemperature.obj);
            case 5: return new TabuSearch(fTabu.obj);
        }
        return null;
    }
}
