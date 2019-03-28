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
    
    protected String missiontype="4";
    protected Integer startyear;
    protected String platform;
    protected Integer missionnumber;
    protected Integer serialnumber;
    protected Integer catchsampleid;
    
    //rather accept catchSampleType
    public IdentificationRecoder(Integer startyear, String platform, Integer missionnumber, Integer serialnumber, Integer catchsampleid){
        this.startyear = startyear;
        this.platform = platform;
        this.missionnumber = missionnumber;
        this.serialnumber = serialnumber;
        this.catchsampleid = catchsampleid;
    }
    
    @Override
    public String getDescription() {
        return this.startyear + this.delim + this.platform + this.delim + this.missiontype +  this.delim + this.missionnumber +  this.delim + this.serialnumber + this.delim + this.catchsampleid;
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
