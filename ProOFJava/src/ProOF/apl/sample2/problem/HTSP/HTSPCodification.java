/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ProOF.apl.sample2.problem.HTSP;

import ProOF.opt.abst.problem.meta.codification.Codification;

/**
 *
 * @author marcio
 */
public class HTSPCodification extends Codification<HTSP, HTSPCodification> {
    protected HTSPEdge set[];
    public HTSPCodification(HTSP prob) {
        this.set = new HTSPEdge[prob.inst.N/2];
    }
    @Override
    public void copy(HTSP prob, HTSPCodification source) throws Exception {
        System.arraycopy(source.set, 0, this.set, 0, this.set.length);
    }
    @Override
    public HTSPCodification build(HTSP prob) throws Exception {
        return new HTSPCodification(prob);
    }
    @Override
    public String toString() {
        String s = "";
        for(HTSPEdge e : set){
            s += String.format("%12s ", e);
        }
        return s;
    }
    
}
