package org.syh.demo.netty.chatapp.protocol.response;

import lombok.Data;

import org.syh.demo.netty.chatapp.protocol.Packet;
import static org.syh.demo.netty.chatapp.protocol.command.Command.JOIN_GROUP_RESPONSE;

@Data
public class JoinGroupResponsePacket extends Packet {
    private String groupId;
    private boolean success;
    private String reason;

    @Override
    public Byte getCommand() {
        return JOIN_GROUP_RESPONSE;
    }
}
