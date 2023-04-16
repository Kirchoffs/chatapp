package org.syh.demo.netty.chatapp.attribute;

import org.syh.demo.netty.chatapp.session.Session;

import io.netty.util.AttributeKey;

public interface Attributes {
    AttributeKey<Session> SESSION = AttributeKey.newInstance("session");
    AttributeKey<String> USER = AttributeKey.newInstance("user");
    AttributeKey<Boolean> LOGIN = AttributeKey.newInstance("login");
    AttributeKey<Boolean> LOGIN_UPDATE = AttributeKey.newInstance("loginUpdate");
    AttributeKey<Boolean> MESSAGE_RECEIVED = AttributeKey.newInstance("messageReceived");
}
