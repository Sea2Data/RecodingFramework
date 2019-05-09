/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imr.fd.ef.datarecoder;

import imr.fd.ef.datarecoder.Biotic.Authenticator;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.PrintStream;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 * User interface(s) for Batch Recodings
 * 
 * @author Edvin Fuglebakk edvin.fuglebakk@imr.no
 */
public class BatchrecodingUI {

    protected IBatchRecoder batchrecoder;
    protected PrintStream progress;

    /**
     * Constructs UI that prints verbose progress messages to System.out
     * @param batchrecoder recoder that UI is interfacing
     */
    public BatchrecodingUI(IBatchRecoder batchrecoder) {
        this.batchrecoder = batchrecoder;
        this.progress = System.out;
    }
    
     /**
     * Constructs UI
     * @param batchrecoder recoder that UI is interfacing
     * @param verbose
     */
    public BatchrecodingUI(IBatchRecoder batchrecoder, boolean verbose) {
        this.batchrecoder = batchrecoder;
        if (verbose){
            this.progress = System.out;
        }
        else{
            this.progress = null;
        }
    }


    /**
     * Initiates command line interface with the given command line arguments
     * @param args
     * @throws Exception 
     */
    public void cli(String... args) throws Exception {
        CommandLineParser parser = new DefaultParser();
        Options options = new Options();

        options.addOption("h", "help", false, "Show this help.");
        options.addOption("c", "create", true, "Creates batchrecoding and stores in argument");
        options.addOption("s", "simulate", true, "Performs a dry run of the recodings stored in argument. Prints report to stdout.");
        options.addOption("r", "recode", true, "Performs recoding stored in argument. Prints report to stdout.");
        options.addOption("l", "list", true, "Lists recoding stored in argument.");

        CommandLine cmd = null;
        try {
            cmd = parser.parse(options, args);
        } catch (ParseException ex) {
            System.err.println("Could not parse arguments");
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp(this.batchrecoder.getDescription(), options);

            System.exit(1);
        }

        if (cmd != null && cmd.hasOption("h")) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp(this.batchrecoder.getDescription(), options);
        } else if (cmd != null && cmd.hasOption("c")) {
            
            String filename = cmd.getOptionValue("c");
            System.out.println("Making batch recoder");
            this.batchrecoder.searchForItemRecoders(this.progress);
            System.out.println("Saving batch recoder to " + filename);
            File file = new File(filename);
            this.batchrecoder.save(file);
            
        } else if (cmd != null && cmd.hasOption("s")) {
            
            String filename = cmd.getOptionValue("s");
            System.out.println("Loading batch recoder from " + filename);
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename));
            this.batchrecoder = (IBatchRecoder)in.readObject();
            System.out.println("Running pre-recoding data checks ...");
            this.batchrecoder.fetchAndTestBatchPre();
            System.out.println("Simulating recoding ...");
            BatchRecodingReport report = this.batchrecoder.recodeBatch(true);
            System.out.println("Simulating report ...");
            report.writeReport(System.out);
            
        } else if (cmd != null && cmd.hasOption("r")) {
            
            String filename = cmd.getOptionValue("r");
            System.out.println("Loading batch recoder from " + filename);
            
            if (!Authenticator.hasToken(this.batchrecoder.getURL())){
                Authenticator.prompt(this.batchrecoder.getURL());
            }

            ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename));
            this.batchrecoder = (IBatchRecoder)in.readObject();
            System.out.println("Running pre-recoding data checks ...");
            this.batchrecoder.fetchAndTestBatchPre();
            System.out.println("Recoding ...");
            BatchRecodingReport report = this.batchrecoder.recodeBatch(false);
            System.out.println("Running post-recoding data checks ...");
            this.batchrecoder.fetchAndTestBatchPost();
            System.out.println("Recoding report");
            report.writeReport(System.out);
            
        } else if (cmd != null && cmd.hasOption("l")) {
            
            String filename = cmd.getOptionValue("l");
            System.out.println("Loading batch recoder from " + filename);
            System.out.println("Listing recoding");
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename));
            this.batchrecoder = (IBatchRecoder)in.readObject();
            BatchRecodingReport listing = this.batchrecoder.listPlannedRecodings();
            listing.writeReport(System.out);
            
        } else {
            System.out.println("Could not parse arguments");
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp(this.batchrecoder.getDescription(), options);

            System.exit(1);
        }
    }

}
