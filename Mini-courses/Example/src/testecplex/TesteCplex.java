package testecplex;

import ilog.concert.IloException;
import ilog.concert.IloNumExpr;
import ilog.concert.IloNumVar;
import ilog.concert.IloNumVarType;
import ilog.cplex.IloCplex;

/**
 * <pre>
 * Example of Linear Programming (LP) problem solved by cplex. 
 * 
 * maximize 0.5*x1 + 4*x2 +7*x3
 * subject to:
 *          7*x1 – 3*x2 + 0.5*x3 ≥ 10
 *                 2*x2 + 5.0*x3 ≤ 80
 *          
 *          0 ≤ x1 ≤ 5; x2, x3 ≥ 0
 * 
 * </pre>
 * @author marcio
 */
public class TesteCplex {

    

    public static void main(String[] args) throws IloException {
        //Define cplex reponsavel por criar variáveis e seus limites, definir restrições, parâmetros e resolver o modelo. 
        IloCplex cplex = new IloCplex();
        
        //Definindo as 3 variáveis do problema no intervalo de 0 até infinito e do tipo Float
        IloNumVar x[] = cplex.numVarArray(3, 0, Double.POSITIVE_INFINITY, IloNumVarType.Float);
        
        //Mundando limite superior (ub - upper bound) da variável x1 como 5
        x[0].setUB(5);
        
        //Definindo expressão para a função objetivo
        IloNumExpr objective = cplex.prod(0.5, x[0]);
        objective = cplex.sum(objective, cplex.prod(4, x[1])); 
        objective = cplex.sum(objective, cplex.prod(7, x[2])); 
        //Adiciona ao cplex a expressão do objetivo como problema de maximização
        cplex.addMaximize(objective);
        
        //Definnindo a expressão para a primeira restrição do problema
        IloNumExpr restriction1 = cplex.prod(7, x[0]);
        restriction1 = cplex.sum(restriction1, cplex.prod(-3, x[1])); 
        restriction1 = cplex.sum(restriction1, cplex.prod(0.5, x[2]));
        //Adiciona ao cplex a restrição como maior ou igual a 10
        cplex.addGe(restriction1, 10);
        
        //Definnindo a expressão para a segunda restrição do problema
        IloNumExpr restriction2 = cplex.prod(2, x[1]);
        restriction2 = cplex.sum(restriction2, cplex.prod(5, x[2])); 
        //Adiciona ao cplex a restrição como menor ou igual a 80
        cplex.addLe(restriction2, 80);
        
        cplex.exportModel("model.lp"); 
        
        if(cplex.solve()){
            System.out.println("O modelo possui solução");
            System.out.println("Status: "+cplex.getStatus());
            
            double obj = cplex.getObjValue();
            System.out.println("objetivo = "+obj);
            
            double sol[] = cplex.getValues(x);
            System.out.println("x1 = "+sol[0]);
            System.out.println("x2 = "+sol[1]);
            System.out.println("x2 = "+sol[2]);
            
        }else{
            System.out.println("O modelo não possui solução");
            System.out.println("Status: "+cplex.getStatus());
        }
    }
    
}
