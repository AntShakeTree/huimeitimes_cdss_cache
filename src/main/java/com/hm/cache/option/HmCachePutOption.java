package com.hm.cache.option;

import com.hm.cache.annotation.HmCachePut;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * Created by ant_shake_tree on 15/11/12.
 */
public class HmCachePutOption extends CacheOperation{
    private String unless;
    private CacheOperation cacheOperation;

    public String getUnless() {
        return unless;
    }

    private long expire;
    public TimeUnit timeUnit;
    public void setUnless(String unless) {
        this.unless = unless;
    }

    @Override
    public CacheOperation option(Method targetMethod) {
        HmCachePut hmCacheable=targetMethod.getAnnotation(HmCachePut.class);
        if(hmCacheable!=null){
            this.setKey(hmCacheable.key());
            this.setCacheName(hmCacheable.name());
            this.setName(hmCacheable.name());
            this.setUnless(hmCacheable.unless());
            this.setCondition(hmCacheable.condition());
            this.setExpire(hmCacheable.expire());
            this.setTimeUnit(hmCacheable.timeUnit());
            if(StringUtils.isNotEmpty(hmCacheable.key())&&hmCacheable.key().startsWith("#")){
                setKeyExpression(true);
            }else setKeyExpression(false);
            return this;
        }else if(this.cacheOperation!=null){
            return this.cacheOperation.option(targetMethod);
        }
        else if(this.cacheOperation!=null){
            return this.cacheOperation.option(targetMethod);
        }
        return null;
    }

    public CacheOperation link(CacheOperation cacheOperation){
        this.cacheOperation=cacheOperation;
        return this;
    }

    public TimeUnit getTimeUnit() {
        return timeUnit;
    }

    public void setTimeUnit(TimeUnit timeUnit) {
        this.timeUnit = timeUnit;
    }

    public long getExpire() {
        return expire;
    }

    public void setExpire(long expire) {
        this.expire = expire;
    }
}
