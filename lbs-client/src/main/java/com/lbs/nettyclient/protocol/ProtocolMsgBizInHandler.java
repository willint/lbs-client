package com.lbs.nettyclient.protocol;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import net.sf.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * @author qiux
 * @Created on 18/2/2.
 * 协议消息业务处理
 */
public class ProtocolMsgBizInHandler extends SimpleChannelInboundHandler {

    private LbsMessageQueue queue;

    private volatile ScheduledFuture<?> heartbeat;


    @Override
    protected void channelRead0(ChannelHandlerContext handlerContext, Object msg) throws Exception {

        if(null != msg &&( msg instanceof LbsMessage)){
            LbsMessage receiveMsg = (LbsMessage)msg;

            if(receiveMsg.getType() == LbsMessageConst.API_TYPE.CONNECT_RESPONSE){
                // 校验业务通道是否链接成功
                JSONObject data = (JSONObject)receiveMsg.getBody();
                System.out.println(data.toString());
                String respCode = data.getString(LbsMessageConst.API_BODY_BIZ_TITLE.RESP_CODE);

                if(LbsMessageConst.API_BODY.SUCCESS.equals(respCode)){
                    LbsMessageDispatcher.isOpen = true;
                    LbsMessageDispatcher dispatcher = LbsMessageDispatcher.getInstance();
                    dispatcher.setHandlerContext(handlerContext);
                    String loginId = data.getString("loginId");
                    //发起心跳检测
                    heartbeat = handlerContext.executor().scheduleAtFixedRate( new HeartbeatTask(handlerContext,loginId),
                            0,10*1000, TimeUnit.MILLISECONDS);
                    // 启动轮询监听 入队 和 出队消息
                    new Thread(dispatcher).start();
                }

            }else if(receiveMsg.getType() == LbsMessageConst.API_TYPE.HEARTBEAT_RESPONSE){
//                JSONObject heartResp = JSONObject.fromObject(receiveMsg.getBody());
//                logger.info("heartbeat check task :"+heartResp.getString("timeStamp"));
            }if(receiveMsg.getType() == LbsMessageConst.API_TYPE.BIZ_RESPONSE){
                System.out.println("broad cast  content from server:"+ receiveMsg.getBody());
                this.queue.pushInBizMsg(receiveMsg.getBody());

            }else{
                //响应消息类型不是链接成功提示，传递给下个handler
                handlerContext.fireChannelRead(msg);
            }
        }

    }

    public ProtocolMsgBizInHandler(LbsMessageQueue q){
        this.queue = q;
    }

    //心跳链接
    public class HeartbeatTask implements Runnable {
        private final ChannelHandlerContext heartbeatContext;
        private  String loginId;

        public HeartbeatTask(ChannelHandlerContext c ,String loginId){
            this.heartbeatContext = c;
            this.loginId = loginId;
        }

        public void run() {
            LbsMessage message = buildHeartbeatMsg(loginId);
            heartbeatContext.writeAndFlush(message);
        }
    }

    //封装心跳数据
    private LbsMessage buildHeartbeatMsg(String loginId){
        LbsMessage heartbeatMessage = new LbsMessage();
        heartbeatMessage.setVerCode(LbsMessageConst.VER_CODE);
        heartbeatMessage.setType(LbsMessageConst.API_TYPE.HEARTBEAT_REQUEST);
        heartbeatMessage.setPriority(LbsMessageConst.DEFAULT_PRIORITY);

        JSONObject body = new JSONObject();
        String sessionCode = "testSessionCode";
        SimpleDateFormat timeFormat=new SimpleDateFormat("yyyyMMDDHHmmss");
        String timeStamp = timeFormat.format(new Date());
        body.put("loginId",loginId);
        body.put("sessionCode",sessionCode);
        body.put("timeStamp",timeStamp);
        heartbeatMessage.setBody(body);

        return heartbeatMessage;
    }
}
