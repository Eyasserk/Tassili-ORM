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
public enum Criteria{
    
    GREATER (">"),
    GREATER_OR_EQUAL (">="),
    EQUAL ("="),
    NOT_EQUAL("<>"),
    LESS ("<"),
    LESS_OR_EQUAL ("<="),
    LIKE("LIKE"),
    BETWEEN ("BETWEEN");
    
    private String value;
    
    Criteria(final String value){
        this.value = value;
    }
    
    public String getValue(){
        return value;
    }
}
