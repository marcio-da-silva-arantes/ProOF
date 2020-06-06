/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ProOF.apl.sample2.problem.RFFO;

import ProOF.apl.advanced2.FMS.RFFO.RFFOModel;
import ProOF.apl.advanced2.FMS.RFFO.RelaxVar;
import ProOF.apl.sample1.problem.NSP.NSPInstance;
import ProOF.apl.sample2.problem.cplex.NSPmodel;
import ProOF.com.Linker.LinkerApproaches;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.time.chrono.ThaiBuddhistEra;
import java.util.ArrayList;

/**
 *
 * @author claudio
 */
public class NSP_RFFO  extends RFFOModel{
    public NSPInstance inst = new NSPInstance();
    private NSPmodel model;
    
    @Override
    public String name() {
        return "NSP-rffo";
    }
    
    @Override
    public void services(LinkerApproaches link) throws Exception {
        super.services(link);
        inst = link.add(inst);
    }
    
    @Override
    public void model() throws Exception {
        model = new NSPmodel(inst, cpx);
        model.model(true);
    }

    
    @Override
    public void print() throws Exception {
        
        double vXij[][][] = cpx.getValues(model.X);
        
        BufferedWriter bw = null;
        FileWriter fw = null;
        
        fw = new FileWriter("rNSP_"+inst.getNoN()+"_"+inst.getHorizon()+"_"+inst.getNoS()+"_"+cpx.getObjValue()+".csv");
        bw = new BufferedWriter(fw);
        
        
        bw.write(" [ Xijk ] ");
        for(int d=0; d<inst.getHorizon(); d++){
            for(int t=0; t<inst.getNoS(); t++){
                bw.write("\t Dia "+(d+1)+": "+(inst.getShiftByIndex(t)));
            }
        }
        
        bw.write("\n");
        
        for(int i=0; i<inst.getNoN(); i++){
            bw.write("PHY["+(i+1)+"]");
            for(int j=0; j<inst.getHorizon(); j++){
                for(int k=0;k<inst.getNoS();k++){
                    bw.write(" \t"+vXij[i][j][k]);
                }
            }
            bw.write("\n");
        }
        
            bw.write("\n");
     
        if (bw != null)
            bw.close();

        if (fw != null)
            fw.close();
        
    }

    
    @Override
    protected ArrayList<RelaxVar> relax_variables() throws Exception {
        
        ArrayList<RelaxVar> list = new ArrayList<RelaxVar>();
                 
        for(int w=0;w <inst.getWeeks();w++){
            for(int i=0; i<inst.getNoN();i++){
                for(int j=inst.beginingW(w);j<inst.endingW(w);j++)
                {
                    for(int k=0;k<inst.getNoS();k++){
                        list.add(new RelaxVar(model.X[i][j][k]));
                    }
                }
                list.add(new RelaxVar(model.K[i][w]));
            }
        }
        
        return list; 
    }
    
    
    @Override
    public ArrayList<RelaxVar> fix_variables() throws Exception {
        
        ArrayList<RelaxVar> list = new ArrayList<RelaxVar>();
        
        for(int w=0;w <inst.getWeeks();w++){
            for(int i=0; i<inst.getNoN();i++){
                for(int j=inst.beginingW(w);j<inst.endingW(w);j++)
                {
                    for(int k=0;k<inst.getNoS();k++){
                        list.add(new RelaxVar(model.X[i][j][k]));
                    }
                }
                list.add(new RelaxVar(model.K[i][w]));
            }
        }        
       return list;
    }
}
