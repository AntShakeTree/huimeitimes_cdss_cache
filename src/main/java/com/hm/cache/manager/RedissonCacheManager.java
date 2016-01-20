package com.hm.cache.manager;

import com.hm.cache.Cache;
import com.hm.cache.HmRedisCache;

import java.util.Collection;

/**
 * Created by ant_shake_tree on 15/11/12.
 */
public class RedissonCacheManager extends HmCacheManager.ManagerDefault {
    private  Cache cache;
    @Override
    public Cache getCache(String name) {
        Cache mc =CACHE_CONCURRENT_HASH_MAP.get(name);
        if(mc!=null&&(mc instanceof HmRedisCache)){
            cache=CACHE_CONCURRENT_HASH_MAP.get(name);
            return cache;
        }else{
            cache=new HmRedisCache(name);
            CACHE_CONCURRENT_HASH_MAP.put(name,cache);
        }
        return cache;
    }
    @Override
    public Collection<String> getCacheNames() {
        return cache.getCacheNames();
    }

}
