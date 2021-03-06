/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imr.fd.ef.datarecoder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;

/**
 * Base implementation for Batch recodings, facilitating reuse of common operations.
 * Implementions of searchForItemRecoders(PrintStream) should populate add IItemRecoder instances via addItemRecoder(IItemRecoder)
 * @author Edvin Fuglebakk edvin.fuglebakk@imr.no
 */
public abstract class SimpleBatchRecoder implements IBatchRecoder {
    
    protected List<IItemRecoder> itemrecoders = new LinkedList<>();
    protected String apiurl;

    /**
     * Constructs simple batch recoder for recoding through the API identified by the string url
     * @param url
     * @throws URISyntaxException 
     */
    public SimpleBatchRecoder(String url) throws URISyntaxException{
        this.apiurl = url;
    }
    
    public void addItemRecorder(IItemRecoder itemrecoder) {
        this.itemrecoders.add(itemrecoder);
    }


    @Override
    public void fetchAndTestBatchPre() throws RecodingDataTestException {
        for (IItemRecoder ir : this.itemrecoders) {
            ir.fetch();
            ir.testPre();
        }
    }

    @Override
    public BatchRecodingReport recodeBatch(boolean simulate) {
        BatchRecodingReport report = new BatchRecodingReport();

        for (IItemRecoder ir : this.itemrecoders) {
            boolean reported = false;

            if (!reported) {
                ir.fetch();
            }
            if (!reported) {

                try {
                    ir.testPre();
                } catch (RecodingDataTestException ex) {
                    report.add(ir, false, "Pre recoding test failed:" + ex.getMessage());
                    reported = true;
                }

            }
            if (!reported) {
                ir.recode();
            }
            if (!reported) {
                try {
                    ir.testPost();
                } catch (RecodingDataTestException ex) {
                    report.add(ir, false, "Post recoding test failed: " + ex.getMessage());
                    reported = true;
                }
            }
            if (!reported && !simulate) {
                try {
                    ir.update();
                    report.add(ir, true, "recoded");
                    reported = true;
                } catch (RecodingDataTestException ex) {
                    report.add(ir, false, "Update after recoding failed:" + ex.getMessage());
                    reported = true;
                }
            }

            if (!reported && simulate) {
                report.add(ir, true, "simulated");
                reported = true;
            }
            assert reported;
        }
        return report;
    }


    @Override
    public void fetchAndTestBatchPost() throws RecodingDataTestException {
        for (IItemRecoder ir : this.itemrecoders) {
            ir.fetch();
            ir.testPost();
        }
    }


    @Override
    public BatchRecodingReport listPlannedRecodings() {
        BatchRecodingReport report = new BatchRecodingReport();
        for (IItemRecoder ir : this.itemrecoders) {
            report.add(ir, true, "Planned");
        }
        return report;
    }


    @Override
    public void save(File file) throws FileNotFoundException, IOException {
        FileOutputStream fileOut = null;
        ObjectOutputStream objectOut = null;
        fileOut = new FileOutputStream(file);
        objectOut = new ObjectOutputStream(fileOut);
        objectOut.writeObject(this);
        objectOut.close();
        fileOut.close();

    }

    @Override
    public abstract void searchForItemRecoders(PrintStream progress) throws Exception;
    
    @Override
    public void clearItemRecoders(){
        this.itemrecoders.clear();
    }

    @Override
    public abstract String getDescription();
    
    @Override
    public String getURL(){
        return this.apiurl;
    }

}
