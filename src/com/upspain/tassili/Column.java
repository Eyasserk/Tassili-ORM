/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.upspain.tassili;

import com.upspain.tassili.data.Transform;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 *
 * @author ykantour
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Column {
    String Name();
    Transform Transform() default Transform.NONE;
    String Format() default "yyyy-MM-dd'T'HHmmsssZ";
}
