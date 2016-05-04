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
import ProOF.com.Linker.LinkerApproaches;
import ProOF.gen.stopping.Stop;
import ProOF.opt.abst.run.Heuristic;

/**
 *
 * @author marcio
 */
public class GreedyAlgorithm extends Heuristic {
    private Stop stop;
    private BranchProblem problem;
    private oExpand expand;
    @Override
    public String name() {
        return "Greedy";
    }
    @Override
    public void services(LinkerApproaches link) throws Exception {
        problem = link.get(fBranchProblem.obj, problem);
        stop    = link.get(fStop.obj, stop);
        expand = link.need(oExpand.class, expand);
    }
    @Override
    public void execute() throws Exception {
        BranchNode node = problem.frist_node();
        recursive(node);
    }
    private void recursive(BranchNode node) throws Exception{
        if(!node.is_integer(problem) && !stop.end()){
            BranchNode[] list = expand.expand(problem, node);
            for(BranchNode no : ordenate(list)){
                recursive(no);
            }
        }
    }
    private BranchNode[] ordenate(BranchNode[] childs) {
        for(int i=1; i<childs.length; i++){
            BranchNode aux = childs[i];
            int j = i-1;
            while(j>=0 && childs[j].compareTo(aux)>0){
                childs[j+1] = childs[j];
                j--;
            }
            childs[j+1] = aux;
        }
        return childs;
    }
}
