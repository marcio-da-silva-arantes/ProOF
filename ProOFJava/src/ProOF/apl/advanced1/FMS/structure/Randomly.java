/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ProOF.apl.advanced1.FMS.structure;

import ProOF.opt.abst.problem.meta.Problem;

/**
 *
 * @author marcio
 */
public class Randomly extends Structure{
    
    public Randomly(Problem problem, int nInds) throws Exception{
        super(problem, nInds);
    }
    public Randomly(){
        super();
    }
    
    @Override
    public String name() {
        return "Randon";
    }
    @Override
    public String description() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    @Override
    public int dad1() {
        return problem.rnd.nextInt(inds.length); //Anybody
    }
    @Override
    public int dad2(int dad1) {
        return problem.rnd.nextInt(0,inds.length-1, dad1); //Anybody except dad1
    }
    @Override
    public void organize() throws Exception {
        //Nothing
    }
    @Override
    public Structure Clone(Problem problem, int nInds) throws Exception {
        return new Randomly(problem, nInds);
    }
}
