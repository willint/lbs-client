package com.lbs.client;

import com.lbs.nettyclient.protocol.LbsMessageInHandle;

/**
 * @author qiux
 * @Created on 18/2/3.
 */
public class ClientHanldeInMsg implements LbsMessageInHandle {


    public Object handleInMsg(Object inMsg) {
        System.out.println("实际处理接收到服务器推送的消息在这里");

        return null;
    }
}
