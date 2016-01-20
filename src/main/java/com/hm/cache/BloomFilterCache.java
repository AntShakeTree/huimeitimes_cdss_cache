package com.hm.cache;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnel;
import com.google.common.hash.PrimitiveSink;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

/**
 * Created by ant_shake_tree on 15/11/15.
 */
public abstract class BloomFilterCache<T extends BloomFilterCache.BloomFilterKV>{
    protected int size;
    private Cache cache;
    public void put(T key) {
        bloomFilter.put(key);
    }

    public boolean contain(T key) {
        boolean isc = bloomFilter.mightContain(key);
        synchronized (object) {
            if (!isc) currentSize++;
        }
        return isc;
    }
    T bloomFilterKV ;
    public void setBloomFilterKV(T bloomFilterKV){
        this.bloomFilterKV=bloomFilterKV;
    }
    protected int currentSize = 0;

    public int size() {
        return this.size;
    }

    public int currentSize() {
        return this.currentSize;
    }


    public abstract byte[] putBytes(T from);

    private static Object object = new Object();
    protected BloomFilter<T> bloomFilter;

    public void newInstance(int size) {
        synchronized (object) {
            if (bloomFilter == null) {
                bloomFilter = BloomFilter.create(new CommonFunnel() {
                    @Override
                    public void funnel(T from, PrimitiveSink into) {
                        into.putBytes(putBytes(from));
                    }
                }, size);
            }
            this.size = size;
        }
    }

    public abstract class CommonFunnel implements Funnel<T> {
        @Override
        public abstract void funnel(T from, PrimitiveSink into);
    }







    static void printTools(Object s) {
        System.out.println(s);
    }

    /**
     * 所有存储单元必须实现此接口
     */
    public interface BloomFilterKV {
        /**
         * cache专用
         *
         * @param name
         * @return
         */
        String name(String name);

        /**
         * cache专用
         *
         * @param key
         * @return
         */
        Object key(Object key);

        byte[] putBytes();
    }

    /**
     * 不作为Cache 实现的接口
     */
    public static abstract class DefaultBloomFilterKV implements BloomFilterKV {
        public String name(String name) {
            return name;
        }

        public Object key(Object key) {
            return key;
        }

        public abstract byte[] putBytes();

    }

    /***
     * <href>cache.setIscontinue</href>
     */

    public void setIscontinue(boolean iscontinue) {
        this.cache.setIscontinue(iscontinue);
    }
    /***
     * <href>cache.getContinue</href>
     */

    public boolean getContinue() {
        return cache.getContinue();
    }
    /***
     * <href>cache.getName</href>
     */

    public String getName() {
        return this.cache.getName();
    }
    /***
     * <href>cache.getNativeCache</href>
     */

    public Object getNativeCache() {
        return cache.getNativeCache();
    }
    /***
     * first validate by BloomFilter
     * <href>cache.getNativeCache</href>
     */

    public <T> T get(Object key, Class<T> type) {
        if (bloomFilterKV != null) {
            bloomFilterKV.name(cache.getName());
            bloomFilterKV.key(key);
            this.contain(bloomFilterKV);
            return cache.get(key, type);
        } else {
            return cache.get(key, type);
        }

    }
    /***
     * first validate by BloomFilter
     * <href>cache.get</href>
     */

    public <T> T get(Object key) {
        if (bloomFilterKV != null) {
            bloomFilterKV.key(key);
            bloomFilterKV.name(cache.getName());
            if (this.contain(bloomFilterKV)) {
                return get(key);
            } else {
                return null;
            }
        }
        return cache.get(key);
    }
    /***
     * first put  BloomFilter
     * <href>cache.put</href>
     */

    public void put(Object key, Object value) {
        if (bloomFilterKV != null) {
            bloomFilterKV.key(key);
            bloomFilterKV.name(cache.getName());
            put(bloomFilterKV);
        }
        cache.put(key, value);
    }
    /***
     * first put  BloomFilter
     * <href>cache.put</href>
     */

    public void put(Object key, Object value, long millisecond, TimeUnit timeUnit) {
        if (bloomFilterKV != null) {
            bloomFilterKV.key(key);
            bloomFilterKV.name(cache.getName());
            put(bloomFilterKV);
        }

        cache.put(key, value, millisecond,timeUnit);

    }
    /***
     * first validate by evict
     * <href>cache.get</href>
     */

    public void evict(Object key) {

        if (bloomFilterKV != null) {
            bloomFilterKV.key(key);
            bloomFilterKV.name(cache.getName());

            if (this.contain(bloomFilterKV)) {
                cache.evict(key);
            }
        }
    }
    /***
     * <href>cache.clear</href>
     */

    public void clear() {
        cache.clear();
        bloomFilter = null;
    }


    public Collection<String> getCacheNames() {

        return cache.getCacheNames();
    }



    /**
     * 这里注册的kv是回调用的,空的对象就行 <example>new BloomFilterImpl</example>
     * @param cache
     */
    public void registerCache(Cache cache) {
        this.cache = cache;
    }

    /**
     * 自己实现的funnel
     */
    CommonFunnel commonFunnel;

    public void registerFunnel(CommonFunnel commonFunnel) {
        this.commonFunnel = commonFunnel;
    }
}