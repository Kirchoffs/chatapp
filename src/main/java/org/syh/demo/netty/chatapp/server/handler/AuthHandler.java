package org.syh.demo.netty.chatapp.server.handler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.syh.demo.netty.chatapp.util.LoginUtil;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class AuthHandler extends ChannelInboundHandlerAdapter {
    private static final Logger logger = LogManager.getLogger(LoginRequestHandler.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (!LoginUtil.hasLogin(ctx.channel())) {
            logger.error("Authentication failed, please login first");
            ctx.channel().close();
        } else {
            ctx.pipeline().remove(this);
            super.channelRead(ctx, msg);
        }
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) {
        if (LoginUtil.hasLogin(ctx.channel())) {
            logger.info("Current connection has completed the login authentication and does not need to be authenticated again. The AuthHandler has been be removed.");
        } else {
            logger.info("No login authentication, shutdown the connection");
        }
    }
}
