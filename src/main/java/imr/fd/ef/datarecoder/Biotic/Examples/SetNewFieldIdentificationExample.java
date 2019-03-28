/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imr.fd.ef.datarecoder.Biotic.Examples;

import imr.fd.ef.datarecoder.BatchRecodingReport;
import imr.fd.ef.datarecoder.Biotic.Biotic3Recoder;
import imr.fd.ef.datarecoder.RecodingException;
import imr.fd.ef.datarecoder.SimpleBatchRecoder;
import java.io.PrintWriter;

/**
 * Example for filling in data in historical records when a new field is introduced that can be filled based on historic records in other fields
 * This example is for the field 'identification' introduced in biotic v3, which can be set based on deprecated, code in the 'tissuesample' field (which has been populated with records from the 'genetics' field in biotic 1.4
 * @author Edvin Fuglebakk edvin.fuglebakk@imr.no
 */
public class SetNewFieldIdentificationExample extends Biotic3Recoder{

    public SetNewFieldIdentificationExample(String uri, String key) {
        super(uri, key, new SimpleBatchRecoder());
    }
    
    
    
    /**
     * Searches through years to identify which catchsamples are to be recoded
     * @param firstyear
     * @param lastyear
     * @return 
     */
    protected void findCatchSamples(int firstyear, int lastyear){
        for (int year=firstyear; year<=lastyear; year++){
            // get all mission of missiontype 4
            // get all stations
            // get all catchsamples with genetics == 6 or 7
            // make itemrecoder
        }
    }
    
    //make batch recoding
    
    //make or run batch recoding
    public void main (String ... args) throws RecodingException{
        String url = null;
        String key = null;
        int firstyear = 2013;
        int lastyear = 2013;
        SetNewFieldIdentificationExample ex = new SetNewFieldIdentificationExample(url, key);
        ex.findCatchSamples(firstyear, lastyear);
        ex.batchrecoder.fetchAndTestBatchPre();
        BatchRecodingReport report = ex.batchrecoder.recodeBatch();
        report.writeReport(new PrintWriter(System.out));
        ex.batchrecoder.fetchAndTestBatchPost();
    }
}
