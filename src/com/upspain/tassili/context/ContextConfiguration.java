/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.upspain.tassili.context;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import java.beans.PropertyVetoException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

/**
 *
 * @author ykantour
 */
public class ContextConfiguration {
    private static final Logger LOGGER = Logger.getLogger(ContextConfiguration.class.getName());
    private static final Map<String,ComboPooledDataSource> cpds;

    static{
        cpds = new HashMap<String,ComboPooledDataSource>();
        LOGGER.info("Iniciando Tassili");
        
        try{
            LOGGER.info("Cargando configuración de Tassili");
            InputStream tassiliBootStream;
            tassiliBootStream = ContextConfiguration.class.getClassLoader().getResourceAsStream("tassili.xml");
            if(tassiliBootStream == null){
                tassiliBootStream = new FileInputStream("tassili.xml");
            }
            
            JAXBContext jc = JAXBContext.newInstance(TassiliDataSource.class);
            Unmarshaller unmarshaller = jc.createUnmarshaller();
            JAXBElement<TassiliDataSource> jaxbelement = unmarshaller.unmarshal(new StreamSource(tassiliBootStream), TassiliDataSource.class);

            TassiliDataSource tbt = (TassiliDataSource) jaxbelement.getValue();
            if(tbt.getDataSources() == null || tbt.getDataSources().size() == 0){
                throw new RuntimeException("No hay ninguna fuente de datos");
            }
            LOGGER.info("Configurando fuentes de datos");
            
            //Añadir un pool por cada datasource especificado
            for(DataSourceConfiguration tb : tbt.getDataSources()){
                if(tb.getId() == null){
                    throw new RuntimeException("No existe el ID de l fuente de datos");
                }
                ComboPooledDataSource cpd = new ComboPooledDataSource();
                if(tb.getDataSource() != null){
                String driver = tb.getDataSource().getDriverClass();
                String server = tb.getDataSource().getServer();
                String ddbb = tb.getDataSource().getSchema();
                String user = tb.getDataSource().getUser();
                String pass = tb.getDataSource().getPass();
                String dbms = tb.getDataSource().getDBMS();
                
                if(driver == null){
                    throw new RuntimeException("Driver class ausente en la configuración");
                }
                if(server == null){
                    throw new RuntimeException("Servidor ausente en la configuración");
                }
                if(ddbb == null){
                    throw new RuntimeException("Base de datos ausente en la configuración");
                }
                if(user == null){
                    throw new RuntimeException("Usuario de BBDD ausente en la configuración");
                }
                if(pass == null){
                    throw new RuntimeException("Password ausente en la configuración");
                }
                if(dbms == null){
                    throw new RuntimeException("DBMS ausente en la configuración");
                }
                
                cpd.setDriverClass(driver);          
                cpd.setJdbcUrl(ConfigurationUtils.getJDBCURL(dbms.toLowerCase(), server, ddbb));
                cpd.setUser(user);                                  
                cpd.setPassword(pass);
            }else{
                throw new RuntimeException("DataSource ausente en la configuración");
            }
            
            
            LOGGER.info("Creando pool de conexiones");
            
            //Configuración del pool de conexiones
            //Si valores nulos, configuración por defecto
            if(tb.getPoolConfiguration() == null){
                LOGGER.log(Level.WARNING,"Configuración del pool inexistente. Se va a proceder a la configuración por defecto");
                cpd.setMinPoolSize(30);                                     
                cpd.setMaxPoolSize(100);
                cpd.setAcquireIncrement(5);
                cpd.setMaxIdleTime(240); 
            }else{
                String string_minPoolSize = tb.getPoolConfiguration().getMinPoolSize();
                String string_maxPoolSize = tb.getPoolConfiguration().getMaxPoolSize();
                String string_maxIdleTime = tb.getPoolConfiguration().getMaxIdleTime();
                String string_aquireIncrement = tb.getPoolConfiguration().getAquireIncrement();
                
                if(string_minPoolSize == null){
                    string_minPoolSize = "30";
                }
                if(string_maxPoolSize == null){
                    string_maxPoolSize = "100";
                }
                if(string_maxIdleTime == null){
                    string_maxIdleTime = "240";
                }
                if(string_aquireIncrement == null){
                    string_aquireIncrement = "5";
                }
                
                int minPoolSize = Integer.parseInt(string_minPoolSize);
                int maxPoolSize = Integer.parseInt(string_maxPoolSize);
                int maxIdleTime = Integer.parseInt(string_maxIdleTime);
                int aquireIncrement = Integer.parseInt(string_aquireIncrement);
                
                cpd.setMinPoolSize(minPoolSize);                                     
                cpd.setMaxPoolSize(maxPoolSize);
                cpd.setAcquireIncrement(aquireIncrement);
                cpd.setMaxIdleTime(maxIdleTime); 
            }   
            LOGGER.info("Configuración de la fuente de datos "+tb.getId()+" terminada");
            
            //Probar la conexión
            LOGGER.info("Probando conexión contra base de datos "+tb.getId());
            try(Connection con = cpd.getConnection()){
                LOGGER.info("Conectado con éxito");
            }catch(SQLException e){
                LOGGER.info("El origen de datos suministrado es erróneo");
                throw new RuntimeException(e);
            }
                cpds.put(tb.getId(), cpd);
            }
            LOGGER.info("Configurando de todas las fuentes de datos terminada con éxito");
        }catch(PropertyVetoException e){
            LOGGER.log(Level.SEVERE, "No se ha encontrado o podido cargar el fichero de configuración tassili.xml");
            throw new RuntimeException(e);
        }catch(JAXBException e){
            LOGGER.log(Level.SEVERE, "El fichero tassili.xml está corrumpido");
            throw new RuntimeException(e);
        }catch(NumberFormatException e){
            LOGGER.log(Level.SEVERE, "Se ha detectado una mala configuración de la de datos de Tassili");
            throw new RuntimeException(e);
        }catch(FileNotFoundException e){
            LOGGER.log(Level.SEVERE, "No se encuentra el fichero tassili.xml");
            throw new RuntimeException(e);
        }
    }
    
    public static ComboPooledDataSource getInstance(String id){
        return cpds.get(id);
    }
    
    public static void shutDown(){
        if(cpds != null){
            for(ComboPooledDataSource cp : cpds.values()){
                if(cp != null){
                    cp.close();
                }
            }
        }
    }
    
    public static int getNumberOfConnections(String id) throws SQLException{
        if(cpds.get(id) == null){
            return 0;
        }
        return cpds.get(id).getNumConnections();
    }
    
    public static int getNumberOfBusyConnections(String id) throws SQLException{
        if(cpds.get(id) == null){
            return 0;
        }
        return cpds.get(id).getNumBusyConnections();
    }
    
    public static int getNumberOfAvailableConnections(String id) throws SQLException{
        if(cpds.get(id) == null){
            return 0;
        }
        return cpds.get(id).getNumIdleConnections();
    }
}
