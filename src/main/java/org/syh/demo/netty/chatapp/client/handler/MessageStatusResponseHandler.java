package org.syh.demo.netty.chatapp.client.handler;

import org.syh.demo.netty.chatapp.protocol.response.MessageStatusResponsePacket;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class MessageStatusResponseHandler extends SimpleChannelInboundHandler<MessageStatusResponsePacket> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageStatusResponsePacket packet) throws Exception {
        if (!packet.isSuccess()) {
            System.out.println(packet.getReason());
        }
    }
}
