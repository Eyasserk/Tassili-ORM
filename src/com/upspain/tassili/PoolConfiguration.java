/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.upspain.tassili;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 *
 * @author ykantour
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface PoolConfiguration {
    int MinPoolSize() default 5;
    int MaxPoolSize() default 50;
    int MaxIdleTime() default 240;
    int AquireIncrement() default 5;
}
