package org.syh.demo.netty.chatapp.client.handler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.syh.demo.netty.chatapp.protocol.response.ExitGroupResponsePacket;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ExitGroupResponseHandler extends SimpleChannelInboundHandler<ExitGroupResponsePacket> {
    private final Logger logger = LogManager.getLogger(ExitGroupResponseHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ExitGroupResponsePacket packet) throws Exception {
        if (packet.isSuccess()) {
            logger.info("Exit group {} successfully", packet.getGroupId());
        } else {
            logger.warn("Exit group {} failed", packet.getGroupId());
        }
    }
}
