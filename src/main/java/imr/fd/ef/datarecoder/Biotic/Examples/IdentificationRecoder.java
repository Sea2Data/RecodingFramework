/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imr.fd.ef.datarecoder.Biotic.Examples;

import imr.fd.ef.datarecoder.Biotic.BioticAPIException;
import imr.fd.ef.datarecoder.Biotic.BioticConnectionV3;
import imr.fd.ef.datarecoder.IItemRecoder;
import imr.fd.ef.datarecoder.RecodingException;
import imr.fd.ef.datarecoder.RecodingIssueException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBException;
import no.imr.formats.nmdbiotic.v3.CatchsampleType;

/**
 * For updating a catchsample line with corrected identification codes.
 *
 * @author Edvin Fuglebakk edvin.fuglebakk@imr.no
 */
public class IdentificationRecoder implements IItemRecoder {

    protected String delim = "|";

    String path;
    Integer serialnumber;
    Integer catchsampleid;
    BioticConnectionV3 bioticconnection;
    CatchsampleType catchsample;
    ZonedDateTime lastmodified;
    boolean recoded = false;
    Set<String> legalTissueSampleValuesForRecoding;

    //rather accept catchSampleType
    public IdentificationRecoder(String path, Integer serialnumber, Integer catchsampleid, BioticConnectionV3 bioticconnection) {
        this.path = path;
        this.serialnumber = serialnumber;
        this.catchsampleid = catchsampleid;
        this.bioticconnection = bioticconnection;
        this.catchsample = null;

        this.legalTissueSampleValuesForRecoding = new HashSet<>();
        this.legalTissueSampleValuesForRecoding.add("7");
        this.legalTissueSampleValuesForRecoding.add("6");

    }

    @Override
    public String getDescription() {
        String desc = this.path + this.delim + this.serialnumber + this.delim + this.catchsampleid;
        if (this.catchsample != null) {
            if (this.catchsample.getTissuesample().equals("6")) {
                desc += "with tissuesample / genetics 6, setting identification to 1";
            } else if (this.catchsample.getTissuesample().equals("7")) {
                desc += "with tissuesample / genetics 7, setting identification to 2";
            }
        }
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
    public void testPre() throws RecodingException {
        if (this.catchsample == null) {
            throw new RecodingIssueException("Data must be fetched before performing tests");
        }
        if (!this.legalTissueSampleValuesForRecoding.contains(this.catchsample.getTissuesample())) {
            throw new RecodingException("Tissuesample: " + this.catchsample.getTissuesample() + " not among legal values for records to be recorded");
        }
    }

    @Override
    public void recode() {
        if (this.catchsample == null) {
            throw new RecodingIssueException("Data must be fetched before recoding");
        }
        if (this.catchsample.getTissuesample().equals("6")) {
            this.catchsample.setIdentification("1");
        } else if (this.catchsample.getTissuesample().equals("7")) {
            this.catchsample.setIdentification("2");
        } else {
            assert false;
        }
        recoded = true;

    }

    @Override
    public void testPost() throws RecodingException {
        if (this.catchsample == null || !this.recoded) {
            throw new RecodingIssueException("Data must be fetched and recoded before performing tests");
        }
        if (!this.legalTissueSampleValuesForRecoding.contains(this.catchsample.getTissuesample())) {
            throw new RecodingException("Tissuesample: " + this.catchsample.getTissuesample() + " not among legal values for records to be recorded");
        }

        if (this.catchsample.getTissuesample().equals("6") && !this.catchsample.getIdentification().equals("1")) {
            throw new RecodingException("Identification: " + this.catchsample.getIdentification() + " not correct for tissuesample: " + this.catchsample.getTissuesample());
        } else if (this.catchsample.getTissuesample().equals("7") && !this.catchsample.getIdentification().equals("2")) {
            throw new RecodingException("Identification: " + this.catchsample.getIdentification() + " not correct for tissuesample: " + this.catchsample.getTissuesample());
        } else {
            assert false;
        }

    }

    @Override
    public void update() {
        if (this.catchsample == null || !this.recoded) {
            throw new RecodingIssueException("Data must be fetched and recoded before performing update");
        }
        try {
            this.bioticconnection.updateCatchsample(this.path, this.serialnumber, this.catchsampleid, this.catchsample, this.lastmodified);
        } catch (JAXBException | IOException | BioticAPIException ex) {
            throw new RecodingIssueException(ex);
        }
    }

}
