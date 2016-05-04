package ProOF.apl.advanced1.method;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import ProOF.apl.advanced1.FMS.local_search.LocalImprovement;
import ProOF.apl.advanced1.FMS.population.Population;
import ProOF.com.Linker.LinkerParameters;
import ProOF.com.Linker.LinkerApproaches;
import ProOF.com.Linker.LinkerResults;
import ProOF.com.language.Factory;
import ProOF.opt.abst.run.MetaHeuristic;
import ProOF.opt.abst.problem.meta.Problem;
import ProOF.gen.stopping.Stop;

/**
 *
 * @author marcio
 */
public class MPGA extends MetaHeuristic{
    private Problem problem;
    
    private Stop stop;
    private int nPops;
    private int nInds;
    
    private Population base;
    private Population pops[];
    
    private LocalImprovement local_improvement;
    
    private final Factory fStop;
    private final Factory fPopulation;
    private final Factory fLocalImprovement;
    private final Factory fProblem;

    public MPGA(Factory fStop, Factory fPopulation, Factory fLocalImprovement, Factory fProblem) {
        this.fStop = fStop;
        this.fPopulation = fPopulation;
        this.fLocalImprovement = fLocalImprovement;
        this.fProblem = fProblem;
    }
    
    @Override
    public String name() {
        return "MPGA";
    }
    @Override
    public void parameters(LinkerParameters link) throws Exception {
        nPops   = link.Int("number of populations", 3,  1,  100);
        nInds   = link.Int("number of individuals", 20, 2,  10000);
    }
    @Override
    public void services(LinkerApproaches link) throws Exception {
        stop                = link.get(fStop, stop);
        base                = link.get(fPopulation, base);
        local_improvement   = link.get(fLocalImprovement, local_improvement);
        problem             = link.get(fProblem, problem);
    }
    public void duplicate() throws Exception {
        pops = new Population[nPops];
        int p;
        for(p=1; p<=nPops; p++){
            if((p*(nInds/nPops) + (nPops-p)*(nInds/nPops+1)) == nInds){
                break;
            }
        }
        for(int i=0; i<nPops; i++){
            if(i<p){
                pops[i] = base.Clone(problem, nInds/nPops, i, nPops);
            }else{
                pops[i] = base.Clone(problem, nInds/nPops+1, i, nPops);
            }
        }
    }
    private long generations;
    @Override
    public void execute() throws Exception {
        duplicate();

        //Inicia, avalia e organiza as populações
        for(Population p : pops){
            p.initialize();
            p.evaluate();
            p.organize();
        }
        generations = 1;
        while(!stop.end()){
            //Evolui as populações até a convergência
            for(Population p : pops){
                p.evolution();
            }
            generations++;
            
            //Busca local sob a melhor solução
            Population pop = BestInd(pops);
            local_improvement.execute(problem, pop.best());
            pop.organize();
            
            //Reinicia as populações mantendo o melhor
            for(Population p : pops){
                p.reinitialize();
            }
            //Efetua as migrações
            if(pops.length>1){
                for(int i=1; i<pops.length+1; i++){
                    pops[i%pops.length].migrate(pops[i-1].best());
                }
            }
            pop.organize();
        }
    }

    @Override
    public void results(LinkerResults win) throws Exception {
        super.results(win);
        win.writeLong("generations", generations);
    }
    
    

    private Population BestInd(Population[] pops) throws Exception {
        Population p = pops[0];
        for(int i=1; i<pops.length; i++){
            if(pops[i].best().LT(p.best())){
                p = pops[i];
            }
        }
        return p;
    }
}
