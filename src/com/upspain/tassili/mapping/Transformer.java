/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.upspain.tassili.mapping;

import com.upspain.tassili.data.Transform;
import com.upspain.tassili.exception.TransformException;

/**
 *
 * @author ykantour
 */
public interface Transformer {
    public <T> T transform(Object o, Transform transform, String format) throws TransformException;
}
