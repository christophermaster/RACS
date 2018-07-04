package com.racs.commons.scheduler;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

/**
 * Obtienen los datos necesarios para establecer el protocolo OAuth2 a nivel del
 * cliente, el archivo de configuración es oauth2.propierties, el cual debe
 * sobreescribirse cuando se use esta librería en una aplicación cliente.
 *
 * @author innova4j
 */
public class SchedulerConfiguration {

    private String urlDB = "jdbc:sqlserver://192.168.0.16:1433;databaseName=SingleSignOnDB;integratedSecurity=false;";
    private String userDB = "innova4j";
    private String passwordDB = "5TF2017$$";
    private String driverClassDB = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    private String processUpdateStatusUser = "0 0/30 * * * ?";
    private String domainAD = "192.168.0.76:389";
    private String userAdminLDAP = "Administrador";
    private String claveAdminLDAP = "admin";
    private String canonicalName = "CN=Users,DC=innova4j,DC=local";
    private String baseAD = "DC=innova4j,DC=local";

    public String getUrlDB() {
        return urlDB;
    }

    public String getUserDB() {
        return userDB;
    }

    public String getPasswordDB() {
        return passwordDB;
    }

    public String getDriverClassDB() {
        return driverClassDB;
    }

    public String getProcessUpdateStatusUser() {
        return processUpdateStatusUser;
    }

    public String getDomainAD() {
        return domainAD;
    }

    public String getUserAdminLDAP() {
        return userAdminLDAP;
    }

    public String getClaveAdminLDAP() {
        return claveAdminLDAP;
    }

    public String getCanonicalName() {
        return canonicalName;
    }

    public String getBaseAD() {
        return baseAD;
    }
}
