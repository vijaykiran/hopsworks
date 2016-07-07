package se.kth.hopsworks.rest;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import org.elasticsearch.search.SearchHit;
import org.json.JSONObject;
import se.kth.hopsworks.dataset.DatasetStructure;

/**
 * Represents a JSONifiable version of the elastic hit object
 * <p/>
 * @author vangelis
 */
@XmlRootElement
public class ElasticHit implements Comparator<ElasticHit> {

    private static final Logger logger = Logger.getLogger(ElasticHit.class.
            getName());

    //the inode id
    private String id;
    //inode name 
    private String name;
    //the rest of the hit (search match) data
    private Map<String, Object> map;
    //whether the inode is a parent, a child or a dataset
    private String type;

    private float score;

    private String publicId;

    private List<String> gvodEndpoints;

    private String originalGvodEndpoint;
    
    private String datasetStructureJson;
    
    private boolean localDataset;

    public ElasticHit() {
    }

    public ElasticHit(SearchHit hit) {
        //the id of the retrieved hit (i.e. the inode_id)
        this.id = hit.getId();
        //the source of the retrieved record (i.e. all the indexed information)
        this.map = hit.getSource();
        this.type = hit.getType();
        this.score = hit.getScore();
        //export the name of the retrieved record from the list
        for (Entry<String, Object> entry : map.entrySet()) {
            //set the name explicitly so that it's easily accessible in the frontend
            if (entry.getKey().equals("name")) {
                this.setName(entry.getValue().toString());
            }

            //logger.log(Level.FINE, "KEY -- {0} VALUE --- {1}", new Object[]{entry.getKey(), entry.getValue()});
        }
        this.gvodEndpoints = new ArrayList<>();
    }

    public ElasticHit(String name, String id, String type, JSONObject json, float score) {

        Map<String, Object> source = new Gson().fromJson(json.toString(), new TypeToken<HashMap<String, Object>>() {
        }.getType());
        this.name = name;
        this.id = id;
        this.type = type;
        this.setHits(source);
        this.score = score;
    }

    public boolean isLocalDataset() {
        return localDataset;
    }

    public void setLocalDataset(boolean localDataset) {
        this.localDataset = localDataset;
    }

    
    
    public String getDatasetStructureJson() {
        return datasetStructureJson;
    }

    public void setDatasetStructureJson(String datasetStructureJson) {
        this.datasetStructureJson = datasetStructureJson;
    }
  
    public String getOriginalGvodEndpoint() {
        return originalGvodEndpoint;
    }

    public void setOriginalGvodEndpoint(String originalGvodEndpoint) {
        this.originalGvodEndpoint = originalGvodEndpoint;
    }
    
    public List<String> getGvodEndpoints() {
        return gvodEndpoints;
    }

    public void appendEndpoint(String gvod_endpoint) {
        if (this.gvodEndpoints != null) {
            this.gvodEndpoints.add(gvod_endpoint);
        }else{
            this.gvodEndpoints = new ArrayList<>();
            this.gvodEndpoints.add(gvod_endpoint);
        }
    }

    public String getPublicId() {
        return publicId;
    }

    public void setPublicId(String publicId) {
        this.publicId = publicId;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return this.id;
    }

    public final void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return this.type;
    }

    public void setHits(Map<String, Object> source) {
        this.map = new HashMap<>(source);
    }

    public Map<String, String> getHits() {
        //flatten hits (remove nested json objects) to make it more readable
        Map<String, String> refined = new HashMap<>();

        for (Entry<String, Object> entry : this.map.entrySet()) {
            //convert value to string
            String value = (entry.getValue() == null) ? "null" : entry.getValue().
                    toString();
            refined.put(entry.getKey(), value);
        }

        return refined;
    }

    @Override
    public int compare(ElasticHit o1, ElasticHit o2) {
        return Float.compare(o2.getScore(), o1.getScore());
    }
}
