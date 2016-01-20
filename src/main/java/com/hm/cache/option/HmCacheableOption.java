package com.hm.cache.option;

import com.hm.cache.annotation.HmCacheable;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * Created by ant_shake_tree on 15/11/12.
 */
public class HmCacheableOption extends CacheOperation {
    CacheOperation cacheOperation;
    private String unless;
    private long expire;
    public TimeUnit timeUnit;

    public String getUnless() {
        return unless;
    }


    public TimeUnit getTimeUnit() {
        return timeUnit;
    }

    public void setTimeUnit(TimeUnit timeUnit) {
        this.timeUnit = timeUnit;
    }

    public void setUnless(String unless) {
        this.unless = unless;
    }

    public CacheOperation option(Method targetMethod) {
        HmCacheable hmCacheable = targetMethod.getAnnotation(HmCacheable.class);
        if (hmCacheable != null) {
            if (hmCacheable.value()==null || hmCacheable.value().length <1) {
                this.setName(targetMethod.getName());
            } else {
                if (hmCacheable.value().length > 1) {
                    this.setCacheNames(hmCacheable.value());
                } else {
                    this.setName(hmCacheable.value()[0]);
                    this.setCacheName(hmCacheable.value()[0]);
                }
            }
            if (StringUtils.isNotEmpty(hmCacheable.key()) && hmCacheable.key().startsWith("#")) {
                setKeyExpression(true);
            } else setKeyExpression(false);
            this.setKey(hmCacheable.key());
            this.setMethod(targetMethod);
            this.setUnless(hmCacheable.unless());
            this.setCondition(hmCacheable.condition());
            if (hmCacheable.timeUnit() != null)
                this.setTimeUnit(hmCacheable.timeUnit());
            if (hmCacheable.expire() > 0)
                this.setExpire(hmCacheable.expire());
            return this;
        } else if (this.cacheOperation != null) {
            return this.cacheOperation.option(targetMethod);
        }
        return null;
    }

    public long getExpire() {
        return expire;
    }

    public void setExpire(long expire) {
        this.expire = expire;
    }

    public CacheOperation link(CacheOperation cacheOperation) {
        this.cacheOperation = cacheOperation;
        return this;
    }


}
