package com.sunsy.netty.nio.c1.bytebuffer;

import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 概念学习：
 * ByteBuffer内部结构：capacity，position，limit
 * clear()和compact()方法
 */
@Slf4j
public class TestByteBuffer {
    public static void main(String[] args) {
        // 获取FileChannel：1.输入输出流；2.RandomAccessFile
        try (FileChannel channel = new FileInputStream("data.txt").getChannel()) {
            ByteBuffer buffer = ByteBuffer.allocate(10);
            while (true){
                int length = channel.read(buffer);
                if (-1 == length) {
                    break;
                }
                // 切换为读模式
                buffer.flip();
                while (buffer.hasRemaining()){
                    log.debug("实际字节为 ：{}", (char) buffer.get());
                }
                // 切换为写模式
                buffer.clear();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
