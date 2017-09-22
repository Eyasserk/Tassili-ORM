/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.upspain.tassili;

import com.upspain.tassili.data.RegisterState;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 *
 * @author ykantour
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface FilterTable {
    String ReferenceTable();
    String DataSourceId();
    RegisterState FilterBy() default RegisterState.ALL;
    String ActiveColumnName() default "ACTIVE";
}
