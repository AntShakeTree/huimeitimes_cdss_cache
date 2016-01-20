//package com.redission.cache;
//import com.redission.RedissionHelpler;
//import com.redission.cache.manager.HmCacheManager;
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Test;
//import java.util.concurrent.TimeUnit;
//import static org.junit.Assert.*;
//
///**
// * Created by ant_shake_tree on 15/11/17.
// */
//
//public class CacheTest {
//    Cache cache = HmCacheManager.ManagerDefault.get().getCache("test");
//
//    @Test
//    public void testSetIscontinue() throws Exception {
//    }
//
//    @Test
//    public void testGetContinue() throws Exception {
//
//    }
//
//    @Test
//    public void testGetName() throws Exception {
//        assertEquals("name", cache.getName());
//    }
//
//    @Test
//    public void testGetNativeCache() throws Exception {
//        assertEquals(ConcurentMapCache.class.getName(), cache.getNativeCache());
//    }
//
//    @Test
//    public void testGet() throws Exception {
//        testPut();
//        assertEquals(cache.get("test"), "test");
//        testPut1();
//        Thread.sleep(2000);
//        assertNotEquals(cache.get("test1"), "test1");
//    }
//
//
//    public void testPut() throws Exception {
////        assertEquals(cache.get("test"),"test");
//        cache.put("test", "test");
//    }
//
//    @Test
//    public void testPut1() throws Exception {
//        cache.put("test1", "test1", 1, TimeUnit.SECONDS);
//
//    }
//
//    @Test
//    public void testEvict() throws Exception {
//        testPut();
//        cache.evict("test");
//        assertNotEquals(cache.get("test"), "test");
//
//    }
//
//    @Test
//    public void testAddSet() throws Exception {
//        cache.addSet(1);
//
//        assertEquals(cache.set().size(), 1);
//    }
//    @Before
//    public void clear(){
//        cache.clear();
//    }
//    @Test
//    public void testAddList() throws Exception {
//        cache.addList("123");
//        assertEquals(cache.list().size(), 1);
//    }
//
//    @Test
//    public void testAddSortSet() throws Exception {
//
//        cache.addSortSet(4);
//        cache.addSortSet(3);
//        cache.addSortSet(3);
//        cache.addSortSet(6);
//        cache.addSortSet(6);
//        cache.addSortSet(3);
//
//        System.out.print(cache.scoreSet());
//
//        assertEquals(cache.scoreSet().size(), 3);
//        Object[] arr = {3,4,6};
//        assertArrayEquals(cache.scoreSet().toArray(), arr);
////        RedissionHelpler.close();
//    }
//
//    @Test
//    public void testAddSortSet1() throws Exception {
//        cache.addSortSet(1, 5);
//        cache.addSortSet(2, 2);
//        cache.addSortSet(4, 3);
//        cache.addSortSet(3, 8);
//        cache.addSortSet(3, 9);
//        assertEquals(cache.scoredScoreSet().size(), 4);
//        Object[] arr = {2, 4, 1, 3};
//        assertArrayEquals(cache.scoredScoreSet().toArray(), arr);
//    }
//
//
//    @Test
//    public void testSortList() throws Exception {
//        cache.clear();
//        cache.list().clear();
//        cache.addList(1);
//        cache.addList(3);
//        cache.addList(1);
//        cache.addList(2);
//        cache.addList(10);
//        cache.addList(100);
//        cache.addList(9);
//        cache.addList(23);
//        Object[] arr = {1, 1, 2, 3, 9, 10, 23, 100};
//       System.out.print( cache.sortList(""));
//        assertArrayEquals(cache.sortList("").toArray(), arr);
//        cache.clear();
////
////        cache.addList(new TestClass("a", 1));
////        cache.addList(new TestClass("b", 2));
////        cache.addList(new TestClass("c", 3));
////        cache.addList(new TestClass("d", 4));
////        cache.addList(new TestClass("e", 5));
////        cache.addList(new TestClass("f", 6));
////        cache.addList(new TestClass("g", 7));
////        cache.addList(new TestClass("h", 8));
////        System.out.print(cache.sortList("name"));
//
//
//    }
//
//    public static class TestClass {
//        private String name;
//        private int sort;
//
//        public TestClass(String name, int sort) {
//            this.name = name;
//            this.sort = sort;
//        }
//
//        public String getName() {
//            return name;
//        }
//
//        public void setName(String name) {
//            this.name = name;
//        }
//
//        public int getSort() {
//            return sort;
//        }
//
//        public void setSort(int sort) {
//            this.sort = sort;
//        }
//    }
//    @After
//    public void shutdown(){
//        if(cache.getNativeCache().equals(HmRedisCache.class.getName()))
//            RedissionHelpler.close();
//    }
//}