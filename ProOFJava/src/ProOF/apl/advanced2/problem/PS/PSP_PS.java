/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ProOF.apl.advanced2.problem.PS;

import ProOF.apl.advanced2.FMS.PS.PSModel;
import ProOF.apl.advanced2.FMS.PS.PSVar;
import ProOF.apl.sample2.problem.RFFO.*;
import ProOF.apl.advanced2.FMS.RFFO.RFFOModel;
import ProOF.apl.advanced2.FMS.RFFO.RelaxVar;
import ProOF.apl.sample1.problem.PSP.EasyInstance;
import ProOF.apl.sample2.problem.cplex.PSPmodel;
import ProOF.com.Linker.LinkerApproaches;
import ProOF.com.Linker.LinkerParameters;
import ilog.concert.IloException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.chrono.ThaiBuddhistEra;
import java.util.ArrayList;

/**
 *
 * @author claudio
 */
public class PSP_PS  extends PSModel{
    public EasyInstance inst = new EasyInstance();
    private PSPmodel model;
    int V[][][]; 
    
    @Override
    public String name() {
        return "PSP-PS";
    }
    
    @Override
    public void services(LinkerApproaches link) throws Exception {
        super.services(link);
        inst = link.add(inst);
    }
    
    @Override
    public void model() throws Exception {
        model = new PSPmodel(inst, cpx);
        model.model(false);
    }

    @Override
    public void start() throws Exception {
        super.start(); //To change body of generated methods, choose Tools | Templates.
     //   startSolutionFromHeuristic(inst);
    }

    
    public void startSolutionFromHeuristic(EasyInstance inst) throws IloException, Exception{
      
        V = new int[inst.getNOE()][inst.getNOD()][inst.getNOS()];
        
        for(int i =0; i<inst.getNOE();i++){
            for(int j =0; j<inst.getNOD();j++){
                for(int k =0; k<inst.getNOS();k++){
                    if(Math.random()< 0.5)
                    {
                        V[i][j][k] = 0;
                    }
                    else{
                        V[i][j][k] = 1;
                    }
                }
            }
        }
        
    }
    
    
    
    @Override
    public void parameters(LinkerParameters link) throws Exception {
        super.parameters(link); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ArrayList<PSVar> variables() throws Exception {
            
        ArrayList<PSVar> list = new ArrayList<PSVar>();
            
        //for(int w=0;w <4;w++){
        for(int i=0; i<inst.getNOE();i++){
            for(int j=0;j<inst.getNOD();j++)
            {
                for(int k=0;k<inst.getNOS();k++){
                    list.add(new PSVar(model.X[i][j][k],V[i][j][k]));
                }
            }
        }
        //}
        return list;
    }
    
    @Override
    public void print() throws Exception {
        model.print();
    }

    
}
