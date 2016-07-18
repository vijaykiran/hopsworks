/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.hopsworks.dataset;

import java.util.Map;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author jsvhqr
 */
@XmlRootElement
public class DatasetStructureJson {
    
    private String name;
    private String description;
    private Map<String,KafkaInfo> childrenFiles;

    public DatasetStructureJson() {
    }

    public DatasetStructureJson(String name, String description, Map<String, KafkaInfo> files) {
        this.name = name;
        this.description = description;
        this.childrenFiles = files;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Map<String, KafkaInfo> getChildrenFiles() {
        return childrenFiles;
    }

    public void setChildrenFiles(Map<String, KafkaInfo> childrenFiles) {
        this.childrenFiles = childrenFiles;
    }
}
