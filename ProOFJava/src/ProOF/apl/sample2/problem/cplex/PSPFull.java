/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ProOF.apl.sample2.problem.cplex;


import ProOF.com.Linker.LinkerApproaches;
import ProOF.CplexOpt.CplexFull;
import ProOF.apl.sample1.problem.PSP.EasyInstance;
import ProOF.apl.sample1.problem.PSP.PSP;
import ProOF.apl.sample1.problem.PSP.cPSP;
import ProOF.com.Linker.LinkerResults;
import ilog.concert.IloException;
import ilog.cplex.IloCplex;
import java.io.File;
import java.util.Collection;

/**
 *
 * @author dexter
 */
public class PSPFull extends CplexFull{
    /**
     */
    public EasyInstance inst = new EasyInstance();
    private PSPmodel model;
    
    public PSPFull() throws IloException {
        super();
    }

    @Override
    public void services(LinkerApproaches link) throws Exception {
        super.services(link);
        inst = link.add(inst);
    }
   
    @Override
    public String name() {
        return "PSP-full";
    }
    @Override
    public void start() throws Exception {
        super.start();
        model = new PSPmodel(inst, cpx);
    }
    
    @Override
    public void model() throws Exception {
        model = new PSPmodel(inst, cpx);
        model.model(false);
  //      model.startSolutionFromFile(inst);
    }
    
    @Override
    public void print() throws Exception {
        super.print(); //To change body of generated methods, choose Tools | Templates.
        model.print();
        
    }
    
    
}
