/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imr.fd.ef.datarecoder.Biotic;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashSet;
import java.util.Set;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import no.imr.formats.nmdcommon.v2.ListElementType;
import no.imr.formats.nmdcommon.v2.ListType;
import no.imr.formats.nmdcommon.v2.RowElementType;

/**
 * adapter to API handles fetching and updating
 * @author Edvin Fuglebakk edvin.fuglebakk@imr.no
 */
public class BioticConnectionV3 {

    protected String apiurl;
    protected HttpURLConnection conn;
    protected String urlencoding = "UTF-8";

    public BioticConnectionV3(String url) {
        this.apiurl = url;
    }

    /**
     * encodes arguments as "/" delimeted elements of a URL path
     */
    protected String encodePath(String... args) {
        String path = "";
        for (String s : args) {
            try {
                path = path + "/" + URLEncoder.encode(s, this.urlencoding);
            } catch (UnsupportedEncodingException ex) {
                throw new RuntimeException("Configuration error. URL encoding not supported");
            }
        }
        return path;
    }

    protected InputStream get(String path) throws IOException, BioticAPIException {
        URL url = new URL(this.apiurl + path);

        conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/xml");

        Integer response = conn.getResponseCode();

        if (response != 200){
            throw new BioticAPIException(response);
        }
        
        return conn.getInputStream();
    }

    protected void disconnect() {
        if (this.conn != null) {
            this.conn.disconnect();
        }
    }

    /**
     * List all data sets
     * Format described in https://confluence.imr.no/display/API/Biotic+V3+API+documentation
     * @return
     * @throws IOException
     * @throws JAXBException
     * @throws BioticAPIException 
     */
    public ListType listAll() throws IOException, JAXBException, BioticAPIException {

        ListType all = null;
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(no.imr.formats.nmdcommon.v2.ObjectFactory.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

            InputStream inStream = this.get("?type=ListAll");
            all = ((JAXBElement<ListType>) jaxbContext.createUnmarshaller().unmarshal(inStream)).getValue();
            inStream.close();
        } finally {
            this.disconnect();

        }

        return all;
    }

    
     /**
     * Find data sets in biotic API with data field values found in the corresponding arguments (all sets).
     * If a null-reference is passed in stead of a set it is interpreted as "all values permitted".
     * Note that not all variables are keys. Unconstrained variables that are not keys might be missing, and will then be permitted.
     * @param years
     * @param missiontypes
     * @param platforms
     * @param missionnumbers
     * @param cruises
     * @return path to data sets in API
     */
    protected Set<String> findDataSets(Set<Integer> years, Set<String> missiontypes, Set<String> platforms, Set<Integer> missionnumbers, Set<String> cruises) throws IOException, JAXBException, BioticAPIException, BioticParsingException{
        ListType all = this.listAll();
        Set<String> paths = new HashSet<>();
        for ( RowElementType row: all.getRow()){
            boolean keep = true;
            String path = null;
            for (ListElementType e : row.getElement()){
                String name = e.getName();
                if (name.equals("startyear") && years!=null && !years.contains(Integer.parseInt(e.getValue()))){
                    keep = false;
                }
                if (name.equals("missiontype") && missiontypes!=null && !missiontypes.contains(e.getValue())){
                    keep = false;
                }
                if (name.equals("platform") && platforms!=null && !platforms.contains(e.getValue())){
                    keep = false;
                }
                if (name.equals("missionnumber") && missionnumbers!=null && !missionnumbers.contains(Integer.parseInt(e.getValue()))){
                    keep = false;
                }
                if (name.equals("cruise") && cruises!=null && !cruises.contains(e.getValue())){
                    keep = false;
                }
                if (name.equals("path")){
                    path = e.getValue();
                }
            }
            if (keep){
                if (path==null){
                    throw new BioticParsingException("Path not given for data set:" + row.toString());
                }
                paths.add(path);
            }
        }
        return paths;
    }
    
    public Set<String> findDataSets(Set<Integer> years, Set<String> missiontypes) throws IOException, JAXBException, JAXBException, BioticAPIException, BioticParsingException{
        return findDataSets(years, missiontypes, null, null, null);
    }
    
}
