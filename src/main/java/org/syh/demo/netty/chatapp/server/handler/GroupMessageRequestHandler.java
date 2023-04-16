package org.syh.demo.netty.chatapp.server.handler;

import org.syh.demo.netty.chatapp.protocol.request.GroupMessageRequestPacket;
import org.syh.demo.netty.chatapp.protocol.response.GroupMessageResponsePacket;
import org.syh.demo.netty.chatapp.util.GroupUtil;
import org.syh.demo.netty.chatapp.util.SessionUtil;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;

public class GroupMessageRequestHandler extends SimpleChannelInboundHandler<GroupMessageRequestPacket> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GroupMessageRequestPacket requestPacket) throws Exception {
        GroupMessageResponsePacket responsePacket = new GroupMessageResponsePacket();

        responsePacket.setFromGroupId(requestPacket.getToGroupId());
        responsePacket.setMessage(requestPacket.getMessage());
        responsePacket.setFromUser(SessionUtil.getSession(ctx.channel()));

        ChannelGroup channelGroup = GroupUtil.getChannelGroup(requestPacket.getToGroupId());
        channelGroup.writeAndFlush(responsePacket);
    }
}
