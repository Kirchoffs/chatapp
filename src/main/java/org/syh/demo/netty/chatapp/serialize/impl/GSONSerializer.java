package org.syh.demo.netty.chatapp.serialize.impl;

import org.syh.demo.netty.chatapp.serialize.Serializer;
import org.syh.demo.netty.chatapp.serialize.SerializerAlgorithm;

import com.google.gson.Gson;

public class GSONSerializer implements Serializer {
    private Gson gson = new Gson();

    @Override
    public byte getSerializerAlgorithm() {
        return SerializerAlgorithm.GSON;
    }

    @Override
    public byte[] serialize(Object object) {
        return gson.toJson(object).getBytes();
    }

    @Override
    public <T> T deserialize(Class<T> clazz, byte[] bytes) {
        return gson.fromJson(new String(bytes), clazz);
    }
}
