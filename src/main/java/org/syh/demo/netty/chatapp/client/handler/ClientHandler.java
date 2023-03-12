package org.syh.demo.netty.chatapp.client.handler;

import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.syh.demo.netty.chatapp.protocol.request.LoginRequestPacket;
import org.syh.demo.netty.chatapp.protocol.response.LoginResponsePacket;
import org.syh.demo.netty.chatapp.protocol.Packet;
import org.syh.demo.netty.chatapp.protocol.PacketCodec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ClientHandler extends ChannelInboundHandlerAdapter {
    private static final Logger logger = LogManager.getLogger(ClientHandler.class);

    public void channelActive(ChannelHandlerContext ctx) {
        LoginRequestPacket loginRequestPacket = new LoginRequestPacket();
        loginRequestPacket.setUserId(UUID.randomUUID().toString());
        loginRequestPacket.setUsername("username");
        loginRequestPacket.setPassword("password");
    
        ByteBuf buffer = ctx.alloc().buffer();
        PacketCodec.INSTANCE.encode(buffer, loginRequestPacket);
    
        ctx.channel().writeAndFlush(buffer);
    }

    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ByteBuf byteBuf = (ByteBuf) msg;
    
        Packet packet = PacketCodec.INSTANCE.decode(byteBuf);
    
        if (packet instanceof LoginResponsePacket) {
            LoginResponsePacket loginResponsePacket = (LoginResponsePacket) packet;
    
            if (loginResponsePacket.isSuccess()) {
                logger.info("Login successful");
            } else {
                logger.error("Login failed: {}", loginResponsePacket.getReason());
            }
        }
    }
}