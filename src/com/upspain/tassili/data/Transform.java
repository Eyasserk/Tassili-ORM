/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.upspain.tassili.data;

/**
 *
 * @author ykantour
 */
public enum Transform {
    UPPER("UPPER"),
    LOWER("LOWER"),
    TRIM("TRIM"),
    DATE("DATE"),
    DATETIME("DATETIME"),
    XMLGREGORIANCALENDAR("XMLGREGORIANCALENDAR"),
    NONE("NONE");
    
    private String value;
    
    Transform(final String value){
        this.value = value;
    }
    
    public String getValue(){
        return value;
    }
}
