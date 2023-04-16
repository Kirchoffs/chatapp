package org.syh.demo.netty.chatapp.protocol.request;

import lombok.Data;
import org.syh.demo.netty.chatapp.protocol.Packet;
import static org.syh.demo.netty.chatapp.protocol.command.Command.MESSAGE_REQUEST;

@Data
public class MessageRequestPacket extends Packet {
    private String toUserName;
    private String message;

    public MessageRequestPacket(String toUserName, String message) {
        this.toUserName = toUserName;
        this.message = message;
    }

    @Override
    public Byte getCommand() {
        return MESSAGE_REQUEST;
    }
}
