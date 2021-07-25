package com.sunsy.netty.netty.c2.agreement;

import com.sunsy.netty.netty.c2.example.HelloWorldClient;
import com.sunsy.netty.util.ServerUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_LENGTH;

@Slf4j
public class TestHttp {
    public static void main(String[] args) {
        List<ChannelHandler> handlers = new ArrayList<>();
        // Http协议编解码器，会将http请求解码成HttpRequest和HttpContent两部分
        handlers.add(new HttpServerCodec());
        // 自定义处理
//        handlers.add(new ChannelInboundHandlerAdapter(){
//            @Override
//            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//                log.debug("{}", msg.getClass());
//                if (msg instanceof HttpRequest) {
//                    // 请求行、请求头
//                } else if (msg instanceof HttpContent) {
//                    // 请求体
//                }
//            }
//        });
        // 只关心某一类消息
        handlers.add(new SimpleChannelInboundHandler<HttpRequest>() {
            @Override
            protected void channelRead0(ChannelHandlerContext context, HttpRequest request) throws Exception {
                // 获取请求
                log.debug(request.getUri());
                // 返回响应
                DefaultFullHttpResponse response = new DefaultFullHttpResponse(
                        request.getProtocolVersion(), HttpResponseStatus.OK
                );
                byte[] bytes = "<h1>Hello, world!</h1>".getBytes();
                // 消息正文长度，防止浏览器无线转圈读取
                response.headers().setInt(CONTENT_LENGTH, bytes.length);
                response.content().writeBytes(bytes);
                // 写回响应
                context.writeAndFlush(response);
            }
        });

        ServerUtil.generateServer(handlers);
    }
}
