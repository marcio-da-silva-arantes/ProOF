/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ProOF.apl.advanced1.FMS.tabu;

import ProOF.com.Linker.LinkerParameters;

/**
 *
 * @author marcio
 */
public final class TabuFix extends TabuList{
    private int tabu_size;           //size of tabuList

    @Override
    public String name() {
        return "Fix";
    }
    @Override 
    public void parameters(LinkerParameters win) throws Exception {
        tabu_size = win.Int("Tabu max", 1, 0, 100, "size of tabuList");
    }
    @Override
    protected int tabu_size() {
        return Math.min(tabu_size, moves_size()-1);
    }
}
