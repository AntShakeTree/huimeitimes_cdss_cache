package com.hm.cache.option;

import com.hm.cache.annotation.HmCacheEvite;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Method;

/**
 * Created by ant_shake_tree on 15/11/12.
 */
public class HmCacheEviteOperation extends CacheOperation {

    private boolean cacheWide = false;

    private boolean beforeInvocation = false;
    private CacheOperation cacheOperation;



    public boolean isCacheWide() {
        return cacheWide;
    }

    public void setCacheWide(boolean cacheWide) {
        this.cacheWide = cacheWide;
    }

    public boolean isBeforeInvocation() {
        return beforeInvocation;
    }

    public void setBeforeInvocation(boolean beforeInvocation) {
        this.beforeInvocation = beforeInvocation;
    }
    public CacheOperation link(CacheOperation cacheOperation){
        this.cacheOperation=cacheOperation;
        return this;
    }

    @Override
    public CacheOperation option(Method targetMethod) {
        HmCacheEvite hmCacheEvite=targetMethod.getAnnotation(HmCacheEvite.class);
        if(hmCacheEvite!=null){
            this.setKey(hmCacheEvite.key());
            if(StringUtils.isNotEmpty(hmCacheEvite.key())&&hmCacheEvite.key().startsWith("#")){
                setKeyExpression(true);
            }else setKeyExpression(false);
            this.setCacheName(hmCacheEvite.value()[0]);
            this.setName(hmCacheEvite.value()[0]);
            this.setCondition(hmCacheEvite.condition());
            return this;
        }else if(this.cacheOperation!=null){
            return this.cacheOperation.option(targetMethod);
        }
        return null;

    }
}
