package com.sunsy.netty.netty.c2.decoder;

import com.sunsy.netty.netty.c2.example.HelloWorldClient;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

/**
 * 短连接：客户端发送消息后断开，不能解决半包问题
 */
@Slf4j
public class TestShortConnection {
    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            ChannelInboundHandlerAdapter adapter = new ChannelInboundHandlerAdapter() {
                // 会在channel建立成功后，触发active事件
                @Override
                public void channelActive(ChannelHandlerContext ctx) throws Exception {
                    ByteBuf byteBuf = ctx.alloc().buffer(16);
                    byteBuf.writeBytes(new byte[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15});
                    ctx.writeAndFlush(byteBuf);
                    ctx.channel().close();
                }
            };
           HelloWorldClient.send(adapter);
        }
        System.out.println("finish");
    }
}
