package org.syh.demo.netty.chatapp.client.handler;

import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.syh.demo.netty.chatapp.protocol.request.LoginRequestPacket;
import org.syh.demo.netty.chatapp.protocol.response.LoginResponsePacket;
import org.syh.demo.netty.chatapp.protocol.response.MessageResponsePacket;
import org.syh.demo.netty.chatapp.util.LoginUtil;
import org.syh.demo.netty.chatapp.util.MessageUtil;
import org.syh.demo.netty.chatapp.protocol.Packet;
import org.syh.demo.netty.chatapp.protocol.PacketCodec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ClientHandler extends ChannelInboundHandlerAdapter {
    private static final Logger logger = LogManager.getLogger(ClientHandler.class);
    private Object loginLock;
    private Object messageLock;

    public ClientHandler(Object loginLock, Object messageLock) {
        this.loginLock = loginLock;
        this.messageLock = messageLock;
    }

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
        byteBuf.release();
    
        if (packet instanceof LoginResponsePacket) {
            LoginResponsePacket loginResponsePacket = (LoginResponsePacket) packet;
            synchronized (loginLock) {
                if (loginResponsePacket.isSuccess()) {
                    LoginUtil.setLogin(ctx.channel());
                    loginLock.notifyAll();
                    logger.info("Login succeeded");
                } else {
                    logger.error("Login failed: {}", loginResponsePacket.getReason());
                }
            }
        } else if (packet instanceof MessageResponsePacket) { 
            MessageResponsePacket messageResponsePacket = (MessageResponsePacket) packet;
            synchronized (messageLock) {
                logger.info("Received message from server: {}", messageResponsePacket.getMessage());
                MessageUtil.setMessageReceived(ctx.channel());
                messageLock.notifyAll();
            }
        }
    }
}