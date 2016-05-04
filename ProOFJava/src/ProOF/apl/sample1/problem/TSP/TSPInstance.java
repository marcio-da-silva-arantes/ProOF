/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ProOF.apl.sample1.problem.TSP;

import ProOF.com.Linker.LinkerParameters;
import ProOF.opt.abst.problem.Instance;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 *
 * @author marcio
 */
public class TSPInstance extends Instance{
    private File file;          //Parameter: data file problem
    
    public int N;               //Data: number of cities 
    public double Cij[][];      //Data: cost matrix
    public double optimal;      //Data: known optimal value
    
    @Override
    public String name() {
        return "Instance-TSP";
    }
    @Override
    public void parameters(LinkerParameters link) throws Exception {
        file = link.File("Instances for TSP", null, "txt");
    }
    @Override
    public void load() throws FileNotFoundException {
        Scanner sc = new Scanner(file);
        sc.nextLine();
        this.optimal = Double.parseDouble(sc.nextLine());
        sc.nextLine();
        this.N = Integer.parseInt(sc.nextLine());
        sc.nextLine();
        this.Cij = new double[N][N];
        for(int i=0; i<N; i++){
            for(int j=0; j<N; j++){
                Cij[i][j] = sc.nextDouble();
            }
        }
        sc.close();
    }
}
