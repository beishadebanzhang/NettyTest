package com.sunsy.netty.netty.c1.eventloop;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

@Slf4j
public class TestEventLoop {
    public static void main(String[] args) {
        // NioEventLoopGroup可处理io事件、定时任务、普通任务
        EventLoopGroup group = new NioEventLoopGroup(2);
        // DefaultEventLoopGroup可处理定时任务、普通任务
        // EventLoopGroup group2 = new DefaultEventLoopGroup(2);

        // 获取下一个EventLoop，内部是个循环链表
        System.out.println(group.next());
        System.out.println(group.next());
        System.out.println(group.next());

        // 执行普通任务
        group.execute(() -> {
            log.debug("普通任务");
        });

        // 执行定时任务
        group.scheduleAtFixedRate(() -> {
            log.debug("定时任务");
        }, 0, 1, TimeUnit.SECONDS);
    }
}
