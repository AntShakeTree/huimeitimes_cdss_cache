package com.hm;

import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.redisson.Redisson;
import org.redisson.client.RedisConnectionException;

import java.io.Closeable;
//import redis.clients.jedis.exceptions.JedisException;

/**
 * Created by ant_shake_tree on 15/10/26.
 */
public abstract class RPool  implements Closeable {

    protected GenericObjectPool<Redisson> internalPool;
    public void initPool(final GenericObjectPoolConfig poolConfig, PooledObjectFactory<Redisson> factory) {

        if (this.internalPool != null) {
            try {
                closeInternalPool();
            } catch (Exception e) {
            }
        }

        this.internalPool = new GenericObjectPool<Redisson>(factory, poolConfig);
    }






    /**
     * Using this constructor means you have to set and initialize the internalPool yourself.
     */
    public RPool() {
    }

    @Override
    public void close() {
        destroy();
    }

    public boolean isClosed() {
        return this.internalPool.isClosed();
    }

    public RPool(final GenericObjectPoolConfig poolConfig, PooledObjectFactory<Redisson> factory) {
        initPool(poolConfig, factory);
    }



    public Redisson getResource() {
        try {
            return internalPool.borrowObject();
        } catch (Exception e) {
            throw new RedisConnectionException("Could not get a resource from the pool", e);
        }
    }


    public void returnBrokenResource(final Redisson resource) {
        if (resource != null) {
            returnBrokenResourceObject(resource);
            Thread thread=new Thread(new Runnable() {
                @Override
                public void run() {
                    resource.shutdown();

                }
            });
            thread.start();
        }
    }



    public void destroy() {
        closeInternalPool();
    }

    protected void returnBrokenResourceObject(final Redisson resource) {
        try {
            internalPool.invalidateObject(resource);

        } catch (Exception e) {
            throw new com.hm.RedssionPoolException("Could not return the resource to the pool", e);
        }
    }

    protected void closeInternalPool() {
        try {
            internalPool.close();
        } catch (Exception e) {
            throw new com.hm.RedssionPoolException("Could not destroy the pool", e);
        }
    }

    public int getNumActive() {
        if (poolInactive()) {
            return -1;
        }

        return this.internalPool.getNumActive();
    }

    public int getNumIdle() {
        if (poolInactive()) {
            return -1;
        }

        return this.internalPool.getNumIdle();
    }

    public int getNumWaiters() {
        if (poolInactive()) {
            return -1;
        }

        return this.internalPool.getNumWaiters();
    }

    public long getMeanBorrowWaitTimeMillis() {
        if (poolInactive()) {
            return -1;
        }

        return this.internalPool.getMeanBorrowWaitTimeMillis();
    }

    public long getMaxBorrowWaitTimeMillis() {
        if (poolInactive()) {
            return -1;
        }

        return this.internalPool.getMaxBorrowWaitTimeMillis();
    }

    private boolean poolInactive() {
        return this.internalPool == null || this.internalPool.isClosed();
    }

    public void addObjects(int count) {
        try {
            for (int i = 0; i < count ; i++) {
                this.internalPool.addObject();
            }
        } catch (Exception e) {
            throw new com.hm.RedssionPoolException("Error trying to add idle objects", e);
        }
    }
}
