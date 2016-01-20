package com.hm.cache.manager;

import org.springframework.stereotype.Service;

/**
 * Created by ant_shake_tree on 15/11/5.
 * 规则服务采用反射动态代理方式实现
 */
@Service
public interface RuleMachine {
    /**
     * 反射方式调用
     * @param object 需要反射的对象
     * @param methedName 对象中的方法
     * @param arguments 方法中的参数
     * @return 返回值
     */
    public Object invork(Object object, String methedName, HmParameter... arguments);
//    public Object invork(Object object, String methedName, List<HmParameter> arguments);

    /**
     *  反射方法调用
     * @param object 反射的对象
     * @param methedName 方法名
     * @return 返回值
     */
    public Object invork(Object object, String methedName);

//    /**
//     * spring 容器取得对象的反射调用
//     * @param beanName
//     * @param methedName
//     * @param arguments
//     * @return
//     */
//    public Object springBeanInvork(String beanName, String methedName, HmParameter... arguments);
//    /**
//     * 反射方式调用
//     * @param pojo 数据(对象,方法,参数)
//     * @return 返回值
//     */
//    public Object invork(RuleMechineDataPojo pojo);

    /**
     * 根据表达式回调计算
     * @param expressionKey
     * @return
     */
//    public Object invorkExpressionAndThreadLocal(String expressionKey);
}
