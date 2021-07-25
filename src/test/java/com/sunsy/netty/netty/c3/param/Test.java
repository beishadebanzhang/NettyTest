package com.sunsy.netty.netty.c3.param;

/**
 * 参数调优：
 *  客户端通过option方法给SocketChannel配置参数
 *  服务器端
 *      通过childOption方法给SocketChannel配置参数
 *      通过option方法给ServerSocketChannel配置参数
 *  CONNECT_TIMEOUT_MILLIS: // todo 源码分析：如何处理连接超时的
 *      属于socketChannel参数
 *      用于在客户端建立连接时，如果在指定毫秒内无法连接，会抛出timeOut异常
 *      SO_TIMEOUT主要用于阻塞IO，不希望阻塞IO中accept/read无限等待时可使用
 *  SO_BACKLOG
 *      属于serverSocketChannel参数
 *      // todo 三次握手与全连接队列、半连接队列关系
 *      全连接队列的大小 --》 全连接队列满了服务器将发送拒绝连接的错误
 *      配置文件与bind(8080, backlog)中取最小值
 *      可以通过option(ChannelOption.SO_BACKLOG, 值)来设置大小
 *      // todo 源码分析：重现拒绝连接现象、找默认值配置文件位置
 *  ulimit-n：
 *      属于操作系统参数
 *      限制一个进程能够打开的最多文件描述符数量
 *  TCP_NODELAY:
 *      属于SocketChannel参数
 *      关闭nagle算法：积攒一批数据再发
 *  SO_SNDBUF & SO_RCVBUF
 *      发送、接收缓冲区，决定了滑动窗口上限，属于SocketChannel参数
 *      操作系统自动调整，程序无需调整
 *  ALLOCATOR：
 *      属于SocketChannel参数
 *      用来分配ByteBuf，ctx.alloc
 *  RECBUF_ALLOCATOR
 *      属于SocketChannel参数
 */
public class Test {
    public static void main(String[] args) {

    }
}
