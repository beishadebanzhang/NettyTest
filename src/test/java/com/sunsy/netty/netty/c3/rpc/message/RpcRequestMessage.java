package com.sunsy.netty.netty.c3.rpc.message;

import com.sunsy.netty.netty.c2.chatroom.message.Message;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
public class RpcRequestMessage extends Message {

    private int sequenceId;

    private String interfaceName;

    private String methodName;

    private Class<?> returnType;

    private Class[] parameterTypes;

    private Object[] parameterValue;

    public RpcRequestMessage(int sequenceId, String interfaceName, String methodName, Class<?> returnType, Class[] parameterTypes, Object[] parameterValue) {
        this.sequenceId = sequenceId;
        this.interfaceName = interfaceName;
        this.methodName = methodName;
        this.returnType = returnType;
        this.parameterTypes = parameterTypes;
        this.parameterValue = parameterValue;
    }

    @Override
    public int getMessageType() {
        return RPC_MESSAGE_REQUEST;
    }
}
