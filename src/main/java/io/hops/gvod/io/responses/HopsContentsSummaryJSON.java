/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.hops.gvod.io.responses;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author jsvhqr
 */
@XmlRootElement
public class HopsContentsSummaryJSON {
    private List<ElementSummaryJSON> contents = new ArrayList<>();

    public HopsContentsSummaryJSON(List<ElementSummaryJSON> contents) {
        this.contents = contents;
    }

    public HopsContentsSummaryJSON() {
    }

    public List<ElementSummaryJSON> getContents() {
        return contents;
    }

    public void setContents(List<ElementSummaryJSON> contents) {
        this.contents = contents;
    }
}
