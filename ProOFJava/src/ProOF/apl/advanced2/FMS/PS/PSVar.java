/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ProOF.apl.advanced2.FMS.PS;

import CplexExtended.CplexExtended;
import ilog.concert.IloException;
import ilog.concert.IloNumVar;
import ilog.concert.IloNumVarType;
import ilog.cplex.IloCplex;

/**
 *
 * @author marcio
 */
public class PSVar {

    public final IloNumVar var;
    private int value;
    
    public PSVar(IloNumVar var, int start_value) throws IloException {
        if(var.getType().getTypeValue() != IloNumVarType.Bool.getTypeValue()){
            throw new IllegalArgumentException("var '"+var+"'type must be boolean, but is "+var.getType());
        }
        this.var = var;
        this.value = start_value;
    }    
    public final void update(CplexExtended cpx) throws IloException {
        value = (int)(cpx.getValue(var)+0.5);
    }
    public final void fix() throws IloException{
        var.setLB(value);
        var.setUB(value);
    }
    public final void free() throws IloException{
        var.setLB(0);
        var.setUB(1);
    }
    @Override
    public final String toString() {
        return ""+value;
    }
    public int getValue() {
        return value;
    }
}
