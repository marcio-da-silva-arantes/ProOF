/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ProOF.apl.advanced1.FMS.structure;

import ProOF.com.Linker.LinkerParameters;
import ProOF.opt.abst.problem.meta.Problem;
import ProOF.opt.abst.problem.meta.Solution;

/**
 *
 * @author marcio
 */
public class Tree extends Structure{
    private int R;
    
    public Tree(Problem problem, int nInds, int R) throws Exception{
        super(problem, nInds);
        this.R = R;
    }
    public Tree() {
        super();
    }
    
    @Override
    public String name() {
        return "Tree";
    }
    @Override
    public String description() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    @Override
    public void parameters(LinkerParameters com) throws Exception {
        R = com.Int("Ramification",   2,      2,      100);
    }

    @Override
    public int dad1() {
        return problem.rnd.nextInt(1, inds.length-1);   //Seguidor
    }
    @Override
    public int dad2(int dad1) {
        return (dad1 - 1) / R;             //Lider do cluster
    }

    @Override
    public void organize() throws Exception {
        //Numero de clusters da arvore;
        int C = (inds.length + R - 2) / R ;
        boolean again =  true;
	while (again){
            again = false;
            for (int i = 0; i < C; i++){
                for (int j = 0; j < R; j++){
                    //Indice do seguidor j que esta no cluster i.
                    int k = R*(i+1)-j;
                    if ( k < inds.length && inds[i].GT(inds[k])){
                        Solution aux = inds[i];
                        inds[i] = inds[k];
                        inds[k] = aux;
                        again = true;
                    }
                }
            }
	}
    }
    @Override
    public Structure Clone(Problem problem, int nInds) throws Exception {
        return new Tree(problem, nInds, R);
    }
}
