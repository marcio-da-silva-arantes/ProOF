/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tsp;

import java.io.File;
import java.util.Locale;
import java.util.Scanner;

/**
 *
 * @author marci
 */
public class TSP {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        Locale.setDefault(Locale.ENGLISH);
        
        //Fazendo leitura do arquivo
        Scanner scan = new Scanner(new File("./instances/A12.txt"));
        
        scan.nextLine(); // ignore this line
        String otimo = scan.nextLine();
        
        scan.nextLine(); // ignore this line
        int N = scan.nextInt();
        scan.nextLine(); // ignore \n
        
        scan.nextLine(); // ignore this line
        double C[][] = new double[N][N];
        for(int i=0; i<N; i++){
            for(int j=0; j<N; j++){
                C[i][j] = scan.nextDouble();
            }
        }
        scan.close();
        
        
        System.out.println("==========[imprimindo dados]========");
        System.out.println("valor ótimo conhecido = "+otimo);
        System.out.println("cidades = "+N);
        for(int i=0; i<N; i++){
            for(int j=0; j<N; j++){
                System.out.printf("%10.2g ", C[i][j]);
            }
            System.out.println();
        }
        
        //TODO faça o modelo para resolver o problema a partir daqui
        
    }
}
