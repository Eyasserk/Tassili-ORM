/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.upspain.tassili.persistence;

import com.upspain.tassili.exception.MappingException;
import java.util.List;

/**
 *
 * @author ykantour
 */
public interface AbstractGenericService<E,F> {
    List<E> findAll(F filter) throws MappingException;
    E findById(long  id) throws MappingException;
    boolean update(E entity, F filter) throws MappingException;
    boolean deleteByFilter(F filter) throws MappingException;
    boolean update(E entity) throws MappingException;
    boolean delete(E entity) throws MappingException;
    boolean insert(E entity) throws MappingException;
}
