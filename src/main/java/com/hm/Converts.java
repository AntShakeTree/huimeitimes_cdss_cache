package com.hm;

import com.alibaba.fastjson.JSON;

import java.nio.charset.Charset;
import java.util.Map;
import java.util.Objects;

/**
 * Created by ant_shake_tree on 15/9/24.
 */
public class Converts {
    /**
     * Convert a long value to a byte array using big-endian.
     *
     * @param val value to convert
     * @return the byte array
     */
    public static byte[] toBytes(long val) {
        byte[] b = new byte[8];
        for (int i = 7; i > 0; i--) {
            b[i] = (byte) val;
            val >>>= 8;
        }
        b[0] = (byte) val;
        return b;
    }

    /**
     * Converts a string to a UTF-8 byte array.
     *
     * @param s string
     * @return the byte array
     */
    public static byte[] toBytes(String s) {
        return s.getBytes(UTF8_CHARSET);
    }

    /**
     * Converts a byte array to a long value. Reverses
     * {@link #toBytes(long)}
     *
     * @param bytes array
     * @return the long value
     */
    public static long toLong(byte[] bytes) {
        return toLong(bytes, 0, SIZEOF_LONG);
    }

    /**
     * Converts a byte array to a long value. Assumes there will be
     * {@link #SIZEOF_LONG} bytes available.
     *
     * @param bytes  bytes
     * @param offset offset
     * @return the long value
     */
    public static long toLong(byte[] bytes, int offset) {
        return toLong(bytes, offset, SIZEOF_LONG);
    }
//HConstants.UTF8_ENCODING should be updated if this changed
    /**
     * When we encode strings, we always specify UTF8 encoding
     */
    private static final String UTF8_ENCODING = "UTF-8";

    //HConstants.UTF8_CHARSET should be updated if this changed
    /**
     * When we encode strings, we always specify UTF8 encoding
     */
    private static final Charset UTF8_CHARSET = Charset.forName(UTF8_ENCODING);

    //HConstants.EMPTY_BYTE_ARRAY should be updated if this changed
    private static final byte[] EMPTY_BYTE_ARRAY = new byte[0];

//    private static final Log LOG = LogFactory.getLog(Bytes.class);

    /**
     * Size of boolean in bytes
     */
    public static final int SIZEOF_BOOLEAN = Byte.SIZE / Byte.SIZE;

    /**
     * Size of byte in bytes
     */
    public static final int SIZEOF_BYTE = SIZEOF_BOOLEAN;

    /**
     * Size of char in bytes
     */
    public static final int SIZEOF_CHAR = Character.SIZE / Byte.SIZE;

    /**
     * Size of double in bytes
     */
    public static final int SIZEOF_DOUBLE = Double.SIZE / Byte.SIZE;

    /**
     * Size of float in bytes
     */
    public static final int SIZEOF_FLOAT = Float.SIZE / Byte.SIZE;

    /**
     * Size of int in bytes
     */
    public static final int SIZEOF_INT = Integer.SIZE / Byte.SIZE;

    /**
     * Size of long in bytes
     */
    public static final int SIZEOF_LONG = Long.SIZE / Byte.SIZE;

    /**
     * Size of short in bytes
     */
    public static final int SIZEOF_SHORT = Short.SIZE / Byte.SIZE;

    /**
     * Mask to apply to a long to reveal the lower int only. Use like this:
     * int i = (int)(0xFFFFFFFF00000000L ^ some_long_value);
     */
    public static final long MASK_FOR_LOWER_INT_IN_LONG = 0xFFFFFFFF00000000L;

    /**
     * Estimate of size cost to pay beyond payload in jvm for instance of byte [].
     * Estimate based on study of jhat and jprofiler numbers.
     */
    // JHat says BU is 56 bytes.
    // SizeOf which uses java.lang.instrument says 24 bytes. (3 longs?)
    public static final int ESTIMATED_HEAP_TAX = 16;

    /**
     * Converts a byte array to a long value.
     *
     * @param bytes  array of bytes
     * @param offset offset into array
     * @param length length of data (must be {@link #SIZEOF_LONG})
     * @return the long value
     * @throws IllegalArgumentException if length is not {@link #SIZEOF_LONG} or
     *                                  if there's not enough room in the array at the offset indicated.
     */
    public static long toLong(byte[] bytes, int offset, final int length) {
        if (length != SIZEOF_LONG || offset + length > bytes.length) {
            throw explainWrongLengthOrOffset(bytes, offset, length, SIZEOF_LONG);
        }

        long l = 0;
        for (int i = offset; i < offset + length; i++) {
            l <<= 8;
            l ^= bytes[i] & 0xFF;
        }
        return l;
    }

    private static IllegalArgumentException
    explainWrongLengthOrOffset(final byte[] bytes,
                               final int offset,
                               final int length,
                               final int expectedLength) {
        String reason;
        if (length != expectedLength) {
            reason = "Wrong length: " + length + ", expected " + expectedLength;
        } else {
            reason = "offset (" + offset + ") + length (" + length + ") exceed the"
                    + " capacity of the array: " + bytes.length;
        }
        return new IllegalArgumentException(reason);
    }

    public static void main(String[] args) {
        System.out.print(toLong(toBytes(5l)));
    }

    /**
     * Converts a byte array to an int value
     *
     * @param bytes byte array
     * @return the int value
     */
    public static int toInt(byte[] bytes) {
        return toInt(bytes, 0, SIZEOF_INT);
    }

    /**
     * Converts a byte array to an int value
     *
     * @param bytes  byte array
     * @param offset offset into array
     * @return the int value
     */
    public static int toInt(byte[] bytes, int offset) {
        return toInt(bytes, offset, SIZEOF_INT);
    }

    /**
     * Converts a byte array to an int value
     *
     * @param bytes  byte array
     * @param offset offset into array
     * @param length length of int (has to be {@link #SIZEOF_INT})
     * @return the int value
     * @throws IllegalArgumentException if length is not {@link #SIZEOF_INT} or
     *                                  if there's not enough room in the array at the offset indicated.
     */
    public static int toInt(byte[] bytes, int offset, final int length) {
        if (length != SIZEOF_INT || offset + length > bytes.length) {
            throw explainWrongLengthOrOffset(bytes, offset, length, SIZEOF_INT);
        }

        int n = 0;
        for (int i = offset; i < (offset + length); i++) {
            n <<= 8;
            n ^= bytes[i] & 0xFF;
        }
        return n;

    }

    /**
     * Convert an int value to a byte array.  Big-endian.  Same as what DataOutputStream.writeInt
     * does.
     *
     * @param val value
     * @return the byte array
     */
    public static byte[] toBytes(int val) {
        byte[] b = new byte[4];
        for (int i = 3; i > 0; i--) {
            b[i] = (byte) val;
            val >>>= 8;
        }
        b[0] = (byte) val;
        return b;
    }

    public static byte[] toBytes(int... v) {
        byte[] bytes = new byte[Converts.SIZEOF_INT * v.length];
        int i = 0;
        for (int k : v)
            for (Byte b : Converts.toBytes(k)) {
                bytes[i++] = b;
            }
        return bytes;
    }

    public static byte[] toBytes(byte[] b1, byte[] b2) {
        byte[] vs = new byte[b1.length + b2.length];

        System.arraycopy(b1, 0, vs, 0, b1.length);
        System.arraycopy(b2, 0, vs, b1.length - 1, b2.length);
        return vs;
    }


    public static String toString(Map<String, String> args) {

        return "";
    }


    public static String toString(byte[] key) {
        return new String(key, UTF8_CHARSET);
    }


    public static String toString(Object object) {
         if (object instanceof String) {
            return (String)object;
        } else if (object instanceof Long) {
            return Long.toString((long)object);
        } else if (object instanceof Integer) {
            return Integer.toString((int)object);
        } else if (object instanceof Double) {
            return Double.toString((double)object);
        } else if (object instanceof Float) {
            return Float.toString((float)object);
        } else if (object instanceof Boolean) {
            return Boolean.toString((boolean) object);
        } else
            return JSON.toJSONString(object);

    }


    public static byte[] toBytes(Object object) {
        if (Objects.isNull(object))
            return new byte[0];
        else if (object instanceof String) {
            return toBytes((String) object);
        } else if (object instanceof Long) {
            return toBytes((long) object);
        } else if (object instanceof Integer) {
            return toBytes((int) object);
        } else if (object instanceof Double) {
            return toBytes((double) object);
        } else if (object instanceof Float) {
            return toBytes((float) object);
        } else if (object instanceof Boolean) {
            return toBytes((boolean) object);
        } else
            return JSON.toJSONBytes(object);

    }

    public static byte[] toBytes(double d) {
        return toBytes(Double.doubleToLongBits(d));
    }

    public static byte[] toBytes(float d) {
        return toBytes(Float.floatToIntBits(d));
    }

    public static byte[] toBytes(boolean d) {
        int b = 1;
        if (!d) b = 0;
        return toBytes(b);
    }

    public static <T> T toObject(String s,Class<T> clazz) {
        return JSON.parseObject(s,clazz);
    }
}
