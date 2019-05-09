/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imr.fd.ef.datarecoder;

import imr.fd.ef.datarecoder.Biotic.Authenticator;
import imr.fd.ef.datarecoder.Biotic.BioticConnectionV3;
import java.io.PrintStream;
import java.math.BigInteger;
import java.net.URISyntaxException;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import no.imr.formats.nmdbiotic.v3.CatchsampleType;
import no.imr.formats.nmdbiotic.v3.FishstationType;

/**
 *
 * @author Edvin Fuglebakk edvin.fuglebakk@imr.no
 */
public class DummyBatchRecoder extends SimpleBatchRecoder {

    protected String newcomment;

    public DummyBatchRecoder(String biotic3url) throws URISyntaxException {
        super(biotic3url);
        assert this.apiurl != null;
        BioticConnectionV3.createBiotic3Conncetion(biotic3url); // testing url formatting
        ZonedDateTime now = ZonedDateTime.now();
        this.newcomment = "DummyTestCommentRecodingTest" + now.toString();

    }

    /**
     * Extracts first catchsample from two first fishstations in first data set
     * of missiontype 4 in 1972
     *
     * @param progress
     * @throws Exception
     */
    @Override
    public void searchForItemRecoders(PrintStream progress) throws Exception {
        BioticConnectionV3 bc = BioticConnectionV3.createBiotic3Conncetion(apiurl);

        Set<Integer> years = new HashSet<>();
        years.add(1972);
        Set<String> missiontypes = new HashSet<>();
        missiontypes.add("4");
        Set<String> datasets = bc.findDataSets(years, missiontypes);

        if (progress != null) {
            progress.println("Datasets: " + datasets.size());
        }

        for (String path : datasets) {
            List<FishstationType> fs = bc.listFishstation(path);
            for (int i = 0; i < 2; i++) {
                FishstationType f = fs.get(i);
                if (progress != null) {
                    progress.println("serialn: " + f.getSerialnumber());
                }
                BigInteger serialn = f.getSerialnumber();
                List<CatchsampleType> cs = bc.listCatchsamples(path, serialn);
                CatchsampleType c = cs.get(0);
                if (progress != null) {
                    progress.println("catchsampleids: " + c.getCatchsampleid());
                }
                if (!c.getCatchcomment().equals(this.newcomment)) {
                    if (progress != null) {
                        progress.println("adding catchsampleids: " + c.getCatchsampleid());
                    }

                    IItemRecoder itemrecoder = new DummyItemRecoder(path, serialn.intValue(), c.getCatchsampleid().intValue(), this.apiurl, this.newcomment);
                    this.addItemRecorder(itemrecoder);

                }
            }
            break;
        }
    }

    @Override
    public String getDescription() {
        return "Dummy batch recoder aggregating dummy item recoders for testing purposes. Two specific catchsamples are identified and the catchsample comment is recoded based on its content";
    }

    public static void test(String... args) throws URISyntaxException, Exception {
        String url = "http://tomcat7-test.imr.no:8080/apis/nmdapi/biotic/v3";
        String key = null;
        int firstyear = 2013;
        int lastyear = 2013;
        DummyBatchRecoder ex = new DummyBatchRecoder(url);
        System.out.println("Make recoder");
        ex.searchForItemRecoders(System.out);
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

    public static void main(String... args) throws URISyntaxException, Exception {
        String url = "http://tomcat7-test.imr.no:8080/apis/nmdapi/biotic/v3";
        String key = null;
        int firstyear = 2013;
        int lastyear = 2013;
        DummyBatchRecoder ex = new DummyBatchRecoder(url);

        BatchrecodingUI ui = new BatchrecodingUI(ex);
        ui.cli("-h");
        System.out.println("---");
        ui.cli("-c", System.getProperty("user.home") + "/temp/recodingtest/dummyrecoding.recoding");
        System.out.println("---");
        ui.cli("-l", System.getProperty("user.home") + "/temp/recodingtest/dummyrecoding.recoding");
        System.out.println("---");
        ui.cli("-s", System.getProperty("user.home") + "/temp/recodingtest/dummyrecoding.recoding");
        System.out.println("---");
        ui.cli("-r", System.getProperty("user.home") + "/temp/recodingtest/dummyrecoding.recoding");
        System.out.println("---");

    }

}
