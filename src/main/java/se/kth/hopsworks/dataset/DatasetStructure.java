/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.hopsworks.dataset;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author jsvhqr
 */
@XmlRootElement
public class DatasetStructure {
    
    private final String name;
    private final String description;
    private final List<String> childrenFiles;
    private final List<Directory> childredDirs;

    public DatasetStructure(String name, String description, List<String> childrenFiles, List<Directory> childredDirs) {
        this.name = name;
        this.description = description;
        this.childrenFiles = childrenFiles;
        this.childredDirs = childredDirs;
    }

    public DatasetStructure(String jsonDatasetStructure) {
        
        JSONObject jsonObject = new JSONObject(jsonDatasetStructure);
        this.name = jsonObject.getString("name");
        this.description = jsonObject.getString("description");
        this.childredDirs = new ArrayList<Directory>(); //TODO
        this.childrenFiles = parseJSONArrayFiles(jsonObject.getJSONArray("childrenFiles"));
        
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<String> getChildrenFiles() {
        return childrenFiles;
    }

    public List<Directory> getChildredDirs() {
        return childredDirs;
    }

    private static List<String> parseJSONArrayFiles(JSONArray jsonArray) {
        
        ArrayList<String> listToBuild = new ArrayList<>();
        for (int i=0; i<jsonArray.length(); i++) {
            listToBuild.add( jsonArray.getString(i) );
        }
        
        return listToBuild;
    }
    
    
}
