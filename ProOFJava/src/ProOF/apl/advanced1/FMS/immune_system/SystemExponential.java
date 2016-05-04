/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ProOF.apl.advanced1.FMS.immune_system;

import ProOF.com.Linker.LinkerParameters;

/**
 *
 * @author marcio
 */
public class SystemExponential extends AIS{

    private int antibodies_number;  //number of antibodies
    private int keep_number;        //number of clones that will be select to next generation
    private int clones_number;      //number of clones generated
    private double exponential_base;
    private double cloning_rate;    //cloning rate
    
    @Override
    public String name() {
        return "Exponential";
    }
    @Override
    public int n_antibodies() {
        return antibodies_number;
    }
    @Override
    public int n_keep() {
        return keep_number;
    }
    @Override
    public int n_clones(int index) {
        return Math.max(0,(int)(antibodies_number * cloning_rate/Math.pow(exponential_base,index) + 0.5));
    }
    
    @Override
    public void parameters(LinkerParameters link) throws Exception {
        super.parameters(link); //To change body of generated methods, choose Tools | Templates.
        antibodies_number   = link.Int("antibodies number", 100, 1, 1000000);
        keep_number         = link.Int("keep number",       80, 1, 1000000);
        clones_number       = link.Int("clones number",     200, 1, 1000000);
        exponential_base    = link.Dbl("exponential base",  1.1, 1.0, 10.0);
        
        
//        antibodies_number = 100;
//        keep_number = 80;
//        cloning_rate = 0.5;
    }

    @Override
    public void start() throws Exception {
        super.start(); //To change body of generated methods, choose Tools | Templates.
        cloning_rate = 1.0;
        double total = 0;
        for(int n=0; n<antibodies_number; n++){
            total += n_clones(n);
        }
        cloning_rate = clones_number/total;
//        System.out.println("total = "+total);
//        System.out.println("cloning_rate = "+cloning_rate);
        total = 0;
        for(int n=0; n<antibodies_number; n++){
            total += n_clones(n);
        }
//        System.out.println("validation = "+total);
//        
//        
//        System.out.println("----------[distribution]----------");
//        for(int n=0; n<antibodies_number; n++){
//            System.out.printf("clone(%2d) = %3d\n",n, n_clones(n));
//        }
    }
    
    

    
    public static void main(String[] args) throws Exception {
        
        SystemExponential  test = new SystemExponential();
        test.antibodies_number = 100;
        test.clones_number = 200;
        test.exponential_base = 1.1;
        test.start();
    }
    
}
