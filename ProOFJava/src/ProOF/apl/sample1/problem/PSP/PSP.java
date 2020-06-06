/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ProOF.apl.sample1.problem.PSP;


import CplexExtended.CplexExtended;
import ProOF.apl.sample2.problem.cplex.PSPmodel;
import ProOF.com.Linker.LinkerApproaches;
import ProOF.gen.best.BestSol;
import ProOF.opt.abst.problem.meta.Objective;
import ProOF.opt.abst.problem.meta.Problem;
import ProOF.opt.abst.problem.meta.codification.Codification;

/**
 *
 * @author dexter
 */
public class PSP extends Problem<BestSol>{
    public EasyInstance inst = new EasyInstance();
    public PSPmodel model;
    CplexExtended cpx;
    
    @Override
    public String name() {
        return "PSP"; //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public Codification build_codif() throws Exception {
        return new cPSP(this);
    }

    @Override
    public Objective build_obj() throws Exception {
        return new PSPObjective(); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public void services(LinkerApproaches link) throws Exception {
        super.services(link);
        link.add(inst);
        link.add(PSPOperator.obj);
    }

    @Override
    public void start() throws Exception {
    //    super.start(); //To change body of generated methods, choose Tools | Templates.
        cpx = new CplexExtended();
        model = new PSPmodel(this.inst, cpx);
        model.model(false);
        
    }
    
    
    @Override
    public BestSol best() {
        return BestSol.object();
    }
     
}
