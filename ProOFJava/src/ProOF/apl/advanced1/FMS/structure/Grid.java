/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ProOF.apl.advanced1.FMS.structure;

import ProOF.com.Linker.LinkerParameters;
import ProOF.com.Linker.LinkerValidations;
import ProOF.opt.abst.problem.meta.Problem;

/**
 *
 * @author marcio
 */
public class Grid extends Structure{
    private int D;
    private final int Length;
    private final int P[];
    public Grid(Problem problem, int nInds, int D) throws Exception{
        super(problem, nInds);
        this.D = D;
        this.Length = D<2 ? nInds : (int) (Math.log(nInds+1)/Math.log(D)+0.99999);
        this.P = new int[D];
    }
    public Grid() {
        super();
        Length = 0;
        P = null;
    }
    
    @Override
    public String name() {
        return "Grid";
    }
    @Override
    public String description() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    @Override
    public void parameters(LinkerParameters com) throws Exception {
        D = com.Int("Dimensions",   2,      1,      100);
    }
    
    @Override
    public boolean validation(LinkerValidations com) throws Exception {
        return D<inds.length;
    }
    
    @Override
    public int dad1() {
        return problem.rnd.nextInt(inds.length);   //Anybody
    }
    @Override
    public int dad2(int dad1) throws Exception {
        encode(dad1);
        
        int cont = problem.rnd.nextInt(neibors());
        for(int i=0; i<D; i++){
            if(contains(i, +1)){
                if(cont==0){
                    P[i]++;
                    return decode();
                }
                cont--;
            }
        }
        for(int i=0; i<D; i++){
            if(contains(i, -1)){
                if(cont==0){
                    P[i]--;
                    return decode();
                }
                cont--;
            }
        }
        throw new Exception("");
    }
    private void encode(int index){
        for(int i=D-1; i>=0; i--){
            P[i] = index % Length;
            index /= Length;
        }
    }
    //1010 = 10
    private int decode(){
        int index = 0;
        for(int i=0; i<D; i++){
            index *= Length;
            index += P[i];
        }
        return index;
    }
    private int neibors(){
        int cont = 0;
        for(int i=0; i<D; i++){
            if(contains(i, +1)){
                cont++;
            }
        }
        for(int i=0; i<D; i++){
            if(contains(i, -1)){
                cont++;
            }
        }
        return cont;
    }
    private boolean contains(int i, int dp){
        P[i] += dp;
        boolean r = 0<=P[i] && P[i]<Length && decode()<inds.length;
        P[i] -= dp;
        return r;
    }
    
    @Override
    public void organize() throws Exception {
        //Nothing
    }
    @Override
    public Structure Clone(Problem problem, int nInds) throws Exception {
        return new Grid(problem, nInds, D);
    }
}
