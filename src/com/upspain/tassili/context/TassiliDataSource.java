/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.upspain.tassili.context;

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author ykantour
 */
@XmlRootElement(name = "TassiliDataSource")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TassiliDataSource")
public class TassiliDataSource {
    @XmlElement(name = "DataSourceConfiguration")
    private List<DataSourceConfiguration> dataSources;

    /**
     * @return the dataSources
     */
    public List<DataSourceConfiguration> getDataSources() {
        return dataSources;
    }

    /**
     * @param dataSources the dataSources to set
     */
    public void setDataSources(List<DataSourceConfiguration> dataSources) {
        this.dataSources = dataSources;
    }
}
