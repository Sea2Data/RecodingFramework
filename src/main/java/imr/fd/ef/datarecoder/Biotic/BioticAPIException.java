/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imr.fd.ef.datarecoder.Biotic;

/**
 *
 * @author Edvin Fuglebakk edvin.fuglebakk@imr.no
 */
public class BioticAPIException extends Exception {

    Integer responsecode;
    
    public BioticAPIException(Integer response) {
        this.responsecode = response;
    }
    
    /**
     * Get API repsonse code causing this exception.
     * Documentation on: https://confluence.imr.no/display/API/Biotic+V3+API+documentation
     * @return 
     */
    public Integer getResponse(){
        return this.responsecode;
    }
    
}
