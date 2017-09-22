/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.upspain.tassili.context;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author ykantour
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PoolConfiguration", propOrder = {
    "MinPoolSize",
    "MaxPoolSize",
    "AquireIncrement",
    "MaxIdleTime"
})
@XmlRootElement(name = "PoolConfiguration", namespace = "")
public class PoolConfiguration {
    @XmlElement(name = "MinPoolSize")
    private String MinPoolSize;
    @XmlElement(name = "MaxPoolSize")
    private String MaxPoolSize;
    @XmlElement(name = "AquireIncrement")
    private String AquireIncrement;
    @XmlElement(name = "MaxIdleTime")
    private String MaxIdleTime;

    /**
     * @return the MinPoolSize
     */
    public String getMinPoolSize() {
        return MinPoolSize;
    }

    /**
     * @param MinPoolSize the MinPoolSize to set
     */
    public void setMinPoolSize(String MinPoolSize) {
        this.MinPoolSize = MinPoolSize;
    }

    /**
     * @return the MaxPoolSize
     */
    public String getMaxPoolSize() {
        return MaxPoolSize;
    }

    /**
     * @param MaxPoolSize the MaxPoolSize to set
     */
    public void setMaxPoolSize(String MaxPoolSize) {
        this.MaxPoolSize = MaxPoolSize;
    }

    /**
     * @return the AquireIncrement
     */
    public String getAquireIncrement() {
        return AquireIncrement;
    }

    /**
     * @param AquireIncrement the AquireIncrement to set
     */
    public void setAquireIncrement(String AquireIncrement) {
        this.AquireIncrement = AquireIncrement;
    }

    /**
     * @return the MaxIndleTime
     */
    public String getMaxIdleTime() {
        return MaxIdleTime;
    }

    /**
     * @param MaxIdleTime the MaxIndleTime to set
     */
    public void setMaxIdleTime(String MaxIdleTime) {
        this.MaxIdleTime = MaxIdleTime;
    }
}
