/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ProOF.apl.advanced2.method;

import ProOF.apl.advanced2.FMS.RFFO.RFFOModel;
import ProOF.apl.advanced2.FMS.RFFO.RelaxVar;
import ProOF.com.Communication;
import ProOF.com.Linker.LinkerApproaches;
import ProOF.com.Linker.LinkerParameters;
import ProOF.com.Linker.LinkerResults;
import ProOF.com.Stream.StreamOutput;
import ProOF.com.language.Factory;
import ProOF.gen.stopping.Time;
import ProOF.opt.abst.run.Heuristic;
import ilog.concert.IloException;
import java.util.ArrayList;

/**
 *
 * @author marcio
 */
public class RFFO extends Heuristic{
    private Time stop = new Time();
    private RFFOModel model;
    
    private int rfSize;
    private int foSize;
    private double rfOverlap;
    private double foOverlap;
    private double foTol;
    private int foInc;
    
    private int foIter;
    private double time_best;
    private double cost_best;
    
    private StreamOutput output;
    
    private final Factory fRFFOModel;

    public RFFO(Factory fRFFOModel) {
        this.fRFFOModel = fRFFOModel;
    }   
    
    @Override
    public String name() {
        return "RFFO";
    }
    @Override
    public void services(LinkerApproaches link) throws Exception {
        stop    = link.add(stop);
        model   = link.get(fRFFOModel, model);
    }

    @Override
    public void parameters(LinkerParameters link) throws Exception {
        rfSize      = link.Int("rf-Size", 40, 1, Integer.MAX_VALUE);
        rfOverlap   = link.Dbl("rf-Overlap", 0.8, 0.0, 1.0);
        foSize      = link.Int("fo-Size", 40, 1, Integer.MAX_VALUE);
        foOverlap   = link.Dbl("fo-Overlap", 0.5, 0.0, 1.0);
        foTol       = link.Dbl("fo-Tol", 0.01, 0.0, 1.0);
        foInc       = link.Int("fo-Inc", 10, 0, Integer.MAX_VALUE);
    }
    @Override
    public void load() throws Exception {
        output = Communication.mkOutput("RFFO");
    }

    @Override
    public void start() throws Exception {
        super.start(); //To change body of generated methods, choose Tools | Templates.
        foIter = 0;
    }
    

    @Override
    public void execute() throws Exception {
        RelaxAndFix();
        while(!stop.end()){
            if(foSize<model.size()){
                FixAndOptimize();
            }else{
                ModelFull();
                break;
            }
        }
        cost_best = model.cost();
    }

    @Override
    public void results(LinkerResults link) throws Exception {
        super.results(link); //To change body of generated methods, choose Tools | Templates.
        link.writeInt("foIter", foIter);
        link.writeInt("foSize", foSize);
        link.writeDbl("time_best", time_best);
        link.writeDbl("cost_best", cost_best);
        link.writeDbl("time_total", stop.time());
    }
    
    
    private void RelaxAndFix() throws IloException, Exception{
        model.setStatus("Relax And Fix");
        time_best = -1;
        output.printf("------------------------[ Relax and Fix ]------------------------\n");
        output.printf("%12s %12s %12s %12s %12s %12s %12s\n", "progress(%)", "cost", "v-Fix", "v-Free", "v-Relax", "time", "gap(%)");
        
        model.solveRF(stop.timeRemaining());
        
        
        //model.print3(var);
        output.printf("%12.2f %12g %12d %12d %12d %12.3f %12.2f\n", 
                0.0, model.cost(), model.fixed(), model.converted()-model.fixed(), model.size()-model.converted(), stop.time(), model.gap());
                
        model.conversion(rfSize);
        
        while(model.fixed()<model.size() && !stop.end()){
            model.solveRF(stop.timeRemaining());
            //model.print3(var);
            if(model.size()==model.converted() && model.gap() <= model.EpGap()){    //If is integer and Gap <= EpGap, then fix all and break
                model.fix(model.converted()-model.fixed());
                break;
            }
            
            
            int step = (int) (rfSize*(1-rfOverlap) + 0.999999);
            
            output.printf("%12.2f %12g %12d %12d %12d %12.3f %12.2f\n", 
                    Math.min((model.fixed())*100.0/model.size(), 100), model.cost(), 
                    model.fixed(), model.converted()-model.fixed(), model.size()-model.converted(), stop.time(), model.gap());
            
            model.fix(step);
            
            model.conversion(step);
        }
        model.solveRF(stop.timeRemaining());
        //model.print3(var);
        output.printf("%12.2f %12g %12d %12d %12d %12.3f %12.2f\n", 
                Math.min(model.fixed()*100.0/model.size(), 100), model.cost(), 
                model.fixed(), model.converted()-model.fixed(), model.size()-model.converted(), stop.time(), model.gap());
        
        model.extra_conversion();
        
        model.solveRF(stop.timeRemaining());
        //model.print3(var);
        output.printf("%12.2f %12g %12d %12d %12d %12.3f %12.2f   extra conversion\n", 
                Math.min(model.fixed()*100.0/model.size(), 100), model.cost(), 
                model.fixed(), model.converted()-model.fixed(), model.size()-model.converted(), stop.time(), model.gap());
        
        
        time_best = stop.time();
        cost_best = model.cost();
    }

    private void FixAndOptimize() throws Exception {
        model.setStatus("Fix And Opt");
        double prevCost = model.cost();
        
        output.printf("------------------------[ Fix and Optimize %2d ]------------------------\n", foIter);
        output.printf("%12s %12s %12s %12s %12s %12s %12s\n", "progress(%)", "cost", "v-Fix", "v-Free", "v-Relax", "time", "gap(%)");

        ArrayList<RelaxVar> var = model.fix_variables();
        int wFree = 0;

        output.printf("%12.2f %12g %12d %12d %12d %12.3f %12.2f\n", 
                0.0, model.cost(), model.size()-model.free(), model.free(), model.size()-model.converted(), stop.time(), model.gap());

        while(wFree<var.size() && !stop.end()){
            int step = (int) (foSize*(1-foOverlap) + 0.999999);
            model.moveWindow(var , wFree, wFree+foSize);

            model.solveFO(stop.timeRemaining());
            //model.print3(var);
            if(model.cost()*1.0001 < cost_best){
                cost_best = model.cost();
                time_best = stop.time();
            }
            wFree += step;

            output.printf("%12.2f %12g %12d %12d %12d %12.3f %12.2f\n", 
                    Math.min(wFree*100.0/var.size(), 100), model.cost(), model.size()-model.free(), model.free(), model.size()-model.converted(), stop.time(), model.gap());
            //var = model.fix_variables();
        }
        double Improvement = (prevCost-model.cost())/prevCost;
        if(Improvement < foTol){
            foSize += foInc;
            System.out.printf("Improvement of %1.2f %% and increase window to %d\n", Improvement*100, foSize);
        }else{
            System.out.printf("Improvement of %1.2f %%\n", Improvement*100);
        }
        foIter++;
    }
    private void ModelFull() throws Exception {
        model.setStatus("Model Full");
        output.printf("------------------------[ Model full ]------------------------\n");
        output.printf("%12s %12s %12s %12s %12s %12s\n", "cost", "v-Fix", "v-Free", "v-Relax", "time", "gap(%)");
        model.solveFull(stop.timeRemaining());
        if(model.cost()*1.0001 < cost_best){
            cost_best = model.cost();
            time_best = stop.time();
        }
        output.printf("%12g %12d %12d %12d %12.3f %12.2f\n", 
            model.cost(), model.size()-model.free(), model.free(), model.size()-model.converted(), stop.time(), model.gap());
    }
}
