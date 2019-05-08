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
import java.net.URISyntaxException;
import java.time.ZonedDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    String biotic3url;
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
     * @param biotic3url
     */
    public DummyItemRecoder(String path, Integer serialnumber, Integer catchsampleid, String biotic3url) throws URISyntaxException {
        BioticConnectionV3.createBiotic3Conncetion(biotic3url); // testing url formatting
        this.path = path;
        this.serialnumber = serialnumber;
        this.catchsampleid = catchsampleid;
        this.biotic3url = biotic3url;
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
            this.catchsample = BioticConnectionV3.createBiotic3Conncetion(biotic3url).getCatchSample(this.path, this.serialnumber, this.catchsampleid);
            this.lastmodified = ZonedDateTime.now();
        } catch (JAXBException | IOException | BioticAPIException | URISyntaxException ex) {
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
            BioticConnectionV3.createBiotic3Conncetion(biotic3url).updateCatchsample(this.path, this.serialnumber, this.catchsampleid, this.catchsample, this.lastmodified, 0);
        } catch (JAXBException | IOException | BioticAPIException | URISyntaxException ex) {
            throw new RecodingIssueException(ex);
        }
    }    
}
