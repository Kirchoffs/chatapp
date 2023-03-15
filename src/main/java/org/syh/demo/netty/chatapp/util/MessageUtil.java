package org.syh.demo.netty.chatapp.util;

import org.syh.demo.netty.chatapp.attribute.Attributes;

import io.netty.channel.Channel;
import io.netty.util.Attribute;

public class MessageUtil {
    public static void setMessageReceived(Channel channel) {
        channel.attr(Attributes.MESSAGE_RECEIVED).set(true);
    }

    public static void unsetAsMessageReceived(Channel channel) {
        channel.attr(Attributes.MESSAGE_RECEIVED).set(false);
    }

    public static boolean hasMessageReceived(Channel channel) {
        Attribute<Boolean> loginAttr = channel.attr(Attributes.MESSAGE_RECEIVED);

        return loginAttr.get() != null && loginAttr.get();
    }
}
