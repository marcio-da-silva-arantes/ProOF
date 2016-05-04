package ProOF.apl.advanced1.FMS.population;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import ProOF.apl.advanced1.FMS.structure.Structure;
import ProOF.com.Linker.LinkerApproaches;
import ProOF.com.Linker.LinkerParameters;
import ProOF.com.Linker.LinkerValidations;
import ProOF.com.language.Factory;
import ProOF.opt.abst.problem.meta.Problem;
import ProOF.opt.abst.problem.meta.Solution;

/**
 *
 * @author marcio
 */
public class Convergence extends Population{    
    private double cross_rate;
    private double mut_rate;
    
    protected Structure struct;
    
    private final Factory fStructture;
    public Convergence(Factory fStructture) {
        super();
        this.fStructture = fStructture;
        this.struct = null;
    }
    
    public Convergence(Problem problem, Convergence base, int nInds, int ID, int MaxID, Convergence pop) throws Exception {
        super(problem, base, ID, MaxID);
        this.struct = base.struct.Clone(problem, nInds);
        this.cross_rate = pop.cross_rate;
        this.mut_rate = pop.mut_rate;
        this.fStructture = null;
    }
    @Override
    public Population Clone(Problem problem, int nInds, int ID, int MaxID) throws Exception{
        return new Convergence(problem, this, nInds, ID, MaxID, this);
    }
    @Override
    public String name() {
        return "Convergence";
    }
    @Override
    public String description() {
        throw new UnsupportedOperationException("Not supported yet.");
    
    }
    @Override
    public void services(LinkerApproaches link) throws Exception {
        super.services(link);
        struct  = link.get(fStructture, struct);
    }
    @Override
    public void parameters(LinkerParameters com) throws Exception {
        cross_rate  = com.Dbl("Crossover Rate", 5.0,    0.0,    100.0);
        mut_rate    = com.Dbl("Mutation Rate",  0.7,    0.0,    1.0);
    }
    @Override
    public boolean validation(LinkerValidations com) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    @Override
    public void evolution()throws Exception {
        int nCross = Math.max(1, (int)(cross_rate*struct.size()));
        boolean again;
        do{
            again =  false;
            for(int n=0; n<nCross; n++){
                //Selection
                int p1 = struct.dad1();
                int p2 = struct.dad2(p1);
                
                //Crossover
                Solution son = cross.crossover(struct.inds(p1), struct.inds(p2));
                
                //Mutation
                if(problem.rnd.nextDouble()<mut_rate){
                    mut.mutation(son);
                }
                
                //Evaluate
                problem.evaluate(son);
                
                if(struct.insert(son, p1, p2)){
                    again = true;
                }
            }
            struct.organize();
        }while(again);
    }
    
    @Override
    public void initialize() throws Exception {
        for(Solution ind : struct.inds()){
            init.initialize(ind);
        }
    }
    @Override
    public void evaluate() throws Exception {
        for(Solution ind : struct.inds()){
            problem.evaluate(ind);
        }
    }
    @Override
    public void reinitialize() throws Exception {
        Solution best = best();
        for(Solution ind : struct.inds()){
            if(ind!=best){
                init.initialize(ind);
                problem.evaluate(ind);
            }
        }
    }
    @Override
    public Solution best() throws Exception{
        return struct.best();
    }
    @Override
    public void migrate(Solution best) throws Exception{
        struct.migrate(best);
    }
    @Override
    public void organize() throws Exception{
        struct.organize();
    }
}
