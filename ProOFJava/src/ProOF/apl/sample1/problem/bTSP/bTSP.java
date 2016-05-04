/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ProOF.apl.sample1.problem.bTSP;
import ProOF.apl.sample1.FMS.branch.BranchBest;
import ProOF.apl.sample1.FMS.branch.BranchBestSol;
import ProOF.apl.sample1.FMS.branch.BranchNode;
import ProOF.apl.sample1.FMS.branch.BranchProblem;
import ProOF.apl.sample1.problem.TSP.TSPInstance;
import ProOF.com.Linker.LinkerApproaches;

/**
 *
 * @author marcio
 */
public class bTSP extends BranchProblem<BranchBest>{
    protected TSPInstance inst = new TSPInstance();
    
    @Override
    public String name() {
        return "bTSP";
    }

    @Override
    public void services(LinkerApproaches link) throws Exception {
        super.services(link);
        link.add(inst);
        link.add(bTSPOperator.obj); 
    }
    
    @Override
    public BranchNode frist_node() throws Exception {
        return new bTSPNode(this, null, 0);
    }
    @Override
    public BranchBest best() {
        return BranchBestSol.object();
    }
}
