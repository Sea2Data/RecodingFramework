/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imr.fd.ef.datarecoder;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.io.Serializable;

/**
 * Interface for batch recoding through API.
 * A batch recoding is a composition of ItemRecodings. Each itemrecoding is an atomic update to the data that either succeeds or fails (does not leave partial updates in case of failures).
 * To be used for automatic database updates correcting for consistent mistakes in data, or to reflect changes in data model.
 * @author Edvin Fuglebakk edvin.fuglebakk@imr.no
 */
public interface IBatchRecoder extends Serializable{
    
    /**
     * @return Detailed description of batch recoder. Should clearly explain the
     * recoding for documentation purposes.
     */
    public String getDescription();
    
    /**
     * Looks up datasets in API and searches for records to be recorded and adds these to this batch recoder.
     * @param progress if not null, progress is written to this.
     * @throws Exception 
     */
    public void searchForItemRecoders(PrintStream progress) throws Exception;
    
    /**
     * Removes all item recodings from this batch recoder
     */
    public void clearItemRecoders();
    
    /**
     * Runs fetch for each item recoder and runs checks conditions expected to hold pre-recoding
     * @throws RecodingDataTestException 
     */
    public void fetchAndTestBatchPre() throws RecodingDataTestException;
    
    /**
     * Listing all registered item recodings, without performing any tests or updates.
     * @return 
     */
    public BatchRecodingReport listPlannedRecodings();
    
    /**
     * Recodes data in batch and returns a report of the recoding of individual items
     * @param simulate if true recodings are performed but not pushed to API
     * @return 
     */
    public BatchRecodingReport recodeBatch(boolean simulate);
    
    /**
     * Runs fetch for each item recoder and runs checks conditions expected to hold post-recoding
     * @throws RecodingDataTestException 
     */
    public void fetchAndTestBatchPost() throws RecodingDataTestException;
    
    /**
     * Serializes batch recoder as Java serialized objects
     * @param file
     * @throws IOException 
     */
    public void save(File file) throws IOException;

    /**
     * @return URL for API to recode against.
     */
    public String getURL();
    
}
