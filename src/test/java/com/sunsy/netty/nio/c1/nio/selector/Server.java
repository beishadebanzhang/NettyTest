package com.sunsy.netty.nio.c1.nio.selector;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

import static com.sunsy.netty.util.ByteBufferUtil.debugRead;

/**
 * selector管理多个channel，检测channel上的事件，无事件时线程阻塞
 * channel上的事件
 *  accept 有连接请求时触发
 *  connect 客户端建立连接后触发
 *  read 可读
 *  write 可写
 */
@Slf4j
public class Server {
    public static void main(String[] args) throws IOException {
        // 创建ServerSocketChannel，设为非阻塞模式，绑定端口
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.configureBlocking(false);
        ssc.bind(new InetSocketAddress(8080));
        // 将channel注册至selector，只关注连接事件
        Selector selector = Selector.open();
        ssc.register(selector, SelectionKey.OP_ACCEPT, null);
        // 处理连接
        while(true) {
            // 获取所有channel事件后向下处理，无事件则阻塞
            selector.select();
            // selected set中的对象不会主动删除，需要主动remove
            // 每次selector.select()后更新selected set
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while(iterator.hasNext()) {
                SelectionKey key = iterator.next();
                // 不进行删除，下一次获取iterator时处理过的key还在，可能触发空指针等问题
                // 比如第二次accept()时拿不到key上对应的channel信息
                iterator.remove();
                log.debug("key ：{}", key);
                // 连接事件
                if (key.isAcceptable()) {
                    ServerSocketChannel channel = (ServerSocketChannel) key.channel();
                    SocketChannel sc = channel.accept();
                    sc.configureBlocking(false);
                    sc.register(selector, SelectionKey.OP_READ, 0);
                    log.debug("sc ：{}", sc);
                } else if (key.isReadable()) {
                    try {
                        SocketChannel channel = (SocketChannel) key.channel();
                        ByteBuffer buffer = ByteBuffer.allocate(16);
                        // 客户端正常断开，返回值为-1
                        int read = channel.read(buffer);
                        if (-1 == read) {
                            log.debug("客户端断开");
                            key.cancel();
                        } else {
                            buffer.flip();
                            debugRead(buffer);
                        }
                    } catch (IOException e) {
                        // 处理非法关闭客户端
                        // 事件处理，若不处理下次还在
                        // 忽略事件不处理，使用key.cancel()反注册，将key移至cancel set中
                        // 下次select()方法执行时从key set中删除，否则即使selected key已经remove
                        // 也不会从key set中移除，下次select()发生时，selected key依旧有该事件要处理
                        // 移除后，将不会再关注其channel的该事件信息
                        e.printStackTrace();
                        key.cancel();
                    }
                }
            }
        }
    }
}
