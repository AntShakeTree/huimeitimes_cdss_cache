package com.hm.cache.manager;

import com.hm.PropertiesUtils;
import com.hm.cache.Cache;
import com.hm.cache.option.CacheOperation;
import com.hm.cache.option.HmCacheEviteOperation;
import com.hm.cache.option.HmCachePutOption;
import com.hm.cache.option.HmCacheableOption;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by ant_shake_tree on 15/11/12.
 */
public interface HmCacheManager  {

    /**
     * 获取getCache
     * @param name
     * @return
     */
    Cache getCache(String name);

    boolean exist(String name);


    /**
     * Return a collection of the cache names known by this manager.
     * @return the names of all caches known by the cache manager
     */
    Collection<String> getCacheNames();

    CacheOperation addOpthion(Method targetMethod);



    public abstract static class ManagerDefault implements HmCacheManager{
        public  CacheOperation addOpthion(Method targetMethod) {
            CacheOperation hm = new HmCacheableOption();
            CacheOperation hm2 = new HmCachePutOption();
            CacheOperation hm3 = new HmCacheEviteOperation();
            hm.link(hm2);
            hm2.link(hm3);
            CacheOperation o = hm.option(targetMethod);
            return o;
        }
        protected final static ConcurrentHashMap<String,Cache> CACHE_CONCURRENT_HASH_MAP=new ConcurrentHashMap<>(100);
        public static HmCacheManager get(){
            if(PropertiesUtils.getProperty("cache.type").equals("ConcurrentHashMap"))
                return new ConcurentCacheManager();
            else if (PropertiesUtils.getProperty("cache.type").equals("JedisCacheManager"))
                    return  new JedisHmCacheManager();
            return  new RedissonCacheManager();
        }
        public static HmCacheManager getLocal(){
                return new ConcurentCacheManager();
        }
        public boolean exist(String name){
            return CACHE_CONCURRENT_HASH_MAP.containsKey(name);
        }

    }

}
