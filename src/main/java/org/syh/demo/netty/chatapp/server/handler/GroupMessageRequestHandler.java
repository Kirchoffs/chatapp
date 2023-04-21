package org.syh.demo.netty.chatapp.server.handler;

import org.syh.demo.netty.chatapp.protocol.request.GroupMessageRequestPacket;
import org.syh.demo.netty.chatapp.protocol.response.GroupMessageResponsePacket;
import org.syh.demo.netty.chatapp.protocol.response.MessageStatusResponsePacket;
import org.syh.demo.netty.chatapp.server.exception.ChannelGroupNotFoundException;
import org.syh.demo.netty.chatapp.util.GroupUtil;
import org.syh.demo.netty.chatapp.util.SessionUtil;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;

@ChannelHandler.Sharable
public class GroupMessageRequestHandler extends SimpleChannelInboundHandler<GroupMessageRequestPacket> {
    public static final GroupMessageRequestHandler INSTANCE = new GroupMessageRequestHandler();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GroupMessageRequestPacket requestPacket) throws Exception {
        GroupMessageResponsePacket groupMessageResponsePacket = new GroupMessageResponsePacket();
        groupMessageResponsePacket.setFromGroupId(requestPacket.getToGroupId());
        groupMessageResponsePacket.setMessage(requestPacket.getMessage());
        groupMessageResponsePacket.setFromUser(SessionUtil.getSession(ctx.channel()));

        MessageStatusResponsePacket messageStatusResponsePacket = new MessageStatusResponsePacket();
        messageStatusResponsePacket.setFromUserName(SessionUtil.getUserName(ctx.channel()));
        messageStatusResponsePacket.setToGroupId(requestPacket.getToGroupId());

        ChannelGroup channelGroup = null;
        try {
            channelGroup = GroupUtil.getChannelGroup(requestPacket.getToGroupId());
            messageStatusResponsePacket.setSuccess(true);
        } catch (ChannelGroupNotFoundException e) {
            messageStatusResponsePacket.setSuccess(false);
            messageStatusResponsePacket.setReason(e.getMessage());
            ctx.writeAndFlush(messageStatusResponsePacket);
            return;
        }
        
        channelGroup.writeAndFlush(groupMessageResponsePacket);
    }
}
