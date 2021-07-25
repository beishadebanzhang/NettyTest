package com.sunsy.netty.netty.c2.example;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HelloWorldClient {

    private static Logger log = LoggerFactory.getLogger(HelloWorldClient.class);

    public static void main(String[] args) {
        ChannelInboundHandlerAdapter adapter = new ChannelInboundHandlerAdapter() {
            // 会在channel建立成功后，触发active事件
            @Override
            public void channelActive(ChannelHandlerContext ctx) throws Exception {
                for (int i = 0; i < 10; i ++) {
                    ByteBuf byteBuf = ctx.alloc().buffer(16);
                    byteBuf.writeBytes(new byte[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15});
                    ctx.writeAndFlush(byteBuf);
                }
            }
        };
        send(adapter);
    }

    public static void send(ChannelInboundHandlerAdapter adapter) {
        NioEventLoopGroup worker = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.group(worker);
            bootstrap.handler(new ChannelInitializer<NioSocketChannel>() {
                @Override
                protected void initChannel(NioSocketChannel channel) throws Exception {
                    channel.pipeline().addLast(adapter);
                }
            });
            ChannelFuture channelFuture = bootstrap.connect("localhost", 8080).sync();
            channelFuture.channel().closeFuture().sync();
        } catch (Exception e) {
            log.error("client error = {}", e);
        } finally {
            worker.shutdownGracefully();
        }
    }
}
