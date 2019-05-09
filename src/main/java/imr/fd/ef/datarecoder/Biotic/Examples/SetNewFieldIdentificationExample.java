/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imr.fd.ef.datarecoder.Biotic.Examples;

import imr.fd.ef.datarecoder.BatchrecodingUI;
import imr.fd.ef.datarecoder.Biotic.BioticAPIException;
import imr.fd.ef.datarecoder.Biotic.BioticConnectionV3;
import imr.fd.ef.datarecoder.Biotic.BioticParsingException;
import imr.fd.ef.datarecoder.IItemRecoder;
import imr.fd.ef.datarecoder.RecodingIssueException;
import imr.fd.ef.datarecoder.SimpleBatchRecoder;
import java.io.IOException;
import java.io.PrintStream;
import java.math.BigInteger;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.xml.bind.JAXBException;
import no.imr.formats.nmdbiotic.v3.CatchsampleType;
import no.imr.formats.nmdbiotic.v3.FishstationType;

/**
 * Example for filling in data in historical records when a new field is
 * introduced that can be filled based on historic records in other fields This
 * example is for the field 'identification' introduced in biotic v3, which can
 * be set based on deprecated, code in the 'tissuesample' field (which has been
 * populated with records from the 'genetics' field in biotic 1.4
 *
 * In order to maintain serializability,  the parent member apiurl are passed to BioticConnectionV3 constructurs when API connection is needed, rather than including as a member in this class.
 * 
 * @author Edvin Fuglebakk edvin.fuglebakk@imr.no
 */
public class SetNewFieldIdentificationExample extends SimpleBatchRecoder {

    protected int firstyear;
    protected int lastyear;

    /**
     * Recoding is performed for firstyear to lastyear inclusive
     *
     * @param biotic3url url for biotic 3 API. Should be acceptable by BioticConnectionV3.createBiotic3Conncetion(String)
     * @param firstyear
     * @param lastyear
     */
    public SetNewFieldIdentificationExample(String biotic3url, int firstyear, int lastyear) throws URISyntaxException {
        super(biotic3url);
        BioticConnectionV3.createBiotic3Conncetion(biotic3url); // testing url formatting
        this.firstyear = firstyear;
        this.lastyear = lastyear;
        
    }

    //
    // Implement lookup of data sets and search for entries to recode here. Add item recodings via this.addItemRecoder
    //
    @Override
    public void searchForItemRecoders(PrintStream progress) throws IOException, JAXBException, BioticAPIException, BioticParsingException {

        BioticConnectionV3 bioticconn;
        try {
            bioticconn = BioticConnectionV3.createBiotic3Conncetion(this.apiurl);
        } catch (URISyntaxException ex) {
            throw new RecodingIssueException(ex);
        }
        
        Set<String> missiontypes = new HashSet<>();
        missiontypes.add("4");
        Set<Integer> years = new HashSet<>();
        for (int year = firstyear; year <= lastyear; year++) {
            years.add(year);
        }

        Set<String> datasets = bioticconn.findDataSets(years, missiontypes);

        //legal values for genetics / tissuesample that are not handled
        Set<String> legalValues = new HashSet<>();
        legalValues.add("1");
        legalValues.add("2");
        legalValues.add("3");
        legalValues.add("4");
        legalValues.add("5");

        for (String path : datasets) {
            List<FishstationType> fs = bioticconn.listFishstation(path);
            for (FishstationType f : fs) {
                BigInteger serialn = f.getSerialnumber();

                List<CatchsampleType> cs = bioticconn.listCatchsamples(path, serialn);
                for (CatchsampleType c : cs) {
                    if (c.getTissuesample() != null && (c.getTissuesample().equals("6") || c.getTissuesample().equals("7"))) {
                        IItemRecoder itemrecoder;
                        try {
                            itemrecoder = new IdentificationRecoder(path, serialn.intValue(), c.getCatchsampleid().intValue(), this.apiurl);
                        } catch (URISyntaxException ex) {
                            throw new RecodingIssueException(ex);
                        }
                        this.addItemRecorder(itemrecoder);
                        if (progress != null) {
                            progress.println("Adding:" + itemrecoder.getDescription() + " to batch recoding.");
                        }

                    } else {
                        assert c.getTissuesample() == null || legalValues.contains(c.getTissuesample());
                    }
                }

            }
        }

    }

    public String getDescription() {
        return "Fills in values for the field 'identification' (catchsample) based on deprecated code used in the 'tissuesample' field (called genetics in biotic1.4).\n"
                + "Samples with 'tissuesample' set to 6 gets 'identification' set to 1.\n"
                + "Samples with 'tissuesample' set to 7 gets 'identification' set to 2.\n"
                + "The values for 'tissuesample is not altered.\n"
                + " Recoding is applied for all data in the years: " + this.firstyear + "-" + this.lastyear + ", inclusive.";
    }

    //
    // implement main in standardized way for running recodings
    //
    public static void main(String... args) throws Exception {
        
        //
        // hard-code all parameters that defines the recoding at this point. 
        // The program is supposed to document the recoding, and these parameters are therefore not supposed to be configurable.
        // 
        
        String url = "http://tomcat7-test.imr.no:8080/apis/nmdapi/biotic/v3";
        String key = null;
        int firstyear = 2013;
        int lastyear = 2013;
        SetNewFieldIdentificationExample ex = new SetNewFieldIdentificationExample(url, firstyear, lastyear);

        //
        // call BatchrecodingUI for standardized running of recodings.
        //
        BatchrecodingUI ui = new BatchrecodingUI(ex);
        ui.cli(args);
    }
}
