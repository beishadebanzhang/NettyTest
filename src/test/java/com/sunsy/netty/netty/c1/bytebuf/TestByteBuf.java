package com.sunsy.netty.netty.c1.bytebuf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

import static io.netty.buffer.ByteBufUtil.appendPrettyHexDump;
import static io.netty.util.internal.StringUtil.NEWLINE;

public class TestByteBuf {
    public static void main(String[] args) {
        // 创建ByteBuf
        ByteBuf buf = ByteBufAllocator.DEFAULT.buffer();
        System.out.println(buf);
        buf.writeBytes("aaaa".getBytes());
        // 打印后可观察是否池化，是否基于直接内存
        System.out.println(buf);
        // 获取buf的属性
        // buf由四部分组成：readerIndex，writeIndex，capacity，maxCapacity
        System.out.println(buf.readerIndex());
        System.out.println(buf.writerIndex());
        System.out.println(buf.capacity());
        System.out.println(buf.maxCapacity());
        // 直接内存与堆内存，默认是池化的
        // 直接内存创建和销毁代价高，但读写性能高，适合配合池化功能使用
        // 直接内存对GC压力小，不受垃圾回收影响，需要主动释放
        ByteBuf buf1 = ByteBufAllocator.DEFAULT.heapBuffer();
        ByteBuf buf2 = ByteBufAllocator.DEFAULT.directBuffer();
        // 池化的最大意义在于可以重用ByteBuf
        // 1.不用频繁创建对象，对堆内存会减少gc压力，对直接内存减少创建销毁对象的性能消耗
        // 2.可重用ByteBuf对象，会基于内存算法提升分配效率
        // 3.高并发时减少内存溢出可能
        // 开启方法：-Dio.netty.allocator.type={unpooled|pooled}

        // byteBuf写入
        buf.writeInt(1);
        buf.setInt(1, 1);// 不改变写指针位置
        // byteBuf扩容规则
        // 1.大小未超过512，取下一个16的整数倍
        // 2.超过512，取下一个2^n
        // 3.扩容不能超过max capacity,否则报错
        // byteBuf读取
        buf.readBytes(1);
        // 重复读取
        buf.markReaderIndex();
        buf.readBytes(1);
        buf.resetReaderIndex();
        buf.getBytes(1, buf);// get方法不改变读指针

        // 内存释放
        // 1.非池化堆内存，只需等待GC回收
        // 2.非池化直接内存和池化内存，采用引用计数法回收，计数为0时回收
        // 3.retain方法计数加一，release方法减一，为0时即使对象还在，各个方法也不能正常使用
        // 4.在handler中，谁最后使用buf，谁负责release
        // 5.若buf传递到head/tail，则会回收未回收的buf，使用ReferenceCountUtil.release(msg);
    }
}
