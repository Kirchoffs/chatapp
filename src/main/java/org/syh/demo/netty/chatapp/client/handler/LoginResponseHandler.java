package org.syh.demo.netty.chatapp.client.handler;

import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.syh.demo.netty.chatapp.protocol.request.LoginRequestPacket;
import org.syh.demo.netty.chatapp.protocol.response.LoginResponsePacket;
import org.syh.demo.netty.chatapp.util.LoginUtil;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class LoginResponseHandler extends SimpleChannelInboundHandler<LoginResponsePacket> {

    private Object loginLock;

    public LoginResponseHandler(Object loginLock) {
        this.loginLock = loginLock;
    }

    private static final Logger logger = LogManager.getLogger(LoginResponseHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LoginResponsePacket loginResponsePacket) {
        synchronized (loginLock) {
            if (loginResponsePacket.isSuccess()) {
                logger.info("Login succeeded: {} with userId {}", loginResponsePacket.getUserName(), loginResponsePacket.getUserId());
                LoginUtil.setLogin(ctx.channel());
                loginLock.notifyAll();
            } else {
                logger.error("Login failed: {}", loginResponsePacket.getReason());
            }
        }
    }
    
}
