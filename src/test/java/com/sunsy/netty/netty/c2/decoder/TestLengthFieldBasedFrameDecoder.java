package com.sunsy.netty.netty.c2.decoder;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * LTC解码器
 *  lengthFieldOffset 长度字段偏移量
 *  lengthFieldLength 长度字段长度
 *  lengthAdjustment 长度字段为基准，还有几个字节之后是内容
 *  initialBytesToStrip 从头剥离几个字节
 */
public class TestLengthFieldBasedFrameDecoder {
    public static void main(String[] args) {
        EmbeddedChannel channel = new EmbeddedChannel(
                new LengthFieldBasedFrameDecoder(1024, 0, 4, 1, 4),
                new LoggingHandler(LogLevel.DEBUG)
        );

        ByteBuf buf = ByteBufAllocator.DEFAULT.buffer();
        send(buf, "Hello, world");
        send(buf, "Hi");
        channel.writeInbound(buf);
    }

    private static void send(ByteBuf buf, String content) {
        byte[] bytes = content.getBytes();
        int length = bytes.length;
        buf.writeInt(length);
        buf.writeByte(1);
        buf.writeBytes(bytes);
    }
}
