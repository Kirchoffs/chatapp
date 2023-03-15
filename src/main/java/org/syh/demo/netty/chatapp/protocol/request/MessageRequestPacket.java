package org.syh.demo.netty.chatapp.protocol.request;
import static org.syh.demo.netty.chatapp.protocol.command.Command.MESSAGE_REQUEST;

import org.syh.demo.netty.chatapp.protocol.Packet;

import lombok.Data;

@Data
public class MessageRequestPacket extends Packet {
    private String message;

    @Override
    public Byte getCommand() {
        return MESSAGE_REQUEST;
    }
}
