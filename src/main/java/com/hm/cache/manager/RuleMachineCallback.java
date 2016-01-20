package com.hm.cache.manager;


import com.hm.engine.common.exception.EngineExceptionHandle;
import com.hm.engine.common.exception.EngineRestException;
import com.hm.cache.Cons;
import org.apache.commons.beanutils.MethodUtils;

import java.util.Arrays;

/**
 * Created by ant_shake_tree on 15/11/5.
 * 反射调用方法封装
 *
 */
public abstract class RuleMachineCallback implements RuleMachine{
    RuleMachine ruleMachine;
    public RuleMachineCallback(RuleMachine ruleMachine){
        this.ruleMachine=ruleMachine;
    }

    public RuleMachineCallback() {
    }
    @Override
    public Object invork(Object object, String methedName, HmParameter... arguments) {
        if(object==null){
            return null;
        }
        Arrays.sort(arguments);
        Class[] classes=new Class[arguments.length];
        Object[] vs=new Object[arguments.length];
        Arrays.setAll(classes,i->arguments[i].getType());
        Arrays.setAll(vs,i->arguments[i].getValue());
        try {
            return MethodUtils.invokeMethod(object,methedName,arguments);
        } catch (Exception e) {
            e.printStackTrace();
            throw new EngineRestException(EngineExceptionHandle.generalError(Cons.PARSE_RULE_MODEL, Cons.PARSE_RULE_ERROR),"Parse expression error. please check expression is right.");
        }
    }

    @Override
    public Object invork(Object object, String methedName) {
        if(object==null){
            return null;
        }
        try {
            return MethodUtils.invokeMethod(object,methedName,null);
        } catch (Exception e) {
            throw new EngineRestException(EngineExceptionHandle.generalError(Cons.PARSE_RULE_MODEL,Cons.PARSE_RULE_ERROR),"Parse expression error. please check expression is right.");
        }
    }


    /**
     * 回调方式调用
     * @return
     */
    public abstract Object call();

    /**
     *  注册规则解析Bean 回调方式调用
     * @param ruleMachine
     * @return
     */
    public abstract Object call(RuleMachine ruleMachine);

    /**
     *  注册业务Bean 回调方式调用
     * @param o 业务bean
     * @return
     */
    @Deprecated
    public abstract Object call(Object... o);

}
