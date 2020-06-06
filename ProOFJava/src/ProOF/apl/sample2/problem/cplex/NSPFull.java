/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ProOF.apl.sample2.problem.cplex;


import ProOF.com.Linker.LinkerApproaches;
import ProOF.CplexOpt.CplexFull;
import ProOF.apl.sample1.problem.NSP.NSPInstance;
import ProOF.com.Linker.LinkerResults;
import ilog.concert.IloException;
import ilog.cplex.IloCplex;
import java.io.File;
import java.util.Collection;


/**
 *
 * @author dexter
 */
public class NSPFull extends CplexFull{

    public NSPInstance inst = new NSPInstance();
    private NSPmodel model;
    
    public NSPFull() throws IloException {
        super();
    }

    @Override
    public void services(LinkerApproaches link) throws Exception {
        super.services(link);
        inst = link.add(inst);
    }
    @Override
    public String name() {
        return "NSP-full";
    }
    @Override
    public void start() throws Exception {
        super.start();
       /** cpx.setParam(IloCplex.IntParam.FlowCovers, IloCplex.CutType.FlowCover);
        cpx.setParam(IloCplex.IntParam.Covers, 3);*/
        model = new NSPmodel(inst, cpx);     
    }
    
    @Override
    public void model() throws Exception {
        model = new NSPmodel(inst, cpx);     
        model.model(false);
   //    model.startSolutionFromHeuristic(inst);
    }
    
    @Override
    public void print() throws Exception {
        super.print(); //To change body of generated methods, choose Tools | Templates.
        model.print();
        
    }
    
    
}
