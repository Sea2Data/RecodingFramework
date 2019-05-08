/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imr.fd.ef.datarecoder;

/**
 * To be used for flagging data tests performed during recoding
 * @author Edvin Fuglebakk edvin.fuglebakk@imr.no
 */
public class RecodingDataTestException extends Exception {

    public RecodingDataTestException(String msg) {
        super(msg);
    }
    
}
