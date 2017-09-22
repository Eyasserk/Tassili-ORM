/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.upspain.tassili.data;

import com.upspain.tassili.exception.SpecificationException;

/**
 *
 * @author ykantour
 */
public class Specification {
    private String key;
    private Values values;
    private Criteria criteria;
    private Transform keyTransform;
    private Transform valuesTransform;
    
    public Specification(){
    
    }

    public Specification(String key, Values values, Criteria criteria) {
        this.key = key;
        this.values = values;
        this.criteria = criteria;
    }

    
    
    public Specification(String key, Values values, Criteria criteria, Transform keyTransform, Transform valuesTransform) {
        this.key = key;
        this.values = values;
        this.criteria = criteria;
        this.keyTransform = keyTransform;
        this.valuesTransform = valuesTransform;
    }
    
    

    /**
     * @return the key
     */
    public String getKey() {
        return key;
    }

    /**
     * @param key the key to set
     */
    public void setKey(String key) {
        this.key = key;
    }

    /**
     * @return the values
     */
    public Values getValues() {
        return values;
    }

    /**
     * @param values the values to set
     */
    public void setValues(Values values) {
        this.values = values;
    }

    /**
     * @return the criteria
     */
    public Criteria getCriteria() {
        return criteria;
    }

    /**
     * @param criteria the criteria to set
     */
    public void setCriteria(Criteria criteria) {
        this.criteria = criteria;
    }

    /**
     * @return the keyTransform
     */
    public Transform getKeyTransform() {
        return keyTransform;
    }

    /**
     * @param keyTransform the keyTransform to set
     */
    public void setKeyTransform(Transform keyTransform) {
        this.keyTransform = keyTransform;
    }

    /**
     * @return the valuesTransform
     */
    public Transform getValuesTransform() {
        return valuesTransform;
    }

    /**
     * @param valuesTransform the valuesTransform to set
     */
    public void setValuesTransform(Transform valuesTransform) {
        this.valuesTransform = valuesTransform;
    }
    
    
    public String stringify() throws SpecificationException{
        if(criteria == null){
            throw new SpecificationException("no criteria specified");
        }
        switch(criteria){
            case BETWEEN : return getBetweenSpecification();
            default: return getOthersSpecification();
        }
    }
    
    private String getBetweenSpecification() throws SpecificationException{
        if(key == null){
            throw new SpecificationException("No key specified");
        }
        if(values == null){
            throw new SpecificationException("No values specified");
        }
        if(values.getMain() == null){
            throw new SpecificationException("No value specified");
        }
        if(values.getAux() == null){
            throw new SpecificationException("Trying to specify between with less than two values");
        }
        StringBuilder sb = new StringBuilder(1024);
        if(keyTransform != null){
            sb.append(keyTransform.getValue());
            sb.append("(");
            sb.append(key);
            sb.append(")");
        }else{
            sb.append(key);
        }
        sb.append(" ");
        sb.append(criteria.getValue());
        sb.append(" ");
        if(valuesTransform != null){
            sb.append(valuesTransform.getValue());
            sb.append("(");
            sb.append(values.getMainValue());
            sb.append(")");
        }else{
            sb.append(values.getMainValue());
        }
        sb.append(" ");
        sb.append("and");
        sb.append(" ");
        if(valuesTransform != null){
            sb.append(valuesTransform.getValue());
            sb.append("(");
            sb.append(values.getAuxValue());
            sb.append(")");
        }else{
            sb.append(values.getAuxValue());
        }
        return sb.toString();
    }

    private String getOthersSpecification() throws SpecificationException {
        if(key == null){
            throw new SpecificationException("No key specified");
        }
        if(criteria == null){
            throw new SpecificationException("No criteria specified");
        }
        if(values == null || values.getMain() == null){
            throw new SpecificationException("No value specified");
        }
        StringBuilder sb = new StringBuilder(1024);
        if(keyTransform != null){
            sb.append(keyTransform.getValue());
            sb.append("(");
            sb.append(key);
            sb.append(")");
        }else{
            sb.append(key);
        }
        sb.append(" ");
        sb.append(criteria.getValue());
        sb.append(" ");
        if(valuesTransform != null){
            sb.append(valuesTransform.getValue());
            sb.append("(");
            sb.append(values.getMainValue());
            sb.append(")");
        }else{
            sb.append(values.getMainValue());
        }
        return sb.toString();
    }
}
