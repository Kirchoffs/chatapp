package org.syh.demo.netty.chatapp.client.handler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.syh.demo.netty.chatapp.protocol.response.LoginResponsePacket;
import org.syh.demo.netty.chatapp.util.LoginUtil;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class LoginResponseHandler extends SimpleChannelInboundHandler<LoginResponsePacket> {
    private final Logger logger = LogManager.getLogger(LoginResponseHandler.class);
    private Object loginLock;

    public LoginResponseHandler(Object loginLock) {
        this.loginLock = loginLock;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LoginResponsePacket packet) {
        synchronized (loginLock) {
            if (packet.isSuccess()) {
                logger.info("Login succeeded: {} with userId {}", packet.getUserName(), packet.getUserId());
                LoginUtil.setLogin(ctx.channel(), packet.getUserName());
                LoginUtil.setLoginUpdate(ctx.channel());
                loginLock.notifyAll();
            } else {
                LoginUtil.setLoginUpdate(ctx.channel());
                loginLock.notifyAll();
                logger.error("Login failed: {}", packet.getReason());
            }
        }
    }
}
