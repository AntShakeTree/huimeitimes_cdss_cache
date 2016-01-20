package com.hm.cache.interceptor;

import com.hm.cache.annotation.Parameter;
import com.hm.cache.annotation.ParameterType;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by ant_shake_tree on 15/11/19.
 */
public class MethodParse {
    public static Object  parse(Method method,Object... params){

            Annotation[][] parameterAnnotations = method.getParameterAnnotations();
            int i = 0;
            ArrayList<Object> los = new ArrayList<>(params.length);
            for (Annotation[] annotations : parameterAnnotations) {

                for (Annotation annotation : annotations) {
                    if (annotation instanceof Parameter) {
                        add(los,((Parameter)annotation).type(),params[i]);
                    }
                }
                i++;
            }
        return KeyGenerator.KeyGeneratorDefault.getKey(los.toArray());
    }

    private static void add(List<Object> contains, ParameterType type, Object param){

        switch (type){
            case List:
                List list= ((List)param);
                contains.addAll(list);
                break;
            case Map:
                Map map =(Map)param;
                for (Object k:map.keySet()){
                    contains.add(k);
                    contains.add(map.get(k));
                }

                break;
            case Set:
                Set set = (Set)param;
                contains.addAll(set);
                break;
            default:
                contains.add(param);
                break;
        }
    }
}
