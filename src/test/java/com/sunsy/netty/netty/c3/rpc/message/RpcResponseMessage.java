package com.sunsy.netty.netty.c3.rpc.message;

import com.sunsy.netty.netty.c2.chatroom.message.Message;
import lombok.Data;
import lombok.ToString;

@Data
@ToString(callSuper = true)
public class RpcResponseMessage extends Message {

    private Object returnValue;

    private Exception exceptionValue;

    @Override
    public int getMessageType() {
        return RPC_MESSAGE_RESPONSE;
    }
}
