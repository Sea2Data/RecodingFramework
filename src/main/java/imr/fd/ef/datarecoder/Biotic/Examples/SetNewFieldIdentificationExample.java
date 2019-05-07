/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imr.fd.ef.datarecoder.Biotic.Examples;

import imr.fd.ef.datarecoder.BatchRecodingReport;
import imr.fd.ef.datarecoder.BatchrecodingUI;
import imr.fd.ef.datarecoder.Biotic.BioticAPIException;
import imr.fd.ef.datarecoder.Biotic.BioticConnectionV3;
import imr.fd.ef.datarecoder.Biotic.BioticParsingException;
import imr.fd.ef.datarecoder.IItemRecoder;
import imr.fd.ef.datarecoder.RecodingException;
import imr.fd.ef.datarecoder.SimpleBatchRecoder;
import java.io.IOException;
import java.io.PrintWriter;
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
 * @author Edvin Fuglebakk edvin.fuglebakk@imr.no
 */
public class SetNewFieldIdentificationExample extends SimpleBatchRecoder{

    protected BioticConnectionV3 bioticconnection;
    protected SimpleBatchRecoder batchrecoder;
    protected int firstyear;
    protected int lastyear;

    public SetNewFieldIdentificationExample(BioticConnectionV3 bioticconnection, int firstyear, int lastyear) {
        this.bioticconnection = bioticconnection;
        this.firstyear = firstyear;
        this.lastyear = lastyear;
    }

    /**
     * Searches through years to identify which catchsamples are to be recoded
     *
     * @param firstyear
     * @param lastyear
     * @return
     */
    @Override
    public void makeBatchRecoder() throws IOException, JAXBException, BioticAPIException, BioticParsingException {
        
        Set<String> missiontypes = new HashSet<>();
        missiontypes.add("4");
        Set<Integer> years = new HashSet<>();
        for (int year = firstyear; year <= lastyear; year++) {
            years.add(year);
        }

        Set<String> datasets = this.bioticconnection.findDataSets(years, missiontypes);

        //legal values for genetics / tissuesample that are not handled
        Set<String> legalValues = new HashSet<>();
        legalValues.add("1");
        legalValues.add("2");
        legalValues.add("3");
        legalValues.add("4");
        legalValues.add("5");
        
        for (String path : datasets) {
                List<FishstationType> fs = this.bioticconnection.listFishstation(path);
                for (FishstationType f : fs) {
                    BigInteger serialn = f.getSerialnumber();
                    
                    List<CatchsampleType> cs = this.bioticconnection.listCatchsamples(path, serialn);
                    for (CatchsampleType c: cs){
                        if (c.getTissuesample()!=null && (c.getTissuesample().equals("6") || c.getTissuesample().equals("7"))){
                            IItemRecoder itemrecoder = new IdentificationRecoder(path, serialn.intValue(), c.getCatchsampleid().intValue(), this.bioticconnection);
                            this.addItemRecorder(itemrecoder);
                            System.out.println("Adding:" + itemrecoder.getDescription() + " to batch recoding.");
                        }
                        else{
                            assert c.getTissuesample()==null || legalValues.contains(c.getTissuesample());
                        }
                    }

                }
        }

    }

    public String getDescription(){
        return "Fills in values for the field 'identification' (catchsample) based on deprecated code used in the 'tissuesample' field (called genetics in biotic1.4).\n"
                + "Samples with 'tissuesample' set to 6 gets 'identification' set to 1.\n"
                + "Samples with 'tissuesample' set to 7 gets 'identification' set to 2.\n"
                + "The values for 'tissuesample is not altered.\n"
                + " Recoding is applied for all data in the years: " + this.firstyear + "-" + this.lastyear + ", inclusive.";
    }
    
    // make or run batch recoding
    // option for making and saving, include progress log
    // option for dry running, include progress log
    // option for proper recoding, include progress log
    public static void main(String... args) throws Exception {
        String url = "http://tomcat7-test.imr.no:8080/apis/nmdapi/biotic/v3";
        String key = null;
        int firstyear = 2013;
        int lastyear = 2013;
        SetNewFieldIdentificationExample ex = new SetNewFieldIdentificationExample(new BioticConnectionV3(url), firstyear, lastyear);
        
        BatchrecodingUI ui = new BatchrecodingUI(ex);
        ui.cli("-c","arg");
        
        System.exit(0);
        ex.makeBatchRecoder();
        ex.fetchAndTestBatchPre();
        BatchRecodingReport planned = ex.listPlannedRecodings();
        planned.writeReport(new PrintWriter(System.out));
        System.exit(0);
        BatchRecodingReport report = ex.recodeBatch();
        ex.fetchAndTestBatchPost();
    }
}
