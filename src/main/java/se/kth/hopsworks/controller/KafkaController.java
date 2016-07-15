/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.hopsworks.controller;

import java.io.File;
import java.io.FileOutputStream;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import se.kth.bbc.project.Project;
import se.kth.hopsworks.certificates.UserCerts;
import se.kth.hopsworks.certificates.UserCertsFacade;
import se.kth.hopsworks.util.Settings;

/**
 *
 * @author jsvhqr
 */

@Stateless
public class KafkaController {
    
    
    @EJB
    private UserCertsFacade userCerts;
    
    
    public String getKafkaCertPaths(Project project) {
        UserCerts userCert = userCerts.findUserCert(project.getName(), project.getOwner().getUsername());
        //Check if the user certificate was actually retrieved
        if (userCert.getUserCert() != null
                && userCert.getUserCert().length > 0
                && userCert.getUserKey() != null
                && userCert.getUserKey().length > 0) {

            File certDir = new File(Settings.KAFKA_TMP_CERT_STORE_LOCAL);

            if (!certDir.exists()) {
                try {
                    certDir.mkdir();
                } catch (Exception ex) {

                }
            }
            try {
                FileOutputStream fos = new FileOutputStream(Settings.KAFKA_TMP_CERT_STORE_LOCAL + "/keystore.jks");
                fos.write(userCert.getUserCert());
                fos.close();

                fos = new FileOutputStream(Settings.KAFKA_TMP_CERT_STORE_LOCAL + "/truststore.jks");
                fos.write(userCert.getUserKey());
                fos.close();

            } catch (Exception e) {

            }

        } else {
            return null;
        }

        return Settings.KAFKA_TMP_CERT_STORE_LOCAL;
    }
    
}
