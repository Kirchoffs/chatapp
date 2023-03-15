package org.syh.demo.netty.chatapp.util;

import org.syh.demo.netty.chatapp.attribute.Attributes;

import io.netty.channel.Channel;
import io.netty.util.Attribute;

public class LoginUtil {
    public static void setLogin(Channel channel) {
        channel.attr(Attributes.LOGIN).set(true);
    }

    public static boolean hasLogin(Channel channel) {
        Attribute<Boolean> loginAttr = channel.attr(Attributes.LOGIN);

        return loginAttr.get() != null;
    }
}
