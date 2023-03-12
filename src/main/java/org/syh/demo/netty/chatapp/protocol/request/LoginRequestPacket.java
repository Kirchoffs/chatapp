package org.syh.demo.netty.chatapp.protocol.request;

import org.syh.demo.netty.chatapp.protocol.Packet;
import lombok.Data;
import static org.syh.demo.netty.chatapp.protocol.command.Command.LOGIN_REQUEST;

@Data
public class LoginRequestPacket extends Packet {
    private String userId;

    private String username;

    private String password;

    @Override
    public Byte getCommand() {
        return LOGIN_REQUEST;
    }
}
