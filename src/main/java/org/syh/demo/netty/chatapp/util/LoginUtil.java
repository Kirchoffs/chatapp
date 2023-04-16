package org.syh.demo.netty.chatapp.util;

import org.syh.demo.netty.chatapp.attribute.Attributes;

import io.netty.channel.Channel;

public class LoginUtil {
    public static void setLogin(Channel channel, String name) {
        channel.attr(Attributes.LOGIN).set(true);
        channel.attr(Attributes.USER).set(name);
    }

    public static void unsetLogin(Channel channel) {
        channel.attr(Attributes.LOGIN).set(null);
        channel.attr(Attributes.USER).set(null);
    }

    public static boolean hasLogin(Channel channel) {
        return channel.attr(Attributes.LOGIN).get() != null;
    }

    public static void setLoginUpdate(Channel channel) {
        channel.attr(Attributes.LOGIN_UPDATE).set(true);
    }

    public static void unsetLoginUpdate(Channel channel) {
        channel.attr(Attributes.LOGIN_UPDATE).set(null);
    }

    public static boolean hasLoginUpdate(Channel channel) {
        return channel.attr(Attributes.LOGIN_UPDATE).get() != null;
    }

    public static String getLoginName(Channel channel) {
        return channel.attr(Attributes.USER).get();
    }
}
