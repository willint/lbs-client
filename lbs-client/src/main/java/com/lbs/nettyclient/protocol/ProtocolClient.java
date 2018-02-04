package com.lbs.nettyclient.protocol;

import com.lbs.nettyclient.utils.ByteUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;
import net.sf.json.JSONObject;

/**
 * @author qiux
 * @Created on 18/2/2.
 * 协议客户端
 *
 */
public class ProtocolClient {

    private static  ProtocolClient client;

    private ChannelFuture  channelFuture;

    private volatile boolean monitorFlag = false;//监听通道标识

    /**
     * 客户端创建连接服务
     * @param host
     * @param port
     * @throws Exception
     */
    public boolean connect(String host , int port ) throws  Exception{

        EventLoopGroup workergroup = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.option(ChannelOption.SO_KEEPALIVE,true);
        bootstrap.group(workergroup).channel(NioSocketChannel.class);
        ChannelFuture future = null;

        bootstrap.handler(new ChannelInitializer<Channel>() {
            @Override
            protected void initChannel(Channel ch) throws Exception {
                ch.pipeline().addLast("timeout",new ReadTimeoutHandler(LbsMessageConst.MSG_READ_TIMEOUT));
                ch.pipeline().addLast("decoder",new LbsMessageDecoder());
                ch.pipeline().addLast("encoder",new LbsMessageEncoder());
                ch.pipeline().addLast("pushMsgHandler",new ProtocolMsgBizInHandler(LbsMessageQueue.getInstance()));//广播消息接收
            }
        });

        bootstrap.option(ChannelOption.TCP_NODELAY, true);
        bootstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000);// 连接超时时间毫秒
        future = bootstrap.connect(host,port).sync();
        if(null != future){
            channelFuture = future;
        }else{
            return false;
        }
        return true;

    }

    /**
     * 当用户登录之后通过 loginId,userId 发起业务通道链接
     * {"loginId":"6d0e2ea3","userId":"a167e5"}
     * 链接业务通道
     * @param data
     * @return
     */
    public boolean openBizChannel(JSONObject data,LbsMessageInHandle handle)throws Exception{
        // 参数校验
        String loginId = data.getString("loginId");
        String userId = data.getString("userId");
        System.out.println("data :"+data.toString());
        if(null == loginId || null == userId
                || "".equals(loginId) || "".equals(userId)){
            return false;
        }
        // 链接成功 发起业务消息链接请求
        LbsMessage message = new LbsMessage();
        message.setVerCode(LbsMessageConst.VER_CODE);
        message.setType(LbsMessageConst.API_TYPE.CONNECT_REQUEST);
        message.setPriority(LbsMessageConst.DEFAULT_PRIORITY);
        message.setBody(data);
        if(null != channelFuture){
            channelFuture.channel().writeAndFlush(message);
            LbsMessageDispatcher.getInstance().setHandleInBiz(handle);
            if(LbsMessageDispatcher.isOpen){ //通道开通后
//                monitorChannel(data,handle);
            }
        }
        return true;
    }

    public ChannelFuture getChannelFuture() {
        return channelFuture;
    }

    // 监听线程，暂时没有用到
    private void monitorChannel(JSONObject channelData , LbsMessageInHandle handle)throws Exception{

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//
//                try {
//                    while(monitorFlag){
//                        System.out.println("MonitorChannel test ");
//                        if(!LbsMessageDispatcher.isOpen){
//                            System.out.println("MonitorChannel post biz channel");
//                            if(null != channelData && null != handle){
//                                openBizChannel(channelData,handle);
//                            }
//                        }
//                        Thread.sleep(30*1000); // 每隔30s 监听
//                    }
//                }catch (Exception e){
//                    e.printStackTrace();
//                    closeBizChannel();
//                }
//            }
//        }).start();

    }

    //关闭通道
    public void closeBizChannel(){
        System.out.println("closeBizChannel");
        LbsMessageDispatcher.getInstance().stop();

    }

    /**
     * 单例模式
     * @return
     */
    public static ProtocolClient getInstance(){
        if(null == client){
            client = new ProtocolClient();
        }
        return client;
    }
    private ProtocolClient(){}

    public static void main(String[] args) {

    }
}
