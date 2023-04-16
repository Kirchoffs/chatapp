package org.syh.demo.netty.chatapp.protocol.request;

import org.syh.demo.netty.chatapp.protocol.Packet;

import lombok.Data;

import static org.syh.demo.netty.chatapp.protocol.command.Command.JOIN_GROUP_REQUEST;

@Data
public class JoinGroupRequestPacket extends Packet {
    private String groupId;

    @Override
    public Byte getCommand() {
        return JOIN_GROUP_REQUEST;
    }
}   
