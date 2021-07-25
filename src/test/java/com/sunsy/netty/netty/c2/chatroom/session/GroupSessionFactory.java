package com.sunsy.netty.netty.c2.chatroom.session;

public abstract class GroupSessionFactory {

    private static GroupSession session = new GroupSessionMemoryImpl();

    public static GroupSession getGroupSession() {
        return session;
    }
}
