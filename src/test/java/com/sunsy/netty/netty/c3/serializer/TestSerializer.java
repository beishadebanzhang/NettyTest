package com.sunsy.netty.netty.c3.serializer;

import com.sunsy.netty.netty.c2.chatroom.message.LoginRequestMessage;
import com.sunsy.netty.netty.c2.chatroom.protocol.MessageCodecSharable;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.logging.LoggingHandler;

// todo 完善测试类
public class TestSerializer {
    public static void main(String[] args) {
        MessageCodecSharable CODEC = new MessageCodecSharable();
        LoggingHandler LOGGING = new LoggingHandler();
        EmbeddedChannel channel = new EmbeddedChannel(LOGGING, CODEC, LOGGING);

        LoginRequestMessage message = new LoginRequestMessage("zhangsan", "123");
        channel.writeInbound(message);
    }
}
