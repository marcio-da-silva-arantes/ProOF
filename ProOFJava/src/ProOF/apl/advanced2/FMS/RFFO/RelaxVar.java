/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ProOF.apl.advanced2.FMS.RFFO;

import ProOF.CplexExtended.CplexExtended;
import ilog.concert.IloException;
import ilog.concert.IloNumVar;
import ilog.concert.IloNumVarType;
import ilog.cplex.IloCplex;

/**
 *
 * @author marcio
 */
public class RelaxVar {

    public static RelaxVar[] extra(IloNumVarType type, double lb, double ub, IloNumVar ...extra) {
        return extra(type, (int)(lb+0.999999), (int)(ub+0.000001), extra);
    }
    public static RelaxVar[] extra(IloNumVarType type, int lb, int ub, IloNumVar ...extra) {
        RelaxVar[] var = new RelaxVar[extra.length];
        for(int i=0; i<var.length; i++){
            var[i] = new RelaxVar(extra[i], type, lb, ub, var);
        }
        return var;
    }
    public final IloNumVar var;
    private final IloNumVarType type;
    private final int lb;
    private final int ub;
    private final RelaxVar extra[];
    
    public RelaxVar(IloNumVar var, RelaxVar ...extra) {
        this(var, IloNumVarType.Bool, 0, 1, extra);
    }
    public RelaxVar(IloNumVar var, int lb, int ub, RelaxVar ...extra) {
        this(var, (lb==0&&ub==1 ? IloNumVarType.Bool : IloNumVarType.Int), lb, ub, extra);
    }
    public RelaxVar(IloNumVar var, IloNumVarType type, int lb, int ub, RelaxVar ...extra) {
        if(lb>ub){
            throw new IllegalArgumentException("lb > ub");
        }
        this.var = var;
        this.type = type;
        this.lb = lb;
        this.ub = ub;
        this.extra = extra;
    }
    public final void convert(IloCplex cpx) throws IloException {
        cpx.add(cpx.conversion(var, type));
        if(extra!=null){
            for (RelaxVar e : extra) {
                e.convert(cpx);
            }
        }
    }
    public final void free() throws IloException {
        var.setLB(lb);
        var.setUB(ub);
    }
    public final void fix(double value) throws IloException {
        var.setLB(value);
        var.setUB(value);
    }
    public final double round(CplexExtended cpx) throws IloException {
        return (int)(cpx.getValue(var)+0.5);
    }
    @Override
    public final String toString() {
        return var.toString();
    }

    
}
