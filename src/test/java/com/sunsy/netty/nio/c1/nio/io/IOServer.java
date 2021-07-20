package com.sunsy.netty.nio.c1.nio.io;

import lombok.extern.slf4j.Slf4j;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

import static com.sunsy.netty.util.ByteBufferUtil.debugRead;

/**
 * 利用NIO理解阻塞模式
 * read和accept方式是阻塞的
 * 只有客户端建立连接时，才会循环处理channel池中的读写操作
 * read操作阻塞时，不能处理连接以及其他channel的读写操作
 */
@Slf4j
public class IOServer {
    public static void main(String[] args) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(16);
        // 创建服务器并绑定端口
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.configureBlocking(false);
        ssc.bind(new InetSocketAddress(8080));
        // 连接集合
        List<SocketChannel> channelList = new ArrayList<>();
        while (true) {
            log.debug("connecting...");
            // 非阻塞模式，accept不停止线程并返回null
            SocketChannel sc = ssc.accept();
            log.debug("connected... {}", sc);
            channelList.add(sc);
            // 处理连接请求
            for (SocketChannel channel : channelList) {
                log.debug("before read... {}", channel);
                // 非阻塞没读写操作时不阻塞，读不到数据，返回0
                channel.read(buffer);
                buffer.flip();
                debugRead(buffer);
                buffer.clear();
                log.debug("after read... {}", channel);
            }
        }
    }
}
