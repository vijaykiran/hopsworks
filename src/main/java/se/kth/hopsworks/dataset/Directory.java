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
public class Directory {
    
    private final String name;
    
    private final List<String> childrenFiles;
    
    private final List<Directory> childrenDirs;

    public Directory(String name, List<String> childrenFiles, List<Directory> childrenDirs) {
        this.name = name;
        this.childrenFiles = childrenFiles;
        this.childrenDirs = childrenDirs;
    }

    public String getName() {
        return name;
    }

    public List<String> getChildrenFiles() {
        return childrenFiles;
    }

    public List<Directory> getChildrenDirs() {
        return childrenDirs;
    }
    
    
    
}
