package org.syh.demo.netty.chatapp.server.handler;

import org.syh.demo.netty.chatapp.protocol.Packet;
import org.syh.demo.netty.chatapp.protocol.PacketCodec;
import org.syh.demo.netty.chatapp.protocol.request.LoginRequestPacket;
import org.syh.demo.netty.chatapp.protocol.response.LoginResponsePacket;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ServerHandler extends ChannelInboundHandlerAdapter {
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ByteBuf requestByteBuf = (ByteBuf) msg;
        Packet packet = PacketCodec.INSTANCE.decode(requestByteBuf);

        LoginResponsePacket loginResponsePacket = new LoginResponsePacket();
        loginResponsePacket.setVersion(packet.getVersion());

        if (packet instanceof LoginRequestPacket) {
            LoginRequestPacket loginRequestPacket = (LoginRequestPacket) packet;
    
            if (valid(loginRequestPacket)) {
                loginResponsePacket.setSuccess(true);
            } else {
                loginResponsePacket.setReason("Password not match");
                loginResponsePacket.setSuccess(false);
            }
        }
        
        ByteBuf responseByteBuf = ctx.alloc().buffer();
        PacketCodec.INSTANCE.encode(responseByteBuf, loginResponsePacket);
        ctx.channel().writeAndFlush(responseByteBuf);
    }
    
    private boolean valid(LoginRequestPacket loginRequestPacket) {
        return true;
    }
}
