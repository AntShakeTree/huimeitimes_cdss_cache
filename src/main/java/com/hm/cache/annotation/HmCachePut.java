package com.hm.cache.annotation;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * Created by ant_shake_tree on 15/11/12.
 */
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface HmCachePut {
    /**
     * Name of the caches in which the update takes place.
     * <p>May be used to determine the target cache (or caches), matching the
     * qualifier value (or the bean name(s)) of (a) specific bean definition.
     */
    String[] value() default {""};

    String name() default "";

    /**
     * HmKeyGeneral is automitic computed key.
     * <p>Default is "", meaning all method parameters are considered as a key.
     */
    String key() default "";


    /**
     * HmKeyGeneral is automitic
     * <p>Default is "", meaning the method is always cached.
     */
    String condition() default "";

    /**
     * HmKeyGeneral is automitic computed key.
     * <p>Unlike {@link #condition()}, this expression is evaluated after the method
     * has been called and can therefore refer to the {@code result}. Default is "",
     * meaning that caching is never vetoed.
     */
    String unless() default "";

    long expire() default 0;

    /**
     * Expire's unit.
     *
     * @return
     */
    TimeUnit timeUnit() default TimeUnit.MILLISECONDS;
}