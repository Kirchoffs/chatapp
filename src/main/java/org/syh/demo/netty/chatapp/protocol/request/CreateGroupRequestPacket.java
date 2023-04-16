package org.syh.demo.netty.chatapp.protocol.request;

import java.util.List;
import lombok.Data;
import org.syh.demo.netty.chatapp.protocol.Packet;
import static org.syh.demo.netty.chatapp.protocol.command.Command.CREATE_GROUP_REQUEST;

@Data
public class CreateGroupRequestPacket extends Packet {
    private List<String> userNameList;

    @Override
    public Byte getCommand() {
        return CREATE_GROUP_REQUEST;
    }
}
