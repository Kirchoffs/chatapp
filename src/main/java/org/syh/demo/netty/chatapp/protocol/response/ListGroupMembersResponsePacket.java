package org.syh.demo.netty.chatapp.protocol.response;

import lombok.Data;

import java.util.List;

import org.syh.demo.netty.chatapp.protocol.Packet;
import org.syh.demo.netty.chatapp.session.Session;

import static org.syh.demo.netty.chatapp.protocol.command.Command.LIST_GROUP_MEMBERS_RESPONSE;

@Data
public class ListGroupMembersResponsePacket extends Packet {
    private String groupId;
    private List<Session> sessionList;

    @Override
    public Byte getCommand() {
        return LIST_GROUP_MEMBERS_RESPONSE;
    }
}
