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
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;

/**
 *
 * @author dexter
 */
    public class NSPInstance extends Instance {

    private int horizon; //number of days
    private Vector<Shift> shifts; //number of shifts 
    private Vector<Staff> staff;
    private Vector<Cover> cv;
    
    public File file;          //Parameter: data file problem

    NSPInstance inst;
    
    public Staff getByID(String ID)
    {
        for(Staff a: staff)
        {
            if(a.getID().equals(ID))
                return a;
        }
        return new Staff();
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
    
    public Cover getByDay(int day)
    { 
        for(Cover s:cv)
        {
            if(s.getDay()==day)
                return s;
        }
        return new Cover();
    }
    
    public NSPInstance(){
    }

    public int getBestBound(){
        return 0;
    }
    
    public Vector<Shift> getShifts() {
        return shifts;
    }

    public void setShifts(Vector<Shift> shifts) {
        this.shifts = shifts;
    }

    public Vector<Staff> getStaff() {
        return staff;
    }

    public void setStaff(Vector<Staff> staff) {
        this.staff = staff;
    }

    public Vector<Cover> getCv() {
        return cv;
    }

    public void setCv(Vector<Cover> cv) {
        this.cv = cv;
    }
    
    public int[] dias(int mes){
        int dias[] = new int[getMonthLenght()];
        for(int i=0; i<dias.length; i++){
            dias[i] = getMonthLenght()*mes + i;
        }
        return dias;
    }

    public int[] diasSemana(int semana){
        int dias[] = new int[getWeekLenght()];
        for(int i=0; i<dias.length; i++){
            dias[i] = getWeekLenght()*semana + i;
        }
        return dias;
    }
    
    public int getMonths(){
        if(getHorizon()<28)
            return 1;
        else 
            return (int) getHorizon()/getMonthLenght();
    }
    
    public int begining(int m) {
	return m*getMonthLenght();
    }
    
    public int ending(int m) {
	return (m+1)*getMonthLenght();
    }
   
        
    public int getMonthLenght() {
        if(getHorizon()<28)
            return getHorizon();
        else 
            return 28;
    }
    
    
    /**
     @return number of weeks*/    
    public int getWeeks()
    {
        return (int) getHorizon()/getWeekLenght();
    }
    
    
    /**@return week length */
    public int getWeekLenght()
    {
        return 7;
    }
    
    
    public int beginingW(int w) {
	return w*getWeekLenght();
    }
    
    public int endingW(int w) {
	return (w+1)*getWeekLenght();
    }
   
    
    public String [] getDistinctShifts() {
        
        String[] evidenceColumn = new String[shifts.size()];
        
        for(int i=0;i<shifts.size();i++)
        {   
            evidenceColumn[i] = shifts.get(i).getID()+"";
        }
       
        List<String> evidenceList = Arrays.asList(evidenceColumn);
        
        Set<String> evidenceTreesetList = new TreeSet<String>();
        evidenceTreesetList.addAll(evidenceList);
        String [] distinctEvidence = (String[]) evidenceTreesetList.toArray(new String[0]);
               
        return distinctEvidence;
    }
    
    public LinkedList<String> getShiftOfCategory(int category) {
        LinkedList<String> sfts = new LinkedList<>();
         
        for(Shift s:shifts)
        {   
            if(s.getDuration() == category)
            {   
                sfts.addLast(s.getID());
            }
        }
        
        
        return sfts;
    }
    
    public int [] getShiftsByDuration() {
        
        String[] evidenceColumn = new String[shifts.size()];
        
        for(int i=0;i<shifts.size();i++)
        {   
            evidenceColumn[i] = shifts.get(i).getDuration()+"";
        }
       
        List<String> evidenceList = Arrays.asList(evidenceColumn);
        
        Set<String> evidenceTreesetList = new TreeSet<String>();
        evidenceTreesetList.addAll(evidenceList);
        String [] distinctEvidence =   (String[]) evidenceTreesetList.toArray(new String[0]);
        int [] classes = new int[distinctEvidence.length];
        
        for(int i=0;i<classes.length;i++){
            classes[i] = Integer.parseInt(distinctEvidence[i]);
        }   
        
        return classes;
    }
    
    public int getMaxOfCategory(int cat, int sft){
        int max = 0;
        
        for(Staff st: getNursesByCategory(cat))
        {    
            System.out.println(" "+sft);
            for(String s: inst.getShiftOfCategory(sft))
            {   
                if(st.getMaxShifts().get(inst.getShiftIndexByID(s)) > max)
                {   
                    max = st.getMaxShifts().get(inst.getShiftIndexByID(s));
                }
            }
        }
    
        return max;
    }
    
    
    public int getMinOfCategory(int cat, int sft){
        int min = 100000;
        
        for(Staff st: getNursesByCategory(cat))
        {   
            System.out.println(" "+inst.getShiftOfCategory(sft).size());
            for(String s: inst.getShiftOfCategory(sft))
            { 
                if(st.getMaxShifts().get(inst.getShiftIndexByID(s)) < min)
                {   
                    min = st.getMaxShifts().get(inst.getShiftIndexByID(s));
                }
            }  
        }
        
        return min;
    }
    
    public int [] getCategory() {
        
        String[] evidenceColumn = new String[staff.size()];
        
        for(int i=0;i<staff.size();i++)
        {   
            evidenceColumn[i] = staff.get(i).getMaxTotalMinutes()+"";
        }
       
        List<String> evidenceList = Arrays.asList(evidenceColumn);
        
        Set<String> evidenceTreesetList = new TreeSet<String>();
        evidenceTreesetList.addAll(evidenceList);
        String [] distinctEvidence =   (String[]) evidenceTreesetList.toArray(new String[0]);
        int [] classes = new int[distinctEvidence.length];
        
        for(int i=0;i<classes.length;i++){
            classes[i] = Integer.parseInt(distinctEvidence[i]);
        }   
        
        return classes;
    }

    public int longestShift()
    {
        int max = 0;
        for(Shift s: getShifts()){
            if(s.getDuration() > max){
             max = s.getDuration();
            }
        }
        return max;
    }
    
    public int getPhysicianOfFirstCat() {
        int [] aux = getCategory();
        
        int count  =0;
        
        for(int i=0;i< staff.size();i++){
            if(aux[getCategory().length-1] == staff.get(i).getMaxTotalMinutes()){
                ++count;
            }
        }
         
        return count;
    }

    
    /**
     @return number of days*/    
    public int getHorizon() {
        return horizon;
    }

    
    /**
     @return total number of shifts, including duties*/    
    public int getNoS() {
        return shifts.size();
    }

    /**
     @return number of physicians*/    
    public int getNoN() {
        return staff.size();
    }
    
    public LinkedList<Staff> getNursesByCategory(int category)
    {
        LinkedList<Staff> nurses = new LinkedList<>();
        
        for(Staff st:staff)
        {
            if(category == st.getMaxTotalMinutes())
                nurses.add(st);
            
        }
        return nurses;
    }
    
    public LinkedList<Integer> getNursesOfCategory(int category)
    {
        LinkedList<Integer> nurses = new LinkedList<>();
        Integer p = 0;
        
        for(Staff st:staff)
        {    if(category == st.getMaxTotalMinutes()){
                nurses.add(p);
            
            }
            p++;           
        }
        return nurses;
    }
    
    protected LinkedList<Integer> getUnavailableDays(Staff staff) {
        LinkedList<Integer> days = new LinkedList();
        for(int d=0; d<getHorizon(); d++){
            if(staff.isUnavailable(d) )
            {
                days.add(d);
            }
            
        }
        return days;
    }
    
    protected LinkedList<Integer> getAllDays() {
        LinkedList<Integer> days = new LinkedList();
        for(int d=0; d<getHorizon(); d++){
            days.add(d);
        }
        return days;
    }
    /**
     @return total ID of the shift*/    
    public String getShiftByIndex(int shift) {
       
        return shifts.get(shift).getID();
    }
    
    /**
     @return total ID of the shift*/    
    public int getShiftIndexByID(String ID) {
        return shifts.indexOf(getShiftByID(ID));
    }
    
    public LinkedList<String> listOfConflicts(int shiftIndex) {
        Shift aux = getShifts().get(shiftIndex);
        LinkedList<String> shifts = new LinkedList();
        for(String i: aux.getForbiddenShifts()){
            shifts.addLast(i);
        }
        
        return shifts;
    }
    public LinkedList<Integer> shiftWithSuccessors() {
        LinkedList<Integer> indexes = new LinkedList();
        for(Shift a:shifts){
            if(!a.getForbiddenShifts().isEmpty()){
                indexes.addLast(shifts.indexOf(a));
            }
        }
        return indexes;
    }
    
    /**
     @return total number of shifts, including duties*/  
    public boolean shiftHasSuccessors(int shiftIndex){
        return shifts.get(shiftIndex).getForbiddenShifts().isEmpty();
    }
    
    
    public int sizeOfSuccessors(int shiftIndex){
        return shifts.get(shiftIndex).getForbiddenShifts().size();
    
    }
    
    /**@param physician
     @return number of maximum consecutive days by physician */
    public int getMaxConsecutiveShiftsByPhysician(int phy) {
        return staff.get(phy).getMaxConsecutiveShifts();
    }
    
    /** @param day
     ** @param shift
    @return number minimum demand of a day and shift. */
    public int getMinDemand(int day, int shift) {
        //(int) Math.random()*
        Cover cover = getByDay(day);
        
        return cover.getDemands().get(shift); 
    }
    
    /** @param day
     ** @param shift
    @return number maximum demand of a day and shift. */
    public int getMaxDemand(int day, int shift) {
       
        Cover cover = getByDay(day);
        return cover.getDemands().get(shift);
    }
    
    public void setNumberOfDays(int d) {
        this.horizon = d;
    }


    public void setS(Vector<Shift> s) {
        this.shifts = s;
    }
    
    
    protected LinkedList<Integer> getAllPhysicians() {
        LinkedList<Integer> doctors = new LinkedList();
        
        for(int p = 0; p < this.getNoN();p++){
            doctors.addLast(p);
        }
        
        return doctors;
    }
    
        
    @Override
    public String name() {
        return "Instance-NSP";
    }

    @Override
    public void parameters(LinkerParameters link) throws Exception {
        file = link.File("Instances for ", null, "txt");
    }

    @Override
    public void load() throws FileNotFoundException {
        
        FileReader fs = new FileReader(file);
            try{
                
                BufferedReader br = new BufferedReader(fs);
               
                String line  = null;
                line = br.readLine();
                
                if(line.equalsIgnoreCase("SECTION_HORIZON"));
                {   line = br.readLine();
                    setNumberOfDays(Integer.parseInt(line));
                }
                
                shifts = new Vector<Shift>();
                line = br.readLine();
                line = br.readLine();
                
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
                staff = new Vector<Staff>();
                if(line.equalsIgnoreCase("SECTION_STAFF"));
                {   while(!(line = br.readLine()).isEmpty()){
                        String [] splitter = line.split("[,|=]");
                        Staff st = new Staff();
                    
                        int e = 0;
                        st.setID(splitter[e]);
                        Vector<Integer> vec = new Vector<>(shifts.size());
                        
                        for(int i = 0; i< shifts.size();i++){
                            e+=2;
                            vec.add(Integer.parseInt(splitter[e]));
                        }
                        st.setMaxShifts(vec);
                        st.setMaxTotalMinutes(Integer.parseInt(splitter[++e]));
                        st.setMinTotalMinutes(Integer.parseInt(splitter[++e]));
                        st.setMaxConsecutiveShifts(Integer.parseInt(splitter[++e]));
                        st.setMinConsecutiveShifts(Integer.parseInt(splitter[++e]));
                        st.setMinConsecutiveDaysOff(Integer.parseInt(splitter[++e]));
                        st.setMaxWeekends(Integer.parseInt(splitter[++e]));
                        staff.add(st);
                    }
                }
                
                
                line = br.readLine();
                if(line.equalsIgnoreCase("SECTION_DAYS_OFF"));
                { 
                    while(!(line = br.readLine()).isEmpty()){
                        Staff st = new Staff();
                        String [] splitter = line.split("[,]");
                        int e = 0;
                        st = getByID(splitter[e]);   
                        
                        while((e+1)<splitter.length){
                            String str1 = splitter[++e];
                            st.getDaysOff().add(Integer.parseInt(str1));
                            
                        }
                        
                        staff.setElementAt(st, staff.indexOf(st));
                    }
                    
                }
                
                line = br.readLine();
                
                if(line.equalsIgnoreCase("SECTION_SHIFT_ON_REQUESTS"));
                { 
                    while(!(line = br.readLine()).isEmpty()){
                        Staff st = new Staff();
                        String [] splitter = line.split("[,]");
                        int e = 0;
                        st = getByID(splitter[e]);   
                        String str1 = splitter[++e];
                        
                        
                        Map <Integer, String> map = new HashMap();
                        String val = splitter[++e];
                        map.put(Integer.parseInt(str1), val);
                        st.getReqShift().add(map);
                        
                        Map <String,Integer >  wei= new HashMap <String, Integer>();
                        wei.put(val, Integer.parseInt(splitter[++e]));
                        st.getReqWeights().add(wei);
                                                
                        staff.setElementAt(st, staff.indexOf(st));
                    }
                }
                
                line = br.readLine();
                if(line.equalsIgnoreCase("SECTION_SHIFT_OFF_REQUESTS"));
                { 
                    while(!(line = br.readLine()).isEmpty()){
                        Staff st = new Staff();
                        String [] splitter = line.split("[,]");
                        
                        int e = 0;
                        st = getByID(splitter[e]);   
                        String str1 = splitter[++e];                        
                        
                        Map <Integer, String>  map= new HashMap <Integer, String>();
                        e++;
                        
                        map.put(Integer.parseInt(str1), splitter[e]);
                        st.getUnavailability().add(map);
                        
                        Map <String,Integer >  wei= new HashMap <String, Integer>();
                        wei.put(splitter[e], Integer.parseInt(splitter[++e]));
                        st.getOffReqWeights().add(wei);
                        
                    /***    System.out.print(st.getID()+" shift off request {");
                        for(Map doff: st.getOffReqWeights())
                            System.out.print(map.toString() +" - "+doff.toString()+" ");
                        System.out.println("}");*/
                        staff.setElementAt(st, staff.indexOf(st));
                    }
                }  
                
                line = br.readLine();
                cv = new Vector<Cover>();
                for(int i = 0; i< horizon; i++)
                {    
                    Cover cover = new Cover();
                    cover.setDay(i);
                    cv.add(cover);
                }
                
                if(line.equalsIgnoreCase("SECTION_COVER"));
                { 
                //    System.out.println("Coverage ");
                    while((line = br.readLine()) != null && !line.isEmpty() && !line.equals("<TrueName>"))
                    {
                        String [] splitter = line.split("[,]");
                        int e = 0;
                        int day = Integer.parseInt(splitter[e]);

                        Cover cover = cv.get(day);
                        e+=2;
                        String a = splitter[e];
                        cover.getDemands().add(Integer.parseInt(a));
                        Map <Integer, int[][]>  wei= new HashMap <Integer, int[][]>();
                        int uWeights = Integer.parseInt(splitter[++e]);
                        int lWeights =Integer.parseInt(splitter[++e]);
                        wei.put(day, new int[][]{{uWeights},{lWeights}});
                        cover.getWeights().add(wei);
                  //      System.out.println(day+", "+ a+", "+uWeights+", "+lWeights);
                        
                        cv.add(cover);
                        
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
}