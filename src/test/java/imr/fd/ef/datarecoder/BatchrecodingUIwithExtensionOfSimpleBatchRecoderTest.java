/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imr.fd.ef.datarecoder;

import imr.fd.ef.datarecoder.Biotic.Authenticator;
import imr.fd.ef.datarecoder.Biotic.BioticConnectionV3Test;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import org.junit.After;
import org.junit.Assert;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.junit.Before;

/**
 *
 * @author Edvin Fuglebakk edvin.fuglebakk@imr.no
 */
public class BatchrecodingUIwithExtensionOfSimpleBatchRecoderTest {
    
    protected String biotic3url;
    protected IBatchRecoder batchrecoder;
    protected File tempfile;
    
    public BatchrecodingUIwithExtensionOfSimpleBatchRecoderTest() throws Exception{
        this.biotic3url = "http://tomcat7-test.imr.no:8080/apis/nmdapi/biotic/v3";
        this.batchrecoder = new DummyBatchRecoder(this.biotic3url);
    }
    
    @Before
    public void setUp() throws Exception{
        this.tempfile = File.createTempFile("BatchrecodingUITest", "temp");
        this.tempfile.deleteOnExit();
    }
    
    @After
    public void tearDown(){
        this.tempfile.delete();
    }

    private void runCLIWithFileArg(String option) throws Exception{
        this.runCLIWithFileArg(option, true);
    }
    
    private void runCLIWithFileArg(String option, boolean verbose) throws Exception{
        BatchrecodingUI ui = new BatchrecodingUI(this.batchrecoder, verbose);
        ui.cli(option, this.tempfile.getAbsolutePath());
    }
    
    private IBatchRecoder loadBatchRecoder(File file) throws FileNotFoundException, IOException, ClassNotFoundException{
        ObjectInputStream in = new ObjectInputStream(new FileInputStream(file));
        IBatchRecoder brc = (IBatchRecoder)in.readObject();
        in.close();
        return brc;
    }
    
    /**
     * Test of cli method, of class BatchrecodingUI.
     */
    @Test
    public void testCliC() throws Exception {
        System.out.println("cli c");
        runCLIWithFileArg("-c");
        IBatchRecoder brc = this.loadBatchRecoder(this.tempfile);
        assertEquals(brc.listPlannedRecodings().itemrecodings.size(),2);
    }
    
    @Test
    public void testCliCVerbose() throws Exception {
        System.out.println("cli -c false");
        runCLIWithFileArg("-c", false);
        runCLIWithFileArg("-s", false);
    }
    
    
        /**
     * Test of cli method, of class BatchrecodingUI.
     */
    @Test
    public void testCliS() throws Exception {
        System.out.println("cli c");
        BatchrecodingUI ui = new BatchrecodingUI(this.batchrecoder);
        
        // not using runCLIWithFileArg because I need access to state of batchrecoder that is not saved in UI
        ui.cli("-c", this.tempfile.getAbsolutePath());
        ui.cli("-s", this.tempfile.getAbsolutePath());
        IBatchRecoder brc = this.loadBatchRecoder(this.tempfile);
        Assert.assertNotEquals(brc.listPlannedRecodings().itemrecodings.size(), 0);
        
        //assert that state of DummyItemRecoders has changed
        this.batchrecoder.fetchAndTestBatchPre();
        DummyBatchRecoder d = (DummyBatchRecoder) this.batchrecoder;
        DummyItemRecoder e = (DummyItemRecoder) d.itemrecoders.get(0);
        String oldcomment = e.catchsample.getCatchcomment();
        
        DummyBatchRecoder b = (DummyBatchRecoder) ui.batchrecoder;
        DummyItemRecoder c = (DummyItemRecoder) b.itemrecoders.get(0);
        String newcomment = c.catchsample.getCatchcomment();
        Assert.assertNotEquals(newcomment, oldcomment);
        
        //assert that running c again ends up with same sized set
        this.batchrecoder.clearItemRecoders();
        runCLIWithFileArg("-c");
        IBatchRecoder brc2 = this.loadBatchRecoder(this.tempfile);
        assertEquals(brc.listPlannedRecodings().itemrecodings.size(), brc2.listPlannedRecodings().itemrecodings.size());

    }
    
    @Test
    public void testCliR() throws Exception {
        Authenticator.setToken(this.biotic3url, BioticConnectionV3Test.loadTestAuth());
        System.out.println("cli r");
        runCLIWithFileArg("-c");
        IBatchRecoder brc = this.loadBatchRecoder(this.tempfile);
        Assert.assertNotEquals(brc.listPlannedRecodings().itemrecodings.size(),0);

        //fetch and store old comment
        this.batchrecoder.fetchAndTestBatchPre();
        DummyBatchRecoder b = (DummyBatchRecoder) this.batchrecoder;
        DummyItemRecoder c = (DummyItemRecoder) b.itemrecoders.get(0);
        String oldcomment = c.catchsample.getCatchcomment();
        
        // recode
        runCLIWithFileArg("-r");
                
        //assert that running c again ends up with empty sized set
        this.batchrecoder.clearItemRecoders();
        runCLIWithFileArg("-c");
        IBatchRecoder brc2 = this.loadBatchRecoder(this.tempfile);
        assertEquals(0, brc2.listPlannedRecodings().itemrecodings.size());

        //assert that running c again with different newcomment ends up with same set size as initial c
        this.batchrecoder = new DummyBatchRecoder(this.biotic3url);
        runCLIWithFileArg("-c");
        IBatchRecoder brc3 = this.loadBatchRecoder(this.tempfile);
        assertEquals(brc.listPlannedRecodings().itemrecodings.size(), brc3.listPlannedRecodings().itemrecodings.size());
        
        //fecth assert that state of DummyItemRecoders has changed
        this.batchrecoder.fetchAndTestBatchPre();
        DummyBatchRecoder d = (DummyBatchRecoder) this.batchrecoder;
        DummyItemRecoder e = (DummyItemRecoder) d.itemrecoders.get(0);
        String newcomment = e.catchsample.getCatchcomment();
        Assert.assertNotEquals(newcomment, oldcomment);


    }
    
}
