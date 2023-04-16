package org.syh.demo.netty.chatapp.protocol.response;

import lombok.Data;

import org.syh.demo.netty.chatapp.protocol.Packet;
import static org.syh.demo.netty.chatapp.protocol.command.Command.MESSAGE_RESPONSE;

@Data
public class MessageResponsePacket extends Packet {
    private String fromUserId;
    private String fromUserName;
    private String message;

    @Override
    public Byte getCommand() {
        return MESSAGE_RESPONSE;
    }
}
