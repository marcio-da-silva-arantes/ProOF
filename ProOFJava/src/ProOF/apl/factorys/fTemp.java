package ProOF.apl.factorys;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



import ProOF.apl.sample1.FMS.temperature.aTemp;
import ProOF.apl.sample1.FMS.temperature.iTempA;
import ProOF.apl.sample1.FMS.temperature.iTempB;
import ProOF.com.language.Factory;

/**
 *
 * @author marcio
 */
public final class fTemp extends Factory<aTemp>{
    public static final fTemp obj = new fTemp();

    @Override
    public String name() {
        return "Temperature Functions";
    }
    @Override
    public aTemp build(int index) {
        switch(index){
            case 0: return new iTempA();
            case 1: return new iTempB();
        }
        return null;
    }
}
