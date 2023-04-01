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
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class MessageRequestHandler extends SimpleChannelInboundHandler<MessageRequestPacket> {
    private Logger logger  = LogManager.getLogger(MessageRequestHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageRequestPacket messageRequestPacket) throws Exception {
        Session session = SessionUtil.getSession(ctx.channel());

        logger.info(
            "Received message from client {}: {}", 
            ctx.channel().attr(Attributes.USER).get(), 
            messageRequestPacket.getMessage()
        );

        MessageResponsePacket messageResponsePacket = new MessageResponsePacket();
        messageResponsePacket.setFromUserId(session.getUserId());
        messageResponsePacket.setFromUserName(session.getUserName());
        messageResponsePacket.setMessage(messageRequestPacket.getMessage());
        String toUserId = UserUtil.getUserId(messageRequestPacket.getToUserName());
        Channel toUserChannel = SessionUtil.getChannel(toUserId);
        if (toUserChannel != null && SessionUtil.hasLogin(toUserChannel)) {
            toUserChannel.writeAndFlush(messageResponsePacket);
        } else {
            logger.error("User {} is not online", messageRequestPacket.getToUserName());
        }
    }
}
