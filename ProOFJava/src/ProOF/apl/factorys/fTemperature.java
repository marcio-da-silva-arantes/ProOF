package ProOF.apl.factorys;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import ProOF.apl.advanced1.FMS.temperature.TempA;
import ProOF.apl.advanced1.FMS.temperature.TempB;
import ProOF.apl.advanced1.FMS.temperature.Temperature;
import ProOF.com.language.Factory;

/**
 *
 * @author marcio
 */
public final class fTemperature extends Factory<Temperature>{
    public static final fTemperature obj = new fTemperature();

    @Override
    public String name() {
        return "Temperature Functions";
    }
    @Override
    public Temperature build(int index) {
        switch(index){
            case 0: return new TempA();
            case 1: return new TempB();
        }
        return null;
    }
}
