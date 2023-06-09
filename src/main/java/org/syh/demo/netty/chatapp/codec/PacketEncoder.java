package org.syh.demo.netty.chatapp.codec;

import org.syh.demo.netty.chatapp.protocol.Packet;
import org.syh.demo.netty.chatapp.protocol.PacketCodec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class PacketEncoder extends MessageToByteEncoder<Packet> {
    @Override
    protected void encode(ChannelHandlerContext ctx, Packet packet, ByteBuf out) {
        PacketCodec.INSTANCE.encode(out, packet);
    }
}
