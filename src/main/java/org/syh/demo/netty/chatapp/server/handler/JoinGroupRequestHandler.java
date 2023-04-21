package org.syh.demo.netty.chatapp.server.handler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.syh.demo.netty.chatapp.protocol.request.JoinGroupRequestPacket;
import org.syh.demo.netty.chatapp.protocol.response.JoinGroupResponsePacket;
import org.syh.demo.netty.chatapp.util.GroupUtil;
import org.syh.demo.netty.chatapp.util.SessionUtil;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

@ChannelHandler.Sharable
public class JoinGroupRequestHandler extends SimpleChannelInboundHandler<JoinGroupRequestPacket> {
    public static final JoinGroupRequestHandler INSTANCE = new JoinGroupRequestHandler();

    private final Logger logger = LogManager.getLogger(JoinGroupRequestHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, JoinGroupRequestPacket requestPacket) throws Exception {
        String groupId = requestPacket.getGroupId();
        JoinGroupResponsePacket responsePacket = new JoinGroupResponsePacket();
        responsePacket.setGroupId(groupId);

        if (GroupUtil.addToChannelGroup(groupId, ctx.channel())) {
            responsePacket.setSuccess(true);
            logger.info("User {} has joined group {}", SessionUtil.getSession(ctx.channel()), groupId);
        } else {
            responsePacket.setSuccess(false);
            responsePacket.setReason("Group not found");
            logger.warn("Group {} not found", groupId);
        }

        ctx.writeAndFlush(responsePacket);
    }
}
