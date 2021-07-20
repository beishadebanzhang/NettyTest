package com.sunsy.netty.nio.c1.nio.message;

import com.sunsy.netty.nio.c1.bytebuffer.TestByteBufferExam;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

import static com.sunsy.netty.util.ByteBufferUtil.debugRead;

/**
 * 消息边界问题：服务器ByteBuffer长度固定导致黏包半包现象
 *  1. 发送定长消息，浪费带宽
 *  2. 按分隔符拆分，效率低
 *  3. TLV格式：Type类型、Length长度、Value数据
 *      buffer需要提前分配，若内容过大则影响Server吞吐量
 *      Http1.1是TLV格式，Http1.2是LTV格式
 */
@Slf4j
public class Server {
    /**
     * 处理消息边界问题：黏包半包处理与buffer自动扩容
     */
    public static void main(String[] args) throws IOException {
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.configureBlocking(false);
        ssc.bind(new InetSocketAddress(8080));

        Selector selector = Selector.open();
        ssc.register(selector, SelectionKey.OP_ACCEPT, null);

        while (true) {
            selector.select();
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                iterator.remove();
                if (key.isAcceptable()) {
                    ServerSocketChannel channel = (ServerSocketChannel) key.channel();
                    SocketChannel sc = channel.accept();
                    sc.configureBlocking(false);
                    ByteBuffer buffer = ByteBuffer.allocate(16);
                    sc.register(selector, SelectionKey.OP_READ, buffer);
                    log.debug("处理连接请求成功");
                } else if (key.isReadable()) {
                    try {
                        SocketChannel channel = (SocketChannel) key.channel();
                        // 消息边界问题：byteBuffer大小固定导致消息多次读取
                        // 处理方法：黏包半包处理 + 自动扩容
                        ByteBuffer buffer = (ByteBuffer) key.attachment();
                        int read = channel.read(buffer);
                        if (read == -1) {
                            log.debug("正常关闭服务器");
                            key.cancel();
                        } else {
                            // 处理黏包半包
                            TestByteBufferExam.split(buffer);
                            // 若最后一条消息不完整，compact()后limit==position
                            // 需要进行扩容处理
                            if (buffer.position() == buffer.limit()) {
                                // limit()表示当前读/写数据边界，capacity()表示容量大小
                                // limit() <= capacity()
                                ByteBuffer newBuffer = ByteBuffer.allocate(buffer.capacity() * 2);
                                buffer.flip();
                                newBuffer.put(buffer);
                                key.attach(newBuffer);
                            }
                        }
                    } catch (IOException e) {
                        log.debug("非法关闭服务器");
                        key.cancel();
                    }
                }
            }
        }
    }
}
