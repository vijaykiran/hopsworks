/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.hopsworks.dataset;

import java.util.List;
import javax.xml.bind.annotation.XmlElement;
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

    public DatasetStructure(String name, String description, List<String> childrenFiles) {
        this.name = name;
        this.description = description;
        this.childrenFiles = childrenFiles;
    }

    public DatasetStructure() {
    }
    
    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    @XmlElement(name = "childrenFiles")
    public List<String> getChildrenFiles() {
        return childrenFiles;
    }
    
    
}
