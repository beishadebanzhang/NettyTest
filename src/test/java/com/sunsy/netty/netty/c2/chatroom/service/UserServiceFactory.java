package com.sunsy.netty.netty.c2.chatroom.service;

public abstract class UserServiceFactory {

    private static UserService userService = new UserServiceMemoryImpl();

    public static UserService getUserService() {
        return userService;
    }
}
