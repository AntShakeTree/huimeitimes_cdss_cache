package com.hm.cache.manager;

import com.hm.cache.Cache;
import com.hm.cache.JedisCache;

import java.util.Collection;

/**
 * Created by ant_shake_tree on 16/1/18.
 */
public class JedisHmCacheManager extends HmCacheManager.ManagerDefault{

    private  Cache cache;
    @Override
    public Cache getCache(String name) {
        Cache mc =CACHE_CONCURRENT_HASH_MAP.get(name);
        if(mc!=null&&(mc instanceof JedisCache)){
            cache=CACHE_CONCURRENT_HASH_MAP.get(name);
            return cache;
        }else{
            cache=new JedisCache(name);
            CACHE_CONCURRENT_HASH_MAP.put(name,cache);
        }
        return cache;
    }

    @Override
    public Collection<String> getCacheNames() {
        return cache.getCacheNames();
    }
}
