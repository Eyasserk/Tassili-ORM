/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.upspain.tassili.context;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author ykantour
 */
@XmlRootElement(name = "DataSourceConfiguration")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DataSourceConfiguration", propOrder = {
    "DataSource",
    "PoolConfiguration"
})
public class DataSourceConfiguration {
    @XmlAttribute(name = "Id")
    private String id;
    private DataSource DataSource;
    private PoolConfiguration PoolConfiguration;

    /**
     * @return the DataSource
     */
    public DataSource getDataSource() {
        return DataSource;
    }

    /**
     * @param DataSource the DataSource to set
     */
    public void setDataSource(DataSource DataSource) {
        this.DataSource = DataSource;
    }

    /**
     * @return the PoolConfiguration
     */
    public PoolConfiguration getPoolConfiguration() {
        return PoolConfiguration;
    }

    /**
     * @param PoolConfiguration the PoolConfiguration to set
     */
    public void setPoolConfiguration(PoolConfiguration PoolConfiguration) {
        this.PoolConfiguration = PoolConfiguration;
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }
    
}
