package com.lbs.nettyclient.protocol;

import com.lbs.nettyclient.utils.ByteUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * lbs消息解码方法
 */
public class LbsMessageDecoder extends ByteToMessageDecoder {


    //判断传送客户端传送过来的数据是否按照协议传输，头部信息的大小应该是 int+byte+byte+int = 4+1+1+4 = 6
    private static final int HEADER_SIZE = 10;

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in,
                          List<Object> out) throws Exception {
        if(in.readableBytes() >= HEADER_SIZE){
            int verCode ;

            if(in.readableBytes() > 2048){
                in.skipBytes(in.readableBytes());
            }

            int beginReader;

            while(true){
                beginReader = in.readerIndex();
                in.markReaderIndex();

                //读到协议标志
                if(in.readInt() == LbsMessageConst.VER_CODE){
                    verCode = LbsMessageConst.VER_CODE;
                    break;
                }

                in.resetReaderIndex();
                in.readByte();

                if(in.readableBytes() < HEADER_SIZE){
                    return;
                }
            }
            //业务类型
            byte type = in.readByte();

            //优先级
            byte priority = in.readByte();

            //body消息的长度
            int length = in.readInt();

            if(in.readableBytes() < length){
                in.readerIndex(beginReader);
                return;
            }
            byte[] data = new byte[length];
            in.readBytes(data);
            Object body = ByteUtil.byte2object(data);
            LbsMessage message = new LbsMessage(verCode,type,priority,length,body);
            out.add(message);
        }

    }


}
