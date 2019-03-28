/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imr.fd.ef.datarecoder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;

/**
 *
 * @author Edvin Fuglebakk edvin.fuglebakk@imr.no
 */
public class BatchRunner {

    /**
     * Loads a preciously saved batch recoding and runs it. Report is written to
     * stdout
     *
     * @param savedrecoding
     * @throws FileNotFoundException
     * @throws IOException
     * @throws ClassNotFoundException
     * @throws RecodingException
     */
    public static void runSavedBatchRecoding(File savedrecoding) throws FileNotFoundException, IOException, ClassNotFoundException, RecodingException {
        FileInputStream fileIn = new FileInputStream(savedrecoding);
        ObjectInputStream in = new ObjectInputStream(fileIn);
        IBatchRecoder recoder = (IBatchRecoder) in.readObject();
        BatchRecodingReport report = recoder.recodeBatch();
        report.writeReport(new PrintWriter(System.out));
        recoder.fetchAndTestBatchPost();
    }

    /**
     * Dry runs a recoding Lists updates on stdout and saves batch recoding
     * object
     *
     * @param batchrecoder batch recoder to dry run
     * @param output file to save batch recoding in
     */
    public static void saveBatchRecoding(IBatchRecoder batchrecoder, File output) throws FileNotFoundException, IOException {
        batchrecoder.listPlannedRecodings().writeReport(new PrintWriter(System.out));
        FileOutputStream fileOut = new FileOutputStream(output);
        ObjectOutputStream out = new ObjectOutputStream(fileOut);
        out.writeObject(batchrecoder);
        out.close();
    }
    
}
