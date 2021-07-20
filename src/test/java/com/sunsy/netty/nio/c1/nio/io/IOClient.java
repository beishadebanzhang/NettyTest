package com.sunsy.netty.nio.c1.nio.io;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

public class IOClient {
    public static void main(String[] args) throws Exception {
        SocketChannel channel = SocketChannel.open();
        channel.connect(new InetSocketAddress("localhost", 8080));
        channel.write(Charset.defaultCharset().encode("123456"));
        System.out.println("waiting...");
//        int count = 0;
//        while (true) {
//            ByteBuffer buffer = ByteBuffer.allocate(1024 * 1024);
//            count += channel.read(buffer);
//            System.out.println(count);
//            buffer.clear();
//        }
    }
}
