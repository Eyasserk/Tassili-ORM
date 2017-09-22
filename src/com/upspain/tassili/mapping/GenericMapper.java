/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.upspain.tassili.mapping;

import com.upspain.tassili.Column;
import com.upspain.tassili.data.Transform;
import com.upspain.tassili.exception.TransformException;
import com.upspain.tassili.utils.Utils;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author ykantour
 */
public class GenericMapper<E> implements Mapper<E>{
    
    public GenericMapper(){
    
    }
    
    @Override
    public E map(ResultSet r, Class c) throws SQLException, NoSuchMethodException,
            InstantiationException, IllegalAccessException, InvocationTargetException,
            ClassCastException, TransformException{
        E e = (E) c.newInstance();
        Field[] f = c.getDeclaredFields();
        for(Field field : f){
            field.setAccessible(true);
            if(field.isAnnotationPresent(Column.class)){
                //Obtener la información de la columna
                Column upColumn = field.getAnnotation(Column.class);
                
                //Crear setter
                Class fieldType = field.getType();
                String fieldName = field.getName();
                String capitalizeName = Utils.capitalize(fieldName);
                String methodName = "set"+capitalizeName;
                Method setter = e.getClass().getDeclaredMethod(methodName, fieldType);
                
                //Obtener el campo resultado de la BBDD
                Object o;
                if(upColumn.Transform().equals(Transform.DATE) ||
                   upColumn.Transform().equals(Transform.DATETIME) ||
                   upColumn.Transform().equals(Transform.XMLGREGORIANCALENDAR)){
                    o = r.getObject(upColumn.Name());
                }else{
                    Method metodo = r.getClass().getDeclaredMethod(Utils.getResultSetMethodFromClass(fieldType), String.class);
                    o = metodo.invoke(r, upColumn.Name());
                }
                
                //Aplicar el transform
                if(!field.getAnnotation(Column.class).Transform().equals(Transform.NONE) && o != null){
                    Transformer transformer = new GenericTransformer();
                    o = transformer.transform(o, field.getAnnotation(Column.class).Transform(), field.getAnnotation(Column.class).Format());
                }
                
                //Invocar el setter
                setter.invoke(e, o);
            }
        }
        return e;
    }
    
}
