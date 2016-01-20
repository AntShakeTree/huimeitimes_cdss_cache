package com.hm.cache.interceptor;

import java.lang.reflect.Method;

/**
 * Created by ant_shake_tree on 15/11/12.
 * hash code.
 */

public class HmKeyGenerator extends KeyGenerator.KeyGeneratorDefault {

    @Override
    public Object generate(Object target, Method method, Object... params) {
        Object re = null;
        if (target == null)
            return SimpleKey.NULL_OBJECT;
        else if (params == null)
            return SimpleKey.EMPTY;
        else {
            if (method != null) {
                re = MethodParse.parse(method, params);
                if (re != null)
                    return re;
            }
            return getKey(params);
        }
    }




}

