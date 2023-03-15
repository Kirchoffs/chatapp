package org.syh.demo.netty.chatapp.attribute;

import io.netty.util.AttributeKey;

public interface Attributes {
    AttributeKey<Boolean> LOGIN = AttributeKey.newInstance("login");
    AttributeKey<Boolean> MESSAGE_RECEIVED = AttributeKey.newInstance("messageReceived");
}
