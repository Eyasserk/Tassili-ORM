/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.upspain.tassili.persistence;

import com.upspain.tassili.data.Specification;
import java.util.List;

/**
 *
 * @author ykantour
 */
public interface Filter {

    /**
     *
     * @return lista de espcificaciones tassili
     */
    public List<Specification> getSpecifications() throws IllegalAccessException;
    public void addSpecification(Specification spec);
}
