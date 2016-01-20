package com.hm.cache.interceptor;


import com.hm.engine.common.BeanUtils;
import com.hm.engine.common.ParseJSON;
import com.hm.engine.common.Utils;
import com.hm.cache.option.CacheOperation;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by ant_shake_tree on 15/11/12.
 */
public interface KeyGenerator {
    /**
     * Generate a key for the given method and its parameters.
     *
     * @param target the target instance
     * @param method the method being called
     * @param params the method parameters (with any var-args expanded)
     * @return a generated key
     */
    Object generate(Object target, Method method, Object... params);

    Object generate(Object target, CacheOperation operation) throws InvocationTargetException, IllegalAccessException;

    public static abstract class KeyGeneratorDefault implements KeyGenerator {
        public static KeyGenerator get() {
            return new HmKeyExpressionGenerator();
        }

        public Object generate(Object target, CacheOperation operation) throws InvocationTargetException, IllegalAccessException {
            return new Object();
        }
        public static Object getKey(Object... params){
            if (params.length == 1) {
                Object param = params[0];
                if (param != null && !param.getClass().isArray()) {
                    if (BeanUtils.typeAllow(param)) {
                        return param;
                    }
                    return Utils.hashCode(ParseJSON.toJson(param));
                }
            }
            return new SimpleKey(params).hashCode();
        }
    }
}
