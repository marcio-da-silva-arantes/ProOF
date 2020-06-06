/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ProOF.apl.advanced2.FMS.PS;

import CplexExtended.CplexExtended;
import ProOF.com.Linker.LinkerApproaches;
import ProOF.com.Linker.LinkerParameters;
import ProOF.com.Linker.LinkerResults;
import ProOF.com.Linker.LinkerValidations;
import ProOF.com.language.Approach;
import ilog.concert.IloConstraint;
import ilog.concert.IloException;
import ilog.concert.IloNumExpr;
import ilog.concert.IloNumVar;
import ilog.concert.IloRange;
import ilog.cplex.IloCplex;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 *
 * @author marci
 */
public abstract class PSModel extends Approach{
    public CplexExtended cpx;
    
    
    
    private int Threads;
    private double EpGap; 
    private long NodeLim;
    private int NodeSel;
    private int RootAlg;
    private int MIPEmphasis;
    private double WorkMem;
    private boolean print_war;
    private boolean print_out;
    
    
    
    private int iteration;
    
    @Override
    public String description() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    @Override
    public void services(LinkerApproaches link) throws Exception {
    
    }
    @Override
    public void parameters(LinkerParameters link) throws Exception {
        Threads = link.Int("Threads", 1, 0, 32);
        EpGap = link.Dbl("EpGap", 1e-4, 1e-9, 1);
        NodeLim = link.Long("NodeLim", 1000000, 1, 1000000000000l);
        NodeSel = link.Itens("NodeSel", 2, new String[]{"Depth-first", "BestBound", "BestEst", "BestEstAlt"});
        RootAlg = link.Itens("RootAlg", 0, new String[]{"AutoAlg", "Primal", "Dual", "Network", "Barrier", "Sifting", "Concurrent"});
        MIPEmphasis = link.Itens("MIPEmphasis", 0, new String[]{"Balanced", "Feasibility", "Optimality", "BestBound", "HiddenFeas"});
        WorkMem = link.Dbl("WorkMem(MB)", 128, 1e-6, 1e6);
        print_war = link.Bool("warning", true);
        print_out = link.Bool("output", true);
    }
    @Override
    public void load() throws Exception {
     
    }

    @Override
    public void start() throws Exception {
        cpx = new CplexExtended();
        
        iteration = 0;
        
        if(!print_war) cpx.setWarning(null);
        if(!print_out) cpx.setOut(null);
        System.out.println("Thread = "+Threads);
        if(Threads==0){
            cpx.setParam(IloCplex.IntParam.Threads, Runtime.getRuntime().availableProcessors());
        }else{
            cpx.setParam(IloCplex.IntParam.Threads, Threads);
        }
        cpx.setParam(IloCplex.DoubleParam.EpGap, EpGap);

        cpx.setParam(IloCplex.LongParam.NodeLim, NodeLim);
        
        cpx.setParam(IloCplex.IntParam.NodeSel, NodeSel);
        
        cpx.setParam(IloCplex.IntParam.RootAlg, RootAlg);
        cpx.setParam(IloCplex.IntParam.NodeAlg, RootAlg);
        
        cpx.setParam(IloCplex.IntParam.MIPEmphasis, MIPEmphasis);
        cpx.setParam(IloCplex.IntParam.NodeFileInd, 3);
        
        cpx.setParam(IloCplex.BooleanParam.MemoryEmphasis, true);
        cpx.setParam(IloCplex.DoubleParam.WorkMem, WorkMem);
        cpx.setParam(IloCplex.StringParam.WorkDir, "./");
        model();
    }

    @Override
    public boolean validation(LinkerValidations link) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public abstract void model() throws Exception;
    public abstract ArrayList<PSVar> variables() throws Exception;
    
    public void print() throws Exception{}
    
    
    public void fix(ArrayList<PSVar> vars) throws IloException {
        //fix on start solution
        for(PSVar v : vars){
            v.fix();
        }
    }
    public void free(ArrayList<PSVar> vars) throws IloException {
        //free all variables
        for(PSVar v : vars){
            v.free();
        }
    }
    public void update(ArrayList<PSVar> vars) throws IloException {
        //update the current solution values on vars structure 
        for(PSVar v : vars){
            v.update(cpx);
        }
    }
    public final boolean solve() throws IloException, Exception {
        return cpx.solve();
    }
    public final double solve(final double time) throws IloException, Exception {
        long t0 = System.currentTimeMillis();
        cpx.setParam(IloCplex.DoubleParam.TiLim, Math.max(time, 1.0));
        if(cpx.solve()){
            iteration++;
            return (System.currentTimeMillis()-t0)/1000.0;
        }else{
            throw new Exception("solve fail, status = "+cpx.getStatus().toString());
        }
    }
    
    @Override
    public void results(LinkerResults link) throws Exception {
        link.writeInt("solver-iteration", iteration);
        link.writeString("PS-Status", cpx.getStatus().toString());
        if(cpx.getStatus() == IloCplex.Status.Optimal || cpx.getStatus() == IloCplex.Status.Feasible){
            link.writeDbl("PS-Obj Value", cpx.getObjValue());
            link.writeDbl("PS-Obj Lower", cpx.getBestObjValue());
            print();
        }
    }
    public IloNumExpr changeExpr(ArrayList<PSVar> vars) throws IloException{
        IloNumExpr changes = null;
        for(PSVar v : vars){
            if(v.getValue()==0){    // changes += var
                changes = cpx.SumProd(changes, 1, v.var);
            }else{                  // changes += not(var) or 1-var
                changes = cpx.SumProd(changes, 1, cpx.Not(v.var));
            }
        }
        return changes;
    } 
    public IloRange addPermanent(IloNumExpr changes, int PSsize) throws IloException {
        return cpx.addGe(changes, PSsize);
    }
    
    private LinkedList<IloRange> range1 = new LinkedList<>();
    public void addChange1(ArrayList<PSVar> vars, int PSsize, int PSexpr) throws IloException {
        IloNumExpr changes = changeExpr(vars);
        clearChange1();
        if(PSexpr==0){
            range1.addLast(cpx.addLe(changes, PSsize));
        }else{
            range1.addLast(cpx.addEq(changes, PSsize));
        }
    }
    public int sizeRange1(){
        return range1.size();
    }
    public void clearChange1() throws IloException {
        for(IloRange r : range1){
            cpx.remove(r);
        }
        range1.clear();
    }

    private LinkedList<IloRange> range2 = new LinkedList<>();
    //private IloRange range = null;
    public int addChange2(ArrayList<PSVar> vars) throws IloException {
        //int bound = range2.size()+1;
        for(IloRange r : range2){
            r.setBounds(r.getLB()+1, r.getUB()+1);
            //System.out.println("set [ "+r.getLB()+" ; "+r.getUB()+" ]");
        }
        
        IloNumExpr changes = changeExpr(vars);
        range2.addLast(cpx.addEq(changes, 1));
        //System.out.println("add [ "+range2.getLast().getLB()+" ; "+range2.getLast().getUB()+" ]");

        return range2.size();
    }
    public int sizeRange2(){
        return range2.size();
    }
    public void clearChange2() throws IloException {
        for(IloRange r : range2){
            cpx.remove(r);
        }
        range2.clear();
    }
    
    public final double gap() throws IloException {
        if(cpx.getBestObjValue()>cpx.getObjValue()){
            return 0.0;
        }
        return (cpx.getObjValue()-cpx.getBestObjValue())*100.0/(Math.abs(cpx.getObjValue()) + 1e-10);
    }

    

    

    
}
