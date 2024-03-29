package com.sunsy.netty.netty.c2;

/**
 * 黏包半包与解决方案--解码器
 *  滑动窗口：
 *      tcp以一个段(segment)为单位，每发送一个段就需要进行一次确认应答(ack)处理，
 *  缺点是包的往返时间越长性能就越差
 *      为解决此问题，引入了滑动窗口的概念，窗口大小即决定了无需等待应答而可以发送的数据最大值
 *      窗口实际起到一个缓冲区的作用，同时也能起到流量控制的作用
 *          --》底层是发送/接收缓冲区ChannelOption.SO_RCVBUF
 *  黏包半包产生原因：
 *      黏包：发送abc def，接收abcdef
 *          应用层：接收方ByteBuf设置太大，Netty默认1024
 *          滑动窗口：滑动窗口较大且报文未及时处理，多条堆积在滑动窗口中
 *          Nagle算法：攒一批数据再发
 *      半包：发送abcdef，，接收abc def
 *          应用层：接收方ByteBuf小于实际发送数据量
 *          滑动窗口：窗口剩余容量不足以接收一个完整报文
 *          MSS限制：当发送数据超过MSS限制后，会将数据切分发送，造成半包
 *      本质是因为TCP是流式协议，消息无边界
 *  解决方案：
 *      短连接，发完一次消息就断开连接 --》 不能处理半包
 *      定长解码器 FixedLengthFrameDecoder
 *      分隔符解码器
 *          行解码器 LineBasedFrameDecoder
 *          分隔符解码器 DelimiterBasedFrameDecoder
 *          LTC解码器 LengthFieldBasedFrameDecoder
 *
 */
public class Test {
    public static void main(String[] args) {

    }
}
