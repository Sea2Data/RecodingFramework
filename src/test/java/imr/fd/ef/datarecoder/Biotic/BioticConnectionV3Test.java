/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imr.fd.ef.datarecoder.Biotic;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;
import no.imr.formats.nmdbiotic.v3.CatchsampleType;
import no.imr.formats.nmdcommon.v2.ListType;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author Edvin Fuglebakk edvin.fuglebakk@imr.no
 */
public class BioticConnectionV3Test {

    protected String url;
    protected String urlencoding = "UTF-8";

    public BioticConnectionV3Test() throws Exception {
        url = "http://tomcat7-test.imr.no:8080/apis/nmdapi/biotic/v3";
    }

    @org.junit.Before
    public void setUp() throws Exception {
            Authenticator.setToken(url, BioticConnectionV3Test.loadTestAuth());
    }

    public static String loadTestAuth() throws Exception {
        InputStream input = BioticConnectionV3Test.class.getClassLoader().getResourceAsStream("testauth.properties");
        Properties prop = new Properties();

        // load a properties file
        prop.load(input);

        // get the property value and print it out
        String user = prop.getProperty("testuser");
        String passwd = prop.getProperty("testpassword");

        return user + ":" + passwd;

    }

    /**
     * Test of get method, of class BioticConnectionV3.
     */
    @org.junit.Test
    public void testGet() throws Exception {
        System.out.println("get");
        String path = "/Forskningsfartøy/2018/G.O.Sars_LMEL";
        BioticConnectionV3 instance = new BioticConnectionV3(this.url);
        InputStream result = instance.get(path, null);

        BufferedReader br = new BufferedReader(new InputStreamReader(result));

        boolean hasbody = false;
        while ((br.readLine()) != null) {
            hasbody = true;
        }
        assert (hasbody);
        instance.disconnect();

    }

    /**
     * Test of put method, of class BioticConnectionV3.
     */
    @org.junit.Test
    public void testPut() throws Exception {
        System.out.println("put");

        String path = "Forskningsfartøy/2013/G.O.Sars_LMEL/2013111";

        BioticConnectionV3 instance = new BioticConnectionV3(this.url);
        CatchsampleType cs = instance.getCatchSample(path, 2774, 15);

        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime anhourago = now.minusHours(1);

        String oldstring = anhourago.format(DateTimeFormatter.RFC_1123_DATE_TIME);

        cs.setCatchcomment(oldstring);

        JAXBContext jaxbContext = JAXBContext.newInstance(no.imr.formats.nmdbiotic.v3.CatchsampleType.class);
        Marshaller m = jaxbContext.createMarshaller();
        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

        JAXBElement<no.imr.formats.nmdbiotic.v3.CatchsampleType> jaxbElement = new JAXBElement<>(new QName("http://www.imr.no/formats/nmdbiotic/v3", "catchsample", ""), no.imr.formats.nmdbiotic.v3.CatchsampleType.class, cs);

        StringWriter sw = new StringWriter();
        m.marshal(jaxbElement, sw);

        String result = sw.toString();

        // test update
        instance.put(path + "/" + "model/mission/fishstation/2774/catchsample/15", "version=3.0", result, now.toEpochSecond() * 1000);

        // test that error is thrown when not authenticated correctly
        try {
            Authenticator.setToken(this.url, "wrongtoken");
            instance.put(path + "/" + "model/mission/fishstation/2774/catchsample/15", "version=3.0", result, anhourago.toEpochSecond() * 1000);
        } catch (BioticAPIException e) {
            assertEquals(e.responsecode.intValue(), 403);
        }

    }

    /**
     * Test of listAll method, of class BioticConnectionV3.
     */
    // @Test commented out to avoid traffic on API
    public void testListAll() throws Exception {
        System.out.println("ListAll");
        BioticConnectionV3 instance = new BioticConnectionV3(this.url);
        ListType result = instance.listAll();
        assertFalse(result.getRow().isEmpty());
    }

    /**
     * Test of findDataSets method, of class BioticConnectionV3.
     */
    @Test
    public void testFindDataSets_5args() throws Exception {
        System.out.println("findDataSets");
        Set<Integer> years = new HashSet<>();
        years.add(1972);
        Set<String> missiontypes = new HashSet<>();
        missiontypes.add("4");
        Set<String> platforms = null;
        Set<Integer> missionnumbers = null;
        Set<String> cruises = new HashSet<>();
        cruises.add("1972002");
        BioticConnectionV3 instance = new BioticConnectionV3(this.url);
        Set<String> result = instance.findDataSets(years, missiontypes, platforms, missionnumbers, cruises);
        for (String s : result) {
            System.out.println(s);
        }
        assertEquals(2, result.size());
    }

    /**
     * Test of findDataSets method, of class BioticConnectionV3.
     */
    @Test
    public void testFindDataSets_Set_Set() throws Exception {
        System.out.println("findDataSets");
        Set<Integer> years = new HashSet<>();
        years.add(1972);
        Set<String> missiontypes = new HashSet<>();
        missiontypes.add("4");
        BioticConnectionV3 instance = new BioticConnectionV3(this.url);
        Set<String> result = instance.findDataSets(years, missiontypes);
        for (String s : result) {
            System.out.println(s);
        }
        assertEquals(3, result.size());
    }

    /**
     * testGetCatchsample
     *
     * @throws Exception
     */
    @Test
    public void testGetCatchSample() throws Exception {
        System.out.println("testGetCatchsample");
        BioticConnectionV3 instance = new BioticConnectionV3(this.url);
        CatchsampleType cs = instance.getCatchSample("Forskningsfartøy/2013/G.O.Sars_LMEL/2013111", 2774, 15);
        System.out.println(cs.getCommonname());
        assertTrue(cs != null);
    }

    /**
     * test update catchsample
     *
     * @throws Exception
     */
    @Test
    public void testUpdateCatchSample() throws Exception {
        System.out.println("testUpdateCatchsample");
        BioticConnectionV3 instance = new BioticConnectionV3(this.url);
        CatchsampleType cs = instance.getCatchSample("Forskningsfartøy/2013/G.O.Sars_LMEL/2013111", 2774, 15);
        CatchsampleType csedit = instance.getCatchSample("Forskningsfartøy/2013/G.O.Sars_LMEL/2013111", 2774, 15);
        ZonedDateTime now = ZonedDateTime.now();
        csedit.setCatchcomment("testGetCatchsample" + now.toString());
        instance.updateCatchsample("Forskningsfartøy/2013/G.O.Sars_LMEL/2013111", 2774, 15, csedit, now, 0);
        CatchsampleType cs2 = instance.getCatchSample("Forskningsfartøy/2013/G.O.Sars_LMEL/2013111", 2774, 15);
        assertTrue(cs2 != null);
        assertTrue(!cs2.getCatchcomment().equals(cs.getCatchcomment()));

    }

}
