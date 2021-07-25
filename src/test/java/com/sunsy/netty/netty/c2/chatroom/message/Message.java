package com.sunsy.netty.netty.c2.chatroom.message;

import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Data
public abstract class Message implements Serializable {

    public static Class<?> getMessageClass(int messageType) {
        return messageClasses.get(messageType);
    }

    private int sequenceId;

    private int messageType;

    public abstract int getMessageType();

    public static final int LoginRequestMessage = 0;
    public static final int LoginResponseMessage = 1;
    public static final int ChatRequestMessage = 2;
    public static final int ChatResponseMessage = 3;
    public static final int GroupCreateRequestMessage = 4;
    public static final int GroupCreateResponseMessage = 5;
    public static final int GroupJoinRequestMessage = 6;
    public static final int GroupJoinResponseMessage = 7;
    public static final int GroupQuitRequestMessage = 8;
    public static final int GroupQuitResponseMessage = 9;
    public static final int GroupChatRequestMessage = 10;
    public static final int GroupChatResponseMessage = 11;
    public static final int GroupMembersRequestMessage = 12;
    public static final int GroupMembersResponseMessage = 13;
    public static final int PingMessage = 14;
    public static final int PongMessage = 15;
    private static final Map<Integer, Class<?>> messageClasses = new HashMap<>();

    static {
        messageClasses.put(LoginRequestMessage, com.sunsy.netty.netty.c2.chatroom.message.LoginRequestMessage.class);
        messageClasses.put(LoginResponseMessage, com.sunsy.netty.netty.c2.chatroom.message.LoginResponseMessage.class);
        messageClasses.put(ChatRequestMessage, com.sunsy.netty.netty.c2.chatroom.message.ChatRequestMessage.class);
        messageClasses.put(ChatResponseMessage, com.sunsy.netty.netty.c2.chatroom.message.ChatResponseMessage.class);
        messageClasses.put(GroupCreateRequestMessage, com.sunsy.netty.netty.c2.chatroom.message.GroupCreateRequestMessage.class);
        messageClasses.put(GroupCreateResponseMessage, GroupCreateResponseMessage.class);
        messageClasses.put(GroupJoinRequestMessage, com.sunsy.netty.netty.c2.chatroom.message.GroupJoinRequestMessage.class);
        messageClasses.put(GroupJoinResponseMessage, com.sunsy.netty.netty.c2.chatroom.message.GroupJoinResponseMessage.class);
        messageClasses.put(GroupQuitRequestMessage, com.sunsy.netty.netty.c2.chatroom.message.GroupQuitRequestMessage.class);
        messageClasses.put(GroupQuitResponseMessage, GroupQuitResponseMessage.class);
        messageClasses.put(GroupChatRequestMessage, com.sunsy.netty.netty.c2.chatroom.message.GroupChatRequestMessage.class);
        messageClasses.put(GroupChatResponseMessage, com.sunsy.netty.netty.c2.chatroom.message.GroupChatResponseMessage.class);
        messageClasses.put(GroupMembersRequestMessage, com.sunsy.netty.netty.c2.chatroom.message.GroupMembersRequestMessage.class);
        messageClasses.put(GroupMembersResponseMessage, com.sunsy.netty.netty.c2.chatroom.message.GroupMembersResponseMessage.class);
    }
}
