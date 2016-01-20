package com.hm.cache.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by ant_shake_tree on 15/11/16.
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface Parameter {
    ParameterType  type() default ParameterType.Primitive;
    String name() default "";
    boolean isKey() default true;
    boolean isValue() default false;
    boolean isName() default false;
}
