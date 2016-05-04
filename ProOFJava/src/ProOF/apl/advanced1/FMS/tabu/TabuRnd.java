/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ProOF.apl.advanced1.FMS.tabu;

import ProOF.com.Linker.LinkerParameters;
import ProOF.gen.operator.oLocalMove;
import ProOF.utilities.uRandom;

/**
 *
 * @author marcio
 */
public final class TabuRnd extends TabuList{
    public final uRandom rnd = new uRandom();
    private int change_iterations;         //number of iterations before change tabu_size to a new random value
    
    private int tabu_size;           //size of tabuList
    @Override
    public String name() {
        return "Randon";
    }
    @Override 
    public void parameters(LinkerParameters win) throws Exception {
        change_iterations = win.Int("Change Iter.", 1, 0, 100, "size of tabuList");
    }
    @Override
    protected int tabu_size() {
        return tabu_size;
    }
    
    private int loops;
    @Override
    public void start() throws Exception {
        super.start();
        tabu_size = rnd.nextInt(1, moves_size()-1);
        loops = 0;
    }
    @Override
    public void update(oLocalMove best_mov) {
        super.update(best_mov);
        if(loops++ >= change_iterations){
            loops = 0;
            tabu_size = rnd.nextInt(1, moves_size()-1);
        }
    }
}
