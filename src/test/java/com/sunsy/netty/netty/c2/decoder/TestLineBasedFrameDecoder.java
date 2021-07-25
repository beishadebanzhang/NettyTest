package com.sunsy.netty.netty.c2.decoder;

import com.sunsy.netty.netty.c2.example.HelloWorldClient;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.Random;

/**
 * 行解码器
 *  支持 \r or \r\n
 *  存在最大长度限制
 *  效率低，需要遍历寻找分割符
 */
public class TestLineBasedFrameDecoder {
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
                        StringBuilder sb = makeString(ch,
                                random.nextInt(256) + 1);
                        ch++;
                        buf.writeBytes(sb.toString().getBytes());
                    }
                    ctx.writeAndFlush(buf);
                }
            };
            HelloWorldClient.send(adapter);
        }
        System.out.println("finish");
    }

    public static StringBuilder makeString(char ch, int length) {
        StringBuilder sb = new StringBuilder(length + 2);
        for (int i = 0; i < length; i++) {
            sb.append(ch);
        }
        sb.append("\n");
        return sb;
    }
}
