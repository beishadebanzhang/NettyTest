package com.sunsy.netty.netty.c2.decoder;

import com.sunsy.netty.netty.c2.example.HelloWorldClient;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.Random;

/**
 * 定长解码器:
 *  存在带宽浪费的情况
 *  同时不够灵活
 */
public class TestFixedLengthFrameDecoder {
    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            ChannelInboundHandlerAdapter adapter = new ChannelInboundHandlerAdapter() {
                // 会在channel建立成功后，触发active事件
                @Override
                public void channelActive(ChannelHandlerContext ctx) throws Exception {
                    ByteBuf buf = ByteBufAllocator.DEFAULT.buffer();
                    char ch = '0';
                    Random random = new Random();
                    for (int i = 0; i < 10; i++) {
                        byte[] bytes = fill0Bytes(ch,
                                random.nextInt(10) + 1);
                        ch++;
                        buf.writeBytes(bytes);
                    }
                    ctx.writeAndFlush(buf);
                }
            };
            HelloWorldClient.send(adapter);
        }
        System.out.println("finish");
    }

    public static byte[] fill0Bytes(char c, int length) {
        byte[] bytes = new byte[10];
        for(byte item : bytes) {
            item = (byte) '.';
        }
        for(int i = 0; i < length; i++) {
            bytes[i] = (byte) c;
        }
        return bytes;
    }
}
