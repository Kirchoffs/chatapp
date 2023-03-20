package org.syh.demo.netty.chatapp.client.handler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.syh.demo.netty.chatapp.protocol.response.MessageResponsePacket;
import org.syh.demo.netty.chatapp.util.MessageUtil;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class MessageResponseHandler extends SimpleChannelInboundHandler<MessageResponsePacket> {

    private static final Logger logger = LogManager.getLogger(MessageResponseHandler.class);
    private Object messageLock;

    public MessageResponseHandler(Object messageLock) {
        this.messageLock = messageLock;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageResponsePacket messageResponsePacket) throws Exception {
        synchronized (messageLock) {
            logger.info("Received message from server: \"{}\"", messageResponsePacket.getMessage());
            MessageUtil.setMessageReceived(ctx.channel());
            messageLock.notifyAll();
        }
    }
    
}
