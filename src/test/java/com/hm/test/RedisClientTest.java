package com.hm.test;

import com.hm.RedissionHelpler;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.FutureListener;
import io.netty.util.concurrent.Promise;
import org.junit.Assert;
import org.junit.Test;
import org.redisson.client.RedisClient;
import org.redisson.client.RedisConnection;
import org.redisson.client.RedisPubSubConnection;
import org.redisson.client.RedisPubSubListener;
import org.redisson.client.codec.StringCodec;
import org.redisson.client.protocol.CommandData;
import org.redisson.client.protocol.CommandsData;
import org.redisson.client.protocol.RedisCommands;
import org.redisson.client.protocol.pubsub.PubSubType;
import org.redisson.core.RList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

public class RedisClientTest {

    @Test
    public void testConnectAsync() throws InterruptedException {
        RedisClient c = new RedisClient("localhost", 6379);
        Future<RedisConnection> f = c.connectAsync();
        final CountDownLatch l = new CountDownLatch(1);
        f.addListener(new FutureListener<RedisConnection>() {
            public void operationComplete(Future<RedisConnection> future) throws Exception {
                RedisConnection conn = future.get();
                Assert.assertEquals("PONG", conn.sync(RedisCommands.PING));
                l.countDown();
            }
        });
        l.await();
    }

    @Test
    public void testSubscribe() throws InterruptedException {
        RedisClient c = new RedisClient("localhost", 6379);
        RedisPubSubConnection pubSubConnection = c.connectPubSub();
        final CountDownLatch latch = new CountDownLatch(2);
        pubSubConnection.addListener(new RedisPubSubListener<Object>() {
            public boolean onStatus(PubSubType type, String channel) {
                Assert.assertEquals(PubSubType.SUBSCRIBE, type);
                Assert.assertTrue(Arrays.asList("test1", "test2").contains(channel));
                latch.countDown();
                return true;
            }

            public void onMessage(String channel, Object message) {
                System.out.println(channel);
                System.out.println(message);
            }

            public void onPatternMessage(String pattern, String channel, Object message) {
            }
        });
        pubSubConnection.subscribe(StringCodec.INSTANCE, "test1", "test2");
        latch.await();
    }

    @Test
    public void test() throws InterruptedException {
        RedisClient c = new RedisClient("localhost", 6379);
        final RedisConnection conn = c.connect();

        conn.sync(StringCodec.INSTANCE, RedisCommands.SET, "test", 0);
        ExecutorService pool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2);
        for (int i = 0; i < 100000; i++) {
            pool.execute(new Runnable() {
                public void run() {
                    conn.async(StringCodec.INSTANCE, RedisCommands.INCR, "test");
                }
            });
        }

        pool.shutdown();
        Assert.assertTrue(pool.awaitTermination(1, TimeUnit.HOURS));

//        Assert.assertEquals(100000L, conn.sync(LongCodec.INSTANCE, RedisCommands.GET, "test"));

        conn.sync(RedisCommands.FLUSHDB);
    }

    @Test
    public void testPipeline() throws InterruptedException, ExecutionException {
        RedisClient c = new RedisClient("localhost", 6379);
        RedisConnection conn = c.connect();

        conn.sync(StringCodec.INSTANCE, RedisCommands.SET, "test", 0);

        List<CommandData<?, ?>> commands = new ArrayList<CommandData<?, ?>>();
        CommandData<String, String> cmd1 = conn.create(null, RedisCommands.PING);
        commands.add(cmd1);
        CommandData<Long, Long> cmd2 = conn.create(null, RedisCommands.INCR, "test");
        commands.add(cmd2);
        CommandData<Long, Long> cmd3 = conn.create(null, RedisCommands.INCR, "test");
        commands.add(cmd3);
        CommandData<String, String> cmd4 = conn.create(null, RedisCommands.PING);
        commands.add(cmd4);

        Promise<Void> p = c.getBootstrap().group().next().newPromise();
        conn.send(new CommandsData(p, commands));

        Assert.assertEquals("PONG", cmd1.getPromise().get());
        Assert.assertEquals(1, (long) cmd2.getPromise().get());
        Assert.assertEquals(2, (long) cmd3.getPromise().get());
        Assert.assertEquals("PONG", cmd4.getPromise().get());

        conn.sync(RedisCommands.FLUSHDB);
    }

    @Test
    public void testBigRequest() throws InterruptedException, ExecutionException {
        RedisClient c = new RedisClient("localhost", 6379);
        RedisConnection conn = c.connect();

        for (int i = 0; i < 50; i++) {
            conn.sync(StringCodec.INSTANCE, RedisCommands.HSET, "testmap", i, "2");
        }

        Map<Object, Object> res = conn.sync(StringCodec.INSTANCE, RedisCommands.HGETALL, "testmap");
        Assert.assertEquals(50, res.size());

        conn.sync(RedisCommands.FLUSHDB);
    }

    @Test
    public void testPipelineBigResponse() throws InterruptedException, ExecutionException {
        RedisClient c = new RedisClient("localhost", 6379);
        RedisConnection conn = c.connect();

        List<CommandData<?, ?>> commands = new ArrayList<CommandData<?, ?>>();
        for (int i = 0; i < 1000; i++) {
            CommandData<String, String> cmd1 = conn.create(null, RedisCommands.PING);
            commands.add(cmd1);
        }

        Promise<Void> p = c.getBootstrap().group().next().newPromise();
        conn.send(new CommandsData(p, commands));

        for (CommandData<?, ?> commandData : commands) {
            commandData.getPromise().get();
        }

        conn.sync(RedisCommands.FLUSHDB);
    }

    public static void main(String[] args) throws InterruptedException {
        RedisClientTest redisClientTest = new RedisClientTest();
        redisClientTest.testSubscribe();


    }

    @Test
    public void testAddByIndex() {
        RList<Object> test2 = RedissionHelpler.getList("test2");

//        test2.clear();
        Map m = RedissionHelpler.getMap("test2");
        m.put(1, 2);
//        m.clear();
        RedissionHelpler.getSet("test2").add(1);
        RedissionHelpler.getSet("test2").add(2);
        RedissionHelpler.getSet("test2").add(3);

//        MatcherAssert.assertThat(test2, (Matcher<? super RList<Object>>) Matchers.contains(1, 2));
    }
//    @Test
//    public void testc(){
//        Redisson redisson =RedissionHelpler.getRedisson();
//        RScoredSortedSet<String> scoredSortedSet= redisson.getScoredSortedSet("name");
//        for(int i=1;i<=20;i++) {
//            scoredSortedSet.add(i, UUID.randomUUID().toString());
//        }
//        Collection<ScoredEntry<String>> scoredEntries=  RedissionHelpler.findPage("name","t".getBytes(), 1, 10);
//        for (ScoredEntry<String> stringScoredEntry:scoredEntries){
//            System.out.println(stringScoredEntry.getScore()+"=======>"+stringScoredEntry.getValue());
//        }
//        Collection<ScoredEntry<String>> scoredEntries1=  RedissionHelpler.findPage("name","t".getBytes(), 2, 10);
//        for (ScoredEntry<String> stringScoredEntry:scoredEntries1){
//            System.out.println(stringScoredEntry.getScore()+"=======>"+stringScoredEntry.getValue());
//        }
//        scoredSortedSet.clear();
//        long start=System.currentTimeMillis();
//
//        RedissionHelpler.close(redisson);
//        System.err.println(System.currentTimeMillis() - start);
//    }
}

