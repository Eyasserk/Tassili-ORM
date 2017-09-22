/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.upspain.tassili.persistence;

import com.upspain.tassili.FilterColumn;
import com.upspain.tassili.FilterTable;
import com.upspain.tassili.context.ContextConfiguration;
import com.upspain.tassili.data.Criteria;
import com.upspain.tassili.data.Ordering;
import com.upspain.tassili.data.Specification;
import com.upspain.tassili.data.Transform;
import com.upspain.tassili.data.Values;
import com.upspain.tassili.exception.SpecificationException;
import com.upspain.tassili.utils.Utils;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author ykantour
 */
public abstract class AbstractFilter implements com.upspain.tassili.persistence.Filter{
    
    List<Specification> specifications = new ArrayList<Specification>();
    private boolean keyTriming = false;
    private boolean pagination = false;
    private int pageSize = 10;
    private int pageNumber = 0;
    private String orderBy = "";
    
    @Override
    public List<Specification> getSpecifications() throws IllegalAccessException {
        if(specifications == null || specifications.size() == 0){
            specifications = new ArrayList<Specification>();
        }
        for(Field field : this.getClass().getDeclaredFields()){
            field.setAccessible(true);
            if(field.get(this) != null
                    && !Collection.class.isAssignableFrom(field.getType())
                    && field.isAnnotationPresent(FilterColumn.class)
                    //Comprobar si no es primitivo con valor por defecto
                    && !(field.getType().isPrimitive() && field.get(this).equals(Utils.defaultValue(field.getType())))
                    ){
                Values v = new Values(field.get(this));
                Specification spec = new Specification();
                spec.setKey(field.getAnnotation(FilterColumn.class).Name());
                spec.setCriteria(Criteria.EQUAL);
                spec.setValues(v);

                if(isKeyTriming()){
                    spec.setKeyTransform(Transform.TRIM);
                }

                specifications.add(spec);

            }
            if(field.getAnnotation(FilterColumn.class) != null && field.getAnnotation(FilterColumn.class).OrderBy() != null && !Ordering.NONE.equals(field.getAnnotation(FilterColumn.class).OrderBy())){
                orderBy = "order by "+field.getAnnotation(FilterColumn.class).Name();
                orderBy = orderBy+" "+field.getAnnotation(FilterColumn.class).OrderBy().getValue();
            }
        }
        return specifications;
    }
    
    @Override
    public void addSpecification(Specification spec){
        if(specifications == null){
            specifications = new ArrayList<Specification>();
        }
        specifications.add(spec);
    }
    
    public String printSpecifications(List<Specification> specs, boolean meta){
        String dataSourceId = getClass().getAnnotation(FilterTable.class).DataSourceId();
        String dbms = ContextConfiguration.getInstance(dataSourceId).getJdbcUrl().split(":")[1].toLowerCase();
        if(specs == null || specs.size() == 0){
            if(!meta){
                return "";
            }else{
                StringBuilder sb = new StringBuilder();
                if(dbms.equals("as400")){
                    if(pagination){
                        String tableName = getClass().getAnnotation(FilterTable.class).ReferenceTable();
                        sb.append(" where rrn(");sb.append(tableName);sb.append(") between ");sb.append(pageNumber*pageSize);sb.append(" and "); sb.append((pageNumber*pageSize)+pageSize);
                    }
                    if(orderBy != null){
                        sb.append(" ");
                        sb.append(orderBy);
                    }
                }else{
                    if(orderBy != null){
                        sb.append(" ");
                        sb.append(orderBy);
                    }
                    if(pagination){
                        String tableName = getClass().getAnnotation(FilterTable.class).ReferenceTable();
                        sb.append(" limit ");sb.append(pageSize);sb.append(" offset ");sb.append(pageNumber*pageSize);
                    }
                }
                return sb.toString();
            }
        }else{
            try{
                StringBuilder sb = new StringBuilder();
                sb.append(" where ");
                sb.append(specs.get(0).stringify());
                for(int i=1; i<specs.size(); i++){
                    sb.append(" and ");
                    sb.append(specs.get(i).stringify());
                }
                if(meta){ //Si hay un order by o una paginación : generalmente en select
                    if(dbms.equals("as400")){
                        if(pagination){
                            String tableName = getClass().getAnnotation(FilterTable.class).ReferenceTable();
                            sb.append(" and rrn(");sb.append(tableName);sb.append(") between ");sb.append(pageNumber*pageSize);sb.append(" and "); sb.append((pageNumber*pageSize)+pageSize);
                        }
                        if(orderBy != null){
                            sb.append(" ");
                            sb.append(orderBy);
                        }
                    }else{
                        if(orderBy != null){
                            sb.append(" ");
                            sb.append(orderBy);
                        }
                        if(pagination){
                            String tableName = getClass().getAnnotation(FilterTable.class).ReferenceTable();
                            sb.append(" limit ");sb.append(pageSize);sb.append(" offset ");sb.append(pageNumber*pageSize);
                        }
                    }
                }
                return sb.toString();
            }catch(SpecificationException e){
                throw new RuntimeException(e.getMessage());
            }
        }
    }
    
    public boolean isKeyTriming(){
        return keyTriming;
    }
    
    public void setKeyTrimming(boolean keyTriming){
        this.keyTriming = keyTriming;
    }

    /**
     * @return the pagination
     */
    public boolean isPagination() {
        return pagination;
    }

    /**
     * @param pagination the pagination to set
     */
    public void setPagination(boolean pagination) {
        this.pagination = pagination;
    }

    /**
     * @return the pageSize
     */
    public int getPageSize() {
        return pageSize;
    }

    /**
     * @param pageSize the pageSize to set
     */
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    /**
     * @return the pageNumber
     */
    public int getPageNumber() {
        return pageNumber;
    }

    /**
     * @param pageNumber the pageNumber to set
     */
    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }
}
