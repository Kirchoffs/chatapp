package org.syh.demo.netty.chatapp.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.syh.demo.netty.chatapp.attribute.Attributes;
import org.syh.demo.netty.chatapp.session.Session;

import io.netty.channel.Channel;

public class SessionUtil {
    private static final Map<String, Channel> userIdChannelMap = new ConcurrentHashMap<>();

    public static void bindSession(Session session, Channel channel) {
        userIdChannelMap.put(session.getUserId(), channel);
        channel.attr(Attributes.SESSION).set(session);
    }

    public static void unBindSession(Channel channel) {
        if (hasLogin(channel)) {
            userIdChannelMap.remove(getSession(channel).getUserId());
            channel.attr(Attributes.SESSION).set(null);
        }
    }

    public static boolean hasLogin(Channel channel) {
        return channel.hasAttr(Attributes.SESSION);
    }

    public static Session getSession(Channel channel) {
        return channel.attr(Attributes.SESSION).get();
    }

    public static Channel getChannel(String userId) {
        if (userId == null) {
            return null;
        }
        return userIdChannelMap.get(userId);
    }
}
