/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imr.fd.ef.datarecoder;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
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

    public BatchrecodingUI(IBatchRecoder batchrecoder) {
        this.batchrecoder = batchrecoder;
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
            System.out.println("Could not parse arguments");
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
            this.batchrecoder.makeBatchRecoder(System.out);
            System.out.println("Saving batch recoder to " + filename);
            File file = new File(filename);
            this.batchrecoder.save(file);
            
        } else if (cmd != null && cmd.hasOption("s")) {
            
            String filename = cmd.getOptionValue("s");
            System.out.println("Loading batch recoder from " + filename);
            System.out.println("Simulating recoding");
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename));
            this.batchrecoder = (IBatchRecoder)in.readObject();
            this.batchrecoder.fetchAndTestBatchPre();
            BatchRecodingReport report = this.batchrecoder.recodeBatch(true);
            report.writeReport(System.out);
            
        } else if (cmd != null && cmd.hasOption("r")) {
            
            String filename = cmd.getOptionValue("r");
            System.out.println("Loading batch recoder from " + filename);
            System.out.println("Recoding");
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename));
            this.batchrecoder = (IBatchRecoder)in.readObject();
            this.batchrecoder.fetchAndTestBatchPre();
            BatchRecodingReport report = this.batchrecoder.recodeBatch(false);
            this.batchrecoder.fetchAndTestBatchPost();
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
