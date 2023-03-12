package org.syh.demo.netty.chatapp.protocol;

import java.util.HashMap;
import java.util.Map;

import org.syh.demo.netty.chatapp.protocol.request.LoginRequestPacket;
import org.syh.demo.netty.chatapp.protocol.response.LoginResponsePacket;
import org.syh.demo.netty.chatapp.serialize.Serializer;
import org.syh.demo.netty.chatapp.serialize.impl.GSONSerializer;

import static org.syh.demo.netty.chatapp.protocol.command.Command.LOGIN_REQUEST;
import static org.syh.demo.netty.chatapp.protocol.command.Command.LOGIN_RESPONSE;

import io.netty.buffer.ByteBuf;

public class PacketCodec {
    public static final PacketCodec INSTANCE = new PacketCodec();

    public static final int MAGIC_NUMBER = 0xFACADE;
    public static final int MAGIC_NUMBER_LENGTH = 4;
    public static final int VERSION_LENGTH = 1;

    private final Map<Byte, Class<? extends Packet>> packetTypeMap;
    private final Map<Byte, Serializer> serializerMap;

    public PacketCodec() {
        packetTypeMap = new HashMap<>();
        packetTypeMap.put(LOGIN_REQUEST, LoginRequestPacket.class);
        packetTypeMap.put(LOGIN_RESPONSE, LoginResponsePacket.class);

        serializerMap = new HashMap<>();
        Serializer serializer = new GSONSerializer();
        serializerMap.put(serializer.getSerializerAlgorithm(), serializer);
    }

    public void encode(ByteBuf byteBuf, Packet packet) {
        byte[] packetBytes = Serializer.DEFAULT.serialize(packet);

        byteBuf.writeInt(MAGIC_NUMBER);
        byteBuf.writeByte(packet.getVersion());
        byteBuf.writeByte(Serializer.DEFAULT.getSerializerAlgorithm());
        byteBuf.writeByte(packet.getCommand());
        byteBuf.writeInt(packetBytes.length);
        byteBuf.writeBytes(packetBytes);
    }

    public Packet decode(ByteBuf byteBuf) {
        byteBuf.skipBytes(MAGIC_NUMBER_LENGTH);
        byteBuf.skipBytes(VERSION_LENGTH);
    
        byte serializeAlgorithm = byteBuf.readByte();
        byte command = byteBuf.readByte();
        int length = byteBuf.readInt();
    
        byte[] bytes = new byte[length];
        byteBuf.readBytes(bytes);
    
        Class<? extends Packet> requestType = getRequestType(command);
        Serializer serializer = getSerializer(serializeAlgorithm);
    
        if (requestType != null && serializer != null) {
            return serializer.deserialize(requestType, bytes);
        }
    
        return null;
    }

    private Class<? extends Packet> getRequestType(byte command) {
        return packetTypeMap.get(command);
    }

    private Serializer getSerializer(byte serializeAlgorithm) {
        return serializerMap.get(serializeAlgorithm);
    }
}
