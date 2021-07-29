package com.sunsy.netty.netty.c3.rpc.service;

public class HelloServiceImpl implements HelloService {
    @Override
    public String sayHello(String msg) {
        return "你好, " + msg;
    }
}