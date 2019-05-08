/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imr.fd.ef.datarecoder;

import imr.fd.ef.datarecoder.Biotic.DummyItemRecoder;
import java.io.File;
import java.io.PrintStream;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Edvin Fuglebakk edvin.fuglebakk@imr.no
 */
public class SimpleBatchRecoderTest {
        
    public SimpleBatchRecoderTest() {
        
    }

    /**
     * Test of addItemRecorder method, of class SimpleBatchRecoder.
     */
    @Test
    public void testAddItemRecorder() {
        System.out.println("addItemRecorder");
        IItemRecoder itemrecoder = null;
        SimpleBatchRecoder instance = new SimpleBatchRecoderImpl();
        instance.addItemRecorder(itemrecoder);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of fetchAndTestBatchPre method, of class SimpleBatchRecoder.
     */
    @Test
    public void testFetchAndTestBatchPre() throws Exception {
        System.out.println("fetchAndTestBatchPre");
        SimpleBatchRecoder instance = new SimpleBatchRecoderImpl();
        instance.fetchAndTestBatchPre();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of recodeBatch method, of class SimpleBatchRecoder.
     */
    @Test
    public void testRecodeBatch() {
        System.out.println("recodeBatch");
        boolean simulate = false;
        SimpleBatchRecoder instance = new SimpleBatchRecoderImpl();
        BatchRecodingReport expResult = null;
        BatchRecodingReport result = instance.recodeBatch(simulate);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of fetchAndTestBatchPost method, of class SimpleBatchRecoder.
     */
    @Test
    public void testFetchAndTestBatchPost() throws Exception {
        System.out.println("fetchAndTestBatchPost");
        SimpleBatchRecoder instance = new SimpleBatchRecoderImpl();
        instance.fetchAndTestBatchPost();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of listPlannedRecodings method, of class SimpleBatchRecoder.
     */
    @Test
    public void testListPlannedRecodings() {
        System.out.println("listPlannedRecodings");
        SimpleBatchRecoder instance = new SimpleBatchRecoderImpl();
        BatchRecodingReport expResult = null;
        BatchRecodingReport result = instance.listPlannedRecodings();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of save method, of class SimpleBatchRecoder.
     */
    @Test
    public void testSave() throws Exception {
        System.out.println("save");
        File file = null;
        SimpleBatchRecoder instance = new SimpleBatchRecoderImpl();
        instance.save(file);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of makeBatchRecoder method, of class SimpleBatchRecoder.
     */
    @Test
    public void testMakeBatchRecoder() throws Exception {
        System.out.println("makeBatchRecoder");
        PrintStream progress = null;
        SimpleBatchRecoder instance = new SimpleBatchRecoderImpl();
        instance.makeBatchRecoder(progress);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getDescription method, of class SimpleBatchRecoder.
     */
    @Test
    public void testGetDescription() {
        System.out.println("getDescription");
        SimpleBatchRecoder instance = new SimpleBatchRecoderImpl();
        String expResult = "";
        String result = instance.getDescription();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    public class SimpleBatchRecoderImpl extends SimpleBatchRecoder {

        public void makeBatchRecoder(PrintStream progress) throws Exception {
        }

        public String getDescription() {
            return "";
        }
    }
    
}
