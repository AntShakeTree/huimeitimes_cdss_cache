//package com.redission.test;
//
//import com.redission.cache.BloomFilterCache;
//import org.junit.Test;
//
///**
// * Created by ant_shake_tree on 15/11/16.
// */
//public class TestBloomFilterCache {
//    @Test
//    public  void testBloomFilter(){
//
//        class StringBKV extends BloomFilterCache.DefaultBloomFilterKV {
//            String string;
//
//            public StringBKV(String s) {
//                this.string = s;
//            }
//
//            @Override
//            public byte[] putBytes() {
//                return string.getBytes();
//            }
//        }
//        BloomFilterCache bloomFilterCache = new BloomFilterCache<StringBKV>() {
//            @Override
//            public byte[] putBytes(StringBKV from) {
//                return from.string.getBytes();
//            }
//        };
//        bloomFilterCache.newInstance(10);
//        bloomFilterCache.put(new StringBKV("10"));
//        bloomFilterCache.put(new StringBKV("10"));
//        bloomFilterCache.put(new StringBKV("01"));
//        bloomFilterCache.put(new StringBKV("dafsdf"));
//        bloomFilterCache.put(new StringBKV("30"));
//        printTools(bloomFilterCache.contain(new StringBKV("")));
//        printTools(bloomFilterCache.contain(new StringBKV("000")));
//        printTools(bloomFilterCache.contain(new StringBKV("10")));
//        printTools(bloomFilterCache.contain(new StringBKV("20")));
//        printTools(bloomFilterCache.contain(new StringBKV("0")));
//        printTools(bloomFilterCache.contain(new StringBKV("30")));
//    }
//    public static  void printTools(Object o){
//        System.out.println(o.toString());
//    }
//}
