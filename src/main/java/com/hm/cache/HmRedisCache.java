package com.hm.cache;

import com.hm.engine.common.Comparetors;
import com.hm.RedissionHelpler;
import org.redisson.client.protocol.ScoredEntry;
import org.redisson.core.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Created by ant_shake_tree on 15/11/12.
 */
public class HmRedisCache implements Cache {

    private boolean iscontinue;

    public String name;
    private String SET_PRE = "set_";
    public HmRedisCache(String name) {
        this.name = name;
        if (!STRING_MAP_CONCURRENT_HASH_MAP.containsKey(name))
            STRING_MAP_CONCURRENT_HASH_MAP.put(name, RedissionHelpler.getMap(name));

    }

    private final static ConcurrentHashMap<String, RMap> STRING_MAP_CONCURRENT_HASH_MAP = new ConcurrentHashMap<>(100);
    private final static ConcurrentHashMap<String, RSortedSet> R_SORTED_SET_CONCURRENT_HASH_MAP = new ConcurrentHashMap<>(100);

    private static String SCORE_SORT_SET = "sss_";
    private static String LIST = "l_";
//    private static

    @Override
    public boolean getContinue() {
        return iscontinue;
    }

    public void setIscontinue(boolean iscontinue) {
        this.iscontinue = iscontinue;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Object getNativeCache() {
        return "redisson";
    }


    @Override
    public <T> T get(Object key, Class<T> type) {
        return RedissionHelpler.get(name, key, type);
    }

    @Override
    public <T> T get(Object key) {
        return (T) RedissionHelpler.getMap(name).get(key);
    }

    @Override
    public void put(Object key, Object value) {
        if(value==null)return;
        if (!STRING_MAP_CONCURRENT_HASH_MAP.containsKey(name))
            STRING_MAP_CONCURRENT_HASH_MAP.put(name, RedissionHelpler.getMap(name));
        STRING_MAP_CONCURRENT_HASH_MAP.get(name).put(key, value);
    }

    @Override
    public void put(Object key, Object value, long time, TimeUnit timeUnit) {
        if(value==null)return;
        if (time <= 0) {
            put(key, value);
        }
        long mill = TimeUnit.MILLISECONDS.convert(time, timeUnit);
        RMap rMap = RedissionHelpler.getMap(name, mill);
        rMap.put(key, value);
    }

    @Override
    public void evict(Object key) {
        RedissionHelpler.remove(name, key);
    }

    @Override
    public void addSet(Object value) {
        if(value==null)return;
        RedissionHelpler.getSet(setName(name)).add(value);


    }

    private String setName(String name) {
        return SET_PRE + name;
    }

    @Override
    public void addList(Object value) {
        if(value==null)return;
        RList<Object> list = RedissionHelpler.getList(listName(name));
        list.add(value);
    }
    @Override
    public <T> void addAllList(List<T> list) {
        RList<Object> lists = RedissionHelpler.getList(listName(name));
        lists.addAllAsync(list);
    }

    @Override
    public void addSortSet(Object o) {
        RedissionHelpler.getScoreSet(scoreSetName(name)).add(o);
    }

    @Override
    public <T> List<T> findAll() {
        return (List<T>) map().values().stream().collect(Collectors.toList());
    }

    @Override
    public <K, V> Map<K, V> map() {
        return null;
    }


    @Override
    public void addSortSet(Object o, double score) {
        RedissionHelpler.getScoredSortedSet(scoredSortedName(name)).add(score, o);
    }


    @Override
    public Set set() {
        return RedissionHelpler.getSet(setName(name));
    }

    private String scoreSetName(String name) {
        return "sc_" + name;
    }


    @Override
    public Collection scoredSortedSet() {
        RScoredSortedSet<Object> rScoredSortedSet = RedissionHelpler.getScoredSortedSet(scoredSortedName(name));
        List<Object> list = new ArrayList<>();
        for (Object o : rScoredSortedSet)
            list.add(o);
        return list;
    }

    private String scoredSortedName(String name) {
        return SCORE_SORT_SET + name;
    }



    @Override
    public Collection scoredSortedSet(int start, int len) {
        RScoredSortedSet<Object> rScoredSortedSet = RedissionHelpler.getScoredSortedSet(scoredSortedName(name));
        Collection<ScoredEntry<Object>> scoredEntryCollection = rScoredSortedSet.entryRange(start, len - start + 1);
        List list = new ArrayList();
        for (ScoredEntry<Object> scoredEntry : scoredEntryCollection) {
            list.add(scoredEntry.getValue());
        }
        return list;
    }

    @Override
    public List list() {
        return RedissionHelpler.getList(listName(name));
    }



    private String listName(String name) {
        return LIST+name;
    }

    @Override
    public void clear() {
        RedissionHelpler.getMap(name).clear();
        set().clear();
        list().clear();
        scoredSortedSet().clear();

    }

    @Override
    public int size() {
        return map().size();
    }


    @Override
    public Collection<String> getCacheNames() {
        return RedissionHelpler.getMap(name).keySet();
    }

    @Override
    public <K, V> void putAll(Map<K, V> map) {

        RedissionHelpler.getMap(name).clear();
        map.entrySet().parallelStream().forEach(kvEntry -> RedissionHelpler.getMap(name).putAll(map));
    }

    @Override
    public <T> Collection<T> sortSet(String propertyName) {

        return Comparetors.sortSet(set(), propertyName);
    }

    @Override
    public <T> Collection<T> sortList(String propertyName) {
        Comparetors.sortList(list(), propertyName);
        return list();
    }

}
