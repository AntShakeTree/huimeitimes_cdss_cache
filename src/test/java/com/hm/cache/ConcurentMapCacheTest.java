package com.hm.cache;

import com.esotericsoftware.kryo.Kryo;
import com.hm.cache.manager.HmCacheManager;
import org.junit.Test;

/**
 * Created by ant_shake_tree on 15/11/16.
 */
public class ConcurentMapCacheTest {
    HmCacheManager concurentMapCache= HmCacheManager.ManagerDefault.ManagerDefault.get();

    @Test
    public void testSetIscontinue() throws Exception {

    }

    @Test
    public void testGetContinue() throws Exception {

    }

    @Test
    public void testGetName() throws Exception {

    }

    @Test
    public void testGetNativeCache() throws Exception {

    }

    @Test
    public void testGet() throws Exception {
        System.out.print(concurentMapCache.getCache("name1").get("xxx",String.class));

    }

    @Test
    public void testGet1() throws Exception {

    }


    @Test
    public void testPut1() throws Exception {

    }

    @Test
    public void testEvict() throws Exception {
        Kryo kryo=new Kryo();
//        CacheTest.TestClass testClass=new CacheTest.TestClass("sadghfjksda",1);

//        kryo.register(testClass.getClass());
//        kryo.
    }


    @Test
    public void testClear() throws Exception {

    }

    @Test
    public void testGetCacheNames() throws Exception {
        Cache cache=HmCacheManager.ManagerDefault.get().getCache("name");
        cache.addSet("1");
        cache.addSet("1");
        cache.addSet("2");
        cache.addSet("2");
        cache.addSortSet("1",1);
        cache.addSortSet("1",1);
        cache.addSortSet("20",20);
        cache.addSortSet("10",10);
        cache.addSortSet("9",9);
        cache.addSortSet("50",50);
        cache.addSortSet("70",70);cache.addSortSet("1",1);
        cache.addSortSet("1");
        cache.addSortSet("20");
        cache.addSortSet("10");
        cache.addSortSet("9");
        cache.addSortSet("50");
        cache.addSortSet("70");
//        System.out.print(cache.scoredScoreSet());
//        System.out.print(cache.scoredScoreSet(0,5));


//        System.out.print(cache.scoreSet());
    }
}