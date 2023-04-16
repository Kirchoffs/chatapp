package org.syh.demo.netty.chatapp.client.handler;

import org.syh.demo.netty.chatapp.protocol.response.ListGroupMembersResponsePacket;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ListGroupMembersResponseHandler extends SimpleChannelInboundHandler<ListGroupMembersResponsePacket> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ListGroupMembersResponsePacket packet) throws Exception {
        System.out.println("Group " + packet.getGroupId() + " members: " + packet.getSessionList());
    }
}
