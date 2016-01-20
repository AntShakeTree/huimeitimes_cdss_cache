//package com.redission.cache.interceptor;
//
//import com.hm.engine.common.exception.HmAssert;
//import com.redission.cache.Cache;
//import com.redission.cache.manager.HmCacheManager;
//import com.redission.cache.option.HmCacheableOption;
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.beans.factory.InitializingBean;
//import org.springframework.cache.interceptor.CacheOperation;
//import org.springframework.util.CollectionUtils;
//
//import java.lang.reflect.Method;
//import java.util.Collection;
//
///**
// * Created by ant_shake_tree on 15/11/13.
// */
//public abstract class HmCacheAspectSupport implements InitializingBean {
//    interface HmInvoker{
//        Object invoke();
//    }
//    CacheOperationSource cacheOperationSource;
//    interface CacheOperationSource {
//
//        /**
//         * Return the collection of cache operations for this method, or {@code null}
//         * if the method contains no <em>cacheable</em> annotations.
//         * @param method the method to introspect
//         * @param targetClass the target class (may be {@code null}, in which case
//         * the declaring class of the method must be used)
//         * @return all cache operations for this method, or {@code null} if none found
//         */
//        Collection<CacheOperation> getCacheOperations(Method method, Class<?> targetClass);
//
//    }
//    private boolean initialized;
//    private KeyGenerator keyGenerator = KeyGenerator.KeyGeneratorDefault.get();
//    private HmCacheManager cacheManager =HmCacheManager.ManagerDefault.get();
//
//    protected Object execute(final HmInvoker invoker, Object target, Method method, Object[] args) throws Throwable {
//        // check whether aspect is enabled
//        // to cope with cases where the AJ is pulled in automatically
//        if (this.initialized) {
//            Class<?> targetClass = getTargetClass(target);
//            Cache.ValueWrapper v= new Cache.ValueWrapper(){
//                @Override
//                public Object get() throws Throwable {
//                    return invoker.invoke();
//                }
//            };
////            if()
//            HmCacheableOption operation = new HmCacheableOption();
//            if (operation.option(method) != null) {
//                if (StringUtils.isEmpty(operation.getKey() + "")) {
//                    operation.setKey(keyGenerator.generate(target, method, args));
//                }
//                Cache cache = cacheManager.getCache(operation.getName());
//                if (cache.get(operation.getKey()) != null) return cache.get(operation.getKey());
//                else {
//                    cache.put(operation.getKey(),v);
//                    return v.get();
//                }
//            }
//        }
//        return invoker.invoke();
//    }
//
//    protected  Class<?> getTargetClass(Object target){
//        return target.getClass();
//    }
//    public void afterPropertiesSet() {
//        HmAssert.state(this.cacheManager != null, "Property 'cacheManager' is required");
////        HmAssert.state(this.cacheOperationSource != null, "Property 'cacheOperationSources' is required: " +
////                "If there are no cacheable methods, then don't use a cache aspect.");
//        this.initialized = true;
//    }
//
//    public CacheOperationSource getCacheOperationSource() {
//        return cacheOperationSource;
//    }
//
//    public void setCacheOperationSource(CacheOperationSource cacheOperationSource) {
//        this.cacheOperationSource = cacheOperationSource;
//    }
//}
