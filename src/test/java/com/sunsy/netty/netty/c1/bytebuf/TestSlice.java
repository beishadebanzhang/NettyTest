package com.sunsy.netty.netty.c1.bytebuf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

/**
 * ByteBuf的零拷贝思想
 */
public class TestSlice {
    public static void main(String[] args) {
        // slice():对原始buf切分，并未发生内存复制，切片后的buf维护独立的读写指针
        ByteBuf buf = ByteBufAllocator.DEFAULT.buffer(10);
        buf.writeBytes(new byte[]{'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j'});
        System.out.println(buf);
        // 切片，并未发生内存复制
        ByteBuf f1 = buf.slice(0, 5);
        f1.retain();
        ByteBuf f2 = buf.slice(5, 5);
        f2.retain();
        System.out.println(f1);
        System.out.println(f2);
        f1.setByte(0, 'b');
        // 切分出的buf无法写入值，源buf可写入值
        // f1.writeBytes(new byte[]{'a', 'b'});
        // 释放源buf可能会导致切分出的对象失效
        buf.release();
        f1.release();
        f2.release();

        // duplicate 复制原始buf, 无max capacity限制
        ByteBuf f3 = buf.duplicate();
        // copy 深拷贝，与源数据无关
        ByteBuf f4 = buf.copy();
    }
}
