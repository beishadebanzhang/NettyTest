package com.sunsy.netty.netty.c2.chatroom.handler;

import com.sunsy.netty.netty.c2.chatroom.message.ChatRequestMessage;
import com.sunsy.netty.netty.c2.chatroom.message.ChatResponseMessage;
import com.sunsy.netty.netty.c2.chatroom.session.SessionFactory;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ChatRequestMessageHandler extends SimpleChannelInboundHandler<ChatRequestMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ChatRequestMessage msg) throws Exception {
        String to = msg.getTo();
        Channel channel = SessionFactory.getSession().getChannel(to);
        if (null != channel) {
            channel.writeAndFlush(new ChatResponseMessage(msg.getFrom(), msg.getContent()));
        } else {
            channel.writeAndFlush(new ChatResponseMessage(false, "对方用户不存在或者不在线"));
        }
    }
}
