package com.lbs.nettyclient.protocol;

import com.lbs.nettyclient.utils.ByteUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * lbs消息编码封装
 */
public class LbsMessageEncoder extends MessageToByteEncoder<LbsMessage> {

    @Override
    protected void encode(ChannelHandlerContext ctx, LbsMessage msg, ByteBuf out) throws Exception {
        byte[] body = ByteUtil.object2bytes(msg.getBody());
        msg.setLength(body.length);

        out.writeInt(msg.getVerCode());
        out.writeByte(msg.getType());
        out.writeByte(msg.getPriority());
        out.writeInt(msg.getLength());
        out.writeBytes(body);
    }
}
