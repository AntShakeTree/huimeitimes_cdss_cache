//package com.redission.test;
//
//import com.hm.engine.common.Utils;
//import com.redission.HMLogger;
//import com.redission.cache.BloomFilterCache;
//import com.redission.cache.Cache;
//import com.redission.cache.interceptor.HmKeyGenerator;
//import com.redission.cache.manager.HmCacheManager;
//import org.apache.log4j.Logger;
//import org.junit.Test;
//import org.springframework.cache.CacheManager;
//
//import java.io.IOException;
//import java.nio.charset.Charset;
//import java.nio.file.*;
//import java.util.*;
//import java.util.concurrent.TimeUnit;
//
///**
// * Created by ant_shake_tree on 15/10/24.
// */
//
//public class DataCovert {
//    public static void main(String[] args) throws IOException {
//        List<String>list= Files.readAllLines(Paths.get("cfda.txt"), Charset.forName("UTF-8"));
//        List<String>dxy= Files.readAllLines(Paths.get("dxy.txt"), Charset.forName("UTF-8"));
//        List<String> dxyC=new LinkedList<String>();
//        List<String> dxyD=new LinkedList<String>();
//        for (String sr:dxy){
//            String[] ss=sr.split("\\t");
//            if(ss.length>1&&ss[1]!=null){
//                if(list.contains(ss[1].trim())){
//                    dxyC.add(sr);
//                    //ss
//                }else {
//                    dxyD.add(sr);
//                }
//            }
//        }
//        Map<String,String> scoreMap=new HashMap<String, String>();
//        for (String d:dxyD){
//            String[] ss=d.split("\\t");
//            double min=0;
//            for(String s :list){
//                s=s.replaceAll("\\s", "");
//                s=s.replaceAll("（","(");
//                s=s.replaceAll("）",")");
//                ss[1]=ss[1].replaceAll("\\s","");
//                ss[1]=ss[1].replaceAll("（","(");
//                ss[1]=ss[1].replaceAll("）",")");
//                double dd=sim(s, ss[1]);
//                if(dd==0){
//                    continue;
//                }
//                if(dd>=min){
//                    min=dd;
//                    scoreMap.put(d,s);
//                }
//            }
//        }
//        List<String> strings =new LinkedList<String>();
//        for (String k:scoreMap.keySet()){
//            strings.add(k+"\t"+scoreMap.get(k));
//        }
//        //
//        Files.write(Paths.get("out.txt"),strings, Charset.defaultCharset(),StandardOpenOption.CREATE);
//        //
//    }
//    private static int min(int one, int two, int three) {
//        int min = one;
//        if(two < min) {
//            min = two;
//        }
//        if(three < min) {
//            min = three;
//        }
//        return min;
//    }
//
//    public static int ld(String str1, String str2) {
//        int d[][];    //矩阵
//        int n = str1.length();
//        int m = str2.length();
//        int i;    //遍历str1的
//        int j;    //遍历str2的
//        char ch1;    //str1的
//        char ch2;    //str2的
//        int temp;    //记录相同字符,在某个矩阵位置值的增量,不是0就是1
//        if(n == 0) {
//            return m;
//        }
//        if(m == 0) {
//            return n;
//        }
//        d = new int[n+1][m+1];
//        for(i=0; i<=n; i++) {    //初始化第一列
//            d[i][0] = i;
//        }
//        for(j=0; j<=m; j++) {    //初始化第一行
//            d[0][j] = j;
//        }
//        for(i=1; i<=n; i++) {    //遍历str1
//            ch1 = str1.charAt(i-1);
//            //去匹配str2
//            for(j=1; j<=m; j++) {
//                ch2 = str2.charAt(j-1);
//                if(ch1 == ch2) {
//                    temp = 0;
//                } else {
//                    temp = 1;
//                }
//                //左边+1,上边+1, 左上角+temp取最小
//                d[i][j] = min(d[i-1][j]+1, d[i][j-1]+1, d[i-1][j-1]+temp);
//            }
//        }
//        return d[n][m];
//    }
//
//    public static double sim(String str1, String str2) {
//        int ld = ld(str1, str2);
//        return 1 - (double) ld / Math.max(str1.length(), str2.length());
//    }
//    Logger logger=Logger.getLogger("");
//    HMLogger hmLogger =HMLogger.getLogger(DataCovert.class);
//    @Test
//    public void te() throws InterruptedException {
//        HmCacheManager cacheManager=HmCacheManager.ManagerDefault.get();
//        Cache cache=cacheManager.getCache("name");
////        cache.put();
//        Object k =HmKeyGenerator.KeyGeneratorDefault.get().generate(this,null,"xxxx","xxxx");
//        //cache.put(k,va);
//        cache.put(k,"xxx",1000, TimeUnit.MILLISECONDS);
//       System.out.print(Utils.beforeTimeFromCurrenttime(1));
//        System.out.println(cache.get(k, String.class));
//        Thread.sleep(10);
//        System.out.println(cache.get(k, String.class) + "sss");
//
//
////        hmLogger.info("xxxxx");
//    }
//
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
//        printTools(bloomFilterCache.contain(new StringBKV("")));
//        printTools(bloomFilterCache.contain(new StringBKV("000")));
//        printTools(bloomFilterCache.contain(new StringBKV("10")));
//        printTools(bloomFilterCache.contain(new StringBKV("20")));
//        printTools(bloomFilterCache.contain(new StringBKV("0")));
//        printTools(bloomFilterCache.contain(new StringBKV("30")));
//    }
//    public static  void printTools(Object o){
//        System.out.print(o.toString());
//    }
//
//
//}
