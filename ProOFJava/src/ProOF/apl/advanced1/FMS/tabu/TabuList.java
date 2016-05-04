/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ProOF.apl.advanced1.FMS.tabu;

import ProOF.com.Linker.LinkerApproaches;
import ProOF.com.Linker.LinkerParameters;
import ProOF.gen.operator.LocalMove;
import ProOF.gen.operator.oLocalMove;
import java.util.LinkedList;

/**
 *
 * @author marcio
 */
public abstract class TabuList extends Tabu{
    private LocalMove moves;
    private LinkedList<oLocalMove> tabuList;
    private LinkedList<oLocalMove> movList;
    
    protected abstract int tabu_size();
    
    @Override
    public void services(LinkerApproaches link) throws Exception {
        moves = link.need(LocalMove.class, moves);
    }
    @Override
    public void start() throws Exception {
        super.start();
        tabuList = new LinkedList<oLocalMove>();
        movList  = new LinkedList<oLocalMove>(moves.list_moves());
    }
    public final int moves_size(){
        return moves.size();
    }
    @Override
    public oLocalMove select() throws Exception {
        return moves.select(movList);
    }
    @Override
    public void update(oLocalMove best_mov) {
//        System.out.println("tabu_size = "+tabu_size());
//        System.out.println("tabulist = "+tabuList);
//        System.out.println("movList  = "+movList);
        //------------------ update tabuList ----------------
        while (tabuList.size() >= tabu_size()){   //while don't have space on tabuList
            movList.addLast(tabuList.removeFirst());    //send back the oldest tabu movment to movList
        }
        tabuList.addLast(best_mov);     //add the best_mov on tabuList
        movList.remove(best_mov);       //remove it from movList
//        System.out.println("tabulist = "+tabuList);
//        System.out.println("movList  = "+movList);
    }
}
