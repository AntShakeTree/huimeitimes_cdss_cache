package com.hm.cache;

import com.alibaba.fastjson.JSON;
import com.hm.engine.common.BeanUtils;
import com.hm.engine.common.DelayItem;
import com.hm.Converts;
import com.hm.Host;
import com.hm.cache.manager.HmCacheManager;
import com.hm.cache.manager.JedisPoolHelper;
import redis.clients.jedis.Tuple;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * Created by ant_shake_tree on 16/1/15.
 */
public class JedisCache implements Cache {
    private static final DelayQueue<DelayItem<ConcurentMapCache.KeyValue>> QU = new DelayQueue<>();
    private static final AtomicBoolean monitor = new AtomicBoolean(false);
    private final ConcurentMapCache concurentMapCache;
    private AtomicInteger integer = new AtomicInteger(0);
    private String score_sort_pre="score_set_";
    final protected String name;
    final String set_pre = "set_";
    private String sore_set="score_set";
    private String list_pre = "list_";
    private void monitor() {
        if (monitor.get()) {
            Thread thread = new Thread(() -> {
                while (true) {
                    try {
                        DelayItem o = QU.take();
                        if (o != null) {
                            ConcurentMapCache.KeyValue k = (ConcurentMapCache.KeyValue) o.getItem();
                            if (k != null) {
                                if (JedisPoolHelper.executor(jedis -> jedis.hexists(Converts.toString((getName())), Converts.toString(k.getKey())))) {
                                    evict(k.getKey());
                                }
                            }
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            });
            thread.setDaemon(true);
            thread.start();
        }

    }



    public JedisCache(String name) {
        this.name = name;
        concurentMapCache = new ConcurentMapCache(name);
        while (!monitor.get()) {
            monitor.set(true);
            monitor();
        }
    }

    @Override
    public void setIscontinue(boolean iscontinue) {

    }

    @Override
    public boolean getContinue() {
        return true;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Object getNativeCache() {
        return "JedisCache";
    }


    @Override
    public <T> T get(Object key, Class<T> type) {
        return Converts.toObject(JedisPoolHelper.executor(jedis -> jedis.hget(name, Converts.toString(key))), type);
    }

    @Override
    public <T> T get(Object key) {
        Object o = Converts.toObject(JedisPoolHelper.executor(jedis -> jedis.hget(name, Converts.toString(key))), Object.class);
        return (T) o;
    }

    @Override
    public <T> void put(Object key, T value) {
        if (value == null) return;
        if (value != null && !BeanUtils.typeAllow(value))
            concurentMapCache.put("map_v", value.getClass());
        //
        if (key != null && !BeanUtils.typeAllow(key)) {
            concurentMapCache.put("map_k", key.getClass());
        }
        JedisPoolHelper.executor(jedis -> jedis.hset(name, Converts.toString(key), Converts.toString(value)));
        integer.incrementAndGet();
    }

    @Override
    public <T> void put(Object key, T value, long millisecond, TimeUnit timeUnit) {
        if (value == null) return;
        if (millisecond > 0) {
            if (timeUnit == null) {
                timeUnit = TimeUnit.MILLISECONDS;
            }
            long nanoTime = TimeUnit.NANOSECONDS.convert(millisecond, timeUnit);
            QU.put(new DelayItem<ConcurentMapCache.KeyValue>(new ConcurentMapCache.KeyValue(name, key), nanoTime));
        }
        put(key, value);
    }

    @Override
    public void evict(Object key) {
        JedisPoolHelper.executor(jedis -> jedis.hdel(getName(), Converts.toString(key)));
    }

    @Override
    public void addSet(Object value) {
        if (value == null) return;
        JedisPoolHelper.executor(jedis -> jedis.sadd(getSetName(), Converts.toString(value)));
        if (!BeanUtils.typeAllow(value))
            concurentMapCache.put("set", value.getClass());
    }



    @Override
    public void addList(Object value) {
        if (value == null) return;
        JedisPoolHelper.executor(jedis -> jedis.lpush(getListName(), Converts.toString(value)));
        if (!BeanUtils.typeAllow(value))
            concurentMapCache.put("list", value.getClass());
    }


    @Override
    public <T> void addAllList(List<T> list) {
        list.stream().forEach(
                t -> addList(t)
        );
    }

    @Override
    public void addSortSet(Object o, double score) {
        if(!BeanUtils.typeAllow(o))
            concurentMapCache.put(sore_set,o.getClass());
        JedisPoolHelper.executor(jedis -> jedis.zadd(getScoreSortName(),score,Converts.toString(o)));
    }


    @Override
    public void addSortSet(Object o) {
        if (o==null)return;
        if(!BeanUtils.typeAllow(o)){
            concurentMapCache.put(sore_set,o.getClass());
        }
        double max=0.1;
        if(JedisPoolHelper.executor(jedis1 -> jedis1.exists(getScoreSortName()))){
            Set<Tuple> scores=JedisPoolHelper.executor(jedis1 -> jedis1.zrangeWithScores(getScoreSortName(),-1l,-1l));
            max=scores.stream().mapToDouble(Tuple::getScore).summaryStatistics().getMax()+0.1;
        }
        final  double maxf=max;
        JedisPoolHelper.executor(jedis -> jedis.zadd(getScoreSortName(),maxf,Converts.toString(o)));
    }


    @Override
    public <T> List<T> findAll() {
        return (List<T>) map().entrySet().stream().map(Map.Entry::getValue).collect(Collectors.toList());
    }

    @Override
    public Set set() {
        Set<String> stringSet = JedisPoolHelper.executor(jedis -> jedis.smembers(getSetName()));
        return stringSet.stream().map(s -> {
            if (concurentMapCache.get("set") != null) {
                return Converts.toObject(s, concurentMapCache.get("set"));
            } else
                return s;
        }).collect(Collectors.toSet());

    }

    @Override
    public <K, V> Map<K, V> map() {
        class MapEntry {
            private K key;
            private V value;

            public MapEntry(K key, V value) {
                this.key = key;
                this.value = value;
            }

            public K getKey() {
                return key;
            }

            public V getValue() {
                return value;
            }
        }
        if (concurentMapCache.get("map_k") != null) {
            return JedisPoolHelper.executor(jedis -> jedis.hgetAll(name)).entrySet().stream().map(kv -> {
                return new MapEntry(JSON.parseObject(kv.getKey(), (Class<K>) concurentMapCache.get("map_k")), JSON.parseObject(kv.getValue(), (Class<V>) concurentMapCache.get("map_v")));
            }).collect(Collectors.toMap(MapEntry::getKey, MapEntry::getValue));
        } else {
            return JedisPoolHelper.executor(jedis -> jedis.hgetAll(name)).entrySet().stream().map(kv -> {
                return new MapEntry((K) kv.getKey(), JSON.parseObject(kv.getValue(), (Class<V>) concurentMapCache.get("map_v")));
            }).collect(Collectors.toMap(MapEntry::getKey, MapEntry::getValue));
        }
    }



    @Override
    public <T> Collection<T> sortSet(String propertyName) {
        return com.hm.engine.common.Comparetors.sortSet(set(), propertyName);
    }

    @Override
    public <T> Collection<T> sortList(String propertyName) {
        List list = list();
        com.hm.engine.common.Comparetors.sortList(list, propertyName);
        return list;
    }

    @Override
    public <T> Collection<T> scoredSortedSet() {

        Set<String> strings=JedisPoolHelper.executor(jedis -> jedis.zrange(getScoreSortName(),0,-1));
        return (Collection<T>)strings.stream().map(s ->{ if(concurentMapCache.get(sore_set)!=null){
            return Converts.toObject(s,concurentMapCache.get(sore_set));
        }else return s;
        }).collect(Collectors.toList());
    }



    @Override
    public Collection scoredSortedSet(int start, int len) {
        Set<String> strings=JedisPoolHelper.executor(jedis -> jedis.zrange(getScoreSortName(),start,start+len));
        return (Collection)strings.stream().map(s ->{ if(concurentMapCache.get(sore_set)!=null){
            return Converts.toObject(s,concurentMapCache.get(sore_set));
        }else return s;
        }).collect(Collectors.toList());    }

    @Override
    public <T> List<T> list() {
        List pare = JedisPoolHelper.executor(jedis -> jedis.lrange(getListName(), 0, -1));
        if (concurentMapCache.get("list") != null) {
            Class<T> c = (Class) concurentMapCache.get("list");
            return (List<T>) pare.stream().map(p -> {
                return Converts.toObject((String)p,c);
            }).collect(Collectors.toList());
        } else
            return pare;
    }


    @Override
    public void clear() {
        JedisPoolHelper.executor(jedis -> jedis.del(getListName()));
        JedisPoolHelper.executor(jedis -> jedis.del(name));
        JedisPoolHelper.executor(jedis -> jedis.del((getSetName())));
        JedisPoolHelper.executor(jedis -> jedis.del((getScoreSortName())));
        concurentMapCache.clear();
    }

    @Override
    public int size() {
        return map().size();
    }

    @Override
    public Collection<String> getCacheNames() {
        return JedisPoolHelper.executor(jedis -> jedis.hgetAll(name).keySet());
    }

    @Override
    public <K, V> void putAll(Map<K, V> map) {
        map.entrySet().parallelStream().forEach(kvEntry -> put(kvEntry.getKey(), kvEntry.getValue()));
    }



    public String getListName() {
        return list_pre + name;
    }

    public String getSetName() {
        return set_pre + name;
    }

    public String getScoreSortName(){
        return score_sort_pre+name;
    }

}
