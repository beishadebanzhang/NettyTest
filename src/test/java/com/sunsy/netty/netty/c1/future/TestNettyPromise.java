package com.sunsy.netty.netty.c1.future;

import io.netty.channel.EventLoop;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.concurrent.DefaultPromise;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestNettyPromise {
    public static void main(String[] args) throws Exception {
        EventLoop eventLoop = new NioEventLoopGroup().next();
        DefaultPromise<Integer> promise = new DefaultPromise<>(eventLoop);
        new Thread(() -> {
            log.debug("开始计算...");
            try {
                // int i = 0/ 0;
                Thread.sleep(1000);
                promise.setSuccess(50);
            } catch (Exception e) {
                e.printStackTrace();
                promise.setFailure(e);
            }
        }).start();
        log.debug("等待结果");
        log.debug("结果是：{}", promise.get());
    }
}
