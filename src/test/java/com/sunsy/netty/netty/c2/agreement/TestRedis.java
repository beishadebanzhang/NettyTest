package com.sunsy.netty.netty.c2.agreement;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.Charset;

/**
 * 以Redis为例实践协议设计与解析
 */
@Slf4j
public class TestRedis {
    /**
     * set name zhangsan
     * *3
     * $3
     * set
     * $4
     * name
     * $8
     * zhangsan
     */
    public static void main(String[] args) {
        final byte[] LINE = {13, 10}; // 换行
        NioEventLoopGroup worker = new NioEventLoopGroup();
        try {
            ChannelFuture channelFuture = new Bootstrap()
                    .channel(NioSocketChannel.class)
                    .group(worker)
                    .handler(new ChannelInitializer<NioSocketChannel>() {
                        @Override
                        protected void initChannel(NioSocketChannel channel) throws Exception {
                            channel.pipeline().addLast(new LoggingHandler());
                            channel.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                                @Override
                                public void channelActive(ChannelHandlerContext ctx) throws Exception {
                                    ByteBuf buf = ctx.alloc().buffer();
                                    buf.writeBytes("*3".getBytes());
                                    buf.writeBytes(LINE);
                                    buf.writeBytes("$3".getBytes());
                                    buf.writeBytes(LINE);
                                    buf.writeBytes("set".getBytes());
                                    buf.writeBytes(LINE);
                                    buf.writeBytes("$4".getBytes());
                                    buf.writeBytes(LINE);
                                    buf.writeBytes("name".getBytes());
                                    buf.writeBytes(LINE);
                                    buf.writeBytes("$8".getBytes());
                                    buf.writeBytes(LINE);
                                    buf.writeBytes("zhangsan".getBytes());
                                    buf.writeBytes(LINE);
                                    ctx.writeAndFlush(buf);
                                }
                            });
                        }

                        @Override
                        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                            ByteBuf buf = ByteBufAllocator.DEFAULT.buffer();
                            System.out.println(buf.toString(Charset.defaultCharset()));
                        }
                    })
                    .connect("localhost", 6379).sync();
                    channelFuture.channel().closeFuture().sync();
        } catch (Exception e) {
            log.error("client error", e);
        } finally {
            worker.shutdownGracefully();
        }
    }
}
