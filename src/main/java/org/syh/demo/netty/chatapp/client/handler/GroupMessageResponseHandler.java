package org.syh.demo.netty.chatapp.client.handler;

import org.syh.demo.netty.chatapp.protocol.response.GroupMessageResponsePacket;
import org.syh.demo.netty.chatapp.session.Session;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class GroupMessageResponseHandler extends SimpleChannelInboundHandler<GroupMessageResponsePacket> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GroupMessageResponsePacket packet) throws Exception {
        String fromGroupId = packet.getFromGroupId();
        Session fromUser = packet.getFromUser();
        String message = packet.getMessage();

        System.out.println("[" + fromGroupId + "] " + fromUser + ": " + message);
    }
}
