package ProOF.apl.factorys;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



import ProOF.apl.advanced1.FMS.tabu.Tabu;
import ProOF.apl.advanced1.FMS.tabu.TabuFix;
import ProOF.apl.advanced1.FMS.tabu.TabuRnd;
import ProOF.com.language.Factory;

/**
 *
 * @author marcio
 */
public final class fTabu extends Factory<Tabu>{
    public static final fTabu obj = new fTabu();

    @Override
    public String name() {
        return "Tabu";
    }
    @Override
    public Tabu build(int index) {
        switch(index){
            case 0: return new TabuFix();
            case 1: return new TabuRnd();
        }
        return null;
    }
}
