package org.syh.demo.netty.chatapp.protocol.request;

import org.syh.demo.netty.chatapp.protocol.Packet;
import static org.syh.demo.netty.chatapp.protocol.command.Command.LOGOUT_REQUEST;

public class LogoutRequestPacket extends Packet{
    @Override
    public Byte getCommand() {
        return LOGOUT_REQUEST;
    }
}
