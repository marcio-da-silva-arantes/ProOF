/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ProOF.apl.sample2.problem.RFFO;

import ProOF.apl.advanced2.FMS.RFFO.RFFOModel;
import ProOF.apl.advanced2.FMS.RFFO.RelaxVar;
import ProOF.apl.sample1.problem.TSP.TSPInstance;
import ProOF.apl.sample2.problem.cplex.TSPmodel;
import ProOF.com.Linker.LinkerApproaches;
import java.util.ArrayList;

/**
 *
 * @author marcio
 */
public class TSP_RFFO extends RFFOModel{
    public TSPInstance inst = new TSPInstance();
    private TSPmodel model;
  
    @Override
    public String name() {
        return "TSP-rffo";
    }
    @Override
    public void services(LinkerApproaches link) throws Exception {
        super.services(link);
        inst = link.add(inst);
    }
     @Override
    public void start() throws Exception {
        model = new TSPmodel(inst, cpx);
    }
    @Override
    public void model() throws Exception {
        model.model(true);
        //cpx.exportModel("../../../TSP.lp");
    }
    @Override
    public void print() throws Exception {
        model.print();
    }
    
    @Override
    public ArrayList<RelaxVar> relax_variables() throws Exception {
        ArrayList<RelaxVar> list = new ArrayList<RelaxVar>();
        for(int i=0; i<inst.N; i++){
            for(int j=0; j<inst.N; j++){
                if(i!=j){
                    list.add(new RelaxVar(model.Xij[i][j]));
                }
            }
        }
        return byNearRef(list, 1.0);
    }

    @Override
    public ArrayList<RelaxVar> fix_variables() throws Exception {
        ArrayList<RelaxVar> list = new ArrayList<RelaxVar>();
        //------------------------[ frist row to col ]--------------------------
        for(int i=0; i<inst.N; i++){
            for(int j=0; j<inst.N; j++){
                if(i!=j){
                    list.add(new RelaxVar(model.Xij[i][j]));
                }
            }
        }
        //-----------------------[ second col to row ]--------------------------
        for(int i=0; i<inst.N; i++){
            for(int j=0; j<inst.N; j++){
                if(i!=j){
                    list.add(new RelaxVar(model.Xij[i][j]));
                }
            }
        }
        return list;
    }
}
