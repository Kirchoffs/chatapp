package org.syh.demo.netty.chatapp.protocol.response;

import lombok.Data;

import org.syh.demo.netty.chatapp.protocol.Packet;
import org.syh.demo.netty.chatapp.session.Session;

import static org.syh.demo.netty.chatapp.protocol.command.Command.GROUP_MESSAGE_RESPONSE;

@Data
public class GroupMessageResponsePacket extends Packet {
    private String fromGroupId;
    private Session fromUser;
    private String message;

    @Override
    public Byte getCommand() {
        return GROUP_MESSAGE_RESPONSE;
    }
}
