package com.sunsy.netty.netty.c2.chatroom.handler;

import com.sunsy.netty.netty.c2.chatroom.message.GroupChatRequestMessage;
import com.sunsy.netty.netty.c2.chatroom.message.GroupChatResponseMessage;
import com.sunsy.netty.netty.c2.chatroom.session.GroupSessionFactory;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.List;

public class GroupChatRequestMessageHandler extends SimpleChannelInboundHandler<GroupChatRequestMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GroupChatRequestMessage msg) throws Exception {
        List<Channel> channels = GroupSessionFactory.getGroupSession()
                .getMembersChannel(msg.getGroupName());
        for (Channel channel : channels) {
            channel.writeAndFlush(new GroupChatResponseMessage(msg.getFrom(), msg.getContent()));
        }
    }
}
