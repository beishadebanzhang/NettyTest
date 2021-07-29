package com.sunsy.netty.netty.c3.rpc;

import com.sunsy.netty.netty.c2.chatroom.protocol.MessageCodecSharable;
import com.sunsy.netty.netty.c2.chatroom.protocol.ProcotolFrameDecoder;
import com.sunsy.netty.netty.c3.rpc.handler.RpcResponseMessageHandler;
import com.sunsy.netty.netty.c3.rpc.message.RpcRequestMessage;
import com.sunsy.netty.netty.c3.rpc.service.HelloService;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.concurrent.DefaultPromise;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Proxy;

@Slf4j
public class RpcClientManager {

    private static Channel channel = null;

    private static final Object LOCK = new Object();

    public static void main(String[] args) {
        HelloService service = getProxyService(HelloService.class);
        service.sayHello("张三");
    }

    public static <T> T getProxyService(Class<T> serviceClass) {
        ClassLoader loader = serviceClass.getClassLoader();
        Class<?>[] interfaces = new Class[]{serviceClass};
        Object o = Proxy.newProxyInstance(loader, interfaces, (proxy, method, args) -> {
            int sequenceId = SequenceIdGenerator.nextId();
            RpcRequestMessage msg = new RpcRequestMessage(
                    sequenceId,
                    serviceClass.getName(),
                    method.getName(),
                    method.getReturnType(),
                    method.getParameterTypes(),
                    args
            );
            getChannel().writeAndFlush(msg);

            DefaultPromise<Object> promise = new DefaultPromise<>(getChannel().eventLoop());
            RpcResponseMessageHandler.PROMISES.put(sequenceId, promise);
            promise.await();
            if (promise.isSuccess()) {
                return promise.getNow();
            } else {
                throw new RuntimeException(promise.cause());
            }
        });
        return (T) o;
    }

    public static Channel getChannel() {
        if (null != channel) {
            return channel;
        }
        synchronized (LOCK) {
            if (null != channel) {
                return channel;
            }
            initChannel();
            return channel;
        }
    }

    private static void initChannel() {
        NioEventLoopGroup group = new NioEventLoopGroup();
        LoggingHandler LOGGING_HANDLER = new LoggingHandler(LogLevel.DEBUG);
        MessageCodecSharable MESSAGE_CODEC = new MessageCodecSharable();
        RpcResponseMessageHandler RPC_HANDLER = new RpcResponseMessageHandler();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.group(group);
        bootstrap.handler(new ChannelInitializer<NioSocketChannel>() {
            @Override
            protected void initChannel(NioSocketChannel channel) throws Exception {
                channel.pipeline().addLast(new ProcotolFrameDecoder());
                channel.pipeline().addLast(LOGGING_HANDLER);
                channel.pipeline().addLast(MESSAGE_CODEC);
                channel.pipeline().addLast(RPC_HANDLER);
            }
        });
        try {
            channel = bootstrap.connect("localhost", 8080).sync().channel();
            channel.closeFuture().addListener(future -> {
                group.shutdownGracefully();
            });
        } catch (Exception e) {
            log.error("client error", e);
        }
    }
}
