/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ProOF.apl.sample1.problem.NSP;

import ProOF.com.Linker.LinkerParameters;
import ProOF.opt.abst.problem.Instance;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Vector;

/**
 *
 * @author valdemar
 */
public class AlternativeInstance extends Instance{
    
    private File file;    
    private Scenario objScenario;
    private Duty objDuty;
    private Physician objPhysician;
    private Contracts objContracts;
    private Requirements objRequirements; 
    
    private Map<String, Map< String,String>> days_Off;
    private Map<String, Map< String,String>> off_reqs;
    
    
    //the necessary structures 
    
    
    public AlternativeInstance() {
        
      //  nrDays = 0;
    }

    
    
    @Override
    public String name() {
        return "Instance-NSP";
    }

    @Override
    public void parameters(LinkerParameters link)  throws Exception {
        setFile(link.File("Instances for ", null, "txt"));
    }

    @Override
    public void load() throws FileNotFoundException {
       
     //   FileReader fs = new FileReader(getFile());
     //   FileReader fs = new FileReader(new File("instance6.txt"));
     
     
     FileReader fs1 = new FileReader("/home/claudio/NetBeansProjects/ProOF-master/Mini-courses/Example/src/testecplex/data/p050_inst_01/scenario.txt");
        
        try{
                BufferedReader br1 = new BufferedReader(fs1);

                String line  = null;
                line = br1.readLine();

                line = br1.readLine();
                line = br1.readLine();
                objScenario = new Scenario();
                if(line.contains("WEEKS"));
                {
                    while(!line.isEmpty()){  
                        String [] splitter = line.split("[,|=]");
                        int e = 0;
                        e++;
                        objScenario.setWeeks(Integer.parseInt(splitter[e]));
                        line = br1.readLine();
                    }
                }


                line = br1.readLine();
                if(line.contains("SKILLS"));
                {
                    while(!line.isEmpty()){  
                        String [] splitter = line.split("[,|=]");
                        int e = 0;
                        e++;
                        objScenario.setNrOfSkills(Integer.parseInt(splitter[e]));
                        while(line != null &&  !line.isEmpty()){  
                            objScenario.getSkills().add(line);
                            line = br1.readLine();
                        }
                    }
                }

                
                if(line.contains("SHIFT_TYPES"));
                {  
                    line = br1.readLine();
                    line = br1.readLine();
                    while(line != null &&  !line.isEmpty()){  

                        String [] str = line.split("[ (,)]");
                        int e1 = 0;
                        Duty newDuty = new Duty();
                        
                        newDuty.setName(str[e1]);
                        
                        e1+=2;
                        int minConsec = Integer.parseInt(str[e1]);
                        e1++;
                        int maxConsec = Integer.parseInt(str[e1]);
                        line = br1.readLine();
                        newDuty.getConsecLimit().put(minConsec, maxConsec);
                        objDuty.getConsecLimit().put(minConsec, maxConsec);
                        
                        objScenario.getDuties().add(newDuty);
                    }
                }

                
                line = br1.readLine();
                if(line.contains("FORBIDDEN_SHIFT_TYPES_SUCCESSIONS"));
                {
                    line = br1.readLine();
                    while(line != null &&  !line.isEmpty()){  
                        String [] str = line.split("[ ,]");
                        int e1 = 0;
                        
                        Duty auxDuty = objScenario.getDutyByID(str[e1]);
                        //System.out.print(str[e1]+": ");
                        e1++;

                        int value = Integer.parseInt(str[e1]);
                        
                        auxDuty.setNumberOfForbidden(value);
                        while(value>0){
                            e1++;
                            auxDuty.getForbiddenShifts().add(str[e1]);
                            value--;
                        }
                        int indexOf = objScenario.getDuties().indexOf(auxDuty);
                        objScenario.getDuties().set(indexOf, auxDuty);
                        
                        line = br1.readLine();
                    }
                }

                line = br1.readLine();
                objContracts = new Contracts();
                if(line.contains("CONTRACTS"));
                {

                    line = br1.readLine();
                    while(line != null &&  !line.isEmpty()){  

                        String [] str = line.split("[ (,)]");
                        int e1 = 0;
                        
                        objContracts.setType(str[e1]);
                        
                        e1+=2;
                        int minAssignments = Integer.parseInt(str[e1]);
                        
                        e1++;
                        int maxAssignments = Integer.parseInt(str[e1]);
                        objContracts.getTotalAssignments().put(minAssignments,maxAssignments);
                        
                        e1+=3;
                        int minConsec = Integer.parseInt(str[e1]);
                        //System.out.print(str[e1]+" "); 
                        e1++;
                        int maxConsec = Integer.parseInt(str[e1]);
                        objContracts.getConsecDaysOff().put(minConsec, maxConsec);
                        //System.out.print(str[e1]+" ");
                        e1+=3;
                        
                        int minDaysOff = Integer.parseInt(str[e1]);
                        e1++;
                        int maxDaysOff = Integer.parseInt(str[e1]);
                        
                        objContracts.getConsecWorkDays().put(minDaysOff, maxDaysOff);
                        
                        e1+=2;
                        
                        objContracts.setMaxWeekends(Integer.parseInt(str[e1]));
                        e1++;
                        boolean value = str[e1].equalsIgnoreCase("1") ? true: false;
                        objContracts.setFullWeekend(value);
                        
                        line = br1.readLine();
                    }
                }
                
                line = br1.readLine();

                line = br1.readLine();
                line = br1.readLine();

                objPhysician = new Physician();
                if(line.contains("NURSES"));
                {
                    String [] str1 = line.split("[=]");
                    int e=0;
                    objScenario.setNrNurses(Integer.parseInt(str1[++e]));
 
                    line = br1.readLine();
                    while(line != null &&  !line.isEmpty()){  

                        String [] str = line.split("[ ]");
                        int e1 = 0;
                        objPhysician.setName(str[e1]);
                        
                        e1++;
                        objPhysician.setContract(str[e1]);
                        e1++;
                        int value = Integer.parseInt(str[e1]);
                        objPhysician.setNrSkill(value);
                        while(value>0){
                            e1++;
                            objPhysician.getSkills().add(str[e1]);
                            value--;
                        }
                        line = br1.readLine();
                    }  
                }
                
            }catch (IOException e) {
            e.printStackTrace();}
            finally {

                try {
                        fs1.close();
                    }catch (IOException ex) {
                                ex.printStackTrace();
                    }
        }
     
     
        FileReader fs = new FileReader(file);
        System.out.println(" "+file.getPath());
            
        try{
                BufferedReader br = new BufferedReader(fs);
	       
                String line  = null;
                line = br.readLine();
                
                line = br.readLine();
                line = br.readLine();
                line = br.readLine();
                
                objRequirements = new Requirements();
                if(line.contains("REQUIREMENTS"));
                {
                    while(!(line = br.readLine()).isEmpty()){  
                        String [] splitter = line.split("[ (),]");
                        int e = 0;
                        objRequirements.setShift(splitter[e]);
                        e++;
                        objRequirements.setSkill(splitter[e]);
                        //System.out.print(splitter[e]+" ");
                        e+=2;
                        int minDem = Integer.parseInt(splitter[e]);
//                        System.out.print(splitter[e]+"-");
                        e++;
                        int maxDem = Integer.parseInt(splitter[e]);
                        
//                        System.out.print(splitter[e]+"|");
                        HashMap map = new HashMap();
                        map.put(minDem, maxDem);
                        objRequirements.getDemands().put(0, map);
                        
                        int i=1;
                        while(i<7){
                            
                            e+=3;
                            minDem = Integer.parseInt(splitter[e]);
                            e++;
                            maxDem = Integer.parseInt(splitter[e]);
                            map.put(minDem, maxDem);
                            objRequirements.getDemands().put(i, map);
                            i++;
                            
                        }
                    }
                }
                
                
                days_Off =  new HashMap<>();
                line = br.readLine();
                if(line.contains("SHIFT_NOT_AVAILABLE"));
                {
                    while(!(line = br.readLine()).isEmpty()){  
                        String [] splitter = line.split("[ ]");
                        int e = 0;
                        
                        
                        HashMap<String, String> m = new HashMap<>();
                        String physicianID = splitter[e];
                        e++;
                        String shiftID = splitter[e];
                        e++;
                        String dayID = splitter[e];
                        m.put(shiftID,dayID);
                        
                        if(!days_Off.containsKey(physicianID)){
                            days_Off.put(physicianID, m);
                        
                        }
                        else{
                            days_Off.get(physicianID).put(shiftID, dayID);
                        }
                    }
                }
                
                
                line = br.readLine();
                if(line.contains("SHIFT_OFF_REQUESTS"));
                { line = br.readLine();
                    while(line != null &&  !line.isEmpty()){   
                        
                        String [] splitter = line.split("[ ]");
                        int e = 0;
                        
                        
                        HashMap<String, String> m = new HashMap<>();
                        String physicianID = splitter[e];
                        e++;
                        String shiftID = splitter[e];
                        e++;
                        String dayID = splitter[e];
                        m.put(shiftID,dayID);
                        
                        if(!off_reqs.containsKey(physicianID)){
                            off_reqs.put(physicianID, m);
                        
                        }
                        else{
                            off_reqs.get(physicianID).put(shiftID, dayID);
                        }
                    }
                }
        }catch (IOException e) {
        	e.printStackTrace();
            }   
            finally {

                        try {
                                fs.close();
                            }catch (IOException ex) {
                                ex.printStackTrace();
                            }
                    }
    }

    /**
     * @return the file
     */
    public File getFile() {
        return file;
    }

    /**
     * @param file the file to set
     */
    public void setFile(File file) {
        this.file = file;
    }

    public int getNOE()
    {
        return this.objScenario.getNrNurses();
    }
    
    public int getNOD()
    {
        return this.objScenario.getWeeks()*7;
    }
    
    
    public int getNOS()
    {
        return this.objScenario.getDuties().size();
    
    }
   
    
    public int getWeeks()
    {
        return this.objScenario.getWeeks();
    }
    
    public int getMonths()
    {
        return this.objScenario.getWeeks()/4;
    }
    
}
