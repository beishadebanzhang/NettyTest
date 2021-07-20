package com.sunsy.netty.netty.c1.eventloop;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.Charset;

/**
 * eventLoop处理IO任务
 */
@Slf4j
public class EventLoopServer {
    public static void main(String[] args) {
        // 多个EventLoopGroup
        // 只能处理普通任务和定时任务
        // 可以将耗时的操作分给group去执行，减少worker内线程循环处理的阻塞时间
        // handler执行中如何换人 --》 static void invokeChannelRead(final AbstractChannelHandlerContext next, Object msg)
        // Executor executor = next.executor() --》 返回下一个handler的eventLoop
        // 通过executor.isEventLoop判断下一个handler中的线程是否属于当前的eventLoop，属于则直接调用next.invokeChannelRead
        // 否则使用 executor.execute(thread), 使用下一个evenLoop的线程调用
        EventLoopGroup group = new DefaultEventLoopGroup();
        new ServerBootstrap()
                // 细分boss与worker
                .group(new NioEventLoopGroup(1), new NioEventLoopGroup(2))
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel channel) throws Exception {
                        channel.pipeline().addLast("handler1", new ChannelInboundHandlerAdapter() {
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                ByteBuf buf = (ByteBuf) msg;
                                log.debug(buf.toString(Charset.defaultCharset()));
                                // 将消息传递给下一个handler
                                ctx.fireChannelRead(msg);
                            }
                        }).addLast(group, "handler", new ChannelInboundHandlerAdapter() {
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                ByteBuf buf = (ByteBuf) msg;
                                log.debug(buf.toString(Charset.defaultCharset()));
                            }
                        });
                    }
                })
                // 断点调试时，记得设为只阻塞当前线程，因为Channel的数据读写是异步的
                .bind(8080);
    }
}
