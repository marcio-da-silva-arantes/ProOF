/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ProOF.apl.sample1.method;

import ProOF.apl.factorys.fBranchProblem;
import ProOF.apl.factorys.fStop;
import ProOF.apl.sample1.FMS.branch.BranchNode;
import ProOF.apl.sample1.FMS.branch.BranchProblem;
import ProOF.apl.sample1.FMS.branch.oExpand;
import ProOF.apl.sample1.FMS.branch.oLowerBound;
import ProOF.com.Linker.LinkerApproaches;
import ProOF.com.Linker.LinkerParameters;
import ProOF.com.Linker.LinkerResults;
import ProOF.gen.stopping.Stop;
import ProOF.opt.abst.run.Exact;
import java.util.TreeSet;

/**
 *
 * @author marcio
 */
public class BranchAndBound extends Exact {
    private Stop stop;
    private BranchProblem problem;
    private oExpand expand;
    private oLowerBound lower;
    
    private double memLim;
    
    private double LB;
    
    @Override
    public String name() {
        return "Branch & Bound";
    }
    @Override
    public String description() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    @Override
    public void services(LinkerApproaches link) throws Exception {
        problem = link.get(fBranchProblem.obj, problem);
        stop    = link.get(fStop.obj, stop);
        expand = link.need(oExpand.class, expand);
        lower = link.need(oLowerBound.class, lower);
    }
    @Override
    public void parameters(LinkerParameters link) throws Exception {
        memLim = link.Dbl("Mem-Lim (MB)", 5, 1, 2048);
    }
    @Override
    public void start() throws Exception {
        LB = 0;
    }@Override
    public void execute() throws Exception {
        double mem = 0;
        BranchNode node = problem.frist_node();
        
        TreeSet<BranchNode> tree = new TreeSet<BranchNode>();
        tree.add(node);
        mem+=node.mem_bytes();
        
        while(!tree.isEmpty() && !stop.end() && mem<memLim*1048576){
            node = tree.pollFirst();
            
            tree.remove(node);
            mem-=node.mem_bytes();
            
            double lower_bound = lower.lower_bound(problem, node);
            
            if(lower_bound < problem.uper_bound()){
                for(BranchNode no : expand.expand(problem, node)){
                    lower_bound = lower.lower_bound(problem, no);
                    if(lower_bound < problem.uper_bound()){
                        tree.add(no);
                        mem+=node.mem_bytes();
                    }
                }
            }
        }
        if(tree.isEmpty()){
            LB = problem.uper_bound();
        }else{
            LB = Double.MAX_VALUE;
            while(!tree.isEmpty()){
                node = tree.pollFirst();
                tree.remove(node);
                mem-=node.mem_bytes();
                LB = Math.min(LB, lower.lower_bound(problem, node));
            }
        }
    }
    @Override
    public void results(LinkerResults link) throws Exception {
        link.writeDbl("lower_bound", LB);
    }
}
