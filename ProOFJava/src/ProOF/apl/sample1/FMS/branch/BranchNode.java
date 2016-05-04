/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ProOF.apl.sample1.FMS.branch;

import ProOF.com.Linker.LinkerResults;
import ProOF.com.Stream.StreamPrinter;

/**
 *
 * @author marcio
 * @param <Prob>
 * @param <N>
 * @param <D>
 */
public abstract class BranchNode <Prob extends BranchProblem, N extends BranchNode, D> implements Comparable<BranchNode>{
    public final long id;
    public final N back;
    protected final D data;
    public final double cur_cost;
    public final int level;

    public BranchNode(Prob prob, N back, D data) throws Exception {
        this.id = prob.TotalNodes++;
        this.back = back;
        this.data = data;
        if(back==null){
            this.level = 1;
            this.cur_cost = 0;
        }else if(back.is_integer(prob)){
            throw new Exception("try create a node by other integer node");
        }else{
            this.level = back.level+1;
            this.cur_cost = evaluate(prob);
        }
        prob.update(this);
    }
    protected final int mem_base(){
        return 28;
    }
    public abstract int mem_bytes() throws Exception;
    
    public abstract boolean is_integer(Prob prob) throws Exception;
    protected abstract double evaluate(Prob prob) throws Exception;

    public final boolean contains(D data){
        BranchNode aux = this;
        while(aux!=null){
            if(aux.data.equals(data)){
                return true;
            }
            aux = aux.back;
        }
        return false;
    }
    @Override
    public String toString() {
        return (back!=null ? back.toString()+" -> ": "" )+String.format("%s", data);
    }
    @Override
    public int compareTo(BranchNode o) {
        double v = cur_cost - o.cur_cost;
        return  ( id == o.id ? 0 :
                    (v<0 ? -1 :
                    (v>0 ? +1 :
                        (id<o.id ? -1 :
                        (id>o.id ? +1 :
                                    0 )))));
    }
    public void results(Prob prob, LinkerResults link, BranchNode base) throws Exception {
        link.writeLong("id", id);
        link.writeInt("level", level);
        link.writeDbl("cost", cur_cost);
        link.writeString("sol", toString());
    }
    public void printer(Prob prob, StreamPrinter link, BranchNode base) throws Exception{
        link.printLong("id", id);
        link.printInt("level", level);
        link.printDbl("cost", cur_cost);
    }
}
