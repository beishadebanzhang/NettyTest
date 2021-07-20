package com.sunsy.netty.netty.c1.example;

/**
 * 1. Netty是什么：
 *      netty是一个异步的，基于事件驱动的网络应用框架，用于快速开发可维护、高性能的网络服务器与客户端
 * 2. 使用Netty的原因：
 *      存在网络通信的需求
 * 3. Netty的优势：
 *      相较于NIO：nio工作量大，bug多
 *          1.nio需要自己构建协议
 *          2.netty会帮我们解决tcp传输问题，如黏包半包
 *          3.linux环境epoll空轮询导致cpu使用率100%的问题(select()方法阻塞不住)
 *          4.netty对原生api做了增强
 *      相较于mina等框架：
 *          1.兼容性好
 *          2.久经考验
 *  4.各个组件的理解：
 *      channel 数据传输通道
 *      msg 传输的数据
 *      handler 工序
 *      pipeline 流水线
 *      eventLoop 事件循环处理器
 */
public class Test {
    public static void main(String[] args) {

    }
}
