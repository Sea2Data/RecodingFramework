/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imr.fd.ef.datarecoder.Biotic;

import imr.fd.ef.datarecoder.BatchRecodingReport;
import imr.fd.ef.datarecoder.BatchrecodingUI;
import imr.fd.ef.datarecoder.IItemRecoder;
import imr.fd.ef.datarecoder.SimpleBatchRecoder;
import java.io.PrintStream;
import java.math.BigInteger;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import no.imr.formats.nmdbiotic.v3.CatchsampleType;
import no.imr.formats.nmdbiotic.v3.FishstationType;

/**
 *
 * @author Edvin Fuglebakk edvin.fuglebakk@imr.no
 */
public class DummyBatchRecoder extends SimpleBatchRecoder {

    protected BioticConnectionV3 bioticconnection;

    public DummyBatchRecoder(BioticConnectionV3 bioticconnection) {
        this.bioticconnection = bioticconnection;
    }

    /**
     * Extracts first catchsample from two first fishstations in first data set of missiontype 4 in 1972
     * @param progress
     * @throws Exception 
     */
    @Override
    public void makeBatchRecoder(PrintStream progress) throws Exception {
        Set<Integer> years = new HashSet<>();
        years.add(1972);
        Set<String> missiontypes = new HashSet<>();
        missiontypes.add("4");
        Set<String> datasets = this.bioticconnection.findDataSets(years, missiontypes);

        if (progress!=null){
            progress.println("Datasets: " + datasets.size());
        }
        
        for (String path : datasets) {
            List<FishstationType> fs = this.bioticconnection.listFishstation(path);
            for (int i = 0; i < 2; i++) {
                FishstationType f = fs.get(i);
                progress.println("serialn: " + f.getSerialnumber());
                BigInteger serialn = f.getSerialnumber();
                List<CatchsampleType> cs = this.bioticconnection.listCatchsamples(path, serialn);
                CatchsampleType c = cs.get(0);
                progress.println("catchsampleids: " + c.getCatchsampleid());
                IItemRecoder itemrecoder = new DummyItemRecoder(path, serialn.intValue(), c.getCatchsampleid().intValue(), this.bioticconnection);
                this.addItemRecorder(itemrecoder);
            }
            break;
        }
    }

    @Override
    public String getDescription() {
        return "Dummy batch recoder aggregating dummy item recoders for testing purposes. Two specific catchsamples are identified and the catchsample comment is recoded based on its content";
    }

    public static void test(String ... args) throws URISyntaxException, Exception{
        String url = "http://tomcat7-test.imr.no:8080/apis/nmdapi/biotic/v3";
        String key = null;
        int firstyear = 2013;
        int lastyear = 2013;
        DummyBatchRecoder ex = new DummyBatchRecoder(new BioticConnectionV3(url));
        System.out.println("Make recoder");
        ex.makeBatchRecoder(System.out);
        System.out.println("List recoder");
        BatchRecodingReport s = ex.listPlannedRecodings();
        s.writeReport(System.out);
        System.out.println("Pre test and simulate");
        ex.fetchAndTestBatchPre();
        s = ex.recodeBatch(true);
        s.writeReport(System.out);
        Authenticator.prompt(url);
        System.out.println("Recode");
        s = ex.recodeBatch(false);
        ex.fetchAndTestBatchPost();
        s.writeReport(System.out);
        
    }
    
        public static void main(String ... args) throws URISyntaxException, Exception{
        String url = "http://tomcat7-test.imr.no:8080/apis/nmdapi/biotic/v3";
        String key = null;
        int firstyear = 2013;
        int lastyear = 2013;
        DummyBatchRecoder ex = new DummyBatchRecoder(new BioticConnectionV3(url));
        
        BatchrecodingUI ui = new BatchrecodingUI(ex);
        ui.cli("-h");
        ui.cli("-c", System.getProperty("user.home") + "/temp/recodingtest/dummyrecoding.recoding");
        
    }
    
}
