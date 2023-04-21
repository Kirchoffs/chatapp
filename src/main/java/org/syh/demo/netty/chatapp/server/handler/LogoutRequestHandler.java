package org.syh.demo.netty.chatapp.server.handler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.syh.demo.netty.chatapp.protocol.request.LogoutRequestPacket;
import org.syh.demo.netty.chatapp.protocol.response.LogoutResponsePacket;
import org.syh.demo.netty.chatapp.util.SessionUtil;
import org.syh.demo.netty.chatapp.util.UserUtil;
import  org.syh.demo.netty.chatapp.attribute.Attributes;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

@ChannelHandler.Sharable
public class LogoutRequestHandler extends SimpleChannelInboundHandler<LogoutRequestPacket> {
    public static final LogoutRequestHandler INSTANCE = new LogoutRequestHandler();

    private final Logger logger = LogManager.getLogger(LogoutRequestHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LogoutRequestPacket requestPacket) throws Exception {
        String username = SessionUtil.getUserName(ctx.channel());
        SessionUtil.unBindSession(ctx.channel());
        UserUtil.removeUser(username);
        LogoutResponsePacket responsePacket = new LogoutResponsePacket();
        responsePacket.setSuccess(true);
        logger.info("Logout request succeeded: user {}", username);
        ctx.writeAndFlush(responsePacket);
    }
}
