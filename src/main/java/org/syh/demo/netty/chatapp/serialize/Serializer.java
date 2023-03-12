package org.syh.demo.netty.chatapp.serialize;

import org.syh.demo.netty.chatapp.serialize.impl.GSONSerializer;

public interface Serializer {
    Serializer DEFAULT = new GSONSerializer();

    byte getSerializerAlgorithm();

    byte[] serialize(Object object);

    <T> T deserialize(Class<T> clazz, byte[] bytes);
}
