package com.lbs.client;

import com.lbs.nettyclient.bizconst.MediaTypeConstants;
import com.lbs.nettyclient.bizconst.MessageSourceConstants;
import com.lbs.nettyclient.protocol.*;
import net.sf.json.JSONObject;

import java.math.BigDecimal;

/**
 * @author qiux
 * @Created on 18/2/2.
 */
public class ProtocolClientTest {

    public static void main(String[] args) {
        ProtocolClient client = ProtocolClient.getInstance();
        try{
            boolean conResult = client.connect("127.0.0.1", 8191);
            if(conResult){
                JSONObject data = new JSONObject();
                data.put("loginId","6c11bc2b5a5842d0a7b72408dd9c1647");
                data.put("userId","2707c73d566049718cc7dff1c7d1e454");

                client.openBizChannel(data,new ClientHanldeInMsg());


                //启动线程发送业务消息

                new Thread(new Runnable() {

                    public void run() {
//                        System.out.println("test flag "+LbsMessageDispatcher.isOpen);
                        int count = 0;
                        while (true){
                            if(LbsMessageDispatcher.isOpen){
                                //获取业务消息
                                JSONObject msgbody = new JSONObject();
                                JSONObject messageContent = new JSONObject();
                                JSONObject from2 = new JSONObject();
                                from2.put("loginId","6c11bc2b5a5842d0a7b72408dd9c1647");
                                from2.put("userId","2707c73d566049718cc7dff1c7d1e454");
                                from2.put("jd",new BigDecimal(102.74154));
                                from2.put("wd",new BigDecimal(25.062706));
                                from2.put("locationName","location address");
                                from2.put("nickName","testUser2");
                                msgbody.put("msgSource", MessageSourceConstants.MSG_VOMIT.FREE_CHAT);

                                messageContent.put("type", MediaTypeConstants.TXT);
                                messageContent.put("txt", "this is test text .count="+System.currentTimeMillis());
                                msgbody.put("msgContent", messageContent);
                                msgbody.put("fromInfo", from2);
                                LbsMessageQueue.getInstance().pushOutBizMsg(msgbody);
                                count++;
                                if(count >=3){
                                    System.out.println("end push message");

                                    break;
                                }
                            }
                            try {
                                Thread.sleep(5*1000);
                            }catch (Exception e){

                            }
                        }
                    }
                }).start();
            }
            System.out.println("end.");
        }catch (Exception e){
            e.printStackTrace();
        }

    }

}
