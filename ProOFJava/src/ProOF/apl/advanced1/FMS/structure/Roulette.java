/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ProOF.apl.advanced1.FMS.structure;

import ProOF.opt.abst.problem.meta.Problem;
import ProOF.opt.abst.problem.meta.Solution;
import ProOF.utilities.uRoulette;

/**
 *
 * @author marcio
 */
public abstract class Roulette extends Structure{
    private final uRoulette roul;
    private final double weigth[];
    
    protected Roulette(Problem problem, int nInds) throws Exception{
        super(problem, nInds);
        weigth = new double[inds.length];
        for(int i=0; i<weigth.length; i++){
            weigth[i] = weigth(i);
        }
        roul = new uRoulette(problem.rnd, weigth);
    }
    public Roulette(){
        super();
        this.weigth = null;
        this.roul = null;
    }
    
    public abstract double weigth(int i);

    @Override
    public int dad1() throws Exception{         //the best of roulette
        return roul.roulette_wheel();   
    }
    @Override
    public int dad2(int dad1) throws Exception{ //the best of roulette and != dad1
        return roul.roulette_wheel(dad1);
    }

    @Override
    public void organize() throws Exception {
        for(int i=1; i<inds.length; i++){
            Solution aux = inds[i];
            int j = i-1;
            while(j>=0 && inds[j].GT(aux)){
                inds[j+1] = inds[j];
                j--;
            }
            inds[j+1] = aux;
        }
    }
}
