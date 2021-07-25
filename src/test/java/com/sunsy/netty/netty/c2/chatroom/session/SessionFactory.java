package com.sunsy.netty.netty.c2.chatroom.session;

public abstract class SessionFactory {

    private static Session session = new SessionMemoryImpl();

    public static Session getSession() {
        return session;
    }
}
