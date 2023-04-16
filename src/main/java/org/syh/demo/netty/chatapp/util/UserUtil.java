package org.syh.demo.netty.chatapp.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UserUtil {
    private static final Map<String, String> userNameToIdMap = new ConcurrentHashMap<>();
    private static final Map<String, String> userIdToNameMap = new ConcurrentHashMap<>();

    public static void addUser(String userName, String userId) {
        userNameToIdMap.put(userName, userId);
        userIdToNameMap.put(userId, userName);
    }

    public static void removeUser(String userName) {
        String userId;
        if ((userId = userNameToIdMap.get(userName)) != null) {
            userNameToIdMap.remove(userName);
            userIdToNameMap.remove(userId);
        }
    }

    public static String getUserId(String userName) {
        return userNameToIdMap.get(userName);
    }

    public static List<String> getUserIdList(List<String> userNameList) {
        List<String> userIdList = new ArrayList<>();
        
        for (String userName: userNameList) {
            String userId = userNameToIdMap.get(userName);
            if (userId != null) {
                userIdList.add(userId);
            }
        }

        return userIdList;
    }

    public static String getUserName(String userId) {
        return userIdToNameMap.get(userId);
    }

    public static List<String> getUserNameList(List<String> userIdList) {
        List<String> userNameList = new ArrayList<>();
        
        for (String userId: userIdList) {
            String userName = userIdToNameMap.get(userId);
            if (userName != null) {
                userNameList.add(userName);
            }
        }

        return userNameList;
    }

    public static boolean isUserLogged(String userName) {
        return userNameToIdMap.containsKey(userName);
    }

    public static synchronized String generateAndRegisterUniqueUserId(String userName) {
        String userId;
        do {
            userId = String.valueOf((int) (Math.random() * 100000));
        } while (userIdToNameMap.containsKey(userId));

        userNameToIdMap.put(userName, userId);
        userIdToNameMap.put(userId, userName);

        return userId;
    }
}
