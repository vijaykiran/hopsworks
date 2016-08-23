/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.hops.gvod.contents;

/**
 *
 * @author jsvhqr
 */
public class HopsContentsReqJSON {
    private Integer projectId;
    
    public HopsContentsReqJSON() {}

    public HopsContentsReqJSON(Integer projectId) {
        this.projectId = projectId;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }
}
