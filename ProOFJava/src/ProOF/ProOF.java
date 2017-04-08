/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ProOF;

import ProOF.apl.factorys.fRun;
import ProOF.com.language.Approach;
import ProOF.com.model.Model;
import ProOF.com.runner.Runner;
import ProOF.opt.abst.problem.meta.Best;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Locale;


/**
 *
 * @author marcio
 */
public class ProOF { 
    /**
     * @param args the command line arguments
     * @throws java.io.FileNotFoundException
     */
    public static void main(String[] args) throws FileNotFoundException, Exception {
        Locale.setDefault(Locale.ENGLISH);
        boolean local = false;
        if (args == null || args.length == 0) {
            local = true;
            args = find_args("../work_space/", "task");
            //args = new String[]{"run", "../work_space/job_local/waiting/task", "../work_space/input/"};
        }
        try{
            starting(args, local);
        }catch(Throwable ex){
            ex.printStackTrace(System.err);
            
            File file;
            if(Approach.job==null){
                file = new File("log_error.err");
            }else{
                file = new File(Approach.job.getName()+".err");
            }
            PrintStream log = new PrintStream(file);
            ex.printStackTrace(log);
            log.close();
        }
        if(!local){
            System.exit(0);
        }
    }
    
    private static void starting(String[] args, boolean local) throws Exception {
        Locale.setDefault(Locale.ENGLISH);
        if (args == null || args.length < 1) {
            throw new Exception("don't have arguments");
        } else if (args[0].equals("model")) {
            Model.PRINT = true;
            Model model = new Model();
            model.create(fRun.obj);
            model.savePof("model.pof");
            model.saveSgl("model.sgl");
        } else if (args[0].equals("run")) {
            Runner.PRINT = false;
            Runner.LOCAL = local;
            Best.force_finish(true);
            Runner runner = new Runner(new File(args[1]), new File(args[2]), fRun.obj);
            runner.run();
        } else {
            throw new Exception(String.format("arg[0]='%s' is not recognized.", args[0]));
        }
    }
    private static String[] find_args(String work_space, String job){
        for(File dir : new File(work_space, "job_local").listFiles()){
            for(File f : dir.listFiles()){
                if(f.getName().equals(job)){
                    return new String[]{"run", work_space+"/job_local/"+dir.getName()+"/"+job, work_space+"input"};
                }
            }
        }
        return null;
    }
}
