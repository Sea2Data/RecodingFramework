/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imr.fd.ef.datarecoder.Biotic;

import imr.fd.ef.datarecoder.IBatchRecoder;

/**
 *
 * @author Edvin Fuglebakk edvin.fuglebakk@imr.no
 */
public class SimpleBiotic3Recoder{

    protected String uri;
    protected String authkey;
    protected IBatchRecoder batchrecoder;
    
    /**
     * 
     * @param uri URI to Biotic API that should be used. E.g: http://tomcat7-test.imr.no:8080/apis/nmdapi/biotic/v3
     * @param key key for authenticating pushes through API.
     */
    public SimpleBiotic3Recoder(String uri, String key){
        this.uri=uri;
        this.authkey=key;
    }
    
    
}
