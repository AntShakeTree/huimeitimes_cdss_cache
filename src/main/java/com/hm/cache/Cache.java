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

    /**
     * 添加指定元素到set集合里
     * @param value
     */
    void addSet(Object value);

    /**
     * 添加指定元素都List集合
     * @param value
     */
    void addList(Object value);

    /**
     * 添加一个list结合到缓存中,最终是添加到list集合
     * @param list
     * @param <T>
     */
    <T>void addAllList(List<T> list);

    /**
     * 添加一个指定元素有序的集合 并给这个元素打分
     * @param o
     * @param score
     */
    void addSortSet(Object o,double score);

    /**
     * 忽略分数 添加一个指定元素到有序集合
     * @param o
     */
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
     * 返回set集合并对其中某一个元素进行排序
     * @param propertyName
     * @param <T>
     * @return
     */
    <T>Collection<T> sortSet(String propertyName);

    /**
     *  返回set集合并对其中某一个元素进行排序
     * @param propertyName
     * @param <T>
     * @return
     */
    <T>Collection<T> sortList(String propertyName);

    /**
     * 返回一个有序set集合
     * @param <T>
     * @return
     */
    <T>Collection<T> scoredSortedSet();

    /**
     * 返回指定索引和长度的有序set集合
     * @param start
     * @param len
     * @return
     */
    Collection scoredSortedSet(int start,int len);

    /**
     * 返回list集合
     * @param <T>
     * @return
     */
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

    /**
     * 存入已知map
     * @param map
     * @param <K>
     * @param <V>
     */
    public <K,V> void putAll(Map<K,V> map);

}