package com.sunsy.netty.netty.c1.eventloop;

/**
 * EventLoop 事件循环对象
 *  -- 继承了java的ScheduleExecutorService-》包含线程池中的所有方法
 *  -- 继承了netty的OrderedEventExecutor
 *      --》boolean inEventLoop(Thread thread) 判断线程是否属于该EventLoop
 *      --》parent() 方法查看属于哪个EventLoopGroup
 * EventLoopGroup 事件循环对象组
 *  channel一般会调用EventLoopGroup的register方法绑定一个EventLoop，后续该channel所有io都由其执行
 *  继承了netty的EventExecutorGroup
 *      --》实现了iterator接口提供的遍历EventLoop能力
 *      --》另有next方法获取集合中的下一个EventLoop
 */
public class Test {
    public static void main(String[] args) {

    }
}
