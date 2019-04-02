/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imr.fd.ef.datarecoder.Biotic;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Edvin Fuglebakk edvin.fuglebakk@imr.no
 */
public class BioticConnectionV3Test {

    protected String url;
    protected String urlencoding = "UTF-8";

    public BioticConnectionV3Test() {
        url = "http://tomcat7-test.imr.no:8080/apis/nmdapi/biotic/v3";
    }

    /**
     * Test of get method, of class BioticConnectionV3.
     */
    @org.junit.Test
    public void testGet() throws Exception {
        System.out.println("get");
        String path = "/" + URLEncoder.encode("Forskningsfartøy", this.urlencoding) + "/" + URLEncoder.encode("2018", this.urlencoding) + "/" + URLEncoder.encode("G.O.Sars_LMEL", this.urlencoding);
        BioticConnectionV3 instance = new BioticConnectionV3(this.url);
        InputStream result = instance.get(path);

        BufferedReader br = new BufferedReader(new InputStreamReader(result));

        boolean hasbody = false;
        System.out.println("Output from Server .... \n");
        while ((br.readLine()) != null) {
            hasbody = true;
        }
        assert (hasbody);
        instance.disconnect();

    }

    /**
     * Test of encodePath method, of class BioticConnectionV3.
     */
    @Test
    public void testEncodePath() throws Exception {
        System.out.println("encodePath");
        BioticConnectionV3 instance = new BioticConnectionV3(this.url);
        String expResult = "/" + URLEncoder.encode("Forskningsfartøy", this.urlencoding) + "/" + URLEncoder.encode("2018", this.urlencoding) + "/" + URLEncoder.encode("G.O.Sars_LMEL", this.urlencoding);
        String result = instance.encodePath("Forskningsfartøy", "2018", "G.O.Sars_LMEL");
        assertEquals(expResult, result);
    }

    /**
     * Test of disconnect method, of class BioticConnectionV3.
     */
    @Test
    public void testDisconnect() {

    }

}
