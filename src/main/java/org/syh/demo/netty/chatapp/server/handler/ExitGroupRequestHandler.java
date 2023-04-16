package org.syh.demo.netty.chatapp.server.handler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.syh.demo.netty.chatapp.protocol.request.ExitGroupRequestPacket;
import org.syh.demo.netty.chatapp.protocol.response.ExitGroupResponsePacket;
import org.syh.demo.netty.chatapp.util.GroupUtil;
import org.syh.demo.netty.chatapp.util.SessionUtil;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;

public class ExitGroupRequestHandler extends SimpleChannelInboundHandler<ExitGroupRequestPacket> {
    private final Logger logger = LogManager.getLogger(ExitGroupRequestHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ExitGroupRequestPacket requestPacket) throws Exception {
        String groupId = requestPacket.getGroupId();
        ChannelGroup channelGroup = GroupUtil.getChannelGroup(groupId);
        
        ExitGroupResponsePacket responsePacket = new ExitGroupResponsePacket();
        responsePacket.setGroupId(groupId);

        if (channelGroup != null) {
            channelGroup.remove(ctx.channel());
            responsePacket.setSuccess(true);
            logger.info("User {} has exited group {}", SessionUtil.getSession(ctx.channel()).getUserId(), groupId);
        } else {
            responsePacket.setSuccess(false);
            logger.warn("Group {} not found", groupId);
        }

        ctx.writeAndFlush(responsePacket);
    }   
}
