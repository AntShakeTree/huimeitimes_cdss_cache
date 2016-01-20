package com.hm.test;

import redis.clients.jedis.Jedis;
/**
 * Created by ant_shake_tree on 16/1/8.
 */
public class JedisTest {




        public static void main(String[] args) {
            try {
                String host = "121.40.104.98";//控制台显示访问地址
//                String host = "6dbc0460f9a34cf9.m.cnhza.kvstore.aliyuncs.com";//控制台显示访问地址
//                int port = 6379;
                int port = 9999;
                Jedis jedis = new Jedis(host, port);
                //鉴权信息由用户名:密码拼接而成
                jedis.auth("6dbc0460f9a34cf9:Huimei2015");//instance_id:password
                String key = "redis";
                String value = "aliyun-redis";
                //select db默认为0
                jedis.select(1);
                //set一个key
                jedis.set(key, value);
                jedis.hset("hk","k","v");
                jedis.hget("hk","k");
                System.out.println("Set Key " + key + " Value: " + value);
                //get 设置进去的key
                String getvalue = jedis.get(key);
                System.out.println("Get Key " + key + " ReturnValue: " + getvalue);
                jedis.quit();
                jedis.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


}
