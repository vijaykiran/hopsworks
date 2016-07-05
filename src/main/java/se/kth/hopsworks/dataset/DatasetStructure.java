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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
    
    
}
