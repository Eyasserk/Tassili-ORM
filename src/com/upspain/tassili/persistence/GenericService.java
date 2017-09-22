/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.upspain.tassili.persistence;

import com.upspain.tassili.Column;
import com.upspain.tassili.FilterTable;
import com.upspain.tassili.Id;
import com.upspain.tassili.Member;
import com.upspain.tassili.Property;
import com.upspain.tassili.PropertyList;
import com.upspain.tassili.Table;
import com.upspain.tassili.context.ContextConfiguration;
import com.upspain.tassili.data.Criteria;
import com.upspain.tassili.data.DataLoading;
import com.upspain.tassili.data.RegisterState;
import com.upspain.tassili.data.Specification;
import com.upspain.tassili.data.Transform;
import com.upspain.tassili.data.Values;
import com.upspain.tassili.exception.MappingException;
import com.upspain.tassili.exception.TransformException;
import com.upspain.tassili.mapping.GenericMapper;
import com.upspain.tassili.mapping.Mapper;
import com.upspain.tassili.utils.Utils;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.datatype.XMLGregorianCalendar;
import org.joda.time.DateTime;

/**
 *
 * @author ykantour
 */
public class GenericService<E,F extends AbstractFilter> implements AbstractGenericService<E,F>{
    
    private static final Logger LOGGER = Logger.getLogger(GenericService.class.getName());
    public Class<E> entityClass;
    private String dataSourceId;
    
    public GenericService(Class<E> entityClass, String dataSourceId){
        this.entityClass = entityClass;
        this.dataSourceId = dataSourceId;
    }
    
    /**
     * 
     * @param filter : filtro de busqueda Query
     * @return lista de entities correspondientes al filtro de busqueda
     * @throws MappingException cuando hay un error de mapeo
     */
    @Override
    public List<E> findAll(F filter) throws MappingException{
        try(Connection con = ContextConfiguration.getInstance(dataSourceId).getConnection()){
            List<E> list = new ArrayList<E>();
            //Obtener la tabla y esquema de la clase indicada
            Mapper<E> mapper = new GenericMapper<E>();
            
            String table = entityClass.getAnnotation(Table.class).Name();
            
            
            String queryString = "select * from "+table;
            if(filter != null){
                if(RegisterState.ACTIVE.equals(filter.getClass().getAnnotation(FilterTable.class).FilterBy())){
                    Specification spec = new Specification(filter.getClass().getAnnotation(FilterTable.class).ActiveColumnName(),
                            new Values(true),
                            Criteria.EQUAL);
                    filter.addSpecification(spec);
                }
                queryString += filter.printSpecifications(filter.getSpecifications(),true);
            }
            LOGGER.log(Level.INFO, queryString.toUpperCase());
            PreparedStatement ps = con.prepareStatement(queryString);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                E e = mapper.map(rs, entityClass);
                
                //Añadir sus Propiedades
                for(final Field field : e.getClass().getDeclaredFields()){
                    field.setAccessible(true);
                    
                    //Añadir las Property List: Relación 1-N
                    if(field.isAnnotationPresent(PropertyList.class) && DataLoading.EAGER.equals(field.getAnnotation(PropertyList.class).Loading())){
                        //Sacar el nombre del metodo
                        Method method = e.getClass().getDeclaredMethod("set"+Utils.capitalize(field.getName()), field.getType());
                        
                        //Sacar el tipo de dato de los elementos de la colección
                        ParameterizedType propType = (ParameterizedType) field.getGenericType();
                        Class<?> propClass = (Class<?>) propType.getActualTypeArguments()[0];
                        
                        //Sacar el id 
                        Object id = null;
                        for(Field f : e.getClass().getDeclaredFields()){
                            f.setAccessible(true);
                            if(f.isAnnotationPresent(Id.class)){
                                id = f.get(e);
                                break;
                            }
                        }
                        if(id == null){
                            throw new MappingException("No se puede cargar el property "+
                                    field.getName()+" de "+e.getClass().getSimpleName()+
                                    ". Anotación @Id ausente en "+e.getClass().getSimpleName());
                        }
                        final Object idObject = id;
                        AbstractFilter propFilter = new AbstractFilter() {
                            //No cambio ningun metodo
                        };
                        Specification specOr = new Specification(field.getAnnotation(PropertyList.class).Target(),
                                        new Values(idObject),
                                        Criteria.EQUAL);
                        propFilter.addSpecification(specOr);
                        method.invoke(e, Service.newInstance(propClass).findAll(propFilter));
                    }
                    
                    //Añadir las Property: Relación 1-1
                    if(field.isAnnotationPresent(Property.class) && DataLoading.EAGER.equals(field.getAnnotation(Property.class).Loading())){
                        //Sacar el nombre del metodo
                        Method method = e.getClass().getDeclaredMethod("set"+Utils.capitalize(field.getName()), field.getType());
                        
                        //Sacar el tipo de dato de la property
                        
                        //Sacar el id 
                        Long id = null;
                        for(Field f : e.getClass().getDeclaredFields()){
                            f.setAccessible(true);
                            if(f.isAnnotationPresent(Id.class)){
                                id = (Long) f.get(e);
                                break;
                            }
                        }
                        if(id == null){
                            throw new MappingException("No se puede cargar el property "+
                                    field.getName()+" de "+e.getClass().getSimpleName()+
                                    ". Anotación @Id ausente en "+e.getClass().getSimpleName());
                        }
                        method.invoke(e, Service.newInstance(field.getType()).findById(id));
                    }
                    
                    //Añadir los Member: id que apunta a otra tabla
                    if(field.isAnnotationPresent(Member.class) && DataLoading.EAGER.equals(field.getAnnotation(Member.class).Loading())){
                        //Obtener el objeto al que apunta el id
                        Method method = e.getClass().getDeclaredMethod("set"+Utils.capitalize(field.getName()), field.getType());
                        Long foreignKey = rs.getLong(field.getAnnotation(Member.class).Name());
                        method.invoke(e, Service.newInstance(field.getType()).findById(foreignKey));
                    }
                }
                
                list.add(e);
            }
            rs.close();
            return list;
            
        }catch(IllegalAccessException e){
            throw new MappingException("Se ha detectado un acceso ilegal: "+e.getMessage());
        }catch(InstantiationException e){
            throw new MappingException("Error al instanciar un objeto genérico: "+e.getMessage());
        }catch(InvocationTargetException e){
            throw new RuntimeException(e);
            //throw new MappingException("Error al invocar un método: "+e.getMessage());
        }catch(NoSuchMethodException e){
            throw new MappingException("Método inexiste: "+e.getMessage());
        }catch(SQLException e){
            throw new MappingException("Error SQL: "+e.getMessage());
        }catch(ClassCastException e){
            throw new MappingException("Tipo de dato declarado erróneo: "+e.getMessage());
        }catch(TransformException e){
            throw new MappingException("No se ha podido transformar el objeto: "+e.getMessage());
        }
    }
    
    /**
     * 
     * @param entity los campos a actualizar
     * @param filter los parametros donde se actualizan los campos de arriba
     * @return true si actualiza false si no
     * @throws MappingException 
     */
    @Override
    public boolean update(E entity, F filter) throws MappingException{
        try(Connection con = ContextConfiguration.getInstance(dataSourceId).getConnection()){
            String table = entityClass.getAnnotation(Table.class).Name();
            StringBuilder queryString = new StringBuilder();
            queryString.append("update ");
            queryString.append(table);
            queryString.append(" set");
            for(Field field : entityClass.getDeclaredFields()){
                field.setAccessible(true);
                if(field.isAnnotationPresent(Column.class) && field.get(entity) != null
                    && !(field.getType().isPrimitive() && field.get(entity).equals(Utils.defaultValue(field.getType())))    
                        ){
                    queryString.append(" ");
                    queryString.append(field.getAnnotation(Column.class).Name());
                    queryString.append(" = ");
                    queryString.append("?");
                    queryString.append(",");
                }
            }
            queryString.deleteCharAt(queryString.length()-1);
            queryString.append(" ");
            if(filter != null && filter.getSpecifications() != null && filter.getSpecifications().size() > 0){
                queryString.append("where ");
                queryString.append(filter.printSpecifications(filter.getSpecifications(),false));
            }
            LOGGER.log(Level.INFO, queryString.toString().toUpperCase());
            PreparedStatement ps = con.prepareStatement(queryString.toString());
            int i = 0;
            for(Field field : entityClass.getDeclaredFields()){
                field.setAccessible(true);
                if(field.isAnnotationPresent(Column.class) && field.get(entity) != null
                    && !(field.getType().isPrimitive() && field.get(entity).equals(Utils.defaultValue(field.getType())))        
                    ){
                    i++;
                    String metodoPS = Utils.getSetPreparedStaementMethodFromClass(field.getType(), field.getAnnotation(Column.class).Transform());
                    Method m = ps.getClass().getDeclaredMethod(metodoPS, int.class, field.getType());
                    m.invoke(ps, i, field.get(entity));
                }
            }
            boolean exe = ps.execute();
            ps.close();
            return exe;
        }catch(SQLException e){
            throw new MappingException("Error SQL: "+e.getMessage()+". Comprobar nombres de columnas en las entities");
        }catch(IllegalAccessException e){
            throw new MappingException("Se ha detectado un acceso ilegal: "+e.getMessage());
        }catch(NoSuchMethodException e){
            throw new MappingException("No se puede crear el PreparedStatement: Método inexistente. "+e.getMessage());
        }catch(InvocationTargetException e){
            throw new MappingException("Error al invocar un método de PreparedStaement: "+e.getMessage());
        }
    }
    
    /**
     * 
     * @param entity
     * @return
     * @throws MappingException 
     */
    @Override
    public boolean delete(E entity) throws MappingException{
        try(Connection con = ContextConfiguration.getInstance(dataSourceId).getConnection()){
            String table = entityClass.getAnnotation(Table.class).Name();
            StringBuilder queryString = new StringBuilder();
            queryString.append("delete from ");
            queryString.append(table);
            queryString.append(" where");
            String idColumnName = "";
            for(Field field : entityClass.getDeclaredFields()){
                field.setAccessible(true);
                if(field.isAnnotationPresent(Id.class) && field.get(entity) != null
                    && !(field.getType().isPrimitive() && field.get(entity).equals(Utils.defaultValue(field.getType())))
                        ){
                    idColumnName = field.getName();
                    queryString.append(" ");
                    queryString.append(field.getAnnotation(Column.class).Name());
                    queryString.append(" = ");
                    queryString.append(field.get(entity));
                    queryString.append("");
                    break;
                }
            }
            if(idColumnName.equals("")){
                throw new MappingException("No se puede borrar una entidad sin Id. Utilizar método de pasar filtro");
            }
            LOGGER.log(Level.INFO, queryString.toString().toUpperCase());
            PreparedStatement ps = con.prepareStatement(queryString.toString());
            boolean exe = ps.execute();
            ps.close();
            return exe;
        }catch(SQLException e){
            throw new MappingException("Error SQL: "+e.getMessage());
        }catch(IllegalAccessException e){
            throw new MappingException("Se ha detectado un acceso ilegal: "+e.getMessage());
        }
    }
    
    /**
     * 
     * @param entity
     * @param filter
     * @return
     * @throws MappingException 
     */
    @Override
    public boolean deleteByFilter(F filter) throws MappingException {
        try(Connection con = ContextConfiguration.getInstance(dataSourceId).getConnection()){
            String table = entityClass.getAnnotation(Table.class).Name();
            StringBuilder queryString = new StringBuilder();
            queryString.append("delete from ");
            queryString.append(table);
            queryString.append(" where ");
            queryString.append(filter.printSpecifications(filter.getSpecifications(), false));
            LOGGER.log(Level.INFO, queryString.toString().toUpperCase());
            PreparedStatement ps = con.prepareStatement(queryString.toString());
            boolean exe = ps.execute();
            ps.close();
            return exe;
        }catch(SQLException e){
            throw new MappingException("Error SQL: "+e.getMessage());
        }catch(IllegalAccessException e){
            throw new MappingException("Se ha detectado un acceso ilegal: "+e.getMessage());
        }
    }
    
    /**
     * 
     * @param entity
     * @return
     * @throws MappingException 
     */
    @Override
    public boolean insert(E entity) throws MappingException{
        try(Connection con = ContextConfiguration.getInstance(dataSourceId).getConnection()){
            String query = getInsertQuery();
            LOGGER.log(Level.INFO, query);
            PreparedStatement ps = con.prepareStatement(query);
            
            //Preparar el statemen
            int i=0;
            for(Field field : entityClass.getDeclaredFields()){
                field.setAccessible(true);
                if(!field.isAnnotationPresent(Id.class) || (field.isAnnotationPresent(Id.class) && !field.getAnnotation(Id.class).AutoIncrement())){
                    if(field.isAnnotationPresent(Column.class)){
                        i++;
                        Method setter = getPreparedStatementSetterMethod(field);
                        Object ob = getValueOfField(field, entity);
                        setter.invoke(ps, i, ob);
                    }
                }
            }
            int result = ps.executeUpdate();
            ps.close();
            if(result == 1){
                return true;
            }else{
                return false;
            }
        }catch(SQLException e){
            throw new MappingException("Error SQL: "+e.getMessage());
        }catch(IllegalAccessException e){
            throw new MappingException("Se ha detectado un acceso ilegal: "+e.getMessage());
        }catch(InvocationTargetException e){
            throw new MappingException("Error al invocar un método de PreparedStaement: "+e.getMessage());
        }catch(NoSuchMethodException e){
             throw new MappingException("No se puede crear el PreparedStatement: Método inexistente. "+e.getMessage());
        }
    }

    /**
     * 
     * @param id
     * @return
     * @throws MappingException 
     */
    @Override
    public E findById(long id_) throws MappingException {
       E entity = null;
        try(Connection con = ContextConfiguration.getInstance(dataSourceId).getConnection()){
            Mapper<E> mapper = new GenericMapper<E>();
            
            String table = entityClass.getAnnotation(Table.class).Name();
            String idFieldName = "";
            for(Field f : entityClass.getDeclaredFields()){
                f.setAccessible(true);
                if(f.isAnnotationPresent(Id.class)){
                    idFieldName = f.getAnnotation(Column.class).Name();
                    break;
                }
            }
            if(idFieldName == ""){
                throw new MappingException("Id no especificado en la clase "+entityClass.getSimpleName());
            }
            
            String queryString = "select * from "+table+" where "+idFieldName+" = "+id_;
            LOGGER.log(Level.INFO, queryString.toUpperCase());
            PreparedStatement ps = con.prepareStatement(queryString);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                entity = mapper.map(rs, entityClass);
                //Añadir sus PropertyList
                for(final Field field : entity.getClass().getDeclaredFields()){
                    field.setAccessible(true);
                    if(field.isAnnotationPresent(PropertyList.class) && DataLoading.EAGER.equals(field.getAnnotation(PropertyList.class).Loading())){
                        //Sacar el nombre del metodo
                        Method method = entity.getClass().getDeclaredMethod("set"+Utils.capitalize(field.getName()), field.getType());
                        
                        //Sacar el tipo de dato de los elementos de la colección
                        ParameterizedType propType = (ParameterizedType) field.getGenericType();
                        Class<?> propClass = (Class<?>) propType.getActualTypeArguments()[0];
                        
                        //Sacar el id 
                        Object id = null;
                        for(Field f : entity.getClass().getDeclaredFields()){
                            f.setAccessible(true);
                            if(f.isAnnotationPresent(Id.class)){
                                id = f.get(entity);
                                break;
                            }
                        }
                        if(id == null){
                            throw new MappingException("No se puede cargar el property "+
                                    field.getName()+" de "+entity.getClass().getSimpleName()+
                                    ". Anotación @Id ausente en "+entity.getClass().getSimpleName());
                        }
                        final Object idObject = id;
                        AbstractFilter propFilter = new AbstractFilter() {
                            //No cambio ningun metodo
                        };
                        Specification specOr = new Specification(field.getAnnotation(PropertyList.class).Target(),
                                        new Values(idObject),
                                        Criteria.EQUAL);
                        propFilter.addSpecification(specOr);
                        method.invoke(entity, Service.newInstance(propClass).findAll(propFilter));
                    }
                    if(field.isAnnotationPresent(Property.class) && DataLoading.EAGER.equals(field.getAnnotation(Property.class).Loading())){
                        //Sacar el nombre del metodo
                        Method method = entity.getClass().getDeclaredMethod("set"+Utils.capitalize(field.getName()), field.getType());
                        
                        //Sacar el tipo de dato de la property
                        
                        //Sacar el id 
                        Long id = null;
                        for(Field f : entity.getClass().getDeclaredFields()){
                            f.setAccessible(true);
                            if(f.isAnnotationPresent(Id.class)){
                                id = (Long) f.get(entity);
                                break;
                            }
                        }
                        if(id == null){
                            throw new MappingException("No se puede cargar el property "+
                                    field.getName()+" de "+entity.getClass().getSimpleName()+
                                    ". Anotación @Id ausente en "+entity.getClass().getSimpleName());
                        }
                        method.invoke(entity, Service.newInstance(field.getType()).findById(id));
                    }
                    if(field.isAnnotationPresent(Member.class) && DataLoading.EAGER.equals(field.getAnnotation(Member.class).Loading())){
                        //Obtener el objeto al que apunta el id
                        LOGGER.info("Buscando un miembro con id "+field.getLong(entity));
                        Method method = entity.getClass().getDeclaredMethod("set"+Utils.capitalize(field.getName()), field.getType());
                        Long foreignKey = field.getLong(entity);
                        method.invoke(entity, Service.newInstance(field.getType()).findById(foreignKey));
                    }
                }
            }
            return entity;
        }catch(IllegalAccessException e){
            throw new MappingException("Se ha detectado un acceso ilegal: "+e.getMessage());
        }catch(InstantiationException e){
            throw new MappingException("Error al instanciar un objeto genérico: "+e.getMessage());
        }catch(InvocationTargetException e){
            throw new MappingException("Error al invocar un método: "+e.getMessage());
        }catch(NoSuchMethodException e){
            throw new MappingException("Método inexiste: "+e.getMessage());
        }catch(SQLException e){
            throw new MappingException("Error SQL: "+e.getMessage());
        }catch(ClassCastException e){
            throw new MappingException("Tipo de dato declarado erróneo: "+e.getMessage());
        }catch(TransformException e){
            throw new MappingException("No se ha podido transformar el objeto: "+e.getMessage());
        }
    }

    @Override
    public boolean update(E entity) throws MappingException {
        try(Connection con = ContextConfiguration.getInstance(dataSourceId).getConnection()){
            String query = getUpdateQueryWithId(entity);
            LOGGER.log(Level.INFO, query);
            PreparedStatement ps = con.prepareStatement(query);
            
            //Preparar el statemen
            int i=0;
            for(Field field : entityClass.getDeclaredFields()){
                field.setAccessible(true);
                if(field.isAnnotationPresent(Column.class) && !field.isAnnotationPresent(Id.class) && field.get(entity) != null){
                    i++;
                    Method setter = getPreparedStatementSetterMethod(field);
                    Object ob = getValueOfField(field, entity);
                    setter.invoke(ps, i, ob);
                }
            }
            boolean result = ps.execute();
            ps.close();
            return result;
        }catch(SQLException e){
            throw new MappingException("Error SQL: "+e.getMessage());
        }catch(IllegalAccessException e){
            throw new MappingException("Se ha detectado un acceso ilegal: "+e.getMessage());
        }catch(InvocationTargetException e){
            throw new MappingException("Error al invocar un método de PreparedStaement: "+e.getMessage());
        }catch(NoSuchMethodException e){
             throw new MappingException("No se puede crear el PreparedStatement: Método inexistente. "+e.getMessage());
        }
    }

    
    /**
     * Obtiene la query update de un objeto de la clase entity 
     * @param entity
     * @return  query string de la entity
     * @throws IllegalArgumentException
     * @throws IllegalAccessException 
     */
    private String getUpdateQueryWithId(E entity) throws IllegalArgumentException, IllegalAccessException{
        StringBuilder sb = new StringBuilder();
        //Obtener el nombre de la tabla
        String table = entityClass.getAnnotation(Table.class).Name();

        //Obtener el field ID
        Field idField = null;
        for(Field f : entityClass.getDeclaredFields()){
            if(f.isAnnotationPresent(Id.class)){
                f.setAccessible(true);
                idField = f;
                break;
            }
        }
        idField.setAccessible(true);
        sb.append("update ");
        sb.append(table);
        sb.append(" set");
        for(Field f : entityClass.getDeclaredFields()){
            f.setAccessible(true);
            if(f.isAnnotationPresent(Column.class) && !f.isAnnotationPresent(Id.class) && f.get(entity) != null){
                Column column = f.getAnnotation(Column.class);
                sb.append(" ");
                sb.append(column.Name());
                sb.append(" = ?,");
            }
        }
        sb.deleteCharAt(sb.length()-1);
        sb.append(" where ");
        sb.append(idField.getAnnotation(Column.class).Name());
        sb.append(" = ");
        sb.append(idField.get(entity));
        return sb.toString();
    }
    
    /**
     * Método para obtener la query delete de un objeto de la clase entity
     * @param entity
     * @return query string detallada arriba
     * @throws IllegalArgumentException
     * @throws IllegalAccessException 
     */
    private String getDeleteQueryWithId(E entity) throws IllegalArgumentException, IllegalAccessException{
        StringBuilder sb = new StringBuilder();
        //Obtener el nombre de la tabla
        String table = entityClass.getAnnotation(Table.class).Name();

        //Obtener el field ID
        Field idField = null;
        for(Field f : entityClass.getDeclaredFields()){
            if(f.isAnnotationPresent(Id.class)){
                f.setAccessible(true);
                idField = f;
                break;
            }
        }
        if(idField != null){
            idField.setAccessible(true);
        }
        
        sb.append("delete from ");
        sb.append(table);
        sb.append(" where ");
        sb.append(idField.getAnnotation(Column.class).Name());
        sb.append(" = ");
        sb.append(idField.get(entity));
        return sb.toString();
    }
    
    /**
     * Método para devolver la query insert de la clase entity
     * @return Query String de la clase entity
     * @throws IllegalArgumentException
     * @throws IllegalAccessException 
     */
    private String getInsertQuery() throws IllegalArgumentException, IllegalAccessException{
        StringBuilder sb = new StringBuilder();
        StringBuilder sb2 = new StringBuilder();
        //Obtener el nombre de la tabla
        String table = entityClass.getAnnotation(Table.class).Name();

        sb.append("insert into ");
        sb.append(table);
        sb.append(" (");
        sb2.append(" values(");
        for(Field f : entityClass.getDeclaredFields()){
            f.setAccessible(true);
            if(!f.isAnnotationPresent(Id.class) || (f.isAnnotationPresent(Id.class) && !f.getAnnotation(Id.class).AutoIncrement())){
                if(f.isAnnotationPresent(Column.class)){
                    sb.append(f.getAnnotation(Column.class).Name());
                    sb.append(",");

                    sb2.append("?");
                    sb2.append(",");
                }
            }
        }
        sb.deleteCharAt(sb.length()-1);
        sb2.deleteCharAt(sb2.length()-1);
        sb.append(")");
        sb2.append(")");
        sb.append(sb2);
        return sb.toString();
    }
    
    /**
     * Obtiene el método setter de un PreparedStatement para un campo de un tipo dado
     * @param field campo
     * @return el método setter de PreparedStatement
     * @throws NoSuchMethodException 
     */
    private Method getPreparedStatementSetterMethod(Field field) throws NoSuchMethodException{
        field.setAccessible(true);
        //Obtener la anotacion Column
        Column column = field.getAnnotation(Column.class);
        String methodName = Utils.getSetPreparedStaementMethodFromClass(field.getType(), column.Transform());
        Class c;
        if(column.Transform().equals(Transform.DATE) ||
           column.Transform().equals(Transform.DATETIME) ||
           column.Transform().equals(Transform.XMLGREGORIANCALENDAR)){
            c = String.class;
        }else{
            c = field.getType();
        }
        return PreparedStatement.class.getDeclaredMethod(methodName, int.class, c);
    }
    
    /**
     * Metodo que devuelve el valor de un campo de un objeto Java ya en su tipo de dato declarado
     * @param field Campo de una clase
     * @param entity objeto de la clase
     * @return valor del campo
     * @throws IllegalArgumentException
     * @throws IllegalAccessException 
     */
    private <T> T getValueOfField(Field field, E entity) throws IllegalArgumentException, IllegalAccessException{
        field.setAccessible(true);
        if(field.get(entity) == null){
            return null;
        }
        Column column = field.getAnnotation(Column.class);
        Transform t = column.Transform();
        
        //Si el field es de tipo Date(General) devolver un string en el formato indicado
        if(t.equals(Transform.DATE) || 
                t.equals(Transform.DATETIME) ||
                t.equals(Transform.XMLGREGORIANCALENDAR)){
            SimpleDateFormat sdf = new SimpleDateFormat(column.Format());
            Date date = null;
            switch(t){
                case DATE: date = (Date) field.get(entity); break;
                case DATETIME: date = ((DateTime) field.get(entity)).toDate(); break;
                case XMLGREGORIANCALENDAR: date = ((XMLGregorianCalendar) field.get(entity)).toGregorianCalendar().getTime(); break;
                default: break;
            }
            return (T) sdf.format(date);
        }else{
            return (T) field.get(entity);
        }
        
    }

}
