package com.hm.test;

import com.hm.cache.Cache;
import com.hm.cache.manager.HmCacheManager;

/**
 * Created by ant_shake_tree on 16/1/14.
 */
public class TestSaveCache {
    public static void main(String[] args){
        Cache cache= HmCacheManager.ManagerDefault.get().getCache("xx");
        cache.put("x","1");
//        System.out.println(cache.get("x").toString());
    }
}
