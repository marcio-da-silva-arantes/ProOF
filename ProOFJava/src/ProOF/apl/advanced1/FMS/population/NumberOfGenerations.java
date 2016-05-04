package ProOF.apl.advanced1.FMS.population;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import ProOF.com.Linker.LinkerParameters;
import ProOF.com.Linker.LinkerValidations;
import ProOF.opt.abst.problem.meta.Problem;
import ProOF.opt.abst.problem.meta.Solution;
import ProOF.utilities.uUtil;

/**
 *
 * @author marcio
 */
public class NumberOfGenerations extends Population{    
    private int generations;
    private double mut_rate;
    protected final Solution inds[];
    
    public NumberOfGenerations(Problem problem, NumberOfGenerations base, int nInds, int ID, int MaxID, NumberOfGenerations pop) throws Exception {
        super(problem, base, ID, MaxID);
        this.inds = new Solution[nInds];
        for(int i=0; i<inds.length; i++){
            inds[i] = problem.build_sol();
        }
        generations = pop.generations;
        mut_rate = pop.mut_rate;
    }
    public NumberOfGenerations() {
        super();
        this.inds = null;
    }
    @Override
    public Population Clone(Problem problem, int nInds, int ID, int MaxID) throws Exception{
        return new NumberOfGenerations(problem, this, nInds, ID, MaxID, this);
    }
    @Override
    public String name() {
        return "NumberOfGenerations";
    }
    @Override
    public String description() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    @Override
    public void parameters(LinkerParameters com) throws Exception {
        generations = com.Int("Generations",    5,      0,    100000);
        mut_rate    = com.Dbl("Mutation Rate",  0.7,  0.0,    1.0);
    }
    @Override
    public boolean validation(LinkerValidations com) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    @Override
    public void evolution()throws Exception {
        for(int g=0; g<generations; g++){
            int p1 = best_index();
            for(int p2=0; p2<inds.length; p2++){
                //Crossover
                Solution son = cross.crossover(inds[p1], inds[p2]);
                
                //Mutation
                if(problem.rnd.nextDouble()<mut_rate){
                    mut.mutation(son);
                }
                
                //Evaluate
                problem.evaluate(son);
                
                //Verificando a inserção
                if( son.LT(inds[p2]) ){
                    inds[p2] = son;
                }
            }
        }
        
    }
    
    @Override
    public void initialize() throws Exception {
        for(Solution ind : inds){
            init.initialize(ind);
        }
    }
    @Override
    public void evaluate() throws Exception {
        for(Solution ind : inds){
            problem.evaluate(ind);
        }
    }
    @Override
    public void reinitialize() throws Exception {
        Solution best = best();
        for(Solution ind : inds){
            if(ind!=best){
                init.initialize(ind);
                problem.evaluate(ind);
            }
        }
    }
    public int best_index(){
        return uUtil.min(inds);
    }
    public int worst_index(){
        return uUtil.max(inds);
    }
    @Override
    public Solution best() throws Exception{
        return inds[best_index()];
    }
    @Override
    public void migrate(Solution best) throws Exception{
        inds[worst_index()] = best.clone(problem);
    }
    @Override
    public void organize() throws Exception{
        //Nothing
    }
}
