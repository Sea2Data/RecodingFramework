/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imr.fd.ef.datarecoder.Biotic;

import imr.fd.ef.datarecoder.IItemRecoder;
import imr.fd.ef.datarecoder.RecodingDataTestException;
import imr.fd.ef.datarecoder.RecodingIssueException;
import java.io.IOException;
import java.time.ZonedDateTime;
import javax.xml.bind.JAXBException;
import no.imr.formats.nmdbiotic.v3.CatchsampleType;

/**
 *
 * @author Edvin Fuglebakk edvin.fuglebakk@imr.no
 */
public class DummyItemRecoder implements IItemRecoder{
    protected String delim = "|";

    String path;
    Integer serialnumber;
    Integer catchsampleid;
    BioticConnectionV3 bioticconnection;
    CatchsampleType catchsample;
    ZonedDateTime lastmodified;
    String newcomment;
    String oldcomment;
    String setcomment;
    
    /**
     * Constructs dummy recoder that recodes comment if comment is expected comment
     * sets the comment setcomment after fetching
     * @param path
     * @param serialnumber
     * @param catchsampleid
     * @param bioticconnection
     */
    public DummyItemRecoder(String path, Integer serialnumber, Integer catchsampleid, BioticConnectionV3 bioticconnection) {
        this.path = path;
        this.serialnumber = serialnumber;
        this.catchsampleid = catchsampleid;
        this.bioticconnection = bioticconnection;
        this.catchsample = null;
        ZonedDateTime now = ZonedDateTime.now();
        this.newcomment = "DummyItemReoder" + now.toString();
      
    }
    
    @Override
    public String getDescription() {
        String desc = this.path + this.delim + this.serialnumber + this.delim + this.catchsampleid;
        desc += ". Sets Catchsample comment of to DummyItemReoder + date";
        return desc;
    }

    @Override
    public void fetch() {
        try {
            this.catchsample = this.bioticconnection.getCatchSample(this.path, this.serialnumber, this.catchsampleid);
            this.lastmodified = ZonedDateTime.now();
        } catch (JAXBException | IOException | BioticAPIException ex) {
            throw new RecodingIssueException(ex);
        }
    }

    @Override
    public void testPre() throws RecodingDataTestException {
        if (this.catchsample == null) {
            throw new RecodingIssueException("Data must be fetched before performing tests");
        }
        if (this.catchsample.getCatchcomment().equals(newcomment)){
            throw new RecodingDataTestException("Old comment does not match expectation");
        }
    }
    
    @Override
    public void recode() {
        if (this.catchsample == null) {
            throw new RecodingIssueException("Data must be fetched before recoding");
        }
        if (!this.catchsample.getCatchcomment().equals(newcomment)){
            this.catchsample.setCatchcomment(this.newcomment);
        }

    }
    @Override
    public void testPost() throws RecodingDataTestException {
        if (!this.catchsample.getCatchcomment().equals(this.newcomment)){
           throw new RecodingDataTestException("Not recoded correctly"); 
        }
    }

    @Override
    public void update() {
        try {
            this.bioticconnection.updateCatchsample(this.path, this.serialnumber, this.catchsampleid, this.catchsample, this.lastmodified, 0);
        } catch (JAXBException | IOException | BioticAPIException ex) {
            throw new RecodingIssueException(ex);
        }
    }    
}
