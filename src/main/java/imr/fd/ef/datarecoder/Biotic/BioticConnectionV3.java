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
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 *
 * @author Edvin Fuglebakk edvin.fuglebakk@imr.no
 */
public class BioticConnectionV3 {
    
    protected String apiurl;
    protected HttpURLConnection conn;
    protected String urlencoding="UTF-8";
    
    public BioticConnectionV3(String url){
        this.apiurl = url;
    }
    
    /**
     * encodes arguments as "/" delimeted elements of a URL path
     */
    protected String encodePath(String ... args){
        String path ="";
        for (String s: args){
            try {
                path = path + "/" + URLEncoder.encode(s, this.urlencoding);
            } catch (UnsupportedEncodingException ex) {
                throw new RuntimeException("Configuration error. URL encoding not supported");
            }
        }
       return path;
    }
    
    protected InputStream get(String path) throws MalformedURLException, IOException{
        URL url = new URL(this.apiurl + path);
        
        conn = (HttpURLConnection) url.openConnection();
	conn.setRequestMethod("GET");
	conn.setRequestProperty("Accept", "application/xml");
        
        Integer response = conn.getResponseCode();
        
        switch(response){
                case 200: System.err.println("200"); break;
                default: System.err.println(response);
        }
            
        return conn.getInputStream();
    }
    
    protected void disconnect(){
        this.conn.disconnect();
    }
    
}
