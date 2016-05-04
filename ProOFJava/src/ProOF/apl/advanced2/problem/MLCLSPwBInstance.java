/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ProOF.apl.advanced2.problem;

import ProOF.com.Linker.LinkerParameters;
import ProOF.opt.abst.problem.Instance;
import ProOF.utilities.uTxtIO;
import java.io.File;
import java.util.Scanner;

/**<pre>
Scalar NT Number of time periods in the horizon;
Scalar NI Number of items;
Scalar NK Number of machines;
Scalar NF Number of product families (each item belongs to one family);
Scalar endp The set of all end-items;
Scalar BC Backlogging cost;

Parameters
     hc(i)    Holding cost for each item i per period  
     td(i)    Total demand for end-item i through horizon
     d(i,t)   Demand for end-item i in period t
     r(i,j)   Amount of item i required to produce one unit of item j
     a(i,k)   Variable production time per unit of item i on machine k
     st(f,k)  Setup time of product family f on machine k
     c(k,t)   Production capacity on machine k in period t
     pf(i,f)  Diz se o produto i pertence a familha f;
     ub(i,t)  Uper bound da quantia do produto i que pode ser produzida no periodo t.
</pre>
* @author marcio
 */
public class MLCLSPwBInstance extends Instance{
    public File file;
    
    public int NI;
    public int endp;
    public int NK;
    public int NF;
    public int NT;
    public double BC;

    public double hc[];
    public double d[][];

    public double r[][];
    public double a[][];
    public double st[][];

    public double c[][];
    public double pf[][];

    public double ub[][];

    /**Conjunto de todos os produtos pertencentes a maquina k*/
    public int Mk[][];
    /**Conjunto de todos os produtos pertencentes a familia f*/
    public int Pf[][];
    /**Conjunto de todas as familias pertencentes a maquina k*/
    public int Fk[][];

    /**Familia na qual o produto i pertence*/
    public int Fi[];
    /**Maquina na qual o produto i pertence*/
    public int Mi[];

    @Override
    public String name() {
        return "MLCLSPwB-Instance";
    }
    @Override
    public void parameters(LinkerParameters link) throws Exception {
        file = link.File("Instance for MLCLSPwB", null, "txt");
    }

    @Override
    public void load() throws Exception {
        Scanner input = new Scanner(file);
        
        NI =    uTxtIO.ReadInt(input);
        endp =  uTxtIO.ReadInt(input);
        NT =    uTxtIO.ReadInt(input);
        NK =    uTxtIO.ReadInt(input);
        NF =    uTxtIO.ReadInt(input);

        BC =    uTxtIO.ReadDouble(input);
        hc =    uTxtIO.ReadVectorDouble(input, NI);
        
        d =     uTxtIO.ReadMatrixDouble(input, NI, NT);
        a =     uTxtIO.ReadMatrixDouble(input, NI, NK);
        c =     uTxtIO.ReadMatrixDouble(input, NK, NT);
        st =    uTxtIO.ReadMatrixDouble(input, NF, NK);
        r =     uTxtIO.ReadMatrixDouble(input, NI, NI);
        pf =    uTxtIO.ReadMatrixDouble(input, NI, NF);

        ub =    uTxtIO.ReadMatrixDouble(input, NI, NT);
        //ub = CalcUB(false);
//        System.out.println("-------------------------A---------------------------");
//        for(int i=0; i<NI; i++){
//            for(int t=0; t<NT; t++){
//                System.out.printf("%d\t", (int)ub[i][t]);
//            }
//            System.out.println();
//        }
//        System.out.println("-------------------------B---------------------------");
//        double ub2[][] = CalcUB(true);
//        for(int i=0; i<NI; i++){
//            for(int t=0; t<NT; t++){
//                System.out.printf("%d\t", (int)(ub[i][t]-ub2[i][t]));
//            }
//            System.out.println();
//        }
//        System.out.println("-------------------------C---------------------------");
//        double ub3[][]  = CalcUB(false);
//        for(int i=0; i<NI; i++){
//            for(int t=0; t<NT; t++){
//                System.out.printf("%d\t", (int)(ub[i][t]-ub3[i][t]));
//            }
//            System.out.println();
//        }
//        ub = ub3;
        
    }
    private double[][] CalcUB(boolean one) {
        double UB[][] = new double[NI][NT];
        for(int j=0; j<NI; j++){
            for(int t=0; t<NT; t++){
                UB[j][t] = one ? Cap(j,t) : Math.min( Cap(j,t), calcDjt(j,t) );
                //UB[i][t] = (long)(UB[i][t] + 0.999999);
            }
        }
        return UB;
    }
    private double calcDjt(int j, int t){
        double sum = 0;
        for(int s=0; s<NT; s++){
            sum += d[j][s];
        }
        for(int k=0; k<NI; k++){
            if(r[j][k]>0.001){
                sum += r[j][k] * calcDjt(k, t);
            }
        }
        return sum;
    }
    /**Maximo do produto i no perioto t que pode ser produzido sem ultrapassar a capacidade*/
    private double Cap(int i, int t){
        double min = Double.MAX_VALUE;
        for(int k=0; k<NK; k++){
            for(int f=0; f<NF; f++){
                if(a[i][k]>0.001){
                    min = Math.min(min, (c[k][t] - st[f][k] * pf[i][f])/a[i][k]);
                }
            }
        }
        return min;
    }
}
