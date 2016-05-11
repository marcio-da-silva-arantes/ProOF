/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ProOF.apl.factorys;

import ProOF.apl.advanced1.method.Tabler;
import ProOF.apl.sample1.method.GeneticAlgorithm;
import ProOF.apl.sample1.method.RandomAlgorithm;
import ProOF.com.language.Factory;
import ProOF.com.language.Run;
import ProOF.CplexOpt.CplexModel;
import ProOF.apl.advanced1.method.ACO;
import ProOF.apl.advanced1.method.CLONALG;
import ProOF.apl.advanced1.method.GRASP;
import ProOF.apl.advanced1.method.LocalSearch;
import ProOF.apl.advanced1.method.MPGA;
import ProOF.apl.advanced1.method.MultiStart;
import ProOF.apl.sample1.method.BranchAndBound;
import ProOF.apl.sample1.method.GreedyAlgorithm;
import ProOF.apl.sample1.method.NSGAII;
import ProOF.apl.sample1.method.SimulatedAnnealing;

/**
 *
 * @author marcio
 */
public class fRun extends Factory<Run>{
    public static final fRun obj = new fRun();
    private fRun(){}
    
    @Override
    public String name() {
        return "Run";
    }
    @Override
    public Run build(int index) {
        switch(index){
            case 0: return new RandomAlgorithm();
            case 1: return new SimulatedAnnealing();
            
            case 2: return new LocalSearch(fStop.obj, fLocalImprovement.obj, fProblem.obj);
            case 3: return new MultiStart(fStop.obj, fLocalImprovement.obj, fProblem.obj);
            case 4: return new GRASP(fStop.obj, fLocalImprovement.obj, fProblem.obj);
            
            case 5: return new GeneticAlgorithm();
            case 6: return new CLONALG(fStop.obj, fAIS.obj, fLocalImprovement.obj, fProblem.obj);
            case 7: return new ACO(fStop.obj, fProblem.obj);
            case 8: return new MPGA(fStop.obj, fPopulation.obj, fLocalImprovement.obj, fProblem.obj);

            case 9: return new BranchAndBound();
            case 10: return new GreedyAlgorithm();
            
            case 11: return new NSGAII();
            case 12: return new Tabler();
        }
        return null;
    }
}
