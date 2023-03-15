package org.syh.demo.netty.chatapp.protocol.response;

import org.syh.demo.netty.chatapp.protocol.Packet;

import lombok.Data;

import static org.syh.demo.netty.chatapp.protocol.command.Command.LOGIN_RESPONSE;

@Data
public class LoginResponsePacket extends Packet {
    private String userId;
    private String userName;
    private boolean success;
    private String reason;

    @Override
    public Byte getCommand() {
        return LOGIN_RESPONSE;
    }
}
