package org.syh.demo.netty.chatapp.client.handler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.syh.demo.netty.chatapp.protocol.response.CreateGroupResponsePacket;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class CreateGroupResponseHandler extends SimpleChannelInboundHandler<CreateGroupResponsePacket> {
    private final Logger logger = LogManager.getLogger(CreateGroupResponseHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, CreateGroupResponsePacket packet) throws Exception {
        logger.info("Group created with Id {}", packet.getGroupId());
        logger.info("Group members: {}", packet.getUserNameList());
    }
}
