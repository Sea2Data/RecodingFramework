/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imr.fd.ef.datarecoder.Biotic.Examples;

import imr.fd.ef.datarecoder.IItemRecoder;
import imr.fd.ef.datarecoder.RecodingException;

/**
 * For updating a catchsample line with corrected identification codes.
 * @author Edvin Fuglebakk edvin.fuglebakk@imr.no
 */
public class IdentificationRecoder implements IItemRecoder{

    protected String delim="|";
    
    String path;
    Integer serialnumber;
    Integer catchsampleid;
    
    //rather accept catchSampleType
    public IdentificationRecoder(String path, Integer serialnumber, Integer catchsampleid){
        this.path=path;
        this.serialnumber = serialnumber;
        this.catchsampleid = catchsampleid;
    }
    
    @Override
    public String getDescription() {
        return this.path +  this.delim + this.serialnumber + this.delim + this.catchsampleid;
    }

    @Override
    public void fetch() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void testPre() throws RecodingException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void recode() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void testPost() throws RecodingException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void update() throws RecodingException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
