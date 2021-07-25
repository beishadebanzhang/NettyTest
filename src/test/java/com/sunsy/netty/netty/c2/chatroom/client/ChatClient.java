package com.sunsy.netty.netty.c2.chatroom.client;

import com.sunsy.netty.netty.c2.chatroom.message.*;
import com.sunsy.netty.netty.c2.chatroom.protocol.MessageCodecSharable;
import com.sunsy.netty.netty.c2.chatroom.protocol.ProcotolFrameDecoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 连接假死：
 *  原因：
 *      网络设备出现故障，例如网卡，机房等，底层的TCP断开了，但应用程序没有感知到，任然占用着资源
 *      公共网络不稳定，出现丢包。如果出现连续丢包，这时现象是客户端数据发不出去，服务端也一直收不到数据，一直耗着
 *      应用程序线程阻塞，无法进行数据读写
 *  问题：
 *      假死的连接占用的资源不能自动释放
 *      向假死的连接发送数据，得到的反馈是发送超时
 */
@Slf4j
public class ChatClient {
    public static void main(String[] args) {
        NioEventLoopGroup group = new NioEventLoopGroup();
        LoggingHandler LOGGING_HANDLER = new LoggingHandler(LogLevel.DEBUG);
        MessageCodecSharable MESSAGE_CODEC = new MessageCodecSharable();
        // 倒计时锁，计数减为0时await方法结束等待
        CountDownLatch WAIT_FOR_LOGIN = new CountDownLatch(1);
        AtomicBoolean LOGIN = new AtomicBoolean(false);
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.group(group);
            bootstrap.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new ProcotolFrameDecoder());
                    ch.pipeline().addLast(LOGGING_HANDLER);
                    ch.pipeline().addLast(MESSAGE_CODEC);

                    ch.pipeline().addLast(new IdleStateHandler(0, 3, 0));
                    ch.pipeline().addLast(new ChannelDuplexHandler() {
                        // 用来触发特殊事件
                        @Override
                        public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
                            IdleStateEvent event = (IdleStateEvent) evt;
                            // 触发了读空闲事件
                            if (event.state() == IdleState.WRITER_IDLE) {
                                log.debug("已经3s未写数据，发送一个心跳包");
                                ctx.writeAndFlush(new PingMessage());
                            }
                        }
                    });


                    ch.pipeline().addLast(new ChannelInboundHandlerAdapter(){
                        // 连接建立后触发active事件
                        @Override
                        public void channelActive(ChannelHandlerContext ctx) throws Exception {
                            // 负责接收用户在控制台输入，负责向服务器发送各种消息
                            new Thread(()->{
                                Scanner scanner = new Scanner(System.in);
                                System.out.println("请输入用户名：");
                                String username = scanner.nextLine();
                                System.out.println("请输入密码：");
                                String password = scanner.nextLine();
                                // 构造消息对象
                                LoginRequestMessage message = new LoginRequestMessage(username, password);
                                // 发送消息
                                ctx.writeAndFlush(message);

                                System.out.println("等待后续操作");
                                try {
                                    WAIT_FOR_LOGIN.await();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                // 如果登录失败
                                if (!LOGIN.get()) {
                                    ctx.channel().close();
                                    return;
                                }
                                while (true) {
                                    System.out.println("==========");
                                    System.out.println("send [username] [content]");
                                    System.out.println("gsend [group name] [content]");
                                    System.out.println("gcreate [group name] [m1,m2,m3...]");
                                    System.out.println("gmembers [group name]");
                                    System.out.println("gjoin [group name]");
                                    System.out.println("gquit [group name]");
                                    System.out.println("quit");
                                    System.out.println("==========");
                                    String command = scanner.nextLine();
                                    String[] s = command.split(" ");
                                    switch (s[0]) {
                                        case "send":
                                            ctx.writeAndFlush(new ChatRequestMessage(username, s[0], s[1]));
                                            break;
                                        case "gsend":
                                            ctx.writeAndFlush(new GroupChatRequestMessage(username, s[0], s[1]));
                                            break;
                                        case "gcreate":
                                            Set<String> set = new HashSet<>(Arrays.asList(s[2].split(",")));
                                            set.add(username);
                                            ctx.writeAndFlush(new GroupCreateRequestMessage(username, set));
                                            break;
                                        case "gmembers":
                                            ctx.writeAndFlush(new GroupMembersRequestMessage(s[1]));
                                            break;
                                        case "gjoin":
                                            ctx.writeAndFlush(new GroupJoinRequestMessage(username, s[1]));
                                            break;
                                        case "gquit":
                                            ctx.writeAndFlush(new GroupQuitRequestMessage(username, s[1]));
                                            break;
                                        case "quit":
                                            ctx.channel().close();
                                            return;
                                    }
                                }
                            }, "system in").start();
                        }

                        @Override
                        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                            log.debug("msg = {}", msg);
                            if (msg instanceof LoginResponseMessage) {
                                LoginResponseMessage response = (LoginResponseMessage) msg;
                                if (response.isSuccess()) {
                                    LOGIN.set(true);
                                }
                                // 唤醒System.in线程
                                WAIT_FOR_LOGIN.countDown();
                            }
                        }
                    });

                }
            });
            Channel channel = bootstrap.connect("localhost", 8080).sync().channel();
            channel.closeFuture().sync();
        } catch (Exception e) {
            log.error("client error", e);
        } finally {
            group.shutdownGracefully();
        }
    }
}
