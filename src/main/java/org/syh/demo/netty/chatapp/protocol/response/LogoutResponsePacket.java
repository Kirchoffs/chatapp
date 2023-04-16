package org.syh.demo.netty.chatapp.protocol.response;

import lombok.Data;

import org.syh.demo.netty.chatapp.protocol.Packet;
import static org.syh.demo.netty.chatapp.protocol.command.Command.LOGOUT_RESPONSE;

@Data
public class LogoutResponsePacket extends Packet {
    private boolean success;
    private String reason;

    @Override
    public Byte getCommand() {
        return LOGOUT_RESPONSE;
    }
}
