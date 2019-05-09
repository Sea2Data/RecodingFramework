/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imr.fd.ef.datarecoder.Biotic.Examples;

import imr.fd.ef.datarecoder.Biotic.BioticAPIException;
import imr.fd.ef.datarecoder.Biotic.BioticConnectionV3;
import imr.fd.ef.datarecoder.Biotic.CatchsampleItemRecoder;
import imr.fd.ef.datarecoder.RecodingDataTestException;
import imr.fd.ef.datarecoder.RecodingIssueException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import javax.xml.bind.JAXBException;
import no.imr.formats.nmdbiotic.v3.CatchsampleType;

/**
 * Example for updating a catchsample line with corrected codes for the field 'identification' introduced in biotic3.
 * This information was preciously coded in the field 'genetics' in biotic 1.4 which has been populated in the field 'tissuesample' in biotic 3.
 *
 * @author Edvin Fuglebakk edvin.fuglebakk@imr.no
 */
public class IdentificationRecoder extends CatchsampleItemRecoder {

    protected String delim = "|";

    String path;
    Integer serialnumber;
    Integer catchsampleid;
    String biotic3url;
    CatchsampleType catchsample;
    ZonedDateTime lastmodified;
    boolean recoded = false;
    Set<String> legalTissueSampleValuesForRecoding;

    public IdentificationRecoder(String path, Integer serialnumber, Integer catchsampleid, String biotic3url) throws URISyntaxException {
        super(path, serialnumber, catchsampleid, biotic3url);
        
        this.legalTissueSampleValuesForRecoding = new HashSet<>();
        this.legalTissueSampleValuesForRecoding.add("7");
        this.legalTissueSampleValuesForRecoding.add("6");
    }

    @Override
    public String getDescription() {
        String desc = super.getDescription();
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
            this.catchsample = BioticConnectionV3.createBiotic3Conncetion(this.biotic3url).getCatchSample(this.path, this.serialnumber, this.catchsampleid);
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
        if (!this.legalTissueSampleValuesForRecoding.contains(this.catchsample.getTissuesample())) {
            throw new RecodingDataTestException("Tissuesample: " + this.catchsample.getTissuesample() + " not among legal values for records to be recorded");
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
    public void testPost() throws RecodingDataTestException {
        if (this.catchsample == null || !this.recoded) {
            throw new RecodingIssueException("Data must be fetched and recoded before performing tests");
        }
        if (!this.legalTissueSampleValuesForRecoding.contains(this.catchsample.getTissuesample())) {
            throw new RecodingDataTestException("Tissuesample: " + this.catchsample.getTissuesample() + " not among legal values for records to be recorded");
        }

        if (this.catchsample.getTissuesample().equals("6") && !this.catchsample.getIdentification().equals("1")) {
            throw new RecodingDataTestException("Identification: " + this.catchsample.getIdentification() + " not correct for tissuesample: " + this.catchsample.getTissuesample());
        } else if (this.catchsample.getTissuesample().equals("7") && !this.catchsample.getIdentification().equals("2")) {
            throw new RecodingDataTestException("Identification: " + this.catchsample.getIdentification() + " not correct for tissuesample: " + this.catchsample.getTissuesample());
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
            BioticConnectionV3.createBiotic3Conncetion(this.biotic3url).updateCatchsample(this.path, this.serialnumber, this.catchsampleid, this.catchsample, this.lastmodified);
        } catch (JAXBException | IOException | BioticAPIException | URISyntaxException ex) {
            throw new RecodingIssueException(ex);
        }
    }

}
