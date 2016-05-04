/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ProOF.apl.advanced1.FMS.structure;


import ProOF.apl.advanced1.FMS.population.Population;
import ProOF.com.Linker.LinkerParameters;
import ProOF.com.Linker.LinkerResults;
import ProOF.com.Linker.LinkerApproaches;
import ProOF.com.Linker.LinkerValidations;
import ProOF.com.language.Approach;
import ProOF.opt.abst.problem.meta.Problem;
import ProOF.opt.abst.problem.meta.Solution;
import ProOF.utilities.uUtil;

/**
 *
 * @author marcio
 */
public abstract class Structure extends Approach{
    protected final Problem problem;
    protected final Solution inds[];
    
    
    protected Structure(Problem problem, int nInds) throws Exception{
        this.problem = problem;
        this.inds = new Solution[nInds];
        for(int i=0; i<inds.length; i++){
            inds[i] = problem.build_sol();
        }
    }
    public Structure() {
        this.problem = null;
        this.inds = null;
    }
    public Solution[] inds(){
        return inds;
    }
    public Solution inds(int i){
        return inds[i];
    }
    public int size(){
        return inds.length;
    }
    
    public void set(int i, Solution sol){
        inds[i] = sol;
    }
    
    public Solution best() throws Exception{
        return inds[best_index()];
    }
    public void migrate(Solution best) throws Exception{
        inds[worst_index()] = best.clone(problem);
    }
    public boolean insert(Solution son, int... dads) throws Exception {
        //Pegando o pior pai
        int z = inds[dads[0]].LT(inds[dads[1]]) ? dads[1] : dads[0];
        //Verificando a inserção
        if( son.LT(inds[z]) ){
            inds[z] = son;
            return true;
        }
        return false;
    }
    
    public abstract int dad1() throws Exception;
    public abstract int dad2(int dad1) throws Exception;
    public abstract void organize() throws Exception;
    public abstract Structure Clone(Problem problem, int nInds) throws Exception;
    
    public int best_index(){
        return uUtil.min(inds);
    }
    public int worst_index(){
        return uUtil.max(inds);
    }
    
    @Override
    public void services(LinkerApproaches com) throws Exception {
        
    }
    @Override
    public void parameters(LinkerParameters com) throws Exception {
        
    }
    @Override
    public boolean validation(LinkerValidations com) throws Exception {
        return true;
    }
    @Override
    public void load() throws Exception {
       
    }
    @Override
    public void start() throws Exception {
        
    }
    @Override
    public void results(LinkerResults com) throws Exception {
    
    }
}
