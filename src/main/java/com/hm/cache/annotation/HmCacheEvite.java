package com.hm.cache.annotation;

import java.lang.annotation.*;

/**
 * Created by ant_shake_tree on 15/11/12.
 */
@Target({ElementType.TYPE,ElementType.FIELD,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface HmCacheEvite {
    /**
     * Qualifier value for the specified cached operation.
     * <p>May be used to determine the target cache (or caches), matching the qualifier
     * value (or the bean name(s)) of (a) specific bean definition.
     */
    String[] value();

    /**
     * Spring Expression Language (SpEL) attribute for computing the key dynamically.
     * <p>Default is "", meaning all method parameters are considered as a key.
     */
    String key() default "";

    /**
     * Spring Expression Language (SpEL) attribute used for conditioning the method caching.
     * <p>Default is "", meaning the method is always cached.
     */
    String condition() default "";

    /**
     * Whether or not all the entries inside the cache(s) are removed or not. By
     * default, only the value under the associated key is removed.
     * <p>Note that setting this parameter to {@code true} and specifying a {@link #key()}
     * is not allowed.
     */
    boolean allEntries() default false;

    /**
     * Whether the eviction should occur after the method is successfully invoked (default)
     * or before. The latter causes the eviction to occur irrespective of the method outcome (whether
     * it threw an exception or not) while the former does not.
     */
    boolean beforeInvocation() default false;
}
