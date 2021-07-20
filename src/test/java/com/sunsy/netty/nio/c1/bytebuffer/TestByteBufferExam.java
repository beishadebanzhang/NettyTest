package com.sunsy.netty.nio.c1.bytebuffer;

import java.nio.ByteBuffer;

import static com.sunsy.netty.util.ByteBufferUtil.debugAll;

/**
 * 黏包与半包
 */
public class TestByteBufferExam {
    public static void main(String[] args) {
        ByteBuffer source = ByteBuffer.allocate(32);
        source.put("Hello,world\nI'm zhangsan\nHo".getBytes());
        split(source);
        source.put("w are you?\n".getBytes());
        split(source);
    }

    public static void split(ByteBuffer source) {
        source.flip();
        for (int i = 0; i < source.limit(); i++) {
            if (source.get(i) == '\n') {
                int lenght = i + 1 - source.position();
                ByteBuffer target = ByteBuffer.allocate(lenght);
                for (int j = 0; j < lenght; j++) {
                    target.put(source.get());
                }
                debugAll(target);
            }
        }
        source.compact();
    }
}
