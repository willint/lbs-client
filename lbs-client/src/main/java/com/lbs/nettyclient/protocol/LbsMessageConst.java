package com.lbs.nettyclient.protocol;

/**
 * 消息常量定义
 */
public interface LbsMessageConst {

    /**
     * 消息协议头
     */
    int VER_CODE = 0x01BCDACD;


    byte DEFAULT_PRIORITY = 1;


     //消息的类型
    interface API_TYPE {
        //业务请求
        byte BIZ_REQUEST = 0;
        //业务响应
        byte BIZ_RESPONSE = 1;
        //业务双向请求
        byte BIZ_ONEWAY = 2;

        //链接请求(握手请求)
        byte CONNECT_REQUEST = 3;
        //链接响应(握手响应)
        byte CONNECT_RESPONSE = 4;

        //心跳检测请求
        byte HEARTBEAT_REQUEST = 5;
        //心跳检测响应
        byte HEARTBEAT_RESPONSE = 6;

    }

     // 响应body消息体中的内容
    interface API_BODY {
        //
        String SUCCESS = "0000";

        String COMM_FAIL = "9999";

        String BIZ_FAIL = "1001";

        String BIZ_FAIL_UNLOGIN= "1002";

    }

     // 消息响应文本信息
    interface API_BODY_BIZ_MSG {
        String SUCCESS = "成功";
        String HEARTBEAT_SUCCESS = "心跳检测";
        String COMM_FAIL_MSG ="内部错误";
        String FAIL_UNLOGIN = "用户未登陆";

    }
    
     //消息响应标题
    interface  API_BODY_BIZ_TITLE {
        String RESP_CODE = "resp_code";
        String RESP_MSG = "resp_msg";
    }
    
     // 消息分发变量名
    interface MSG_DISPATCH_KEY {
    	String DISPATCH_LIST = "dispatchList";
    	String DISPATCH_MSG = "messagecontent";
    }

     //消息读取超时时间，单位秒
    int MSG_READ_TIMEOUT = 50 ;


}
