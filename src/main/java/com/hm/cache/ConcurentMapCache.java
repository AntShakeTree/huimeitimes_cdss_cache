package com.hm.cache;

import com.google.common.collect.Maps;
import com.hm.engine.common.Comparetors;
import com.hm.engine.common.DelayItem;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

/**
 * Created by ant_shake_tree on 15/11/13.
 */
public class ConcurentMapCache implements Cache {
    private boolean isContinue;
    private final String name;
    private static final AtomicBoolean monitor = new AtomicBoolean(false);
    private static final DelayQueue<DelayItem<KeyValue>> QU = new DelayQueue<>();

    private static void monitor() {
        if (monitor.get()) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        try {
                            DelayItem o = QU.take();
                            if (o != null) {
                                KeyValue k = (KeyValue) o.getItem();
                                if (k != null) {
                                    caches.get(k.getName()).remove(k.getKey());
                                }
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                }
            });
            thread.setDaemon(true);
            thread.start();
        }

    }

    public ConcurentMapCache(String name) {
        this.name = name;
        if (caches.get(name) == null) {
            Map<Object, Object> map = Maps.newHashMap();
            caches.put(name, map);
        }

        LIST.computeIfAbsent(name, k -> new ArrayList());

        TREESET.computeIfAbsent(name, k -> new TreeSet());

        SCOERTREESET.computeIfAbsent(name, k -> new TreeSet());

        SET.computeIfAbsent(name, k -> new HashSet());
        if (!monitor.get()) {
            monitor.set(true);
            monitor();
        }
    }


    private final static ConcurrentHashMap<String, Map> caches = new ConcurrentHashMap<>(100);
    private final static ConcurrentHashMap<String, Set> SET = new ConcurrentHashMap<>(100);
    private final static ConcurrentHashMap<String, List> LIST = new ConcurrentHashMap<>(100);
    private final static ConcurrentHashMap<String, TreeSet> TREESET = new ConcurrentHashMap<>(100);
    private final static ConcurrentHashMap<String, TreeSet> SCOERTREESET = new ConcurrentHashMap<>(100);

    @Override
    public void setIscontinue(boolean iscontinue) {
        this.isContinue = iscontinue;
    }

    @Override
    public boolean getContinue() {
        return isContinue;
    }


    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Object getNativeCache() {
        return ConcurentMapCache.class.getName();
    }

    @Override
    public <T> T get(Object key, Class<T> type) {
        return (T) caches.get(name).get(key);
    }

    @Override
    public <T> T get(Object key) {
        return (T) caches.get(name).get(key);
    }

    @Override
    public void put(Object key, Object value) {
        caches.get(name).put(key, value);

    }

    @Override
    public void put(Object key, Object value, long millisecond, TimeUnit timeUnit) {
        if (millisecond > 0) {
            if (timeUnit == null) {
                timeUnit = TimeUnit.MILLISECONDS;
            }
            long nanoTime = TimeUnit.NANOSECONDS.convert(millisecond, timeUnit);
            QU.put(new DelayItem<KeyValue>(new KeyValue(name, key), nanoTime));
        }
        put(key, value);
    }


    @Override
    public void evict(Object key) {
        caches.get(name).remove(key);
    }

    @Override
    public void addSet(Object value) {
        if (!SET.containsKey(name))
            SET.put(name, new HashSet());
        SET.get(name).add(value);
    }

    @Override
    public void addList(Object value) {
        if (!LIST.containsKey(name))
            LIST.put(name, new ArrayList());
        LIST.get(name).add(value);
    }

    private static final String LIST_TIME_KEY = "T_LIST";

//    @Override
//    public void addList(Object value, long time, TimeUnit timeUnit) {
//        if (this.get(LIST_TIME_KEY) != null) {
//            ((ArrayList)this.get(LIST_TIME_KEY)).add(value);
//        } else
//            this.put(LIST_TIME_KEY, Arrays.asList(value), time, timeUnit);
//    }

    @Override
    public <T> void addAllList(List<T> list) {
        LIST.put(name, list);
    }

    @Override
    public void addSortSet(Object o, double score) {
        if (!SCOERTREESET.containsKey(name))
            SCOERTREESET.put(name, new TreeSet());
        Comparetors.sorts(SCOERTREESET.get(name), o, score);
    }

    @Override
    public void addSortSet(Object o) {
        if (!TREESET.containsKey(name))
            TREESET.put(name, new TreeSet());
        Comparetors.sorts(TREESET.get(name), o, System.nanoTime());
    }

    @Override
    public <T> List<T> findAll() {
        return (List<T>) map().values().stream().collect(Collectors.toList());
    }

    @Override
    public <K, V> Map<K, V> map() {
        return (Map<K, V>) caches.computeIfAbsent(name, k -> new HashMap());
    }


    @Override
    public <T> Collection<T> sortSet(String propertyName) {
        return Comparetors.sortSet(SET.get(name), propertyName);
    }

    @Override
    public <T> Collection<T> sortList(String propertyName) {
        Comparetors.sortList(LIST.get(name), propertyName);
        return LIST.get(name);
    }




    @Override
    public Set set() {
        return SET.get(name);
    }

//    @Override
//    public Collection scoreSet() {
//        return fromTreeSetToList(TREESET.get(name));
//    }
//

    @Override
    public Collection scoredSortedSet() {
        return fromTreeSetToList(SCOERTREESET.get(name));
    }




    @Override
    public Collection scoredSortedSet(int start, int len) {
        return fromTreeSetToList(SCOERTREESET.get(name), start, len);
    }

    @Override
    public List list() {
        return LIST.get(name);
    }



    @Override
    public void clear() {
        caches.get(name).clear();
        LIST.get(name).clear();
        SET.get(name).clear();
        TREESET.get(name).clear();
        SET.get(name).clear();
    }

    @Override
    public int size() {
        return map().size();
    }

    @Override
    public Collection<String> getCacheNames() {
        return caches.keySet();
    }

    @Override
    public <K, V> void putAll(Map<K, V> map) {
        caches.put(name, map);
    }


    public static class KeyValue {
        private Object key;
        private Object name;

        public KeyValue(String name, Object key) {
            this.name = name;
            this.key = key;
        }

        public Object getKey() {
            return key;
        }

        public void setKey(Object key) {
            this.key = key;
        }

        public Object getName() {
            return name;
        }

        public void setName(Object name) {
            this.name = name;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;

            if (o == null || getClass() != o.getClass()) return false;

            KeyValue keyValue = (KeyValue) o;

            return new EqualsBuilder()
                    .append(key, keyValue.key)
                    .append(name, keyValue.name)
                    .isEquals();
        }

        @Override
        public int hashCode() {
            return new HashCodeBuilder(17, 37)
                    .append(key)
                    .append(name)
                    .toHashCode();
        }
    }


    private static List fromTreeSetToList(TreeSet<Comparetors.ComparetorObject> set) {
        if (set == null) return Collections.EMPTY_LIST;
        List<Object> list = new ArrayList<>(set.size());
        for (Comparetors.ComparetorObject o : set) {
            list.add(o.getObject());
        }
        return list;
    }

    private static List fromTreeSetToList(TreeSet<Comparetors.ComparetorObject> set, int start, int len) {
        if (set == null) return Collections.EMPTY_LIST;
        List<Object> list = new ArrayList<>(set.size());
        int i = 0;
        int l = 0;
        for (Comparetors.ComparetorObject o : set) {
            if (i >= start && l < len) {
                list.add(o.getObject());
                l++;
            }
            i++;
            if (l >= len)
                break;
        }
        return list;
    }
}

