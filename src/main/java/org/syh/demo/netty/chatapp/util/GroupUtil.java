package org.syh.demo.netty.chatapp.util;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;

public class GroupUtil {
    private static Logger logger = LogManager.getLogger(GroupUtil.class);

    private static final Set<String> groupIdSet = ConcurrentHashMap.newKeySet();
    private static final Map<String, ChannelGroup> channelGroupMap = new ConcurrentHashMap<>();

    public static synchronized String generateAndRegisterUniqueGroupId(List<String> userIdList) {
        String groupId;
        
        do {
            groupId = String.valueOf((int) (Math.random() * 100000));
        } while (groupIdSet.contains(groupId));

        groupIdSet.add(groupId);

        return groupId;
    }

    public static void registerChannelGroup(String groupId, ChannelGroup channelGroup) {
        channelGroupMap.put(groupId, channelGroup);
    }

    public static void unregisterChannelGroup(String groupId) {
        channelGroupMap.remove(groupId);
    }

    public static boolean addToChannelGroup(String groupId, Channel channel) {
        ChannelGroup channelGroup = channelGroupMap.get(groupId);
        if (channelGroup != null) {
            channelGroup.add(channel);
            return true;
        } else {
            logger.warn("Channel group not found, group ID: {}", groupId);
            return false;
        }
    }

    public static void removeFromChannelGroup(String groupId, Channel channel) {
        ChannelGroup channelGroup = channelGroupMap.get(groupId);
        if (channelGroup != null) {
            channelGroup.remove(channel);
        } else {
            logger.warn("Channel group not found, group ID: {}", groupId);
        }
    }

    public static ChannelGroup getChannelGroup(String groupId) {
        ChannelGroup channelGroup = channelGroupMap.get(groupId);
        if (channelGroup == null) {
            logger.warn("Channel group not found, group ID: {}", groupId);
        }
        return channelGroup;
    }
}
