/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imr.fd.ef.datarecoder;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Edvin Fuglebakk edvin.fuglebakk@imr.no
 */
public class SimpleBatchRecoder implements IBatchRecoder {

    List<IItemRecoder> itemrecoders = new LinkedList<>();


    public void addItemRecorder(IItemRecoder itemrecoder){
        this.itemrecoders.add(itemrecoder);
    }
    
    /**
     * Fetches data using fetch methods of registered itemrecoders, and run
     * their pre-recoding tests.
     *
     * @throws RecodingException
     */
    @Override
    public void fetchAndTestBatchPre() throws RecodingException {
        for (IItemRecoder ir : this.itemrecoders) {
            ir.fetch();
            ir.testPre();
        }
    }
    
    /**
     * fetches, tests, recodes, tests and updates for all registered
     * itemrecorders
     *
     * @return report of recoding
     */
    @Override
    public BatchRecodingReport recodeBatch() {
        BatchRecodingReport report = new BatchRecodingReport();

        for (IItemRecoder ir : this.itemrecoders) {
            boolean reported = false;

            if (!reported) {
                ir.fetch();
            }
            if (!reported) {

                try {
                    ir.testPre();
                } catch (RecodingException ex) {
                    report.add(ir, false, "Pre recoding test failed:" + ex.getMessage());
                    reported = true;
                }

            }
            if (!reported) {
                ir.recode();
            }
            if (!reported) {
                try {
                    ir.testPost();
                } catch (RecodingException ex) {
                    report.add(ir, false, "Post recoding test failed: " + ex.getMessage());
                    reported = true;
                }
            }
            if (!reported) {
                try {
                    ir.update();
                } catch (RecodingException ex) {
                    report.add(ir, false, "Update after recoding failed:" + ex.getMessage());
                    reported = true;
                }
            }
            report.add(ir, true, "");
            reported = true;
        }
        return report;
    }

    /**
     * Fetches data using fetch methods resitered itemrecoders, and run their
     * post-recoding tests.
     *
     * @throws RecodingException
     */
    @Override
    public void fetchAndTestBatchPost() throws RecodingException {
        for (IItemRecoder ir : this.itemrecoders) {
            ir.fetch();
            ir.testPost();
        }
    }

    @Override
    public BatchRecodingReport listPlannedRecodings() {
        BatchRecodingReport report = new BatchRecodingReport();
        for (IItemRecoder ir : this.itemrecoders) {
            report.add(ir, false, "Dry-run");
        }
        return report;
    }

}
