/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.upspain.tassili.mapping;

import com.upspain.tassili.data.Transform;
import com.upspain.tassili.exception.TransformException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 *
 * @author ykantour
 */
public class GenericTransformer implements Transformer{

    @Override
    public Object transform(Object o, Transform transform, String format) throws TransformException{
        if(o == null || transform == null){
            throw new TransformException("Null transform parameters");
        }
        String theObject;
        try{
            //theObject = (String) o;
            theObject = o.toString();
        }catch(ClassCastException e){
            throw new TransformException("Cannot transform object "+o.getClass()+" to String: "+e.getMessage());
        }
        switch(transform){
            case NONE: return theObject;
            case UPPER: return theObject.toUpperCase();
            case LOWER: return theObject.toLowerCase();
            case TRIM: return theObject.trim();
            case DATE: return getDate(theObject, format);
            case DATETIME: return getDateTime(theObject, format);
            case XMLGREGORIANCALENDAR: return getXMLGregorianCalendar(theObject, format);
            default: return o;
        }
    }
    
    private Date getDate(String dateString, String format) throws TransformException{
        try{
            DateFormat formatter = new SimpleDateFormat(format);
            return formatter.parse(dateString.trim());
        }catch(ParseException e){
            throw new TransformException(e.getMessage());
        }
    }
    
    private DateTime getDateTime(String dateString, String format) throws TransformException{
        try{
            DateTimeFormatter formatter = DateTimeFormat.forPattern(format);
            return formatter.parseDateTime(dateString.trim());
        }catch(UnsupportedOperationException e){
            throw new TransformException("Parse no soportado: "+e.getMessage());
        }catch(IllegalArgumentException e){
            throw new TransformException("Formato de fecha no válido: "+e.getMessage());
        }
    }
    
    private XMLGregorianCalendar getXMLGregorianCalendar(String dateString, String format) throws TransformException{
        try{
            GregorianCalendar cal = new GregorianCalendar();
            cal.setTime(new SimpleDateFormat(format).parse(dateString.trim()));
            return DatatypeFactory.newInstance().newXMLGregorianCalendar( cal);
        }catch(DatatypeConfigurationException e){
            throw new TransformException(e.getMessage());
        }catch(ParseException e){
            throw new TransformException(e.getMessage());
        }
    }
}
