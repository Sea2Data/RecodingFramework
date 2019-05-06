/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imr.fd.ef.datarecoder;

/**
 *
 * @author Edvin Fuglebakk edvin.fuglebakk@imr.no
 */
public class RecodingIssueException extends RuntimeException {

    protected Exception parentException;

    public RecodingIssueException(String msg) {
        super(msg);
    }
    
    public RecodingIssueException(Exception e){
        this.parentException=e;
    }
    
    @Override
    public String toString(){
        if (this.parentException==null){
            return super.toString();
        }
        else{
            return "RecodingStateException:" + this.parentException.toString();
        }
    }
    
}
