package com.lbs.nettyclient.protocol;

/**
 * 消息数据格式实体
 *
 */
public class LbsMessage {


    /**
     * 校验码
     */
    private int verCode;


    /**
     * 消息类型
     */
    private byte type;


    /**
     * 优先级
     */
    private byte priority;


    /**
     * 消息长度，实体body部分
     */
    private int length;


    private Object body;

    public LbsMessage() {
    }

    public LbsMessage(int verCode, byte type, byte priority, int length, Object body) {
        this.verCode = verCode;
        this.type = type;
        this.priority = priority;
        this.length = length;
        this.body = body;
    }

    public int getVerCode() {
        return verCode;
    }

    public void setVerCode(int verCode) {
        this.verCode = verCode;
    }

    public byte getType() {
        return type;
    }

    public void setType(byte type) {
        this.type = type;
    }

    public byte getPriority() {
        return priority;
    }

    public void setPriority(byte priority) {
        this.priority = priority;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }
}
