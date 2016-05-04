/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ProOF.apl.sample2.problem.HTSP;

/**
 *
 * @author marcio
 */
public class HTSPEdge {
    public final int i; //source
    public final int j; //destine

    public HTSPEdge(int i, int j) {
        this.i = i;
        this.j = j;
    }

    @Override
    public String toString() {
        return String.format("[%2d -> %2d]", i, j);
    }
    
}
