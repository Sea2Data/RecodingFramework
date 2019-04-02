/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imr.fd.ef.datarecoder.Biotic.Examples;

import imr.fd.ef.datarecoder.BatchRecodingReport;
import imr.fd.ef.datarecoder.Biotic.BioticAPIException;
import imr.fd.ef.datarecoder.Biotic.BioticConnectionV3;
import imr.fd.ef.datarecoder.Biotic.BioticParsingException;
import imr.fd.ef.datarecoder.RecodingException;
import imr.fd.ef.datarecoder.SimpleBatchRecoder;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;
import javax.xml.bind.JAXBException;

/**
 * Example for filling in data in historical records when a new field is introduced that can be filled based on historic records in other fields
 * This example is for the field 'identification' introduced in biotic v3, which can be set based on deprecated, code in the 'tissuesample' field (which has been populated with records from the 'genetics' field in biotic 1.4
 * @author Edvin Fuglebakk edvin.fuglebakk@imr.no
 */
public class SetNewFieldIdentificationExample{

    protected BioticConnectionV3 bioticconnection;
    protected SimpleBatchRecoder batchrecoder;
    
    public SetNewFieldIdentificationExample(BioticConnectionV3 bioticconnection) {
        this.bioticconnection = bioticconnection;
    }
    
    /**
     * Searches through years to identify which catchsamples are to be recoded
     * @param firstyear
     * @param lastyear
     * @return 
     */
    protected void makeBatchRecoding(int firstyear, int lastyear) throws IOException, JAXBException, BioticAPIException, BioticParsingException{
        SimpleBatchRecoder batchRecoder = new SimpleBatchRecoder();
        
        Set<String> missiontypes = new HashSet<>();
        missiontypes.add("4");
        Set<Integer> years = new HashSet<>();
        for (int year=firstyear; year<=lastyear; year++){
            years.add(year);
        }
        
        Set<String> datasets = this.bioticconnection.findDataSets(years, missiontypes);
        
        for (String path: datasets){
            
            // list stations
            // for all stations
            // get catches
            // for all catches
            // make itemrecorder for catches with genetics == 6 or 7
            
        }
                    
        this.batchrecoder = batchRecoder;
    }
    
    
    //make or run batch recoding
    public void main (String ... args) throws RecodingException, IOException,  JAXBException, BioticAPIException, BioticParsingException{
        String url = null;
        String key = null;
        int firstyear = 2013;
        int lastyear = 2013;
        SetNewFieldIdentificationExample ex = new SetNewFieldIdentificationExample(new BioticConnectionV3(url));
        ex.makeBatchRecoding(firstyear, lastyear);
        ex.batchrecoder.fetchAndTestBatchPre();
        BatchRecodingReport report = ex.batchrecoder.recodeBatch();
        report.writeReport(new PrintWriter(System.out));
        ex.batchrecoder.fetchAndTestBatchPost();
    }
}
