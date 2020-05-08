package com.jno.cloud.framework.redis.config;

import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import java.io.*;

/**
 * byte序列化value-redis模板
 */
public class RedisByteSerializer<T> implements RedisSerializer<T> {

	@Override
	public byte[] serialize(T session) throws SerializationException {
		ByteArrayOutputStream bo = new ByteArrayOutputStream();
		byte[] bytes = null;
		try {
			ObjectOutputStream oo = new ObjectOutputStream(bo);
			oo.writeObject(session);
			bytes = bo.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bytes;
	}

	@Override
	public T deserialize(byte[] bytes) throws SerializationException {
	    if(bytes != null){
            ByteArrayInputStream bi = new ByteArrayInputStream(bytes);
            ObjectInputStream in;
            T session = null;
            try {
                in = new ObjectInputStream(bi);
                session = (T) in.readObject();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return session;
        }else{
	        return null;
        }

	}

}
