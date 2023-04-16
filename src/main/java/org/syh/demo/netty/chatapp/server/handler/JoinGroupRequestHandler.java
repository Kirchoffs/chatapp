package org.syh.demo.netty.chatapp.server.handler;

import org.syh.demo.netty.chatapp.protocol.request.JoinGroupRequestPacket;
import org.syh.demo.netty.chatapp.protocol.response.JoinGroupResponsePacket;
import org.syh.demo.netty.chatapp.util.GroupUtil;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class JoinGroupRequestHandler extends SimpleChannelInboundHandler<JoinGroupRequestPacket> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, JoinGroupRequestPacket requestPacket) throws Exception {
        String groupId = requestPacket.getGroupId();
        GroupUtil.addToChannelGroup(groupId, ctx.channel());

        JoinGroupResponsePacket responsePacket = new JoinGroupResponsePacket();
        responsePacket.setSuccess(true);
        responsePacket.setGroupId(groupId);
        ctx.channel().writeAndFlush(responsePacket);
    }
}
