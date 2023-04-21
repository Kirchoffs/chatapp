package org.syh.demo.netty.chatapp.protocol.response;

import org.syh.demo.netty.chatapp.protocol.Packet;

import lombok.Data;

import static org.syh.demo.netty.chatapp.protocol.command.Command.MESSAGE_STATUS_RESPONSE;

@Data
public class MessageStatusResponsePacket extends Packet {
    private String fromUserName;
    private String toUserName;
    private String toGroupId;
    private boolean success;
    private String reason;

    @Override
    public Byte getCommand() {
        return MESSAGE_STATUS_RESPONSE;
    }
}
