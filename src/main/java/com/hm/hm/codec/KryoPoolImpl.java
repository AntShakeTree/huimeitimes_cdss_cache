package com.hm.hm.codec;

import com.esotericsoftware.kryo.Kryo;

import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by ant_shake_tree on 16/1/15.
 */
public class KryoPoolImpl implements KryoPool {
    private final Queue<Kryo> objects = new ConcurrentLinkedQueue();
    private final List<Class<?>> classes;

    public KryoPoolImpl(List<Class<?>> classes) {
        this.classes = classes;
    }


    public Kryo get() {
        Kryo kryo;
        if((kryo = (Kryo)this.objects.poll()) == null) {
            kryo = this.createInstance();
        }

        return kryo;
    }

    public void yield(Kryo kryo) {
        this.objects.offer(kryo);
    }

    protected Kryo createInstance() {
        Kryo kryo = new Kryo();
        kryo.setReferences(false);
        Iterator it = this.classes.iterator();
        while(it.hasNext()) {
            Class clazz = (Class)it.next();
            kryo.register(clazz);
        }
        return kryo;
    }
}
