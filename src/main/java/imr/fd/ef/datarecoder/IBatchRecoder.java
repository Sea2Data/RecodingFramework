/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imr.fd.ef.datarecoder;

/**
 * Interface for batch recoding through API
 * To be used for automatic database updates correcting for consistent mistakes in data, or to reflect changes in data model.
 * @author Edvin Fuglebakk edvin.fuglebakk@imr.no
 */
public interface IBatchRecoder {
    
    /**
     * Fetches data and checks conditions expected to hold pre-recoding
     * @throws RecodingException 
     */
    public void fetchAndTestBatchPre() throws RecodingException;
    
    /**
     * Recodes data in batch and returns a report of the recoding of individual items
     * @return 
     */
    public BatchRecodingReport recodeBatch();
    
    /**
     * Fetches data through API and checks conditions expected to hold post-recoding
     * @throws RecodingException 
     */
    public void fetchAndTestBatchPost() throws RecodingException;
    
    
}
