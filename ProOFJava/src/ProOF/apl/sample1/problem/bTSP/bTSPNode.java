/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ProOF.apl.sample1.problem.bTSP;

import ProOF.apl.sample1.FMS.branch.BranchNode;

/**
 *
 * @author marcio
 */
public class bTSPNode extends BranchNode<bTSP, bTSPNode, Integer>{
    public bTSPNode(bTSP prob, bTSPNode back, Integer city) throws Exception {
        super(prob, back, city);
    }
    @Override
    public int mem_bytes() throws Exception {
        return 4+mem_base();
    }
    @Override
    public boolean is_integer(bTSP prob) throws Exception {
        return level>=prob.inst.N;
    }
    @Override
    protected double evaluate(bTSP prob) throws Exception {
        if(is_integer(prob)){
            int a = back.city();
            int b = this.city();
            int c = 0;      //next.city() is equal to the frist.city();
            return back.cur_cost + prob.inst.Cij[a][b] + prob.inst.Cij[b][c];
        }else{
            int a = back.city();
            int b = this.city();
            return back.cur_cost + prob.inst.Cij[a][b];
        }
    }
    public int city() {
        return data;
    }
}
