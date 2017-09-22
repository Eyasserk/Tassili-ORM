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
public enum RegisterState {
    ALL("ALL"),
    ACTIVE("ACTIVE");
    
    private String value;
    
    RegisterState(String value){
        this.value = value;
    }
    
    public String getValue(){
        return value;
    }
}
