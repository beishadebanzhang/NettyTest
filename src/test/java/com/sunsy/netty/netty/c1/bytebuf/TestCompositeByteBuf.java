package com.sunsy.netty.netty.c1.bytebuf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.CompositeByteBuf;

/**
 * 多个buf在逻辑上合成一个buf不发生数据复制
 */
public class TestCompositeByteBuf {
    public static void main(String[] args) {
        ByteBuf buf1 = ByteBufAllocator.DEFAULT.buffer();
        buf1.writeBytes(new byte[]{1, 2, 3, 4});
        ByteBuf buf2 = ByteBufAllocator.DEFAULT.buffer();
        buf1.writeBytes(new byte[]{1, 2, 3, 4});

        CompositeByteBuf buf = ByteBufAllocator.DEFAULT.compositeBuffer();
        // true：自动调整写指针
        // 另外需要注意release问题
        buf.addComponents(true, buf1, buf2);

    }
}
