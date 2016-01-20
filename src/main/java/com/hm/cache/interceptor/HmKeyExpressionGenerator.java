package com.hm.cache.interceptor;

import com.hm.engine.common.exception.HmAssert;
import com.hm.cache.option.CacheOperation;
import org.apache.commons.beanutils.PropertyUtils;

import java.lang.reflect.Method;

/**
 * Created by ant_shake_tree on 15/11/19.
 */
public class HmKeyExpressionGenerator extends KeyGenerator.KeyGeneratorDefault {


    @Override
    public Object generate(Object target, Method method, Object... params) {
        return new HmKeyGenerator().generate(target, method, params);
    }


    public Object generate(Object target, CacheOperation operation) {
        HmAssert.notNull(target);
        HmAssert.notNull(operation);
        if(!(operation.getKey()+"").contains("#")){
            return generate(target,operation.getMethod(),operation.getArgs());
        }
        String[] as = ((operation.getKey().toString()).split(","));
        HmAssert.notNull(as);

        Object[] kes = new Object[as.length];


        try {
            for (int i = 0; i < kes.length; i++) {
                Object t=operation.getArgs()[getIndex(as[i])];
                String pref=as[i].substring(as[i].lastIndexOf(")")+1);
                kes[i] = PropertyUtils.getProperty(t, pref);
            }
        } catch (Exception e) {
            return SimpleKey.EMPTY;
        }

        return getKey(kes);
    }
   private static int getIndex(String key){
       String rex="[()]+";
       String[] str=key.split(rex);
       return Integer.parseInt(str[1]);
   }
}
