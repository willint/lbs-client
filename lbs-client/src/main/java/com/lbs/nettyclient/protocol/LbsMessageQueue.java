package com.lbs.nettyclient.protocol;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author qiux
 * @Created on 18/2/2.
 * 消息队列，包括入队消息和出队消息
 */
public class LbsMessageQueue {

    private static LbsMessageQueue queue;

    //接收入队消息
    private Queue<Object> inQueue;

    //发送出队消息
    private Queue<Object> outQueue;


    /**
     * 客户端推送业务消息入队
     * @param msg
     */
    public boolean pushInBizMsg(Object msg){
        if(null == inQueue){
            inQueue = new ConcurrentLinkedQueue<Object>();
        }
        return inQueue.add(msg);
    }

    /**
     * 取出入队消息，并删除
     */
    public Object getInBizMsg(){
        if(null != inQueue && inQueue.size() > 0){
            return inQueue.poll();
        }
        return null;
    }

    /**
     * 接收到服务端消息放入到 出队队列
     * @param msg
     * @return
     */
    public boolean pushOutBizMsg(Object msg){
        if(null == outQueue){
            outQueue = new ConcurrentLinkedQueue<Object>();
        }
        return outQueue.add(msg);
    }

    /**
     * 客户端获取 服务端返回的消息
     * @return
     */
    public Object getOutBizMsg(){
        if(null != outQueue && outQueue.size() > 0){
            return outQueue.poll();
        }
        return null;
    }


    public static LbsMessageQueue getInstance(){
        if(queue == null){
            queue = new LbsMessageQueue();
        }
        return queue;
    }

    /**
     * 构造私有 单例模式
     */
    private LbsMessageQueue(){
        if(inQueue != null){
            inQueue.clear();
        }else{
            inQueue =  new ConcurrentLinkedQueue<Object>();
        }

        if(null != outQueue){
            outQueue.clear();
        }else{
            outQueue = new ConcurrentLinkedQueue<Object>();
        }
    }

}
