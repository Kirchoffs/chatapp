package org.syh.demo.netty.chatapp.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UserUtil {
    private static final Map<String, String> userMap = new ConcurrentHashMap<>();

    public static void addUser(String userName, String userId) {
        userMap.put(userName, userId);
    }

    public static String getUserId(String userName) {
        return userMap.get(userName);
    }
}
