package com.lbs.nettyclient.bizconst;

/**
 * Created by will on 18/1/2.
 * 消息对应的功能模块
 */
public interface MessageSourceConstants {
	
    //吐槽消息功能模块类型
    interface MSG_VOMIT {
        //自由聊天
        String FREE_CHAT = "FREE_CHAT";

        //专题聊天
        String SPECIAL_CHAT = "SPECIAL_CHAT";

        //自由@信息
        String FREE_ATA = "FREE_ATA";

        //专题@消息
        String SPECIAL_ATA = "SPECIAL_ATA";


    }
    
    interface MSG_COMMON{
    	/**
    	 * 私信
    	 */
    	String PRIVATE_LETTER="PRIVATE_LETTER";
    }
}
