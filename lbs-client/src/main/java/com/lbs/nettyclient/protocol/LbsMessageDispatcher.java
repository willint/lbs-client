package com.lbs.nettyclient.protocol;

import io.netty.channel.ChannelHandlerContext;

/**
 * @author qiux
 * @Created on 18/2/2.
 * 消息轮询分发
 */
public class LbsMessageDispatcher implements Runnable {

    private static LbsMessageDispatcher dispatcher;

    private ChannelHandlerContext handlerContext;

    private LbsMessageInHandle handleInBiz;

    public static volatile boolean isOpen = false;

    private long interval = 20; // 间隔 20毫秒

    @Override
    public void run() {

        int count = 1;
        while (isOpen){
            try{
//                System.out.println("监听分发消息"+count+"次");
//                count++;
                // 发送消息队列
                Object outMsg = LbsMessageQueue.getInstance().getOutBizMsg();
                if(null != outMsg && null != handlerContext){
                    LbsMessage message = new LbsMessage();
                    message.setType(LbsMessageConst.API_TYPE.BIZ_REQUEST);
                    message.setVerCode(LbsMessageConst.VER_CODE);
                    message.setPriority(LbsMessageConst.DEFAULT_PRIORITY);
                    message.setBody(outMsg);
                    handlerContext.writeAndFlush(message);
                }

                Thread.sleep(interval);
                // 接收消息队列
                Object inMsg = LbsMessageQueue.getInstance().getInBizMsg();
                if(null != inMsg && null != handleInBiz){
                    handleInBiz.handleInMsg(inMsg);
                }

            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    //终止线程
    public  void stop(){
        isOpen = false;
        handlerContext = null;
        handleInBiz = null;
    }

    public void setHandlerContext(ChannelHandlerContext handlerContext) {
        this.handlerContext = handlerContext;
    }

    //
    public static LbsMessageDispatcher getInstance(){
        if(null == dispatcher){
            dispatcher = new LbsMessageDispatcher();
        }
        return dispatcher;
    }

    private LbsMessageDispatcher(){}


    public LbsMessageInHandle getHandleInBiz() {
        return handleInBiz;
    }

    public void setHandleInBiz(LbsMessageInHandle handleInBiz) {
        this.handleInBiz = handleInBiz;
    }
}
