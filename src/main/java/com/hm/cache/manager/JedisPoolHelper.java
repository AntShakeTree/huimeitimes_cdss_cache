package com.hm.cache.manager;

import com.hm.Host;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.function.Function;

/**
 * Created by ant_shake_tree on 16/1/15.
 */
public class JedisPoolHelper {
    static Host host = getHost();

    static JedisPool pool = new JedisPool(new JedisPoolConfig(), host.getAddress(), host.getPort(), host.getTimeout(), host.getPassword());


    public JedisPoolHelper() {

    }

    private static Host getHost() {
        return new Host(com.hm.PropertiesUtils.getProperty("redis.address"), com.hm.PropertiesUtils.getIntProperty("redis.port"), com.hm.PropertiesUtils.getIntProperty("redis.pool.timeout"), com.hm.PropertiesUtils.getIntProperty("max.total"), com.hm.PropertiesUtils.getProperty("redis.username"), com.hm.PropertiesUtils.getProperty("redis.password"));
    }
    public static <T> T executor(Function<Jedis,T> jedisTFunction){
        Jedis jedis=pool.getResource();
        try {
            jedis.select(0);
        return     jedisTFunction.apply(jedis);
        }finally {
            pool.returnResourceObject(jedis);// 向连接池“归还”资源，千万不要忘记。
        }

    }


}
