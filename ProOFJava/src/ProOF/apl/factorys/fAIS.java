/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ProOF.apl.factorys;

import ProOF.apl.advanced1.FMS.immune_system.AIS;
import ProOF.apl.advanced1.FMS.immune_system.SystemExponential;
import ProOF.apl.advanced1.FMS.immune_system.SystemHill;
import ProOF.apl.advanced1.FMS.immune_system.SystemInverse;
import ProOF.apl.advanced1.FMS.immune_system.SystemLinear;
import ProOF.com.language.Factory;

/**
 *
 * @author marcio
 */
public final class fAIS extends Factory<AIS>{
    public static final fAIS obj = new fAIS();
    
    @Override
    public String name() {
        return "Artificial Immune System";
    }
    
    @Override
    public AIS build(int index) {
        switch(index){
            case 0: return new SystemInverse();
            case 1: return new SystemExponential();
            case 2: return new SystemLinear();
            case 3: return new SystemHill();
        }
        return null;
    }
}
