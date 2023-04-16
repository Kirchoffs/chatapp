package org.syh.demo.netty.chatapp.protocol.request;

import lombok.Data;

import org.syh.demo.netty.chatapp.protocol.Packet;
import static org.syh.demo.netty.chatapp.protocol.command.Command.EXIT_GROUP_REQUEST;

@Data
public class ExitGroupRequestPacket extends Packet {
    private String groupId;

    @Override
    public Byte getCommand() {
        return EXIT_GROUP_REQUEST;
    }
}
