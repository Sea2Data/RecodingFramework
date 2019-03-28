/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imr.fd.ef.datarecoder;

/**
 * Interface for recoding an item in a database.
 * To be used for atomic recoding, that either fails or completely succeeds (one single update call to API).
 * To be used for automatic database updates correcting for consistent mistakes in data, or to reflect changes in data model.
 * @author Edvin Fuglebakk edvin.fuglebakk@imr.no
 */
public interface IItemRecoder {
    
    /**
     * 
     * @return Description of item update, should identify the data to be updated for manual follow-up.
     */
    public String getDescription();
    
    /**
     * Fetches data to be recoded.
     */
    public void fetch();
    
    /**
     * Test fetched data to check that fetch is correctly specified.
     * Could involve anything that goes into a unit test, but is run on the actual data to be recoded.
     */
    public void testPre() throws RecodingException;
    
    /**
     * Transforms data to recoded form.
     */
    public void recode();
    
    /**
     * Tests recoded data.
     * Could involve anything that goes into a unit test, but is run on the actual data that has been recoded.
     * @throws RecodingException 
     */
    public void testPost() throws RecodingException;
    
    /**
     * Update database with corrected data.
     * This should consist of a single update call to the API
     */
    public void update() throws RecodingException;
    
}
