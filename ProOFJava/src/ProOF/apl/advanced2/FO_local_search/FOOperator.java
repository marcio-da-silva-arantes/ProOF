/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ProOF.apl.advanced2.FO_local_search;

import ProOF.gen.operator.*;
import ProOF.com.Linker.LinkerApproaches;
import ProOF.com.Linker.LinkerParameters;
import ProOF.com.Linker.LinkerResults;
import ProOF.com.Linker.LinkerValidations;
import ProOF.com.language.Approach;
import ProOF.opt.abst.problem.meta.Problem;
import ProOF.opt.abst.problem.meta.Solution;
import ProOF.opt.abst.problem.meta.codification.Codification;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author marcio
 */
public final class FOOperator extends Approach{
    public final static FOOperator obj = new FOOperator();
    
    private Problem prob;
    private oFOOperator move[];
    
    private FOOperator() {
        
    }
    @Override
    public String name() {
        return "LocalMove";
    }
    @Override
    public String description() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    @Override
    public void parameters(LinkerParameters link) throws Exception {
        
    }
    @Override
    public void services(LinkerApproaches link) throws Exception {
        prob = link.need(Problem.class, prob);
        move = link.needs(oFOOperator.class, new oFOOperator[1]);
    }
    @Override
    public boolean validation(LinkerValidations link) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    @Override
    public void load() throws Exception {
        
    }
    @Override
    public void start() throws Exception {
        
    }
    @Override
    public void results(LinkerResults link) throws Exception {
        
    }
    public Solution execute_FO_strategy(Solution ind) throws Exception {
        int index = prob.rnd.nextInt(move.length);
        Solution sol = ind.clone(prob);
        Codification codif = move[index].execute_FO_strategy(prob, sol.codif());
        Solution sol2 = prob.build_sol(codif);
        prob.evaluate(sol2);
        return sol2;
    }
    public int size(){
        return move.length;
    }
    public List<oFOOperator> list_moves(){
        return Arrays.asList(move);
    }
    public oFOOperator select(List<oFOOperator> movs) throws Exception {
        return movs.get(prob.rnd.nextInt(movs.size()));
    }
}
