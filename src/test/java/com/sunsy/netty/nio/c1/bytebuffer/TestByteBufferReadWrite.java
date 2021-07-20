package com.sunsy.netty.nio.c1.bytebuffer;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

/**
 * byteBuffer方法演示
 */
public class TestByteBufferReadWrite {
    public static void main(String[] args) {
        // 创建buffer不能动态调整
        // allocate使用堆内存，读写效率低分配块，可能受gc影响移动数据
        // allocateDirect使用系统内存，读写效率高，分配速度慢，不受gc影响，少一次拷贝，存在内存泄漏风险
        ByteBuffer buffer1 = ByteBuffer.allocate(10);
        ByteBuffer buffer2 = ByteBuffer.allocateDirect(10);

        // 写入数据
        buffer1.put((byte) 0x62);
        buffer1.put(new byte[]{0x62, 0x63, 0x64});
        // channel.read(buffer1);

        // 读数据
        buffer1.flip();
        buffer1.get(); // 导致position指针移动
        // channel.write(buffer1);

        // 重复读数据
        buffer1.rewind(); // position置0
        buffer1.get(1); // 包含index参数不引起指针移动
        buffer1.mark(); // 对当前位置作标记
        buffer1.reset();// 使position回到当前位置

        // 字符串与ByteBuffer互相转换
        // 1. put(str.getBytes())
        String str = "Hello World";
        ByteBuffer buffer = ByteBuffer.allocate(20);
        buffer.put(str.getBytes());
        // 2. Charset.encode，自动切换为读模式
        buffer = StandardCharsets.UTF_8.encode(str);
        // 3. wrap，自动切换为读模式
        buffer = ByteBuffer.wrap(str.getBytes());
        // 4. Charset.decode，需要读模式
        str = StandardCharsets.UTF_8.decode(buffer).toString();
    }
}
