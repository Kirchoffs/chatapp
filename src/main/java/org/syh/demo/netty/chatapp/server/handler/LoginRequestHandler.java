package org.syh.demo.netty.chatapp.server.handler;

import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.syh.demo.netty.chatapp.attribute.Attributes;
import org.syh.demo.netty.chatapp.protocol.request.LoginRequestPacket;
import org.syh.demo.netty.chatapp.protocol.response.LoginResponsePacket;
import org.syh.demo.netty.chatapp.session.Session;
import org.syh.demo.netty.chatapp.util.LoginUtil;
import org.syh.demo.netty.chatapp.util.SessionUtil;
import org.syh.demo.netty.chatapp.util.UserUtil;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class LoginRequestHandler extends SimpleChannelInboundHandler<LoginRequestPacket> {
    private static final Logger logger = LogManager.getLogger(LoginRequestHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LoginRequestPacket loginRequestPacket) throws Exception {
        logger.info("Received login request from client");
        
        String username = loginRequestPacket.getUsername();
        LoginResponsePacket loginResponsePacket = new LoginResponsePacket();
        loginResponsePacket.setVersion(loginRequestPacket.getVersion());
        loginResponsePacket.setUserName(username);

        if (valid(loginRequestPacket)) {
            logger.info("Login request succeeded: user {}", username);
            String userId = randomUserId();
            ctx.channel().attr(Attributes.USER).set(username);
            SessionUtil.bindSession(new Session(userId, username), ctx.channel());
            UserUtil.addUser(username, userId);
            loginResponsePacket.setUserId(userId);
            loginResponsePacket.setSuccess(true);
        } else {
            logger.info("Login request failed: user {}");
            loginResponsePacket.setReason("Username/Password not match");
            loginResponsePacket.setSuccess(false);
        }
        
        ctx.channel().writeAndFlush(loginResponsePacket);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        SessionUtil.unBindSession(ctx.channel());
    }

    private String randomUserId() {
        return String.valueOf(new Random().nextInt(100000000));
    }

    private boolean valid(LoginRequestPacket loginRequestPacket) {
        return true;
    }
}
