/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ProOF.apl.factorys;

import ProOF.com.language.Factory;
import ProOF.gen.stopping.Stop;
import ProOF.gen.stopping.Evaluations;
import ProOF.gen.stopping.Forever;
import ProOF.gen.stopping.Integers;
import ProOF.gen.stopping.Iterations;
import ProOF.gen.stopping.Time;
import ProOF.gen.stopping.TimeAndCut;

/**
 *
 * @author marcio
 */
public final class fStop extends Factory<Stop>{
    public static final fStop obj = new fStop();
    
    @Override
    public String name() {
        return "Stop";
    }
    
    @Override
    public Stop build(int index) {
        switch(index){
            case 0: return new Evaluations();
            case 1: return new Time();
            case 2: return new Iterations();
            case 3: return new TimeAndCut();
            case 4: return new Forever();
            case 5: return new Integers();
        }
        return null;
    }
}
