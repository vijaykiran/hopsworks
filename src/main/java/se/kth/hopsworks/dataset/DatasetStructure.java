/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.hopsworks.dataset;

import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author jsvhqr
 */
@XmlRootElement
public class DatasetStructure {
    
    private String name;
    private String description;
    private List<String> childrenFiles;

    
    
    public DatasetStructure() {
    }
    
    
    public DatasetStructure(String name, String description, List<String> childrenFiles) {
        this.name = name;
        this.description = description;
        this.childrenFiles = childrenFiles;
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

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setChildrenFiles(List<String> childrenFiles) {
        this.childrenFiles = childrenFiles;
    }
    
    
    
}
