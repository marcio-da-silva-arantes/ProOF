/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ProOF.apl.advanced1.FMS.structure;

import ProOF.com.Linker.LinkerParameters;
import ProOF.com.Linker.LinkerValidations;
import ProOF.opt.abst.problem.meta.Problem;
import ProOF.opt.abst.problem.meta.Solution;
import ProOF.utilities.uTournament;

/**
 *
 * @author marcio
 */
public class Tournament extends Structure{
    private int R;
    private final uTournament tour;
    public Tournament(Problem problem, int nInds, int R) throws Exception{
        super(problem, nInds);
        this.R = R;
        this.tour = new uTournament(problem.rnd, inds, R);
    }
    public Tournament(){
        super();
        tour = null;
    }
    
    @Override
    public String name() {
        return "Tournament";
    }
    @Override
    public String description() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    @Override
    public void parameters(LinkerParameters com) throws Exception {
        R = com.Int("Tournament",   2,      2,      100);
    }
    @Override
    public boolean validation(LinkerValidations com) throws Exception {
        return R<inds.length;
    }
    
    @Override
    public int dad1() throws Exception{         //the best of tournament
        return tour.select();   
    }
    @Override
    public int dad2(int dad1) throws Exception{ //the best of tournament and != dad1
        return tour.select(dad1);
    }

    @Override
    public void organize() throws Exception {
        //Nothing
    }
    @Override
    public Structure Clone(Problem problem, int nInds) throws Exception {
        return new Tournament(problem, nInds, R);
    }
}
