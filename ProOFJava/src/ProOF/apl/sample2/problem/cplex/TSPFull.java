/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ProOF.apl.sample2.problem.cplex;

import ProOF.apl.sample1.problem.TSP.TSPInstance;
import ProOF.com.Linker.LinkerApproaches;
import ProOF.CplexOpt.CplexFull;
import ilog.concert.IloException;

/**
 *
 * @author marcio
 */
public class TSPFull extends CplexFull{

    public TSPInstance inst = new TSPInstance();
    private TSPmodel model;
    
    public TSPFull() throws IloException {
        super();
    }

    @Override
    public void services(LinkerApproaches link) throws Exception {
        super.services(link);
        inst = link.add(inst);
    }
    @Override
    public String name() {
        return "TSP-full";
    }
    @Override
    public void model() throws Exception {
        model = new TSPmodel(inst, cpx);
        model.model(false);
        //cpx.exportModel("../../../TSP.lp");
    }
    @Override
    public void print() throws Exception {
        super.print(); //To change body of generated methods, choose Tools | Templates.
        model.print();
    }
}
