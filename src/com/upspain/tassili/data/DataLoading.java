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
public enum DataLoading {
    LAZY("LAZY"),
    EAGER("EAGER");
    
    private String value;

    private DataLoading(String value) {
        this.value = value;
    }
    
    public String getValue(){
        return value;
    }
}
