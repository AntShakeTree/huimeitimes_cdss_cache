package com.hm.hm.codec;

import com.esotericsoftware.kryo.Kryo;

/**
 * Created by ant_shake_tree on 16/1/15.
 */
public interface KryoPool {
    Kryo get();
    void yield(Kryo var1);
}
