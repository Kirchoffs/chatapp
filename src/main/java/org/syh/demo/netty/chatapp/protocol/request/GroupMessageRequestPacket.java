package org.syh.demo.netty.chatapp.protocol.request;

import lombok.Data;
import lombok.NoArgsConstructor;

import org.syh.demo.netty.chatapp.protocol.Packet;
import static org.syh.demo.netty.chatapp.protocol.command.Command.GROUP_MESSAGE_REQUEST;

@Data
@NoArgsConstructor
public class GroupMessageRequestPacket extends Packet {
    private String toGroupId;
    private String message;

    public GroupMessageRequestPacket(String toGroupId, String message) {
        this.toGroupId = toGroupId;
        this.message = message;
    }

    @Override
    public Byte getCommand() {
        return GROUP_MESSAGE_REQUEST;
    }
}
