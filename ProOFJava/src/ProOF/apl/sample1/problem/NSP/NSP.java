/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ProOF.apl.sample1.problem.NSP;

import ProOF.com.Linker.LinkerApproaches;
import ProOF.gen.best.BestSol;
import ProOF.opt.abst.problem.meta.Objective;
import ProOF.opt.abst.problem.meta.Problem;
import ProOF.opt.abst.problem.meta.codification.Codification;

/**
 *
 * @author dexter
 */
public class NSP extends Problem<BestSol>{
    EasyInstance inst = new EasyInstance();

    @Override
    public String name() {
        return "NSP"; //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public Codification build_codif() throws Exception {
        return new cNSP(this);
    }

    @Override
    public Objective build_obj() throws Exception {
        return new NSPObjective(); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public void services(LinkerApproaches link) throws Exception {
        super.services(link);
        link.add(inst);
        link.add(NSPOperator.obj);
    }
    
    @Override
    public BestSol best() {
        return BestSol.object();
    }
        
}
