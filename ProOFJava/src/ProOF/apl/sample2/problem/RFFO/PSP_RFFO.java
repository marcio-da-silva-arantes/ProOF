/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ProOF.apl.sample2.problem.RFFO;

import ProOF.apl.advanced2.FMS.RFFO.RFFOModel;
import ProOF.apl.advanced2.FMS.RFFO.RelaxVar;
import ProOF.apl.sample1.problem.PSP.EasyInstance;
import ProOF.apl.sample2.problem.cplex.PSPmodel;
import ProOF.com.Linker.LinkerApproaches;
import ProOF.com.Linker.LinkerParameters;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.time.chrono.ThaiBuddhistEra;
import java.util.ArrayList;

/**
 *
 * @author claudio
 */
public class PSP_RFFO  extends RFFOModel{
    public EasyInstance inst = new EasyInstance();
    private PSPmodel model;
    
    private int windowsTypeRF;
    private int windowsTypeFO;

    @Override
    public String name() {
        return "PSP-rffo";
    }
    
    @Override
    public void services(LinkerApproaches link) throws Exception {
        super.services(link);
        inst = link.add(inst);
    }
    
    @Override
    public void model() throws Exception {
        model = new PSPmodel(inst, cpx);
        model.model(true);
    }

    
    @Override
    public void parameters(LinkerParameters link) throws Exception {
        super.parameters(link);
        
        windowsTypeRF = link.Itens("RF-Type", 0, "value-wise", 
                "row-wise", "column-wise");
        
        windowsTypeFO = link.Itens("FO-Type", 0,
                "only row", "only column");
    }
    
    @Override
    public void print() throws Exception {
        model.print();
    }

    
    @Override
    protected ArrayList<RelaxVar> relax_variables() throws Exception {
        
        switch(windowsTypeRF){
            case 0 : return byDistRef(row_wise(), 0.5);
            case 1 : return row_wise(); 
            case 2 : return column_wise();
        }
        
        throw new Exception("windowsTypeRF = "+windowsTypeRF+" is invalid"); 
    }
    
    
    @Override
    public ArrayList<RelaxVar> fix_variables() throws Exception {
        
        switch(windowsTypeFO){
            case 0 : return row_wise(); 
            case 1 : return column_wise();
        }
        throw new Exception("windowsTypeFO = "+windowsTypeFO+" is invalid"); 
    }
    
    private ArrayList<RelaxVar> row_wise() {
        ArrayList<RelaxVar> list = new ArrayList<RelaxVar>();
        
        for(int p=0; p<inst.getNOE();p++){
            
            for(int w=0;w <inst.getWeeks();w++){
                for(int d=inst.begining(w);d<inst.ending(w);d++)
                {
                    for(int s=0; s<inst.getNOS(); s++){
                        list.add(new RelaxVar(model.X[p][d][s]));
                    }
                }
                list.add(new RelaxVar(model.K[p][w]));
            }
        }
        return list;
    }
    
    private ArrayList<RelaxVar> column_wise() {
        ArrayList<RelaxVar> list = new ArrayList<RelaxVar>();
        
        for(int w=0;w <inst.getWeeks();w++){
            for(int p=0; p<inst.getNOE();p++){
                for(int d=inst.begining(w);d<inst.ending(w);d++)
                {
                    for(int s=0;s<inst.getNOS();s++){
                        list.add(new RelaxVar(model.X[p][d][s]));
                    }
                }
                list.add(new RelaxVar(model.K[p][w]));
            }
        }    
        return list;
    }
    
}
