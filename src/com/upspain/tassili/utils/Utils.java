/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.upspain.tassili.utils;

import com.upspain.tassili.Id;
import com.upspain.tassili.data.Transform;
import com.upspain.tassili.exception.MappingException;
import com.upspain.tassili.persistence.AbstractEntity;
import java.lang.reflect.Field;

/**
 *
 * @author ykantour
 */
public class Utils {
    public static String capitalize(String s){
        if(s == null){
            return null;
        }
        if(s.isEmpty()){
            return "";
        }
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }
    
    public static String getResultSetMethodFromClass(Class<?> cl){
        if(cl == null){
            return null;
        }
        if(cl == Integer.class){
            return "getInt";
        }else{
            return "get"+capitalize(cl.getSimpleName());
        }
    }
    public static String getSetPreparedStaementMethodFromClass(Class<?> cl, Transform t){
        if(cl == null){
            return null;
        }
        if(cl == Integer.class){
            return "setInt";
        }
        if(t == Transform.DATE || t == Transform.DATETIME || t == Transform.XMLGREGORIANCALENDAR){
            return "setString";
        }else{
            return "set"+capitalize(cl.getSimpleName());
        }
    }

    public static Object defaultValue(Class<?> type) {
        if(type == int.class){
            return 0;
        }
        if(type == long.class){
            return 0L;
        }if(type == double.class){
            return 0.0d;
        }else{
            return false;
        }
        
    }
    
    public static boolean entityHasID(Class clazz){
        boolean has = false;
        for(Field f : clazz.getDeclaredFields()){
            f.setAccessible(true);
            if(f.isAnnotationPresent(Id.class)){
                has = true;
                break;
            }
        }
        return has;
    }
    
    public static Object getIdValue(AbstractEntity entity) throws MappingException{
        try{
            Object value = null;
            for(Field f : entity.getClass().getDeclaredFields()){
                f.setAccessible(true);
                if(f.isAnnotationPresent(Id.class)){
                    value = f.get(entity);
                    break;
                }
            }
            if(value == null){
                throw new MappingException("Either Id value of entity is null or entity has no Id annotation");
            }
            return value;
        }catch(IllegalAccessException e){
            throw new MappingException("Illegal access detected: "+e.getMessage());
        }
    }
}
