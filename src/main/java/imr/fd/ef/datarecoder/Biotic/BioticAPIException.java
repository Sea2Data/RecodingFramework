/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imr.fd.ef.datarecoder.Biotic;

import java.net.URI;

/**
 *
 * @author Edvin Fuglebakk edvin.fuglebakk@imr.no
 */
public class BioticAPIException extends Exception {

    Integer responsecode;
    URI uri;
    
    public BioticAPIException(Integer response, URI uri) {
        this.responsecode = response;
        this.uri = uri;
    }
    
    /**
     * Get API repsonse code causing this exception.
     * Documentation on: https://confluence.imr.no/display/API/Biotic+V3+API+documentation
     * @return 
     */
    public Integer getResponse(){
        return this.responsecode;
    }
    
    /**
     * Get URI that produced this response.
     * @return 
     */
    public URI getURI(){
        return this.uri;
    }
    
    @Override
    public String toString(){
        String stringrep = "return code: " + this.responsecode +" ("+this.uri.toString()+")";
        return stringrep;
    }
}
