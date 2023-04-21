package org.syh.demo.netty.chatapp.server.handler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.syh.demo.netty.chatapp.attribute.Attributes;
import org.syh.demo.netty.chatapp.protocol.request.MessageRequestPacket;
import org.syh.demo.netty.chatapp.protocol.response.MessageResponsePacket;
import org.syh.demo.netty.chatapp.session.Session;
import org.syh.demo.netty.chatapp.util.SessionUtil;
import org.syh.demo.netty.chatapp.util.UserUtil;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

@ChannelHandler.Sharable
public class MessageRequestHandler extends SimpleChannelInboundHandler<MessageRequestPacket> {
    public static final MessageRequestHandler INSTANCE = new MessageRequestHandler();

    private final Logger logger  = LogManager.getLogger(MessageRequestHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageRequestPacket requestPacket) throws Exception {
        long begin = System.currentTimeMillis();
        Session session = SessionUtil.getSession(ctx.channel());

        logger.info(
            "Received message from client {}: {}", 
            session.getUserName(), 
            requestPacket.getMessage()
        );

        MessageResponsePacket responsePacket = new MessageResponsePacket();
        responsePacket.setFromUserId(session.getUserId());
        responsePacket.setFromUserName(session.getUserName());
        responsePacket.setMessage(requestPacket.getMessage());
        String toUserId = UserUtil.getUserId(requestPacket.getToUserName());
        Channel toUserChannel = SessionUtil.getChannel(toUserId);
        if (toUserChannel != null && SessionUtil.hasLogin(toUserChannel)) {
            toUserChannel.writeAndFlush(responsePacket).addListener(future -> {
                if (future.isSuccess()) {
                    logger.info("Sent message to user {} successfully", requestPacket.getToUserName());
                } else {
                    logger.error("Sent message to user {} failed", requestPacket.getToUserName());
                }
                long end = System.currentTimeMillis();
                logger.info("Time elapsed for message sending: {} ms", end - begin);
            });
        } else {
            logger.error("User {} is not online", requestPacket.getToUserName());
        }
    }
}
