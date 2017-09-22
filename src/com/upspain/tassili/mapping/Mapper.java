/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.upspain.tassili.mapping;

import com.upspain.tassili.exception.TransformException;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author ykantour
 */
public interface Mapper<E> {
    public E map(ResultSet r, Class c) throws SQLException, NoSuchMethodException,
            InstantiationException, IllegalAccessException, InvocationTargetException,
            ClassCastException, TransformException;
}
