package ProOF.apl.factorys;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import ProOF.apl.advanced1.FMS.structure.Grid;
import ProOF.apl.advanced1.FMS.structure.Randomly;
import ProOF.apl.advanced1.FMS.structure.RouletteExp;
import ProOF.apl.advanced1.FMS.structure.RouletteLinear;
import ProOF.apl.advanced1.FMS.structure.Structure;
import ProOF.apl.advanced1.FMS.structure.Tournament;
import ProOF.apl.advanced1.FMS.structure.Tree;
import ProOF.com.language.Factory;

/**
 *
 * @author marcio
 */
public final class fStructure extends Factory<Structure>{
    public static final fStructure obj = new fStructure();
    
    @Override
    public String name() {
        return "Structure";
    }
    
    @Override
    public Structure build(int index) {
        switch(index){
            case 0: return new Randomly();
            case 1: return new Tournament();
            case 2: return new RouletteLinear();
            case 3: return new RouletteExp();
            case 4: return new Tree();
            case 5: return new Grid();
        }
        return null;
    }
}
