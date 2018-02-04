package com.lbs.nettyclient.protocol;

/**
 * @author qiux
 * @Created on 18/2/3.
 * 入队消息处理接口
 */
public interface  LbsMessageInHandle {

    //处理接收到服务器消息业务
    public  Object handleInMsg(Object inMsg);


}
