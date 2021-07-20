package com.sunsy.netty.nio.c1;

/**
 * NIO基础：non-blocking io 非阻塞IO
 * 三大组件：channel、buffer、selector
 *
 * 服务器演变：
 *  多线程版：
 *      内存占用高 --> 线程本身占内存 --> 每个线程需要分配独立的栈
 *      线程上下文切换成本高 --> 多线程受cpu核数限制，太多线程会造成频繁的上下文切换
 *      只适合连接数较少的情况
 *  线程池版：
 *      阻塞模式下，线程仅能处理一个socket连接 --> 多个socket阻塞且连接未断开会造成资源浪费
 *      仅适合短连接场景
 *  selector版：
 *      selector的作用是配合一个线程管理多个channel，获取channel上发生的事件
 *      不让线程吊死在一个线程下，适合连接数较多，但流量较低的场景
 *      通过selector.select()阻塞线程直到channel上发生事件
 */
public class Test {
}
