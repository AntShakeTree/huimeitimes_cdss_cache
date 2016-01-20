package com.hm.hm.codec;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Output;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;

/**
 * Created by ant_shake_tree on 16/1/15.
 */
public interface HmDecode {


    default byte[] decode(Object in){
        KryoPool kryoPool =new KryoPoolImpl(Arrays.asList(in.getClass()));
        Kryo kryo1 = null;
        byte[] var5=null;
        try {
            ByteArrayOutputStream e1 = new ByteArrayOutputStream();
            Output output = new Output(e1);
            kryo1 = kryoPool.get();
            kryo1.writeClassAndObject(output, in);
            output.close();
            var5 = e1.toByteArray();
            return var5;
        } catch (Exception var9) {
            if(var9 instanceof RuntimeException) {
                throw (RuntimeException)var9;
            }
        } finally {
            if(kryo1 != null) {
                kryoPool.yield(kryo1);
            }
        }
        return var5;
    }
}
