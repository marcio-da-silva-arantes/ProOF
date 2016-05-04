/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ProOF.apl.sample1.FMS.branch;

import ProOF.com.Communication;
import ProOF.com.Linker.LinkerApproaches;
import ProOF.com.Linker.LinkerParameters;
import ProOF.com.Linker.LinkerResults;
import ProOF.com.Stream.StreamPrinter;
import ProOF.com.runner.ExceptionForceFinish;
import ProOF.gen.best.*;
import ProOF.gen.stopping.Stop;
import ProOF.gen.stopping.CountInteger;
import ProOF.utilities.uTime;
import ProOF.utilities.uTimeMilli;

/**
 *
 * @author marcio
 */
public class BranchBestSol extends BranchBest{
    private static BranchBestSol obj = null;
    public static BranchBestSol object(){
        if(obj==null){
            obj = new BranchBestSol();
        }
        return obj;
    }

    private class Sol{
        private BranchNode base;
        private long eval;
        private long ints;
        
        private double time;
        private double progress;

        public Sol(BranchNode sol, long eval, long ints, double time, double progress) {
            this.base = sol;
            this.eval = eval;
            this.ints = ints;
            this.time = time;
            this.progress = progress;
        }
    }

    private Sol best;
    private long cout;
    private final uTime time = new uTimeMilli();
    
    private final nEvaluations cont_eval = nEvaluations.object();
    private final CountInteger cont_ints = CountInteger.object();
    
    public Stop stop;
    private StreamPrinter com;
    
    private double progress_factor;
    
    @Override
    public String name() {
        return "BestSol";
    }
    @Override
    public String description() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public BranchNode ind() throws Exception {
        return best.base;
    }
    public long id(){
        return best.eval;
    }
    @Override
    public void services(LinkerApproaches link) throws Exception {
        link.add(cont_eval);
        link.add(cont_ints);
        stop = link.need(Stop.class, stop);
    }

    @Override
    public void parameters(LinkerParameters link) throws Exception {
        progress_factor = link.Dbl("progress-factor", 0.01, -0.01, 0.5);
    }
    
    @Override
    public void load() throws Exception {
        com = Communication.mkPrinter("exact-bests");
    }
    @Override
    public void start() throws Exception {
        best = new Sol(null, 0, 0, 0, 0);
        time.start();
        cout = 0;
    }
    @Override
    public void results(BranchProblem prob, LinkerResults link) throws Exception {
        link.writeLong("eval tot", cont_eval.value());
        link.writeLong("eval best", best.eval);
        link.writeDbl("time tot", time_force>0 ? time_force : time_now());
        link.writeDbl("time best", time_best());
        link.writeDbl("time after", time_after());
        link.writeLong("ints tot", cont_ints.value());
        link.writeLong("ints best", best.ints);
        if(best.base!=null){
            link.writeString("type", best.base.is_integer(prob)?"integer":"relaxed");
            best.base.results(prob, link, best.base);
        }
    }
    private double time_force = -1;
    private boolean print_buffer = false;
    @Override
    public void better(BranchProblem prob, BranchNode base) throws Exception {
        //best = best==null ? sol : stop.evaluate() ? best : best.minimum(sol);
        cont_eval.update();
        
        
        if(!stop.end()){
            if(base.is_integer(prob)){
                cont_ints.update(); 
            }
            if( best.base==null || 
                    (best.base.is_integer(prob) && base.is_integer(prob) && base.cur_cost < best.base.cur_cost) || 
                    (!best.base.is_integer(prob) && base.is_integer(prob)) ||
                    (!best.base.is_integer(prob) && !base.is_integer(prob) && (base.level > best.base.level || (base.level == best.base.level && base.cur_cost < best.base.cur_cost))) 
            ){
                best.base = base;
                best.eval = cont_eval.value();
                best.ints = cont_ints.value();
                best.time = time_now();
                
                if(cout<2 || stop.progress()>=best.progress+progress_factor){
                    best.progress = stop.progress();
                    com.printLong("eval", best.eval);
                    com.printLong("ints", best.ints);
                    com.printDbl("time", time_best());
                    com.printString("type", best.base.is_integer(prob)?"integer":"relaxed");
                    best.base.printer(prob, com, best.base);
                    com.flush();
                    cout++;
                    print_buffer = false;
                }else{
                    print_buffer = true;
                }
            }
        }else if(force_finish){
            time_force = time_now();
            throw new ExceptionForceFinish();
        }
    }
    @Override
    public void flush(BranchProblem prob) throws Exception {
        if(print_buffer){
            best.progress = stop.progress();
            com.printLong("eval", best.eval);
            com.printDbl("time", time_best());
            com.printString("type", best.base.is_integer(prob)?"integer":"relaxed");
            best.base.printer(prob, com, best.base);
            com.flush();
            cout++;
            print_buffer = false;
        }
    }
    @Override
    public double time_now() {
        return time.time();
    }
    @Override
    public double time_best() {
        return best.time;
    }
    @Override
    public double time_after() {
        return time_now()-time_best();
    }
}
