/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ProOF.apl.advanced2.method;

import ProOF.apl.advanced2.FMS.PS.PSModel;
import ProOF.apl.advanced2.FMS.PS.PSVar;
import ProOF.com.Communication;
import ProOF.com.Linker.LinkerApproaches;
import ProOF.com.Linker.LinkerParameters;
import ProOF.com.Stream.StreamOutput;
import ProOF.com.language.Factory;
import ProOF.gen.stopping.Time;
import ProOF.opt.abst.run.Heuristic;
import ilog.concert.IloNumExpr;
import ilog.concert.IloRange;
import ilog.cplex.IloCplex;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * PS - polynomial search
 * @author marci
 */
public class PS extends Heuristic{
    private Time stop = new Time();
    private PSModel model;
    
    
    private boolean usesInitSol;
    private double time_cplex;
    private int PSsize;
    private int PSexpr;
    private int PSchange;
    private int PSwalk;
    private int PSgreedy;
    
    private StreamOutput output;
    
    private final Factory fPSModel;
    public PS(Factory fPSModel) {
        this.fPSModel = fPSModel;
    }
    
    @Override
    public String name() {
        return "PS(Polynomial Search)";
    }
    
    @Override
    public void services(LinkerApproaches link) throws Exception {
        stop    = link.add(stop);
        model   = link.get(fPSModel, model);
    }

    @Override
    public void parameters(LinkerParameters link) throws Exception {
        super.parameters(link); //To change body of generated methods, choose Tools | Templates.
        
        
        usesInitSol = false;
        time_cplex = 600.0;
        PSchange = 3;
        PSchange = link.Int("PS-charge", 4, 1, Integer.MAX_VALUE);
        PSexpr  = link.Itens("PS-expr", 1, "<=", "==");
        int PSstart = link.Int("PS-start", 1, 1, Integer.MAX_VALUE);
        int PSinc   = link.Int("PS-inc", 1, 1, Integer.MAX_VALUE);
//        
        
        usesInitSol = link.Bool("PS-init", false, null);
        PSexpr  = link.Itens("PS-expression", 1, "<=", "==");
        time_cplex = link.Dbl("PS-cplex(s)", 600, 0.0, 3600);
        PSchange = link.Int("PS-charge", 4, 1, Integer.MAX_VALUE);
        PSwalk = link.Int("PS-walk", 120, 1, Integer.MAX_VALUE);
        PSgreedy = link.Int("PS-greedy", 0, 0, Integer.MAX_VALUE);
        
//System.out.println("PSexpr = "+PSexpr);
    //    time_cplex = 60;
        //usesInitSol = true;
    //    PSchange = 40;
        //PSwalk = 1;
        //PSgreedy = 0;
    }
    @Override
    public void load() throws Exception {
        output = Communication.mkOutput("PS");
    }
    
    private double initSol(ArrayList<PSVar> vars) throws Exception{
        model.fix(vars);
        output.printf("fix start solution and solve\n");
        model.cpx.setOut(null);
        model.cpx.setWarning(null);
        if(model.solve()){
            double best = model.cpx.getObjValue();
            output.printf("start solution is feasible, objective = %g\n", best);
            model.update(vars);
            model.free(vars);
            output.printf("free all variables\n");
            return best;
        }else{
            output.printf("start solution is infeasible\n");
            throw new Exception("start solution is infeasible");
        }
    }
    
    private double cplexSol(ArrayList<PSVar> vars, double best) throws Exception{
        output.printf("------------------------[ Cplex Search Evolution ]------------------------\n");
        output.printf("%12s %12s %12s %12s %12s\n", "cost", "time", "gap(%)", "time-solve", "status");
        
        double dt = model.solve(time_cplex);
        if(model.cpx.getStatus() == IloCplex.Status.Optimal || model.cpx.getStatus() == IloCplex.Status.Feasible){
            while(model.cpx.getObjValue()<best-1e-6){  
                best = model.cpx.getObjValue();
                output.printf("%12.2f %12.2f %12.2f %12.2f %12s\n", best, stop.time(), model.gap(), dt, model.cpx.getStatus());
                model.update(vars);
                dt = model.solve(time_cplex);
            }
        }else{
            output.printf("%12.2f %12.2f %12.2f %12.2f %12s\n", Double.NaN, stop.time(), 0.0, dt, model.cpx.getStatus());
        }
        return best;
    }

    @Override
    public void execute() throws Exception {
        model.cpx.setOut(null);
        model.cpx.setWarning(null);
        
        
        output.printf("------------------------[ Polynomial Search Start ]------------------------\n");
        ArrayList<PSVar> vars = model.variables();
        output.printf("start search has %d binary variables\n", vars.size());
        double best = Double.POSITIVE_INFINITY;
        if(usesInitSol){
            best = initSol(vars);
        }
        if(time_cplex>1e-6){
            best = cplexSol(vars, best);
        }
        PSsize = 1;
        output.printf("------------------------[ Polynomial Search Evolution ]------------------------\n");
        output.printf("%12s %12s %12s %12s  %12s %12s %12s\n", "best", "local", "cur", "PS-size", "time", "gap(%)", "time-solve");
        //output.printf("%12.2f %12d  %12.2f %12.2f %12.2f\n", best, PSsize, stop.time(), gap, 0.0);
        
        LinkedList<Double[]> cuts = new LinkedList<>();
        
        int greedy = 0;
        double local = best;
        IloRange permanent = null;
        while(!stop.end()){
            if(PSsize<=PSchange){
                model.clearChange2();
                model.addChange1(vars, PSsize, PSexpr);
                double dt = model.solve(stop.timeRemaining()); 
                if(model.cpx.getObjValue()<best-1e-6){//if there is improvement
                    local = best = model.cpx.getObjValue();
                    output.printf("*%11.2f +%11.2f +%11.2f %12d  %12.2f %12.2f %12.2f \n", best, local, model.cpx.getObjValue(), PSsize, stop.time(), model.gap(), dt);
                    PSsize = 1;
                    model.update(vars);
                }else if(model.cpx.getObjValue()<local-1e-6){//if there is improvement
                    local = model.cpx.getObjValue();
                    output.printf(" %11.2f +%11.2f +%11.2f %12d  %12.2f %12.2f %12.2f \n", best, local, model.cpx.getObjValue(), PSsize, stop.time(), model.gap(), dt);
                    PSsize = 1;
                    model.update(vars);
                }else{
                    if(PSsize+1>PSchange){
                        output.printf(" %11.2f  %11.2f @%11.2f %12d+ %12.2f %12.2f %12.2f \n", best, local, model.cpx.getObjValue(),  PSsize, stop.time(), model.gap(), dt);
                        output.println("cut space for: "+local+ " using size = "+(PSsize+1));
                        
//                        for(PSVar v : vars){
//                            output.printf("%s", v);
//                        }
//                        output.println();
                        
                        Double sol[] = new Double[vars.size()+1];
                        int i=0;
                        for(PSVar v : vars){
                            sol[i] = (double)v.getValue();//output.printf("%s", v);
                            i++;
                        }
                        sol[i] = local;
                     //   cuts.addLast(sol); --innclude
                      //  dist(cuts);--innclude
                        
                        //output.println();
                        IloNumExpr expr = model.changeExpr(vars);
                        model.update(vars);
                        permanent = model.addPermanent(expr, PSsize+1);
                        greedy = PSgreedy;
                    }else{
                        output.printf(" %11.2f  %11.2f  %11.2f %12d+ %12.2f %12.2f %12.2f \n", best, local, model.cpx.getObjValue(),  PSsize, stop.time(), model.gap(), dt);
                    }
                    PSsize += 1;
                }
            }else{
                if(greedy >= PSgreedy){
                    greedy--;
                }else if(greedy>=0){
                    //output.println("greedy added: "+(PSgreedy-greedy));
                    permanent.setLB(permanent.getLB()+1);
                    greedy--;
                }
                model.clearChange1();
                int size = model.addChange2(vars);
                double dt = model.solve(stop.timeRemaining());
                if(model.cpx.getObjValue()<best-1e-6){//if there is improvement
                    local = best = model.cpx.getObjValue();
                    output.printf("*%11.2f +%11.2f +%11.2f %12d- %12.2f %12.2f %12.2f \n", best, local, model.cpx.getObjValue(), size, stop.time(), model.gap(), dt);
                    PSsize = 1;
                    model.update(vars);
                }else if(model.cpx.getObjValue()<local-1e-6){//if there is improvement
                    local = model.cpx.getObjValue();
                    output.printf(" %11.2f +%11.2f +%11.2f %12d- %12.2f %12.2f %12.2f \n", best, local, model.cpx.getObjValue(), size, stop.time(), model.gap(), dt);
                    PSsize = 1;
                    model.update(vars);
                }else if(size>=PSwalk){
                    local = model.cpx.getObjValue();
                    output.printf(" %11.2f -%11.2f  %11.2f %12d- %12.2f %12.2f %12.2f \n", best, local, model.cpx.getObjValue(), size, stop.time(), model.gap(), dt);
                    PSsize = 1;
                    model.update(vars);
                }else{
                    output.printf(" %11.2f  %11.2f  %11.2f %12d+ %12.2f %12.2f %12.2f \n", best, local, model.cpx.getObjValue(), size, stop.time(), model.gap(), dt);
                    model.update(vars);
                    //PSsize += PSinc;
                }   
            }
                
        }

    }
    
    private double dist(Double[] a, Double[] b){
        double dist = 0;
        for(int i=0; i<a.length-1; i++){
            dist += Math.abs(a[i]-b[i]);
        }
        return dist;
    }
    private void dist(LinkedList<Double[]> cuts){
        if(cuts.size()>1){
            output.println("---------------------------------------------------------");
            output.printf("%12s | %12s\n", "local", "distances");
            Double[][] array = cuts.toArray(new Double[cuts.size()][]);
            for(int i=0; i<array.length; i++){
                output.printf("%12.2f | ", array[i][array[i].length-1]);
                for(int j=0; j<array.length; j++){
                    output.printf("%3.0f ", dist(array[i], array[j]));
                }
                output.println();
            }
            output.println("---------------------------------------------------------");
        }
    }
}
