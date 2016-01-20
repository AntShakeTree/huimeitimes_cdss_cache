//package com.redission;
//
//
////import com.redission.cache.MsgPackCodec;
//import org.apache.commons.pool2.PooledObject;
//import org.apache.commons.pool2.PooledObjectFactory;
//import org.apache.commons.pool2.impl.DefaultPooledObject;
//import org.apache.commons.pool2.impl.GenericObjectPool;
//import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
//import org.redisson.ClusterServersConfig;
//import org.redisson.Config;
//import org.redisson.Redisson;
//import org.redisson.SingleServerConfig;
//
//import java.net.URI;
//import java.util.concurrent.Executors;
//
///**
// * Created by ant_shake_tree on 15/10/26.
// */
//public class RedissionPool extends RPool {
////    private static  codec;
////    private final static MsgPackCodec codec =new MsgPackCodec();
//
//    public RedissionPool(final Host host) {
//        URI uri = URI.create(host.getAddress());
//        if (isValid(uri)) {
//            String h = uri.getPath();
//            int port = host.getPort();
//
//            this.internalPool = new GenericObjectPool<Redisson>(new RedissionFactory(h, port,
//                    Host.DEFAULT_TIMEOUT, Host.DEFAULT_TIMEOUT),
//                    new RedissionConfig());
//
//        } else {
//            this.internalPool = new GenericObjectPool<Redisson>(new RedissionFactory("localhost",
//                    Host.DEFAULT_PORT, Host.DEFAULT_TIMEOUT, Host.DEFAULT_TIMEOUT), new RedissionConfig());
//        }
//    }
//
//    public static RedissionPool getPools(Host h){
//        return  new RedissionPool(h);
//    }
//
//    public Redisson getResource(){
//        return super.getResource();
//    }
//    public void returnResource(Redisson red){
//        super.returnBrokenResource(red);
//    }
//
//    public static class RedissionFactory  implements PooledObjectFactory<Redisson>{
//        private Host host;
//
//        public RedissionFactory( String h,int port, int defaultTimeout, int defaultTimeout1) {
//            host= new Host();
//            host.setAddress(h);
//            host.setPort(port);
//            host.setTimeout(defaultTimeout);
//        }
//
//        public Host getHost() {
//            return host;
//        }
//
//        public void setHost(Host host) {
//            this.host = host;
//        }
//
//        @Override
//        public PooledObject<Redisson> makeObject() throws Exception {
//            Redisson redisson =Redisson.create(config(host));
//            return new DefaultPooledObject<>(redisson);
//        }
//
//        @Override
//        public void destroyObject(PooledObject<Redisson> p) throws Exception {
//            final Redisson redisson= p.getObject();
//            Executors.newSingleThreadExecutor().submit(new Runnable() {
//                @Override
//                public void run() {
//                    redisson.shutdown();
//                }
//            });
//        }
//
//        @Override
//        public boolean validateObject(PooledObject<Redisson> p) {
//            return true;
//        }
//
//        @Override
//        public void activateObject(PooledObject<Redisson> p) throws Exception {
//        }
//
//        @Override
//        public void passivateObject(PooledObject<Redisson> p) throws Exception {
//        }
//    }
//    public static boolean isValid(URI uri) {
//
////        if (Utils.isIp(uri.getPath())||Utils)  {
////            return true;
////        }
////        if (uri.getScheme()!=null&& uri.getHost()!=null){
////            return true;
////        }
//
//        return true;
//    }
//
//    private static boolean isEmpty(String value) {
//        return value == null || value.trim().length() == 0;
//    }
//    private final  static     Config config=new Config();
//    protected static Config config(Host... hosts){
//        if(isSingle()){
//            SingleServerConfig singleServerConfig = config.useSingleServer();
//            if(hosts==null||hosts.length==0) {
//                singleServerConfig.setAddress("localhost:6379");
//
//            }else if(hosts.length==1) {
//                singleServerConfig.setAddress(hosts[0].getAddress()+":"+hosts[0].getPort());
//            }
//            singleServerConfig.setConnectionPoolSize(180);
//            singleServerConfig.setDnsMonitoring(true);
//            singleServerConfig.setDnsMonitoringInterval(60000);
//            singleServerConfig.setRetryInterval(5000);
//
////            singleServerConfig.setPassword("6dbc0460f9a34cf9:hm2015RediswszD");
//            singleServerConfig.setSubscriptionConnectionPoolSize(10);
//        }else {
//            ClusterServersConfig clusterServersConfig=config.useClusterServers();
//            clusterServersConfig.setMasterConnectionPoolSize(180);
//            clusterServersConfig.setScanInterval(3000);
//        }
////        config.setCodec(codec);
//        return config;
//    }
//
//    public static boolean isSingle(){
//        String deployModel= PropertiesUtils.getProperty("redis.host.type");
//        if(deployModel.equalsIgnoreCase("single")){
//            return true;
//        }
//        return false;
//    }
//    public static class RedissionConfig extends GenericObjectPoolConfig{
//        public RedissionConfig() {
//            // defaults to make your life with connection pool easier :)
//            setTestWhileIdle(true);
//            setMinEvictableIdleTimeMillis(60000);
//            setTimeBetweenEvictionRunsMillis(30000);
//            setNumTestsPerEvictionRun(-1);
//        }
//    }
//
//
//}
