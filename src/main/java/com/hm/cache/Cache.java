package com.hm.cache;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Created by ant_shake_tree on 15/11/12.
 * Interface that defines the common cache operations.
 * <p/>
 * <b>Note:</b> Due to the generic use of caching, it is recommended that
 * implementations allow storage of <tt>null</tt> values (for example to
 * cache methods that return {@code null}).
 */

public interface Cache {
    public void setIscontinue(boolean iscontinue);

    boolean getContinue();

    /**
     * Return the cache name.
     */
    String getName();

    /**
     * Return the the underlying native cache provider.
     */
    Object getNativeCache();


    <T> T get(Object key, Class<T> type);

    /**
     * 获取回调方式的值
     *
     * @param key
     * @param <T>
     * @return
     */
    <T> T get(Object key);


    /**
     * Associate the specified value with the specified key in this cache.
     * <p>If the cache previously contained a mapping for this key, the old
     * value is replaced by the specified value.
     *
     * @param key   the key with which the specified value is to be associated
     * @param value the value to be associated with the specified key
     */
   <T> void put(Object key, T value);
    /**
     * expire time
     */
  <T>  void put(Object key, T value, long millisecond, TimeUnit timeUnit);

    /**
     * Evict the mapping for this key from this cache if it is present.
     *
     * @param key the key whose mapping is to be removed from the cache
     */
    void evict(Object key);

    void addSet(Object value);
    void addList(Object value);
    <T>void addAllList(List<T> list);
    void addSortSet(Object o,double score);
    void addSortSet(Object o);

    /**
     * 查出 通过 key ,value 存储的
     *
     * @param <T>
     * @return
     */
    <T>List<T> findAll();
    <T>Set<String> set();
    <K,V>Map<K,V> map();
    /**
     * 返回的是list
     * @param propertyName
     * @param <T>
     * @return
     */
    <T>Collection<T> sortSet(String propertyName);
    <T>Collection<T> sortList(String propertyName);
    <T>Collection<T> scoredSortedSet();
    Collection scoredSortedSet(int start,int len);
    <T> List<T> list();
    /**
     * Remove all mappings from the cache.
     */
    void clear();
    int size();
    /**
     * @return get all name
     */
    public Collection<String> getCacheNames();
    public <K,V> void putAll(Map<K,V> map);

}

