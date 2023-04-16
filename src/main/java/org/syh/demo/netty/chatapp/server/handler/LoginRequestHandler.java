package org.syh.demo.netty.chatapp.server.handler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.syh.demo.netty.chatapp.attribute.Attributes;
import org.syh.demo.netty.chatapp.protocol.request.LoginRequestPacket;
import org.syh.demo.netty.chatapp.protocol.response.LoginResponsePacket;
import org.syh.demo.netty.chatapp.session.Session;
import org.syh.demo.netty.chatapp.util.SessionUtil;
import org.syh.demo.netty.chatapp.util.UserUtil;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class LoginRequestHandler extends SimpleChannelInboundHandler<LoginRequestPacket> {
    private final Logger logger = LogManager.getLogger(LoginRequestHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LoginRequestPacket requestPacket) throws Exception {
        logger.info("Received login request from client");
        
        String username = requestPacket.getUsername();
        LoginResponsePacket responsePacket = new LoginResponsePacket();
        responsePacket.setVersion(requestPacket.getVersion());
        responsePacket.setUserName(username);

        if (valid(requestPacket) && !UserUtil.isUserLogged(username)) {
            logger.info("Login request succeeded: user {}", username);
            ctx.channel().attr(Attributes.USER).set(username);
            String userId = UserUtil.generateAndRegisterUniqueUserId(username);
            SessionUtil.bindSession(new Session(userId, username), ctx.channel());
            responsePacket.setUserId(userId);
            responsePacket.setSuccess(true);
        } else {
            logger.info("Login request failed: user {}");
            responsePacket.setSuccess(false);

            if (UserUtil.isUserLogged(username)) {
                responsePacket.setReason("Already logged in");
            } else {
                responsePacket.setReason("Username/Password not match");
            }
        }
        
        ctx.channel().writeAndFlush(responsePacket);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        SessionUtil.unBindSession(ctx.channel());
    }

    private boolean valid(LoginRequestPacket requestPacket) {
        return true;
    }
}
