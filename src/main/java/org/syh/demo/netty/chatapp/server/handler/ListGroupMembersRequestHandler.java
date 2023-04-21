package org.syh.demo.netty.chatapp.server.handler;

import java.util.ArrayList;
import java.util.List;

import org.syh.demo.netty.chatapp.protocol.request.ListGroupMembersRequestPacket;
import org.syh.demo.netty.chatapp.protocol.response.ListGroupMembersResponsePacket;
import org.syh.demo.netty.chatapp.session.Session;
import org.syh.demo.netty.chatapp.util.GroupUtil;
import org.syh.demo.netty.chatapp.util.SessionUtil;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;

@ChannelHandler.Sharable
public class ListGroupMembersRequestHandler extends SimpleChannelInboundHandler<ListGroupMembersRequestPacket> {
    public static final ListGroupMembersRequestHandler INSTANCE = new ListGroupMembersRequestHandler();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ListGroupMembersRequestPacket requestPacket) throws Exception {
        String groupId = requestPacket.getGroupId();
        ChannelGroup channelGroup = GroupUtil.getChannelGroup(groupId);

        List<Session> sessionList = new ArrayList<>();
        for (Channel channel : channelGroup) {
            sessionList.add(SessionUtil.getSession(channel));
        }

        ListGroupMembersResponsePacket responsePacket = new ListGroupMembersResponsePacket();
        responsePacket.setGroupId(groupId);
        responsePacket.setSessionList(sessionList);
        ctx.writeAndFlush(responsePacket);
    }
}
