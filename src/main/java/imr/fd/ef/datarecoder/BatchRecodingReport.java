/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imr.fd.ef.datarecoder;

import java.io.PrintStream;
import java.io.Serializable;
import java.util.List;

/**
 * Holds data for reporting after batch recoding
 * @author Edvin Fuglebakk edvin.fuglebakk@imr.no
 */
public class BatchRecodingReport implements Serializable{
    
    protected final String statusfailedstring="failed";
    protected final String statussuccededstring="OK";
    protected final String delimstring =",";
    
    protected List<IItemRecoder> itemrecodings;
    protected List<Boolean> recodingstatus;
    protected List<String> reasons;
    
    public void add(IItemRecoder itemrecoder, Boolean status, String reason){
        this.itemrecodings.add(itemrecoder);
        this.recodingstatus.add(status);
        this.reasons.add(reason);
        assert itemrecodings.size() == recodingstatus.size();
        assert itemrecodings.size() == reasons.size();
    }
    
    /**
     * writes report to writer
     * @param writer 
     */
    public void writeReport(PrintStream writer){
        assert itemrecodings.size() == recodingstatus.size();
        assert itemrecodings.size() == reasons.size();
        
        for (int i=0; i<this.itemrecodings.size(); i++){
            String itemrec = this.itemrecodings.get(i).getDescription();
            String reason = this.reasons.get(i);
            String status = statusfailedstring;
            if (this.recodingstatus.get(i)){
                status = statussuccededstring;
            }
            else{
                assert !this.recodingstatus.get(i);
            }
            writer.print(itemrec);
            writer.print(this.delimstring);
            writer.print(status);
            writer.print(this.delimstring);
            writer.println(reason);
        }
    }
}
