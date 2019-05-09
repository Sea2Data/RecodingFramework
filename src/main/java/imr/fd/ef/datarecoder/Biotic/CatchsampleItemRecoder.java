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
import javax.xml.bind.JAXBException;
import no.imr.formats.nmdbiotic.v3.CatchsampleType;

/**
 * Base class for recoding of catchsample records.
 * @author Edvin Fuglebakk edvin.fuglebakk@imr.no
 */
public abstract class CatchsampleItemRecoder implements IItemRecoder{
    
    protected String delim = "|";

    String path;
    Integer serialnumber;
    Integer catchsampleid;
    String biotic3url; // constructs BioticConectionV3 when needed, rather than having it as a member here, in order to maintain serializability.
    CatchsampleType catchsample;
    ZonedDateTime lastmodified;
    boolean recoded = false;

    /**
     * Constructs item recoder for catchsample entries identified in NMDBiottic v.3. by path, serialnumber and catchsampleid
     * @param path identifies data set
     * @param serialnumber identifies station
     * @param catchsampleid identifies catchsample
     * @param biotic3url URL for bioticv4 API
     * @throws URISyntaxException IF URL is malformed.
     */
    public CatchsampleItemRecoder(String path, Integer serialnumber, Integer catchsampleid, String biotic3url) throws URISyntaxException {
        BioticConnectionV3.createBiotic3Conncetion(biotic3url); // testing url formatting
        this.path = path;
        this.serialnumber = serialnumber;
        this.catchsampleid = catchsampleid;
        this.biotic3url = biotic3url;
        this.catchsample = null;
    }

    @Override
    public String getDescription() {
        String desc = this.path + this.delim + this.serialnumber + this.delim + this.catchsampleid;
        return desc;
    }

    @Override
    public void fetch() {
        try {
            this.catchsample = BioticConnectionV3.createBiotic3Conncetion(this.biotic3url).getCatchSample(this.path, this.serialnumber, this.catchsampleid);
            this.lastmodified = ZonedDateTime.now();
        } catch (JAXBException | IOException | BioticAPIException | URISyntaxException ex) {
            throw new RecodingIssueException(ex);
        }
    }

    @Override
    public abstract void testPre() throws RecodingDataTestException;

    @Override
    public abstract void recode();

    @Override
    public abstract void testPost() throws RecodingDataTestException;

    @Override
    public void update() {
        if (this.catchsample == null || !this.recoded) {
            throw new RecodingIssueException("Data must be fetched and recoded before performing update");
        }
        try {
            BioticConnectionV3.createBiotic3Conncetion(this.biotic3url).updateCatchsample(this.path, this.serialnumber, this.catchsampleid, this.catchsample, this.lastmodified);
        } catch (JAXBException | IOException | BioticAPIException | URISyntaxException ex) {
            throw new RecodingIssueException(ex);
        }
    }
    
}
