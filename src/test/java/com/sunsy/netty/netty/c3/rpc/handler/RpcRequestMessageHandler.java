package com.sunsy.netty.netty.c3.rpc.handler;

import com.sunsy.netty.netty.c3.rpc.message.RpcRequestMessage;
import com.sunsy.netty.netty.c3.rpc.message.RpcResponseMessage;
import com.sunsy.netty.netty.c3.rpc.service.HelloService;
import com.sunsy.netty.netty.c3.rpc.service.ServicesFactory;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;

@Slf4j
@ChannelHandler.Sharable
public class RpcRequestMessageHandler extends SimpleChannelInboundHandler<RpcRequestMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcRequestMessage message) throws Exception {
        RpcResponseMessage response = new RpcResponseMessage();
        response.setSequenceId(message.getSequenceId());
        try {
            HelloService service = (HelloService) ServicesFactory.getService(
                    Class.forName(message.getInterfaceName()));
            Method method = service.getClass().getMethod(message.getMethodName(), message.getParameterTypes());
            Object value = method.invoke(service, message.getParameterValue());
            response.setReturnValue(value);
        } catch (Exception e) {
            e.printStackTrace();
            response.setExceptionValue(new Exception("远程调用出错：" + e.getCause().getMessage()));
        }
    }
}
