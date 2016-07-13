/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.hopsworks.gvod;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author jsvhqr
 */
@XmlRootElement
public class DownloadGVoDJson {

    private HdfsResource hdfsResource;
    private KafkaResource kafkaResource;
    private HopsResource hopsResource;
    private TorrentId torrentId;
    private List<String> partners;

    public DownloadGVoDJson(HdfsResource hdfsResource, KafkaResource kafkaResource,HopsResource hopsResource, TorrentId torrentId, List<String> partners) {
        this.hdfsResource = hdfsResource;
        this.kafkaResource = kafkaResource;
        this.hopsResource = hopsResource;
        this.torrentId = torrentId;
        this.partners = partners;
    }

    public DownloadGVoDJson() {
    }
    
    

    public HdfsResource getHdfsResource() {
        return hdfsResource;
    }

    public KafkaResource getKafkaResource() {
        return kafkaResource;
    }

    public TorrentId getTorrentId() {
        return torrentId;
    }
    
    
    @XmlElement(name = "partners")
    public List<String> getPartners() {
        return partners;
    }

    public HopsResource getHopsResource() {
        return hopsResource;
    }

    private List<AddressJSON> parseJSONArray(JSONArray partners) {
        
        List<AddressJSON> listToBuild = new ArrayList<>();
        for(int i = 0; i< partners.length(); i++){
            JSONObject jsonObject = new JSONObject(partners.get(i).toString().replaceAll("'\'", ""));
            listToBuild.add(new AddressJSON(jsonObject.getString("ip"), jsonObject.getInt("port"), jsonObject.getInt("id")));
        }
        return listToBuild;
    }
    
    
    
    
    
}
