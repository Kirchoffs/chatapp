package org.syh.demo.netty.chatapp.protocol.response;

import lombok.Data;

import org.syh.demo.netty.chatapp.protocol.Packet;
import static org.syh.demo.netty.chatapp.protocol.command.Command.EXIT_GROUP_RESPONSE;;

@Data
public class ExitGroupResponsePacket extends Packet {
    private String groupId;
    private boolean success;
    private String reason;

    @Override
    public Byte getCommand() {
        return EXIT_GROUP_RESPONSE;
    }
}
