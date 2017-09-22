/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.upspain.tassili.exception;

import java.sql.SQLException;

/**
 *
 * @author ykantour
 */
public class MappingException extends SQLException{
    public MappingException(String msg){
        super(msg);
    }
}
