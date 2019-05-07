/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imr.fd.ef.datarecoder;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.OperationNotSupportedException;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 *
 * @author Edvin Fuglebakk edvin.fuglebakk@imr.no
 */
public class BatchrecodingUI {

    protected IBatchRecoder batchrecoder;

    public BatchrecodingUI(IBatchRecoder batchrecoder) {
        this.batchrecoder = batchrecoder;
    }

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
            throw new OperationNotSupportedException("c");
        } else if (cmd != null && cmd.hasOption("s")) {
            throw new OperationNotSupportedException("s");
        } else if (cmd != null && cmd.hasOption("r")) {
            throw new OperationNotSupportedException("r");
        } else if (cmd != null && cmd.hasOption("l")) {
            throw new OperationNotSupportedException("l");
        } else {
            System.out.println("Could not parse arguments");
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp(this.batchrecoder.getDescription(), options);

            System.exit(1);
        }
    }

}
