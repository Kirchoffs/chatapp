package org.syh.demo.netty.chatapp.client.handler;

import org.syh.demo.netty.chatapp.protocol.response.MessageResponsePacket;
import org.syh.demo.netty.chatapp.util.MessageUtil;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class MessageResponseHandler extends SimpleChannelInboundHandler<MessageResponsePacket> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageResponsePacket packet) throws Exception {
        String fromUserId = packet.getFromUserId();
        String fromUserName = packet.getFromUserName();
        System.out.println(fromUserId + ":" + fromUserName + " -> " + packet .getMessage());
        MessageUtil.setMessageReceived(ctx.channel());
    }
}
