package com.sunsy.netty.netty.c2.chatroom.handler;

import com.sunsy.netty.netty.c2.chatroom.message.LoginRequestMessage;
import com.sunsy.netty.netty.c2.chatroom.message.LoginResponseMessage;
import com.sunsy.netty.netty.c2.chatroom.service.UserServiceFactory;
import com.sunsy.netty.netty.c2.chatroom.session.SessionFactory;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
/**
 * 匿名内部类idea转换
 */
@ChannelHandler.Sharable
public class LoginRequestMessageHandler extends SimpleChannelInboundHandler<LoginRequestMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LoginRequestMessage msg) throws Exception {
        String username = msg.getUsername();
        String password = msg.getPassword();
        boolean login = UserServiceFactory.getUserService().login(username, password);
        LoginResponseMessage message;
        SessionFactory.getSession().bind(ctx.channel(), username);
        if (login) {
            message = new LoginResponseMessage(true, "登录成功");
        } else {
            message = new LoginResponseMessage(false, "用户名或密码不正确");
        }
        ctx.writeAndFlush(message);
    }
}
