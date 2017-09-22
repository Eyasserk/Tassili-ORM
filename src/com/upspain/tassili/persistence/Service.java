/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.upspain.tassili.persistence;

import com.upspain.tassili.Table;
import com.upspain.tassili.context.ContextConfiguration;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Logger;

/**
 *
 * @author ykantour
 */
public class Service {
    private static final Logger LOGGER = Logger.getLogger(Service.class.getName());
    public static GenericService newInstance(Class<?> c){
        return new GenericService(c,c.getAnnotation(Table.class).DataSourceId());
    }
    
    public static int executeUpdate(String query, String dataSourceId) throws SQLException{
        LOGGER.info(query.toUpperCase());
        try(Connection conn = ContextConfiguration.getInstance(dataSourceId).getConnection();){
            PreparedStatement ps = conn.prepareStatement(query);
            int n = ps.executeUpdate();
            return n;
        }catch(SQLException e){
            throw new SQLException(e);
        }
    }
    
    public static int getNumberOfConnections(String dataSourceId) throws SQLException{
        return ContextConfiguration.getNumberOfConnections(dataSourceId);
    }
    
    public static int getNumberOfBusyConnections(String dataSourceId) throws SQLException{
        return ContextConfiguration.getNumberOfBusyConnections(dataSourceId);
    }
    
    public static int getNumberOfAvailableConnections(String daSourceId) throws SQLException{
        return ContextConfiguration.getNumberOfAvailableConnections(daSourceId);
    }
}
