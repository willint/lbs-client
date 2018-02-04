package com.lbs.nettyclient.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by will on 17/12/26.
 * 对象序列化方法
 *
 */
public class ByteUtil {


    public static Object byte2object(byte[] data)throws Exception{
        Object object = null;
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        ObjectInputStream oi = new ObjectInputStream(in);
        object = oi.readObject();
        in.close();
        oi.close();
        return object;
    }


    public static byte[] object2bytes(Object object)throws Exception{
        byte[] data = null;

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream oi = new ObjectOutputStream(out);
        oi.writeObject(object);
        data = out.toByteArray();
        oi.close();
        out.close();

        return data;
    }
}
