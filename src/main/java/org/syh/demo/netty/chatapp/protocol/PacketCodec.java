package org.syh.demo.netty.chatapp.protocol;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.syh.demo.netty.chatapp.protocol.request.CreateGroupRequestPacket;
import org.syh.demo.netty.chatapp.protocol.request.ExitGroupRequestPacket;
import org.syh.demo.netty.chatapp.protocol.request.JoinGroupRequestPacket;
import org.syh.demo.netty.chatapp.protocol.request.GroupMessageRequestPacket;
import org.syh.demo.netty.chatapp.protocol.request.HeartBeatRequestPacket;
import org.syh.demo.netty.chatapp.protocol.request.ListGroupMembersRequestPacket;
import org.syh.demo.netty.chatapp.protocol.request.LoginRequestPacket;
import org.syh.demo.netty.chatapp.protocol.request.LogoutRequestPacket;
import org.syh.demo.netty.chatapp.protocol.request.MessageRequestPacket;
import org.syh.demo.netty.chatapp.protocol.response.CreateGroupResponsePacket;
import org.syh.demo.netty.chatapp.protocol.response.ExitGroupResponsePacket;
import org.syh.demo.netty.chatapp.protocol.response.JoinGroupResponsePacket;
import org.syh.demo.netty.chatapp.protocol.response.GroupMessageResponsePacket;
import org.syh.demo.netty.chatapp.protocol.response.HeartBeatResponsePacket;
import org.syh.demo.netty.chatapp.protocol.response.ListGroupMembersResponsePacket;
import org.syh.demo.netty.chatapp.protocol.response.LoginResponsePacket;
import org.syh.demo.netty.chatapp.protocol.response.LogoutResponsePacket;
import org.syh.demo.netty.chatapp.protocol.response.MessageResponsePacket;
import org.syh.demo.netty.chatapp.protocol.response.MessageStatusResponsePacket;
import org.syh.demo.netty.chatapp.serialize.Serializer;
import org.syh.demo.netty.chatapp.serialize.impl.GSONSerializer;

import static org.syh.demo.netty.chatapp.protocol.command.Command.LOGIN_REQUEST;
import static org.syh.demo.netty.chatapp.protocol.command.Command.LOGIN_RESPONSE;
import static org.syh.demo.netty.chatapp.protocol.command.Command.MESSAGE_REQUEST;
import static org.syh.demo.netty.chatapp.protocol.command.Command.MESSAGE_RESPONSE;
import static org.syh.demo.netty.chatapp.protocol.command.Command.LOGOUT_REQUEST;
import static org.syh.demo.netty.chatapp.protocol.command.Command.LOGOUT_RESPONSE;
import static org.syh.demo.netty.chatapp.protocol.command.Command.CREATE_GROUP_REQUEST;
import static org.syh.demo.netty.chatapp.protocol.command.Command.CREATE_GROUP_RESPONSE;
import static org.syh.demo.netty.chatapp.protocol.command.Command.LIST_GROUP_MEMBERS_REQUEST;
import static org.syh.demo.netty.chatapp.protocol.command.Command.LIST_GROUP_MEMBERS_RESPONSE;
import static org.syh.demo.netty.chatapp.protocol.command.Command.JOIN_GROUP_REQUEST;
import static org.syh.demo.netty.chatapp.protocol.command.Command.JOIN_GROUP_RESPONSE;
import static org.syh.demo.netty.chatapp.protocol.command.Command.EXIT_GROUP_REQUEST;
import static org.syh.demo.netty.chatapp.protocol.command.Command.EXIT_GROUP_RESPONSE;
import static org.syh.demo.netty.chatapp.protocol.command.Command.GROUP_MESSAGE_REQUEST;
import static org.syh.demo.netty.chatapp.protocol.command.Command.GROUP_MESSAGE_RESPONSE;
import static org.syh.demo.netty.chatapp.protocol.command.Command.MESSAGE_STATUS_RESPONSE;
import static org.syh.demo.netty.chatapp.protocol.command.Command.HEARTBEAT_REQUEST;
import static org.syh.demo.netty.chatapp.protocol.command.Command.HEARTBEAT_RESPONSE;

import io.netty.buffer.ByteBuf;

public class PacketCodec {
    public static final PacketCodec INSTANCE = new PacketCodec();

    public static final int MAGIC_NUMBER = 0xFACADE;
    public static final int MAGIC_NUMBER_LENGTH = 4;
    public static final int VERSION_LENGTH = 1;

    private final Logger logger = LogManager.getLogger(PacketCodec.class);

    private final Map<Byte, Class<? extends Packet>> packetTypeMap;
    private final Map<Byte, Serializer> serializerMap;

    public PacketCodec() {
        packetTypeMap = new HashMap<>();
        packetTypeMap.put(LOGIN_REQUEST, LoginRequestPacket.class);
        packetTypeMap.put(LOGIN_RESPONSE, LoginResponsePacket.class);
        packetTypeMap.put(MESSAGE_REQUEST, MessageRequestPacket.class);
        packetTypeMap.put(MESSAGE_RESPONSE, MessageResponsePacket.class);
        packetTypeMap.put(LOGOUT_REQUEST, LogoutRequestPacket.class);
        packetTypeMap.put(LOGOUT_RESPONSE, LogoutResponsePacket.class);
        packetTypeMap.put(CREATE_GROUP_REQUEST, CreateGroupRequestPacket.class);
        packetTypeMap.put(CREATE_GROUP_RESPONSE, CreateGroupResponsePacket.class);
        packetTypeMap.put(LIST_GROUP_MEMBERS_REQUEST, ListGroupMembersRequestPacket.class);
        packetTypeMap.put(LIST_GROUP_MEMBERS_RESPONSE, ListGroupMembersResponsePacket.class);
        packetTypeMap.put(JOIN_GROUP_REQUEST, JoinGroupRequestPacket.class);
        packetTypeMap.put(JOIN_GROUP_RESPONSE, JoinGroupResponsePacket.class);
        packetTypeMap.put(EXIT_GROUP_REQUEST, ExitGroupRequestPacket.class);
        packetTypeMap.put(EXIT_GROUP_RESPONSE, ExitGroupResponsePacket.class);
        packetTypeMap.put(GROUP_MESSAGE_REQUEST, GroupMessageRequestPacket.class);
        packetTypeMap.put(GROUP_MESSAGE_RESPONSE, GroupMessageResponsePacket.class);
        packetTypeMap.put(MESSAGE_STATUS_RESPONSE, MessageStatusResponsePacket.class);
        packetTypeMap.put(HEARTBEAT_REQUEST, HeartBeatRequestPacket.class);
        packetTypeMap.put(HEARTBEAT_RESPONSE, HeartBeatResponsePacket.class);

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
            logger.debug("decode packet: {}, serializer: {}.", requestType, serializer);
            return serializer.deserialize(requestType, bytes);
        } else {
            logger.warn("Unknown packet type: {}, or unknown serializer: {}.", requestType, serializer);
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
