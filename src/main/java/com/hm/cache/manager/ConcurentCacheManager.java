package com.hm.cache.manager;

import com.hm.cache.Cache;
import com.hm.cache.ConcurentMapCache;

import java.util.Collection;

/**
 * Created by ant_shake_tree on 15/11/13.
 */
public class ConcurentCacheManager extends HmCacheManager.ManagerDefault {
   private Cache cache;

    @Override
    public Cache getCache(String name) {
        Cache mc =CACHE_CONCURRENT_HASH_MAP.get(name);
        if(mc!=null&&(mc instanceof ConcurentMapCache)){
            cache=CACHE_CONCURRENT_HASH_MAP.get(name);
            return cache;
        }else{
            cache=new ConcurentMapCache(name);
            CACHE_CONCURRENT_HASH_MAP.put(name,cache);
        }
        return cache;
    }

    private String name;
    @Override
    public Collection<String> getCacheNames() {
        return cache.getCacheNames();
    }

}
