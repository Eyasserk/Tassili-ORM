/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.upspain.tassili.context;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author ykantour
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DataSource", propOrder = {
    "Server",
    "Schema",
    "User",
    "Pass",
    "DriverClass",
    "DBMS"
})
public class DataSource {
    private String Server;
    private String Schema;
    private String User;
    private String Pass;
    private String DriverClass;
    private String DBMS;

    /**
     * @return the Server
     */
    public String getServer() {
        return Server;
    }

    /**
     * @param Server the Server to set
     */
    public void setServer(String Server) {
        this.Server = Server;
    }

    /**
     * @return the Schema
     */
    public String getSchema() {
        return Schema;
    }

    /**
     * @param Schema the Schema to set
     */
    public void setSchema(String Schema) {
        this.Schema = Schema;
    }

    /**
     * @return the User
     */
    public String getUser() {
        return User;
    }

    /**
     * @param User the User to set
     */
    public void setUser(String User) {
        this.User = User;
    }

    /**
     * @return the Pass
     */
    public String getPass() {
        return Pass;
    }

    /**
     * @param Pass the Pass to set
     */
    public void setPass(String Pass) {
        this.Pass = Pass;
    }

    /**
     * @return the DriverClass
     */
    public String getDriverClass() {
        return DriverClass;
    }

    /**
     * @param DriverClass the DriverClass to set
     */
    public void setDriverClass(String DriverClass) {
        this.DriverClass = DriverClass;
    }

    /**
     * @return the DBMS
     */
    public String getDBMS() {
        return DBMS;
    }

    /**
     * @param DBMS the DBMS to set
     */
    public void setDBMS(String DBMS) {
        this.DBMS = DBMS;
    }

    
}
