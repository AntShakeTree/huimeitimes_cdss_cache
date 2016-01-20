package com.hm;


import org.apache.commons.lang3.StringUtils;
import org.redisson.ClusterServersConfig;
import org.redisson.Config;
import org.redisson.Redisson;
import org.redisson.SingleServerConfig;
import org.redisson.core.*;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Created by ant_shake_tree on 15/10/22.
 */

public class RedissionHelpler {
    private static String DEFAULT_KEY = "REMAINDER";
    private static String PAGE_PREX = "pi:";
    //    private static final RedissionPool redissionPool=RedissionPool.getPools(getDefaultHost());
    private final static Redisson redisson = Redisson.create(ConfigTools.config(getDefaultHost()));

    public static Host getDefaultHost() {
        return new Host(com.hm.PropertiesUtils.getProperty("redis.address"), com.hm.PropertiesUtils.getIntProperty("redis.port"), com.hm.PropertiesUtils.getIntProperty("redis.pool.timeout"), com.hm.PropertiesUtils.getIntProperty("max.total"), com.hm.PropertiesUtils.getProperty("redis.username"), com.hm.PropertiesUtils.getProperty("redis.password"));
    }

    public static Redisson getRedisson() {
        return redisson;
    }

    public static <ID> PageEntity<ID> findPage(String tablename, byte[] key, int currentPage, int pageSize) {
        PageEntity<ID> pageEntity = new PageEntity();
        RScoredSortedSet scoredSortedSet = redisson.getScoredSortedSet(findPageKey(tablename, key));
        RSet<RedissionPageInfo> rpset = redisson.getSet(tablename + PAGE_PREX);
        for (RedissionPageInfo pageInfo : rpset) {
            if (pageInfo.getName().equals(Converts.toString(key))) {
                pageEntity.setPageInfo(pageInfo);
                break;
            }
        }
        int starIndex = (currentPage - 1) * pageSize;
        int endIndex = starIndex + pageSize - 1;
        Collection<ID> scoredEntries = scoredSortedSet.valueRange(starIndex, endIndex);
        pageEntity.setScoredEntries(scoredEntries);
        return pageEntity;
    }

    public static <ID> PageEntity<ID> findPage(String tablename, String key, int currentPage, int pageSize) {
        PageEntity<ID> pageEntity = new PageEntity();
        RScoredSortedSet scoredSortedSet = redisson.getScoredSortedSet(findPageKey(tablename, key));
        RSet<RedissionPageInfo> rpset = redisson.getSet(tablename + PAGE_PREX);
        for (RedissionPageInfo pageInfo : rpset) {
            if (pageInfo.getName().equals(key)) {
                pageEntity.setPageInfo(pageInfo);
                break;
            }
        }
        int starIndex = (currentPage - 1) * pageSize;
        int endIndex = starIndex + pageSize - 1;
        Collection<ID> scoredEntries = scoredSortedSet.valueRange(starIndex, endIndex);
        pageEntity.setScoredEntries(scoredEntries);
        return pageEntity;
    }

    public static void setPage(String table, byte[] key, List scoredEntries, int talSize) {
        RSet<RedissionPageInfo> rpset = redisson.getSet(table + PAGE_PREX);
        RedissionPageInfo redissionPageInfo = new RedissionPageInfo();
        redissionPageInfo.setToltalRecord(talSize);
        redissionPageInfo.setName(Converts.toString(key));
        boolean isco = false;
        for (RedissionPageInfo o : rpset) {
            if (o.getName().equals(key)) {
                o.setToltalRecord(talSize);
                isco = true;
            }
        }
        if (!isco) rpset.add(redissionPageInfo);
        RScoredSortedSet scoredSortedSet = redisson.getScoredSortedSet(findPageKey(table, key));
        scoredSortedSet.expire(com.hm.PropertiesUtils.getIntProperty("query.page.key.expire"), TimeUnit.SECONDS);
        int i = 1;
        for (Object entry : scoredEntries) {
            i++;
            scoredSortedSet.add(i, entry);
        }
    }

    public static void setPage(String table, String key, List scoredEntries, int talSize) {
        RSet<RedissionPageInfo> rpset = redisson.getSet(table + PAGE_PREX);
        RedissionPageInfo redissionPageInfo = new RedissionPageInfo();
        redissionPageInfo.setToltalRecord(talSize);
        redissionPageInfo.setName(key);
        boolean ico = false;
        for (RedissionPageInfo o : rpset) {
            if (o.getName().equals(key)) {
                o.setToltalRecord(talSize);
                ico = true;
            }
        }

        if (!ico) rpset.add(redissionPageInfo);
        RScoredSortedSet scoredSortedSet = redisson.getScoredSortedSet(findPageKey(table, key));
        scoredSortedSet.expire(com.hm.PropertiesUtils.getIntProperty("query.page.key.expire"), TimeUnit.SECONDS);
        int i = 1;
        for (Object entry : scoredEntries) {
            i++;
            scoredSortedSet.add(i, entry);
        }
    }

    public static void asyncDel(String table, Serializable id) {
        try {
            RSet<RedissionPageInfo> rpset = redisson.getSet(table + PAGE_PREX);

            Set<RedissionPageInfo> cp = new HashSet<RedissionPageInfo>(rpset.size());
            for (RedissionPageInfo o : rpset) {
                RScoredSortedSet scoredSortedSet = redisson.getScoredSortedSet(findPageKey(table, (Serializable) o.getName()));

                if (scoredSortedSet.contains(id)) {
                    o.setToltalRecord(o.getToltalRecord() - 1);
                }
                cp.add(o);
            }
            rpset.clear();
            rpset.addAllAsync(cp);
            RMap map = redisson.getMap(table);
            map.remove(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Object put(String table, Object id, Object value) {
        RMap map = redisson.getMap(table);
        return map.put(id, value);
    }

    public static Object remove(String table, Object key) {
        RMap map = redisson.getMap(table);
        return map.remove(key);
    }

    public static Object get(String table, Object id) {
        RMap map = redisson.getMap(table);
        return map.get(id);
    }

    public static <T> T get(String table, Object id, Class<T> tClass) {
        RMap map = redisson.getMap(table);
        Object o = map.get(id);
        return (T) o;
    }

    public static void save(String table, Serializable id, Object value) {
        RMap map = redisson.getMap(table);
        map.fastPut(id, value);
    }

    private static String findPageKey(String tablename, byte[] key) {
        byte[] bytes = Converts.toBytes(tablename + ":");
        byte[] byt = Converts.toBytes(bytes, key);
        return Converts.toString(byt);
    }

    private static String findPageKey(String tablename, Serializable key) {
        return tablename + ":" + key;
    }
//    public  static Redisson getRedisson(){
//        return redissionPool.getResource();
//    }

    public static void close() {
        if (redisson != null)
            redisson.shutdown();
    }

    //    public static void close(){
//        redissionPool.returnResource(redisson);
//    }
    public static void main(String[] args) throws InterruptedException {
//        RedissionHelpler.registerRedissin();
        List<Integer> ar = new LinkedList<>();
//        for(int i=0;i<100;i++){
//            ar.add(i);
//        }
//        setPage("name","t",ar,ar.size());
//        System.out.print(findPage("name","t",1,10));
//        asyncDel("name",5);
        Map m = getMap("aaa");
        System.out.print(m.get(1));
//        m.put(1,2);
//        setPage("name","11",ar,ar.size());
//        System.out.println(findPage("name","11",1,10).getPageInfo().getToltalRecord());
//        System.out.println(getMap("aaa").get(1));
        Thread.sleep(1000);
//        asyncDel("name",1);
//        System.out.println(m.get(1));
//        setPage("name","11",ar,ar.size());
//        System.out.println(findPage("name","11",1,10).getPageInfo().getToltalRecord());
//        close(redisson);
    }
//    public static void registerRedissin(){
//        redisson=redissionPool.getResource();
//    }

    public static <T> List<T> getAll(String tablename, Collection ids) {
        List<T> list = new LinkedList<>();
        if (ids == null || ids.size() == 0) {
            List<T> redisList = getAll(tablename);
            if (redisList != null && redisList.size() > 0) return redisList;
        } else
            for (Object id : ids) {
                list.add((T) get(tablename, (Serializable) id));
            }
        return list;
    }

    private static <T> List<T> getAll(String tablename) {
        List<T> list = new LinkedList<>();

        RMap map = redisson.getMap(tablename);

        if (map != null) {

            list.addAll(map.values());
        }
        return list;

    }

    private static List<Class<?>> clazzs = Collections.synchronizedList(new LinkedList<Class<?>>());

    //    public static  void registerClazz(Class t){
//        clazzs.add(t);
//    }
    public static void removeAll(String tablename, Collection scoredEntries) {
        for (Object id : scoredEntries) {
            asyncDel(tablename, (Serializable) id);
        }
    }

    public static void setIds(String table, byte[] key, List scoredEntries) {
        //
        RScoredSortedSet scoredSortedSet = redisson.getScoredSortedSet(findPageKey(table, key));
        scoredSortedSet.expire(PropertiesUtils.getIntProperty("query.page.key.expire"), TimeUnit.SECONDS);
        int i = 1;
        for (Object entry : scoredEntries) {
            i++;
            scoredSortedSet.add(i, entry);

        }
    }


    public static List<Serializable> getIds(String table, byte[] key) {
        //
        List<Serializable> ids = new ArrayList<>();
        RScoredSortedSet scoredSortedSet = redisson.getScoredSortedSet(findPageKey(table, key));

        int i = 1;
        for (Object entry : scoredSortedSet) {
            ids.add((Serializable) entry);
        }
        return ids;
    }

    public static List<Serializable> getIds(String table, String key) {
        //
        RScoredSortedSet scoredSortedSet = redisson.getScoredSortedSet(findPageKey(table, key));
        List<Serializable> ids = new ArrayList<>();

        int i = 1;
        for (Object entry : scoredSortedSet) {
            ids.add((Serializable) entry);
        }
        return ids;
    }

    /***
     * @param key
     * @param value
     */
    public static void set(String key, Object value) {
        redisson.getSet(key).add(value);
    }

    public static void list(String key, Object value) {
        redisson.getList(key).add(value);
    }

    public static RList<Object> getList(String key) {
        return redisson.getList(key);
    }

    public static RSet getSet(String key) {
        return redisson.getSet(key);
    }

    public static void putDefaultMap(Object key, Object value) {
        redisson.getMap(DEFAULT_KEY).put(key, value);
    }

    public static RMap getDefaultMap() {
        return redisson.getMap(DEFAULT_KEY);
    }

    public static RMap getMap(String name) {
        return redisson.getMap(name);
    }

    public static RMap getMap(String name, long expire) {
        RMap rMap = redisson.getMap(name);
        rMap.expire(expire, TimeUnit.MILLISECONDS);
        return rMap;
    }

    public static RSortedSet<Object> getScoreSet(String name) {

        return redisson.getSortedSet(name);
    }

    public static RScoredSortedSet<Object> getScoredSortedSet(String name) {
        return redisson.getScoredSortedSet(name);
    }

    public static class ConfigTools {
        private final static Config config = new Config();

        protected static Config config(Host... hosts) {

            if (isSingle()) {
                SingleServerConfig singleServerConfig = config.useSingleServer();
                if (hosts == null || hosts.length == 0) {
                    singleServerConfig.setAddress("localhost:6379");

                } else if (hosts.length == 1) {
                    singleServerConfig.setAddress(hosts[0].getAddress() + ":" + hosts[0].getPort());
                }
                singleServerConfig.setConnectionPoolSize(180);
                singleServerConfig.setDnsMonitoring(true);
                singleServerConfig.setDnsMonitoringInterval(60000);
                singleServerConfig.setRetryInterval(5000);
                if (StringUtils.isNotEmpty(hosts[0].getUsername())) {
                    singleServerConfig.setPassword(hosts[0].getUsername() + ":" + hosts[0].getPassword());
                }
                singleServerConfig.setSubscriptionConnectionPoolSize(10);
            } else {
                for (Host h : hosts) {
                    ClusterServersConfig clusterServersConfig = config.useClusterServers();
                    clusterServersConfig.setMasterConnectionPoolSize(180);
                    clusterServersConfig.setScanInterval(3000);
                    if (StringUtils.isNotEmpty(h.getUsername())) {
                        clusterServersConfig.setPassword(h.getUsername() + ":" + h.getPassword());
                    }
                }
            }
//        config.setCodec(codec);
            return config;
        }

        private static boolean isSingle() {
            return PropertiesUtils.getProperty("redis.host.type").equals("single");
        }
    }


}
