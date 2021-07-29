package com.sunsy.netty.netty.c3.rpc;

import com.sunsy.netty.netty.c2.chatroom.protocol.MessageCodec;
import com.sunsy.netty.netty.c2.chatroom.protocol.MessageCodecSharable;
import com.sunsy.netty.netty.c2.chatroom.protocol.ProcotolFrameDecoder;
import com.sunsy.netty.netty.c3.rpc.handler.RpcRequestMessageHandler;
import com.sunsy.netty.netty.c3.rpc.message.RpcRequestMessage;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RpcServer {
    public static void main(String[] args) {
        NioEventLoopGroup boss = new NioEventLoopGroup();
        NioEventLoopGroup worker = new NioEventLoopGroup();
        LoggingHandler LOGGING_HANDLER = new LoggingHandler(LogLevel.DEBUG);
        MessageCodecSharable MESSAGE_CODEC = new MessageCodecSharable();
        // rpc请求处理器
        RpcRequestMessageHandler RPC_HANDLER = new RpcRequestMessageHandler();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.channel(NioServerSocketChannel.class);
            serverBootstrap.group(boss, worker);
            serverBootstrap.childHandler(new ChannelInitializer<NioSocketChannel>() {
                @Override
                protected void initChannel(NioSocketChannel channel) throws Exception {
                    channel.pipeline().addLast(new ProcotolFrameDecoder());
                    channel.pipeline().addLast(LOGGING_HANDLER);
                    channel.pipeline().addLast(MESSAGE_CODEC);
                    channel.pipeline().addLast(RPC_HANDLER);
                }
            });
            Channel channel = serverBootstrap.bind(8080).sync().channel();
            channel.closeFuture().sync();
        } catch (InterruptedException e) {
            log.error("server error", e);
        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }
}
