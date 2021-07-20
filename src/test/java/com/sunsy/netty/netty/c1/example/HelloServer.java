package com.sunsy.netty.netty.c1.example;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;

import java.net.InetSocketAddress;

/**
 * 使用netty构建服务器
 */
public class HelloServer {
    public static void main(String[] args) {
        // 服务器启动类
        new ServerBootstrap()
                // 配置事件循环组
                .group(new NioEventLoopGroup())
                // 选择服务器ServerSocketChannel类型
                .channel(NioServerSocketChannel.class)
                // 配置worker的handler
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel channel) throws Exception {
                        // ByteBuf转为字符串的工序
                        channel.pipeline().addLast(new StringDecoder());
                        channel.pipeline().addLast(new ChannelInboundHandlerAdapter(){
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                System.out.println(msg);
                            }
                        });
                    }
                })
                // 绑定端口
                .bind(new InetSocketAddress(8080));
    }
}
