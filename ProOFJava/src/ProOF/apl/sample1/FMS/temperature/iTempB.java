/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ProOF.apl.sample1.FMS.temperature;

import ProOF.com.Linker.LinkerParameters;
import ProOF.com.Linker.LinkerResults;
import ProOF.com.Linker.LinkerApproaches;
import ProOF.com.Linker.LinkerValidations;


/**
 *
 * @author marcio
 */
public final class iTempB extends aTemp {
    private double T;
    
    private double max;
    private double min;
    private double alpha;
    
    @Override
    public String name() {
        return "T = T /[ 1+alpha*sqrt(T) ]";
    }
    @Override
    public String description() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    @Override
    public void start() {
        this.T = max;
    }
    @Override
    public double decress() {
        return T = T / ( 1 + alpha * Math.sqrt(T) );
    }
    @Override
    public double temperature() {
        return T;
    }
    @Override
    public boolean end() {
        return T <= min;
    }
    
    @Override
    public void services(LinkerApproaches win) throws Exception {
        
    }
    @Override
    public void parameters(LinkerParameters win) throws Exception {
        max = win.Dbl("T max", 1e3, 1e-6, 1e6, "the maximum temperature");
        min = win.Dbl("T min", 1e-3, 1e-6, 1e6, "the minimum temperature");
        alpha = win.Dbl("alpha", 0.95, 1e-4, 0.9999, "the alpha parameter");
    }
    @Override
    public boolean validation(LinkerValidations com) throws Exception {
        return true;
    }
    @Override
    public void load() throws Exception {
        
    }
    @Override
    public void results(LinkerResults win) throws Exception {
        
    }
}
