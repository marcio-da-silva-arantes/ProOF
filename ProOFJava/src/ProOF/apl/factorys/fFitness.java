/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ProOF.apl.factorys;

import ProOF.com.language.Factory;
import ProOF.opt.abst.problem.extra.F;
import ProOF.opt.abst.problem.extra.Fitness;

/**
 *
 * @author marcio
 */
public final class fFitness extends Factory<Fitness>{
    public static final fFitness obj = new fFitness();
    
    @Override
    public String name() {
        return "Fitness";
    }
    
    @Override
    public Fitness build(int index) {
        switch(index){
            case 0: return new F();
        }
        return null;
    }
}
