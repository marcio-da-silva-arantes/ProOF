/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ProOF.apl.sample1.problem.NSP;

import java.util.Vector;

/**
 *
 * @author valdemar
 */
public class Schedule {
    public final int [] schedule;
    
    public Schedule(int days){   
        schedule = new int[days];
        for(int i=0;i< schedule.length;i++)
        {
            schedule[i]=-3;
        }
    }
    
    public void assignShift(int day, int shift){
        schedule[day] = shift;
    }
    
}
