package com.sunsy.netty.netty.c4;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LoggingHandler;

/**
 * Netty服务器启动流程
 *  init:创建ssc; register:绑定ssc至selector; doBind：绑定端口
 *  1. init & register regFuture处理
 *      1.1 init main
 *          创建NioServerSocketChannel main
 *              ServerSocketChannel ssc = ServerSocketChannel.open()
 *          添加NioServerSocketChannel初始化handler main
 *              初始化handler等待调用 --> ssc.pipeline().addLast--> initChannel --> register中调用
 *              向ssc中加入了acceptor handler --> 在accept事件发生后建立连接
 *      1.2 register
 *              --> 切换线程 --> eventLoop为懒加载，首次调用execute时被创建
 *              --> 会给regFuture setSuccess，进行nio线程回调
 *          启动nio boss线程 main
 *          原生selector注册至selector未关注事件 nio-thread
 *              --> doRegister:
 *                      由Nio线程执行eventLoop.execute(thread:doRegister())
 *                      将原生channel与nio eventLoop中的selector绑定
 *                      NioServerSocketChannel作为附件绑定
 *                      暂时未关注事件
 *          执行NioServerSocketChannel初始化handler nio-thread
 *              pipeline.invokeHandleAddedIfNeeded()调用handler
 *              之后回调regFuture的doBind方法
 *  2. regFuture等待回调 doBind0 main
 *      原生ServerSocketChannel绑定 nio-thread
 *          javaChannel().bind(localAddress, config.getBacklog());
 *      触发NioServerSocketChannel active事件 nio-thread
 *          head --> acceptor --> tail
 *          head:
 *              headContext channelActive --> selectionKey.interest(SelectionKey.OP_ACCEPT);
 */
public class TestServerStart {
    public static void main(String[] args) {
        new ServerBootstrap()
                .group(new NioEventLoopGroup())
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel channel) throws Exception {
                        channel.pipeline().addLast(new LoggingHandler());
                    }
                })
                .bind(8080); // bind为入口

    }
}
