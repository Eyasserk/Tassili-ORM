/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.upspain.tassili.persistence;

import com.upspain.tassili.Column;
import com.upspain.tassili.Id;
import com.upspain.tassili.Member;
import com.upspain.tassili.Property;
import com.upspain.tassili.PropertyList;
import com.upspain.tassili.Table;
import com.upspain.tassili.context.ContextConfiguration;
import com.upspain.tassili.data.Criteria;
import com.upspain.tassili.data.Specification;
import com.upspain.tassili.data.Values;
import com.upspain.tassili.exception.MappingException;
import com.upspain.tassili.utils.Utils;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.logging.Logger;

/**
 *
 * @author ykantour
 */
public class AbstractEntity {
    private boolean fullAttributes = false;
    private static final Logger LOGGER = Logger.getLogger(AbstractEntity.class.getName());
    
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("{\n");
        for(Field f : getClass().getDeclaredFields()){
            f.setAccessible(true);
            if(fullAttributes || f.isAnnotationPresent(Column.class) || f.isAnnotationPresent(PropertyList.class) || f.isAnnotationPresent(Property.class) || f.isAnnotationPresent(Member.class)){
                //Si el field es de tipo collection
                if(!Collection.class.isAssignableFrom(f.getType())){
                    sb.append("\t");
                    sb.append(f.getName());
                    sb.append("=");
                    try{
                        sb.append(f.get(this));
                    }catch(IllegalAccessException e){
                        sb.append("UNREPORTED");
                    }
                    sb.append("\n");
                }else{
                    try{
                        ParameterizedType propType = (ParameterizedType) f.getGenericType();
                        Class<?> propClass = (Class<?>) propType.getActualTypeArguments()[0];
                        Collection c = (Collection) f.get(this);

                        sb.append("\t");
                        sb.append(f.getName());
                        sb.append(":");
                        sb.append(c == null ? "null": c.toString());
                        /**
                        sb.append("[");

                        for(Object o : c){
                            sb.append(o);
                            sb.append(",");
                        }
                        if(c.size() != 0){
                            sb.deleteCharAt(sb.length()-1);
                        }
                        sb.append("}");
                        */
                    }catch(IllegalAccessException e){
                        sb.append("\t");
                        sb.append(f.getName());
                        sb.append("=");
                        sb.append("UNREPORTED");
                    }
                    sb.append("\n");
                }
            }
        }
        sb.append("}");
        return sb.toString();
    }

    /**
     * @return the fullAttributes
     */
    public boolean isFullAttributes() {
        return fullAttributes;
    }

    /**
     * @param fullAttributes the fullAttributes to set
     */
    public void setFullAttributes(boolean fullAttributes) {
        this.fullAttributes = fullAttributes;
    }
    
    public String getTable(){
        if(getClass().isAnnotationPresent(Table.class)){
            return getClass().getAnnotation(Table.class).Name();
        }else{
            return null;
        }
    }
    
    public boolean insert() throws MappingException{
        try{
        boolean insercion = Service.newInstance(getClass()).insert(this);
        for(Field f : getClass().getDeclaredFields()){
            if (Collection.class.isAssignableFrom(f.getType()) && f.isAnnotationPresent(PropertyList.class)) {
                f.setAccessible(true);
                ParameterizedType propType = (ParameterizedType) f.getGenericType();
                Class<?> propClass = (Class<?>) propType.getActualTypeArguments()[0];
                
                Collection c = (Collection) f.get(this);
                for(Object o : c){
                    //insercion = insercion & gs.insert(propClass.cast(o));
                    insercion = insercion & ((AbstractEntity) propClass.cast(o)).insert();
                }
            }
            if (f.isAnnotationPresent(Property.class)) {
                    f.setAccessible(true);

                    insercion = insercion & ((AbstractEntity) f.get(this)).insert();
                }
        }
        return insercion;
        }catch(IllegalAccessException e){
            throw new MappingException("Se ha detectado un acceso ilegal: "+e.getMessage());
        }
    }
    
    public boolean update() throws MappingException{
        try{
            boolean update = Service.newInstance(getClass()).update(this);
            for(Field f : getClass().getDeclaredFields()){
                if (Collection.class.isAssignableFrom(f.getType()) && f.isAnnotationPresent(PropertyList.class)) {
                    f.setAccessible(true);
                    ParameterizedType propType = (ParameterizedType) f.getGenericType();
                    Class<?> propClass = (Class<?>) propType.getActualTypeArguments()[0];

                    Collection c = (Collection) f.get(this);
                    for(Object o : c){
                        //update = update & gs.update(propClass.cast(o));
                        update = update & ((AbstractEntity) propClass.cast(o)).update();
                    }
                }
                if (f.isAnnotationPresent(Property.class)) {
                    f.setAccessible(true);

                    update = update & ((AbstractEntity) f.get(this)).update();
                }
            }
            return update;
        }catch(IllegalAccessException e){
            throw new MappingException("Se ha detectado un acceso ilegal: "+e.getMessage());
        }
    }
    
    public boolean delete() throws MappingException{
        try{
            boolean delete = Service.newInstance(getClass()).delete(this);
            for(Field f : getClass().getDeclaredFields()){
                if (Collection.class.isAssignableFrom(f.getType()) && f.isAnnotationPresent(PropertyList.class)) {
                    f.setAccessible(true);
                    ParameterizedType propType = (ParameterizedType) f.getGenericType();
                    Class<?> propClass = (Class<?>) propType.getActualTypeArguments()[0];

                    Collection c = (Collection) f.get(this);
                    boolean deleteExecuted = false;
                    for(Object o : c){
                        //delete = delete & gs.delete(propClass.cast(o));
                        AbstractEntity child = (AbstractEntity) propClass.cast(o); 
                        if(Utils.entityHasID(child.getClass())){
                            delete = delete & child.delete();
                        }else{
                            if(!deleteExecuted){
                                AbstractFilter filter = new AbstractFilter() {};
                                Specification spec = new Specification(f.getAnnotation(PropertyList.class).Target(), new Values(Utils.getIdValue(this)) , Criteria.EQUAL);
                                filter.addSpecification(spec);
                                delete = delete & Service.newInstance(child.getClass()).deleteByFilter(filter);
                                deleteExecuted = true;
                            }
                        }
                    }
                }
                if (f.isAnnotationPresent(Property.class)) {
                    f.setAccessible(true);
                    if(Utils.entityHasID(f.getType())){
                        delete = delete & ((AbstractEntity) f.get(this)).delete();
                    }else{
                        AbstractFilter filter = new AbstractFilter() {};
                        Specification spec = new Specification(f.getAnnotation(PropertyList.class).Target(), new Values(Utils.getIdValue(this)) , Criteria.EQUAL);
                        filter.addSpecification(spec);
                        delete = delete & Service.newInstance(f.getType()).deleteByFilter(filter);
                    }
                }
            }
            return delete;
        }catch(IllegalAccessException e){
            throw new MappingException("Se ha detectado un acceso ilegal: "+e.getMessage());
        }
    }
    
    public long getNextId() throws MappingException{
        if(getClass().isAnnotationPresent(Table.class)){
            Table table = getClass().getAnnotation(Table.class);
            Field idField = null;
            for(Field field : getClass().getDeclaredFields()){
                field.setAccessible(true);
                if(field.isAnnotationPresent(Id.class)){
                    idField = field;
                    break;
                }
            }
            if(idField == null){
                throw new MappingException("No se puede obtener el next ID de una clase que no tiene ID");
            }
            idField.setAccessible(true);
            try(Connection conn = ContextConfiguration.getInstance(table.DataSourceId()).getConnection();){
                String query = "SELECT MAX("+idField.getName()+") AS MAXID FROM "+table.Name();
                PreparedStatement ps = conn.prepareStatement(query);
                ResultSet rs = ps.executeQuery();
                long id;
                if(rs.next()){
                    id = rs.getLong("MAXID") + 1;
                }else{
                    id = 1;
                }
                rs.close();
                ps.close();
                return id;
            }catch(SQLException e){
                throw new MappingException("Error SQL: "+e.getMessage());
            }
        }else{
            throw new MappingException("No se puede obtener el next ID de una clase que no tiene la anotación Table");
        } 
    }
}
