/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.upspain.tassili.context;

import java.util.logging.Logger;

/**
 *
 * @author ykantour
 */
public class ConfigurationUtils {
    private static final Logger LOGGER = Logger.getLogger(ConfigurationUtils.class.getName());
    
    public static String getJDBCURL(String dbms, String server, String ddbb){
        if(dbms == null || dbms.isEmpty()){
            throw new IllegalArgumentException("DBMS not provided");
        }
        switch(dbms.toLowerCase()){
            //
            default: LOGGER.info("Connecting to JDBC "+"jdbc:"+dbms+"://"+server+"/"+ddbb); return "jdbc:"+dbms+"://"+server+"/"+ddbb;
        }
    }
}
