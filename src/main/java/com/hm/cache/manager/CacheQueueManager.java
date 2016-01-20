package com.hm.cache.manager;

import com.hm.engine.common.HmTreadPoolTools;
import com.hm.cache.Cache;
import com.hm.cache.option.CacheOperation;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.PriorityQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by ant_shake_tree on 15/11/23.
 */

public class CacheQueueManager implements HmCacheManager {
    private final RuleMachine ruleMachine = new RuleMachineImpl();

    private final static PriorityQueue<CacheJob> CACHE_QUEUE = new PriorityQueue<>();
    private HmCacheManager hmCacheManager;
    private final transient ReentrantLock lock = new ReentrantLock();
    private final Condition available = lock.newCondition();


    public CacheQueueManager() {
    }

    protected CacheQueueManager(HmCacheManager hmCacheManager) {
        this.hmCacheManager = hmCacheManager;
    }


    public HmCacheManager get(HmCacheManager hmCacheManager) {
        this.hmCacheManager = hmCacheManager;

        return this;
    }


    public void setHmCacheManager(HmCacheManager hmCacheManager) {
        this.hmCacheManager = hmCacheManager;
    }

    @Override
    public Cache getCache(String name) {
        return hmCacheManager.getCache(name);
    }

    @Override
    public boolean exist(String name) {
        return hmCacheManager.exist(name);
    }

    @Override
    public Collection<String> getCacheNames() {
        return hmCacheManager.getCacheNames();
    }

    @Override
    public CacheOperation addOpthion(Method targetMethod) {
        return hmCacheManager.addOpthion(targetMethod);
    }

    public CacheQueueManager addTask(CacheJob cacheTask) {
        lock.lock();
        try {
            if (!CACHE_QUEUE.contains(cacheTask))
                offer(cacheTask);
        } finally {
            lock.unlock();
        }
        return this;
    }

    public void runTask(String name) {
        HmTreadPoolTools.monitorExecute(() -> {
            while (true) {
                CacheJob cacheTask = null;
                try {
                    cacheTask = take(name);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Cache cache = getCache(cacheTask.name);
                Object re = ruleMachine.invork(cacheTask.getTaget(), cacheTask.methedName, cacheTask.getArguments());
                cache.put(cacheTask.getKey(), re);
            }
        });
    }


    public static class CacheJob implements Comparable<CacheJob> {
        Object target;
        String methedName;
        HmParameter[] arguments;
        String key;
        String name;
        int order;

        public Object getTaget() {
            return target;
        }

        public CacheJob target(Object taget) {
            this.target = taget;
            return this;
        }

        public CacheJob name(String name) {
            this.name = name;
            return this;
        }

        public String getMethedName() {
            return methedName;
        }

        public CacheJob methedName(String methedName) {
            this.methedName = methedName;
            return this;
        }

        public CacheJob key(String key) {
            this.key = key;
            return this;
        }

        public CacheJob order(int order) {
            this.order = order;
            return this;
        }

        public HmParameter[] getArguments() {
            return arguments;
        }

        public CacheJob arguments(HmParameter... arguments) {
            this.arguments = arguments;
            return this;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            CacheJob cacheJob = (CacheJob) o;

            if (order != cacheJob.order) return false;
            if (!key.equals(cacheJob.key)) return false;
            return name.equals(cacheJob.name);

        }

        @Override
        public int hashCode() {
            int result = key.hashCode();
            result = 31 * result + name.hashCode();
            result = 31 * result + order;
            return result;
        }

        @Override
        public int compareTo(CacheJob o) {
            if (o == null)
                return -1;

            return this.order - o.order;
        }

        public Object getTarget() {
            return target;
        }

        public String getName() {
            return name;
        }

        public int getOrder() {
            return order;
        }

        public String getKey() {
            return key;
        }

    }



    private boolean offer(CacheJob e) {
        final ReentrantLock lock = this.lock;
        lock.lock();
        try {
            CACHE_QUEUE.offer(e);
            if (CACHE_QUEUE.peek() == e) {
                available.signal();
            }
            return true;
        } finally {
            lock.unlock();
        }
    }

    public CacheJob take() throws InterruptedException {
        final ReentrantLock lock = this.lock;
        lock.lockInterruptibly();
        try {
            for (; ; ) {
                CacheJob first = CACHE_QUEUE.peek();
                if (first == null)
                    available.await();
                else {
                    return CACHE_QUEUE.poll();
                }
            }
        } finally {
            if (CACHE_QUEUE.peek() != null)
                available.signal();
            lock.unlock();
        }
    }
    public CacheJob take(String name) throws InterruptedException {
        final ReentrantLock lock = this.lock;
        lock.lockInterruptibly();
        try {
            for (; ; ) {
                CacheJob first = CACHE_QUEUE.peek();
                if (first == null||!name.equals(first.getName()))
                    available.await();
                else {
                    return CACHE_QUEUE.poll();
                }
            }
        } finally {
            if (CACHE_QUEUE.peek() != null&&name.equals(CACHE_QUEUE.peek().getName()))
                available.signal();
            lock.unlock();
        }
    }



    public static CacheQueueManager getInstance(HmCacheManager cacheQueueManagerhmCacheManager){
        return Instance.getInstance(cacheQueueManagerhmCacheManager);
    }
    private static class Instance {
        private static CacheQueueManager cacheQueueManager = new CacheQueueManager();

        public static CacheQueueManager getInstance(HmCacheManager cacheQueueManagerhmCacheManager) {
            cacheQueueManager.setHmCacheManager(cacheQueueManagerhmCacheManager);
            return cacheQueueManager;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        TestClass testClass=new TestClass();
        CacheJob cacheJob =new CacheJob();
        cacheJob.key("nima");
        cacheJob.methedName("testCache");
        cacheJob.name("nima");
        cacheJob.target(testClass);
        cacheJob.arguments(new HmParameter().position(1).type(String.class).value("xxxxxx"));
        cacheJob.order(2);
        CacheQueueManager cacheQueueManager=CacheQueueManager.Instance.getInstance(ManagerDefault.get());
        cacheQueueManager.addTask(cacheJob);
        TestClass testClass2=new TestClass();
        CacheJob cacheJob2 =new CacheJob();
        cacheJob2.key("nima");
        cacheJob2.name("nima");
        cacheJob2.methedName("testCache");
        cacheJob2.target(testClass2);
        cacheJob2.arguments(new HmParameter().position(1).type(String.class).value("nimashishanpao"));
        cacheJob2.order(1);
        cacheQueueManager.addTask(cacheJob2);

        Thread.sleep(1000);
        cacheQueueManager.runTask("nima");

    }
    public static class TestClass{
        public Object testCache(String message){
            System.out.println("xxxxxx"+message);
            return "nima"+message;
        }
    }
}

