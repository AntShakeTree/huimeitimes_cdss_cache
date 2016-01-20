package com.hm.cache.interceptor;

import com.hm.engine.common.ParseJSON;
import com.hm.engine.common.exception.HmAssert;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * Created by ant_shake_tree on 15/11/12.
 */
public class SimpleKey {
    public static final SimpleKey EMPTY = new SimpleKey();
    public static final int NULL_OBJECT = 0;

    private Method method;
    private Object target;
    private final Object[] params;

    /**
     * Create a new {@link HmKeyGenerator} instance.
     *
     * @param elements the elements of the key
     */
    public SimpleKey(Object... elements) {
        HmAssert.notNull(elements, "Elements must not be null");
        this.params = new Object[elements.length];
        System.arraycopy(elements, 0, this.params, 0, elements.length);
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Object getTarget() {
        return target;
    }

    public void setTarget(Object target) {
        this.target = target;
    }

    @Override
    public boolean equals(Object obj) {
        return (this == obj || (obj instanceof SimpleKey
                && Arrays.deepEquals(this.params, ((SimpleKey) obj).params)));
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(this.params);
    }

    @Override
    public String toString() {
        return "HmKeyGenerator [" + ParseJSON.toJson(params) + "]";
    }

    public Object[] getParams() {
        return params;
    }
}
