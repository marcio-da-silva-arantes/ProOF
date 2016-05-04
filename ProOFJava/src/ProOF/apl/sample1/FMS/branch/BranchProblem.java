/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ProOF.apl.sample1.FMS.branch;

import ProOF.com.Linker.LinkerApproaches;
import ProOF.com.Linker.LinkerParameters;
import ProOF.com.Linker.LinkerResults;
import ProOF.com.Linker.LinkerValidations;
import ProOF.com.language.Approach;
import ProOF.utilities.uRandom;

/**
 *
 * @author marcio
 * @param <B>
 */
public abstract class BranchProblem<B extends BranchBest> extends Approach {
    public final uRandom rmd = new uRandom();
    public abstract B best();
    
    public abstract BranchNode frist_node() throws Exception;
    
    public final double total_nodes(){
        return TotalNodes;
    }
    public boolean has_integer() throws Exception {
        return best().ind().is_integer(this);
    }
    public double uper_bound() throws Exception{
        if(has_integer()){
            return best().ind().cur_cost;
        }else{
            return Double.MAX_VALUE;
        }
    }
    protected long TotalNodes;
    
    @Override
    public String description() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    @Override
    public void services(LinkerApproaches link) throws Exception {
        link.add(best());
    }
    @Override
    public void parameters(LinkerParameters link) throws Exception {
        
    }
    @Override
    public void load() throws Exception {
        
    }
    @Override
    public void start() throws Exception {
        TotalNodes = 0;
    }
    @Override
    public boolean validation(LinkerValidations link) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    public void update(BranchNode no) throws Exception{
        if(best()!=null){
            best().better(this, no);
        }
    }

    @Override
    public void finish() throws Exception {
        if(best()!=null){
            best().flush(this);
        }
    }
    
    @Override
    public void results(LinkerResults link) throws Exception {
        if(best()!=null){
            best().results(this, link);
        }
    }
}
