package com.hm.cache.option;

import com.hm.cache.Cache;
import com.hm.cache.interceptor.KeyGenerator;
import com.hm.cache.manager.HmCacheManager;
import org.springframework.util.Assert;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * cache 操作类,仿照spring cache设计.
 */
public abstract class CacheOperation {

    private Set<String> cacheNames = Collections.emptySet();

    public abstract CacheOperation link(CacheOperation cacheOperation);

    private String condition = "";

    private Object key = "";

    private String name = "";

    private Method method;
    private Object[] args;

    private long expire;
    private TimeUnit timeUnit;
    private boolean keyExpression;

    public Set<String> getCacheNames() {
        return cacheNames;
    }

    public String getCondition() {
        return condition;
    }

    public Object getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    public void setCacheName(String cacheName) {
        Assert.hasText(cacheName);
        this.cacheNames = Collections.singleton(cacheName);
    }

    public void setCacheNames(String[] cacheNames) {
        Assert.notEmpty(cacheNames);
        this.cacheNames = new LinkedHashSet<String>(cacheNames.length);
        for (String string : cacheNames) {
            this.cacheNames.add(string);
        }
    }

    public abstract CacheOperation option(Method targetMethod);


    public void setCondition(String condition) {
        Assert.notNull(condition);
        this.condition = condition;
    }

    public void setKey(Object key) {
        Assert.notNull(key);
        this.key = key;
    }

    public void setName(String name) {
        Assert.hasText(name);
        this.name = name;
    }

    /**
     * This implementation compares the {@code toString()} results.
     *
     * @see #toString()
     */
    @Override
    public boolean equals(Object other) {
        return (other instanceof CacheOperation && toString().equals(other.toString()));
    }

    /**
     * This implementation returns {@code toString()}'s hash code.
     *
     * @see #toString()
     */
    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    /**
     * Return an identifying description for this cache operation.
     * <p>Has to be overridden in subclasses for correct {@code equals}
     * and {@code hashCode} behavior. Alternatively, {@link #equals}
     * and {@link #hashCode} can be overridden themselves.
     */
    @Override
    public String toString() {
        return getOperationDescription().toString();
    }

    /**
     * Return an identifying description for this caching operation.
     * <p>Available to subclasses, for inclusion in their {@code toString()} result.
     */
    protected StringBuilder getOperationDescription() {
        StringBuilder result = new StringBuilder();
        result.append(getClass().getSimpleName());
        result.append("[");
        result.append(this.name);
        result.append("] caches=");
        result.append(this.cacheNames);
        result.append(" | key='");
        result.append(this.key);
        result.append("' | condition='");
        result.append(this.condition);
        result.append("'");
        return result;
    }

    public boolean isKeyExpression() {
        return keyExpression;
    }

    public void setKeyExpression(boolean keyExpression) {
        this.keyExpression = keyExpression;
    }

    public void setCacheNames(Set<String> cacheNames) {
        this.cacheNames = cacheNames;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

    HmCacheManager hmCacheManager = HmCacheManager.ManagerDefault.get();

    public Object findCache(Object target, Method targetMethod, Object[] params, Supplier<Object> supplier) throws InvocationTargetException, IllegalAccessException {
        if (this.option(targetMethod) != null) {
            this.setArgs(params);
            this.setMethod(targetMethod);
            KeyGenerator keyGenerator = KeyGenerator.KeyGeneratorDefault.get();
            Object key = keyGenerator.generate(target, this);
            this.setKey(key);
            Cache cache = hmCacheManager.getCache(this.getName());
            Object v = cache.get(this.getKey());
            if (v != null) {
                return v;
            } else {
                Object o = supplier.get();
                if (this.getExpire() > 0&&this.getTimeUnit()!=null){
                    cache.put(this.getKey(), o, this.getExpire(), this.getTimeUnit());
                }
                else cache.put(this.getKey(), o);
                return o;
            }

        }
        return null;
    }

    public long getExpire() {
        return expire;
    }

    public void setExpire(long expire) {
        this.expire = expire;
    }

    public TimeUnit getTimeUnit() {
        return timeUnit;
    }

    public void setTimeUnit(TimeUnit timeUnit) {
        this.timeUnit = timeUnit;
    }
}


