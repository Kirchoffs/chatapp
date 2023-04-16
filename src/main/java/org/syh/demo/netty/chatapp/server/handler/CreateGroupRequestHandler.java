package org.syh.demo.netty.chatapp.server.handler;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.syh.demo.netty.chatapp.protocol.request.CreateGroupRequestPacket;
import org.syh.demo.netty.chatapp.protocol.response.CreateGroupResponsePacket;
import org.syh.demo.netty.chatapp.util.GroupUtil;
import org.syh.demo.netty.chatapp.util.SessionUtil;
import org.syh.demo.netty.chatapp.util.UserUtil;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;

public class CreateGroupRequestHandler extends SimpleChannelInboundHandler<CreateGroupRequestPacket> {
    private final Logger logger = LogManager.getLogger(CreateGroupRequestHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, CreateGroupRequestPacket requestPacket) throws Exception {
        List<String> userNameList = requestPacket.getUserNameList();
        List<String> userIdList = UserUtil.getUserIdList(userNameList);
        ChannelGroup channelGroup = new DefaultChannelGroup(ctx.executor());

        for (String userName: userNameList) {
            String userId = UserUtil.getUserId(userName);
            Channel channel = SessionUtil.getChannel(userId);
            if (channel != null) {
                channelGroup.add(channel);
            }
        }

        CreateGroupResponsePacket responsePacket = new CreateGroupResponsePacket();
        String groupId = GroupUtil.generateAndRegisterUniqueGroupId(userIdList);
        responsePacket.setSuccess(true);
        responsePacket.setGroupId(groupId);
        responsePacket.setUserNameList(userNameList);

        channelGroup.writeAndFlush(responsePacket);
        GroupUtil.registerChannelGroup(groupId, channelGroup);
        logger.info("Create group success, groupId: {}, userNameList: {}", responsePacket.getGroupId(), responsePacket.getUserNameList());
    }
}
