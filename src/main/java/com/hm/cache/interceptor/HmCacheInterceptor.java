package com.hm.cache.interceptor;

import com.hm.engine.common.BeanUtils;
import com.hm.engine.common.Constants;
import com.hm.cache.Cache;
import com.hm.cache.manager.HmCacheManager;
import com.hm.cache.manager.HmCacheManager.ManagerDefault;
import com.hm.cache.option.HmCacheEviteOperation;
import com.hm.cache.option.HmCacheableOption;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.function.Supplier;

//import com.hm.engine.common.log.LoggerFactory;


/**
 * 通用的异常与日志管理
 *
 * @author ant_shake_tree
 */
@Aspect
@Component
public class HmCacheInterceptor {
    static Logger log = Logger.getLogger(Constants.PROJECT_NAME);


    @Pointcut("@annotation(com.hm.cache.annotation.HmCacheable)")
    public void able() {
    }

    //	@Pointcut("@annotation(com.redission.cache.annotation.HmCachePut)")
//	public void put() {
//	}
    @Pointcut("@annotation(com.hm.cache.annotation.HmCacheEvite)")
    public void evite() {
    }

    @Pointcut("@annotation(com.hm.cache.annotation.HmCachePut)")
    public void put() {
    }

    private static final Logger logger = Logger
            .getLogger(HmCacheInterceptor.class);

    /**
     * 拦截所有业务方法重新包装异常
     */
    @Around(value = "able()")
    public Object able(final ProceedingJoinPoint j) throws Throwable {
        Signature signature = j.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Class clazz = j.getTarget().getClass();
        Method targetMethod = BeanUtils.findDeclaredMethod(clazz, j.getSignature().getName(), methodSignature.getParameterTypes());
        HmCacheableOption operation = new HmCacheableOption();
        return operation.findCache(j.getTarget(), targetMethod, j.getArgs(), () -> {
            try {
                 Object o=j.proceed(j.getArgs());
                return o;
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
            return null;
        });
    }


//    @Around(value = "put()")
//    public Object put(final ProceedingJoinPoint j) throws Throwable {
//        Signature signature = j.getSignature();
//        MethodSignature methodSignature = (MethodSignature) signature;
//        Class clazz = j.getTarget().getClass();
//        Method targetMethod = BeanUtils.findDeclaredMethod(clazz, j.getSignature().getName(), methodSignature.getParameterTypes());
//        HmCacheManager hmCacheManager = ManagerDefault.get();
//        HmCachePutOption operation = new HmCachePutOption();
//        Object car = operation.findCache(j.getTarget(), targetMethod, j.getArgs(), () -> {
//            try {
//                return j.proceed(j.getArgs());
//            } catch (Throwable throwable) {
//                throwable.printStackTrace();
//            }
//            return null;
//        });
//        if (car != null) return car;
//        return j.proceed(j.getArgs());
//    }

    @Around(value = "evite()")
    public Object evite(final ProceedingJoinPoint j) throws Throwable {
        Signature signature = j.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Class clazz = j.getTarget().getClass();
        Method targetMethod = BeanUtils.findDeclaredMethod(clazz, j.getSignature().getName(), methodSignature.getParameterTypes());
        HmCacheManager hmCacheManager = ManagerDefault.get();
        HmCacheEviteOperation operation = new HmCacheEviteOperation();
        if (operation.option(targetMethod) != null) {
            if (StringUtils.isEmpty(operation.getKey() + "")) {
                Object key = KeyGenerator.KeyGeneratorDefault.get().generate(j.getTarget(), targetMethod, j.getArgs());
                debug(() -> key);
                operation.setKey(key);
            }
            Cache cache = hmCacheManager.getCache(operation.getName());
            if (cache.get(operation.getKey()) != null) {
                cache.evict(operation.getKey());
            }
        }
        return j.proceed(j.getArgs());

    }

    private static void info(Supplier<Object> key) {
        if (logger.isInfoEnabled())
            logger.info("key======>" + key.get());
    }

    private static void debug(Supplier<Object> key) {
        if (logger.isDebugEnabled())
            logger.debug("key======>" + key.get());
    }




}
