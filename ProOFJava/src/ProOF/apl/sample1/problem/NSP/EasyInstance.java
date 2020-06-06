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
public class EasyInstance extends Instance{
    
    private File file;    
    
    //the necessary structures 
    private Map<String, Map< Integer, Map<String, Integer>>> off_reqs;
    private Map<String, Map< Integer, Map<String, Integer>>> on_reqs;
    private Map<Integer, Map< String, Map<Integer, Map< Integer, Integer>>>> covers;
    private Map<String, Vector<Integer>> daysOff;
    private Vector<Employee> employees;
    private Vector<Shift> shifts; 
    private int nrDays;
    
    public EasyInstance() {
        
        nrDays = 0;
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
        FileReader fs = new FileReader(file);
        System.out.println(" "+file.getPath());
        try{
            BufferedReader br = new BufferedReader(fs);
               
                String line  = null;
                line = br.readLine();
                
                if(line.equalsIgnoreCase("SECTION_HORIZON"));
                {   
                    line = br.readLine();
                    setNrDays(Integer.parseInt(line));
                }
                
                line = br.readLine();
                line = br.readLine();
                
                shifts = new Vector<>();
                if(line.equalsIgnoreCase("SECTION_SHIFTS"))
                {  
                    while(!(line = br.readLine()).isEmpty()){
                        String [] splitter = line.split("[,|]");
                        
                        
                        Shift sf = new Shift();
                        int e = 0;
                        sf.setID(splitter[e]);
                        e++;
                        sf.setDuration(Integer.parseInt(splitter[e]));
                        
                        while((e+1)<splitter.length){
                            String str1 = splitter[++e];
                            sf.getForbiddenShifts().add(str1);
                        }
                        shifts.add(sf);
                    }
                }
        
                line = br.readLine();
                employees =  new Vector<>();
               if(line.equalsIgnoreCase("SECTION_STAFF"));
                {   while(!(line = br.readLine()).isEmpty()){
                        String [] splitter = line.split("[,|=]");
                        int e = 0;
                        
                        Employee employee = new Employee();
                        employee.setID(splitter[e]);
                        Vector<Integer> eShifts = new Vector<>(shifts.size());
                        
                        for(int i = 0; i< shifts.size();i++){
                            e+=2;
                            eShifts.add(Integer.parseInt(splitter[e]));
                        }
                        employee.setMaxShifts(eShifts);
                        employee.setMaxMinutes(Integer.parseInt(splitter[++e]));
                        employee.setMinMinutes(Integer.parseInt(splitter[++e]));
                        employee.setMaxCons(Integer.parseInt(splitter[++e]));
                        employee.setMinCons(Integer.parseInt(splitter[++e]));
                        employee.setMinDaysOff(Integer.parseInt(splitter[++e]));
                        employee.setMaxWeekends(Integer.parseInt(splitter[++e]));
                        employees.add(employee);
                    }
                }
                
                line = br.readLine();
                daysOff = new HashMap<>();
                if(line.equalsIgnoreCase("SECTION_DAYS_OFF"));
                { 
                    while(!(line = br.readLine()).isEmpty()){                        
                        String [] splitter = line.split("[,]");
                        int e = 0;
                        String physician = splitter[e];
                        Vector <Integer> days = new Vector<>();
                        
                        while((e+1)<splitter.length){
                            String str1 = splitter[++e];
                            days.add(Integer.parseInt(str1));                            
                        }
                        daysOff.put(physician, days);
                    }
                }
                
                line = br.readLine();
                on_reqs =  new HashMap<>();
                if(line.equalsIgnoreCase("SECTION_SHIFT_ON_REQUESTS"));
                { 
                       
                    while(!(line = br.readLine()).isEmpty()){
                         
                        String [] splitter = line.split("[,]");
                        int e = 0;
                        String cod = splitter[e];
                        Integer day = Integer.parseInt(splitter[++e]);
                        String sft = splitter[++e];
                        int weight = Integer.parseInt(splitter[++e]);
                        
                        if(!on_reqs.containsKey(cod)){
                            HashMap<String, Integer> m = new HashMap<>();
                            m.put(sft, weight);
                            HashMap<Integer, Map<String, Integer>> m2 = new HashMap<>();
                            m2.put(day, m);
                            on_reqs.put(cod, m2);
                        }
                        else{
                                on_reqs.get(cod);
                                if(!on_reqs.get(cod).containsKey(day))
                                {
                                    HashMap<String, Integer> m = new HashMap<>();
                                    m.put(sft, weight);
                                    on_reqs.get(cod).put(day, m);
                                }
                                else{
                                    on_reqs.get(cod).get(day).put(sft,weight);                
                                }
                        }
                    }
                    
                }
                
                line = br.readLine();
                off_reqs =  new HashMap<>();
                if(line.equalsIgnoreCase("SECTION_SHIFT_OFF_REQUESTS"));
                { 
                       
                    while(!(line = br.readLine()).isEmpty()){
                         
                        String [] splitter = line.split("[,]");
                        int e = 0;
                        String physicianID = splitter[e];
                        Integer day = Integer.parseInt(splitter[++e]);
                        String sftID = splitter[++e];
                        int weight = Integer.parseInt(splitter[++e]);
                        
                        if(!off_reqs.containsKey(physicianID)){
                            HashMap<String, Integer> m = new HashMap<>();
                            m.put(sftID, weight);
                            HashMap<Integer, Map<String, Integer>> m2 = new HashMap<>();
                            m2.put(day, m);
                            off_reqs.put(physicianID, m2);
                        }
                        else{
                                off_reqs.get(physicianID);
                                if(!off_reqs.get(physicianID).containsKey(day))
                                {
                                    HashMap<String, Integer> m = new HashMap<>();
                                    m.put(sftID, weight);
                                    off_reqs.get(physicianID).put(day, m);
                                }
                                else{
                                    off_reqs.get(physicianID).get(day).put(sftID,weight);                
                                }
                        }
                   //     System.out.print(cod+":"+off_reqs.get(cod)+"\n");
                    }
                    
                }
                
                
                line = br.readLine();
                covers =  new HashMap<>();
                if(line.equalsIgnoreCase("SECTION_COVER"));
                { 
                       
                    while(!(line = br.readLine()).isEmpty()){
                         
                        String [] splitter = line.split("[,]");
                        int e = 0;                        
                        int day = Integer.parseInt(splitter[e]);                        
                        String sft = splitter[++e];
                        int dem = Integer.parseInt(splitter[++e]);
                        int oCost = Integer.parseInt(splitter[++e]);
                        int uCost = Integer.parseInt(splitter[++e]);                        
                        //System.out.println(day+" "+sft+" "+dem);
                        if(!covers.containsKey(day)){
                            HashMap<Integer, Integer> m = new HashMap<>();
                            m.put(oCost, uCost);
                            HashMap<Integer, Map<Integer,Integer>> m2 = new HashMap<>();
                            m2.put(dem, m);
                            Map<String, Map<Integer, Map<Integer,Integer>>> m3 = new HashMap<>();
                            m3.put(sft, m2);
                            covers.put(day, m3);
                        }
                        else
                        {
                            covers.get(day);

                            if(!covers.get(day).containsKey(sft))
                            { 
                                HashMap<Integer, Integer> m = new HashMap<>();
                                m.put(oCost, uCost);
                                HashMap<Integer, Map<Integer,Integer>> m2 = new HashMap<>();
                                m2.put(dem, m);
                                
                                covers.get(day).put(sft, m2);
                            }
                            else{
                                covers.get(day).get(sft).put(dem, new HashMap<>(oCost,uCost));
                            }
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

    /**
     * @return the off_reqs
     */
    public Map<String, Map< Integer, Map<String, Integer>>> getOff_reqs() {
        return off_reqs;
    }

    /**
     * @param off_reqs the off_reqs to set
     */
    public void setOff_reqs(Map<String, Map< Integer, Map<String, Integer>>> off_reqs) {
        this.off_reqs = off_reqs;
    }

    /**
     * @return the on_reqs
     */
    public Map<String, Map< Integer, Map<String, Integer>>> getOn_reqs() {
        return on_reqs;
    }

    /**
     * @param on_reqs the on_reqs to set
     */
    public void setOn_reqs(Map<String, Map< Integer, Map<String, Integer>>> on_reqs) {
        this.on_reqs = on_reqs;
    }

    /**
     * @return the covers
     */
    public Map<Integer, Map< String, Map<Integer, Map< Integer, Integer>>>> getCovers() {
        return covers;
    }

    /**
     * @param covers the covers to set
     */
    public void setCovers(Map<Integer, Map< String, Map<Integer, Map< Integer, Integer>>>> covers) {
        this.covers = covers;
    }

    /**
     * @return the daysOff
     */
    public Map<String, Vector<Integer>> getDaysOff() {
        return daysOff;
    }

    /**
     * @param daysOff the daysOff to set
     */
    public void setDaysOff(Map<String, Vector<Integer>> daysOff) {
        this.daysOff = daysOff;
    }

    /**
     * @return the employees
     */
    public Vector<Employee> getEmployees() {
        return employees;
    }

    /**
     * @param employees the employees to set
     */
    public void setEmployees(Vector<Employee> employees) {
        this.employees = employees;
    }

    /**
     * @return number of employees
     */
    public int getNOE()
    {
        return employees.size();
    }
    
    /**
     * @return number of shifts
     */
    public int getNOS(){
        return shifts.size();
    }
    /**
     * @return the shifts
     */
    public Vector<Shift> getShifts() {
        return shifts;
    }

    /**
     * @param shifts the shifts to set
     */
    public  void setShifts(Vector<Shift> shifts) {
        this.shifts = shifts;
    }

    /**
     * @return the number of days
     */
    public int getNOD() {
        return nrDays;
    }

    /**
     * @param nrDays the nrDays to set
     */
    public void setNrDays(int nrDays) {
        this.nrDays = nrDays;
    }
    
    
    
    /**
     @return total ID of the shift*/    
    public int getShiftIndexByID(String ID) {
        return shifts.indexOf(getShiftByID(ID));
    }
    
    public Shift getShiftByID(String ID)
    {
        for(Shift a: shifts)
        {
            if(a.getID().equals(ID))
                return a;
        }
        return new Shift();
    }
    
    public LinkedList<Integer> getAvailable(cNSP codif, int d){
        LinkedList<Integer> list = new LinkedList<>();
        
        for(int p=0;p<getNOE();p++){
        
            if(codif.schedule[p][d]!=-1){
                list.addLast(p);
            }
        }
        return list;
    
    }
    
    int getOtherShift(int id) {
        int s;
        
        do{
            s = (int) (Math.random()*getNOS());
        }while(s==id);
        
        return s;
    }
    
    
    LinkedList<Integer> getWeekends(){
        LinkedList<Integer> list = new LinkedList<>();
        for(int i=5; i<getNOD();i+=7)
        {
            list.addLast(i);
            list.addLast(i+1);
           
        }
        return list;
    }
    
    LinkedList<Integer> getAvailableShifts(Employee emp){
        
        LinkedList<Integer> list = new LinkedList<>();
        
        for(Integer s: emp.getMaxShifts()){
            if(s > 0)
            {   
                list.addLast(emp.getMaxShifts().indexOf(s)+1);
            }
        }
        
        return list;
    }
    
    
    public int getWeeks()
    {
        return (int) getNOD()/getWLenght();
    }
    
    public int getWLenght()
    {
        return 7;
    }
    
    public int begining(int w) {
	return w*getWLenght();
    }
    
    public int ending(int w) {
	return (w+1)*getWLenght();
    }
   
}
