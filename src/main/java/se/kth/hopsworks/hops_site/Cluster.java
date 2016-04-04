/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.hopsworks.hops_site;

/**
 *
 * @author gnomer_fedora
 */
public class Cluster {
    
    private String cluster_name;
    private String cluster_endpoint;
    private String cluster_email;
    private String cluster_cert;
    private String udp_endpoint;
    private long heartbeatsmissed;
    private String dateregistered;

    public Cluster(String cluster_name, String cluster_endpoint, String cluster_email, String cluster_cert, String udp_endpoint, long heartbeatsmissed, String dateregistered) {
        this.cluster_name = cluster_name;
        this.cluster_endpoint = cluster_endpoint;
        this.cluster_email = cluster_email;
        this.cluster_cert = cluster_cert;
        this.udp_endpoint = udp_endpoint;
        this.heartbeatsmissed = heartbeatsmissed;
        this.dateregistered = dateregistered;
    }
    

    public long getHeartbeatsmissed() {
        return heartbeatsmissed;
    }

    public void setHeartbeatsmissed(long heartbeatsmissed) {
        this.heartbeatsmissed = heartbeatsmissed;
    }

    public String getDateregistered() {
        return dateregistered;
    }

    public void setDateregistered(String dateregistered) {
        this.dateregistered = dateregistered;
    }

    public String getCluster_name() {
        return cluster_name;
    }

    public void setCluster_name(String cluster_name) {
        this.cluster_name = cluster_name;
    }

    public String getCluster_endpoint() {
        return cluster_endpoint;
    }

    public void setCluster_endpoint(String cluster_endpoint) {
        this.cluster_endpoint = cluster_endpoint;
    }

    public String getCluster_email() {
        return cluster_email;
    }

    public void setCluster_email(String cluster_email) {
        this.cluster_email = cluster_email;
    }

    public String getCluster_cert() {
        return cluster_cert;
    }

    public void setCluster_cert(String cluster_cert) {
        this.cluster_cert = cluster_cert;
    }

    public String getUdp_endpoint() {
        return udp_endpoint;
    }

    public void setUdp_endpoint(String udp_endpoint) {
        this.udp_endpoint = udp_endpoint;
    }
    
}
