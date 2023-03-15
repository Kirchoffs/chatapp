package org.syh.demo.netty.chatapp.server.handler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.syh.demo.netty.chatapp.protocol.Packet;
import org.syh.demo.netty.chatapp.protocol.PacketCodec;
import org.syh.demo.netty.chatapp.protocol.request.LoginRequestPacket;
import org.syh.demo.netty.chatapp.protocol.request.MessageRequestPacket;
import org.syh.demo.netty.chatapp.protocol.response.LoginResponsePacket;
import org.syh.demo.netty.chatapp.protocol.response.MessageResponsePacket;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ServerHandler extends ChannelInboundHandlerAdapter {

    private static final Logger logger = LogManager.getLogger(ServerHandler.class);
    
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ByteBuf requestByteBuf = (ByteBuf) msg;
        Packet packet = PacketCodec.INSTANCE.decode(requestByteBuf);

        ByteBuf responseByteBuf = ctx.alloc().buffer();

        if (packet instanceof LoginRequestPacket) {
            LoginRequestPacket loginRequestPacket = (LoginRequestPacket) packet;
    
            LoginResponsePacket loginResponsePacket = new LoginResponsePacket();
            loginResponsePacket.setVersion(packet.getVersion());

            if (valid(loginRequestPacket)) {
                logger.info("Login request succeeded");
                loginResponsePacket.setSuccess(true);
            } else {
                logger.info("Login request failed");
                loginResponsePacket.setReason("Password not match");
                loginResponsePacket.setSuccess(false);
            }

            PacketCodec.INSTANCE.encode(responseByteBuf, loginResponsePacket);
        } else if (packet instanceof MessageRequestPacket) {
            MessageRequestPacket messageRequestPacket = (MessageRequestPacket) packet;
            logger.info("Received message from client: {}", messageRequestPacket.getMessage());

            MessageResponsePacket messageReponsePacket = new MessageResponsePacket();
            messageReponsePacket.setMessage(String.format("Hi, I received your message: %s", messageRequestPacket.getMessage()));
            PacketCodec.INSTANCE.encode(responseByteBuf, messageReponsePacket);
        }
        
        ctx.channel().writeAndFlush(responseByteBuf);
    }
    
    private boolean valid(LoginRequestPacket loginRequestPacket) {
        return true;
    }
}
