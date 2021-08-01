package com.sunsy.netty.netty.c4;

/**
 * Accept事件：
 *  netty处理accept事件，会以message的方式传递给ServerSocketChannel的acceptor handler去处理
 *  流程：
 *      1. selector.selector() 阻塞直到事件发生
 *      2. 遍历处理 selectedKeys
 *      3. 拿到一个key，判断事件类型是否为accept
 *      4. 创建SocketChannel，设置为非阻塞
 *          并创建了NioSocketChannel
 *      5. 将SocketChannel注册至selector
 *          sc.register(eventLoop的选择器，0，NioSocketChannel)
 *          调用NioSocketChannel上的初始化器
 *      6. 关注selection key的read事件
 *
 *  Read事件：
 *      略
 */
public class TestAccept {
    public static void main(String[] args) {

    }
}
