package org.syh.demo.netty.chatapp.client.handler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.syh.demo.netty.chatapp.protocol.response.LogoutResponsePacket;
import org.syh.demo.netty.chatapp.util.LoginUtil;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class LogoutResponseHandler extends SimpleChannelInboundHandler<LogoutResponsePacket> {
    private final Logger logger = LogManager.getLogger(LogoutResponseHandler.class);
    private Object loginLock;

    public LogoutResponseHandler(Object loginLock) {
        this.loginLock = loginLock;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LogoutResponsePacket packet) throws Exception {
        synchronized (loginLock) {
            if (packet.isSuccess()) {
                logger.info("Logout succeeded");
                LoginUtil.unsetLogin(ctx.channel());
                LoginUtil.setLoginUpdate(ctx.channel());
                loginLock.notifyAll();
            } else {
                LoginUtil.setLoginUpdate(ctx.channel());
                loginLock.notifyAll();
                logger.error("Logout failed: {}", packet.getReason());
            }
        }
    }
}
