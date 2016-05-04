/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ProOF.apl.advanced1.method;

import ProOF.com.Linker.LinkerApproaches;
import ProOF.com.Linker.LinkerParameters;
import ProOF.com.Linker.LinkerResults;
import ProOF.com.Linker.LinkerValidations;
import ProOF.com.language.Run;
import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author marcio
 */
public final class Tabler extends Run {

    private File jobFile;
    //private File outDir;
    private ArrayList<String[]> data;

    @Override
    public String name() {
        return "Tabler";
    }

    @Override
    public String description() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void parameters(LinkerParameters win) throws Exception {
        //(.*\\.bin)
        jobFile = win.FileRgx("Jobs", null, ".*");
        //outDir = win.Directory("Out Directory");
    }

    @Override
    public void execute() throws Exception {
        Scanner scanner = new Scanner(jobFile);

        while (scanner.hasNextLine() && !scanner.nextLine().equals("---------- summarizing ----------"));

        data = new ArrayList<String[]>();
        String token[];
        while (scanner.hasNextLine()) {
            token = scanner.nextLine().split("[;]");
            data.add(token);
        }
        scanner.close();
        
        
        
        scanner = new Scanner(job);
        while (scanner.hasNextLine() && !scanner.nextLine().equals("Run;Tabler"));

        String line;
        String origJob = scanner.nextLine().split("[;]")[1];
        scanner.close();
        
        
        String outjob = origJob+".out";
        System.out.println("Opening " + origJob);
        File out = new File(job.getParentFile().getParentFile().getParent()+"/results_cluster/", outjob);
        System.out.println(out + " -> exists = "+out.exists());

        if (out.exists()) {
            scanner = new Scanner(out);
            while (scanner.hasNextLine()) {
                line = scanner.nextLine();
                //if (line.contains("#results$")) {
                if(line.contains("#open$write$results")){
                    break;
                }
                System.out.println(line);
            }
            scanner.close();
        }
    }

    @Override
    public void results(LinkerResults win) throws Exception {
        String method = null;
        String instance = null;
        
        for (String[] token : data) {
            if(token.length>1){
                String list = token[1];
                for (int i = 2; i < token.length; i++) {
                    list = list + " " + token[i];
                }
                if (token[0].equals("Jobs")) {
                    win.writeString("JobsOld", list);
                } else if (token[0].equals("Run")) {
                    win.writeString("method", list);
                    method = list;
                } else if(token[0].equals("Instances for PPDCP")){
                    instance = list;
                    win.writeString(token[0], list);
                } else {
                    win.writeString(token[0], list);
                }
            }
        }

        Scanner scanner = new Scanner(job);
        while (scanner.hasNextLine() && !scanner.nextLine().equals("Run;Tabler"));

        String line;
        String origJob = scanner.nextLine().split("[;]")[1];

        scanner.close();
        
        
        File png = new File(job.getParentFile().getParentFile().getParent()+"/results_cluster/", "sol_"+origJob+".png");
        if(png.exists()){
            png.renameTo(new File(job.getParentFile().getParentFile().getParent()+"/results_cluster/", method+"_"+instance+"_"+origJob+".png")); 
        }
        
        String outjob = origJob+".out";
        System.out.println("Opening " + outjob);
        File out = new File(job.getParentFile().getParentFile().getParent()+"/results_cluster/", outjob);
        System.out.println(out + " -> exists = "+out.exists());

        if (out.exists()) {
            scanner = new Scanner(out);

            while (scanner.hasNextLine()) {
                line = scanner.nextLine();
                if (line.contains("#results$")) {
                //if(!line.contains("#open$write$results")){
                    System.out.println(line);
                }
            }
            scanner.close();
            win.writeString("error", "false");
        } else {
            win.writeString("error", "true");
        }
    }

    @Override
    public void services(LinkerApproaches com) throws Exception {
    }

    @Override
    public boolean validation(LinkerValidations com) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void load() throws Exception {
    }

    @Override
    public void start() throws Exception {
    }
}
