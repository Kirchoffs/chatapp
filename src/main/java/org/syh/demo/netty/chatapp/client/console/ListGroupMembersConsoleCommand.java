package org.syh.demo.netty.chatapp.client.console;

import java.util.Scanner;

import org.syh.demo.netty.chatapp.protocol.request.ListGroupMembersRequestPacket;

import io.netty.channel.Channel;

public class ListGroupMembersConsoleCommand implements ConsoleCommand {
    @Override
    public void exec(Scanner scanner, Channel channel) {
        ListGroupMembersRequestPacket listGroupMembersRequestPacket = new ListGroupMembersRequestPacket();
        
        System.out.print("Please enter group ID: ");
        String groupId = scanner.nextLine();

        listGroupMembersRequestPacket.setGroupId(groupId);
        channel.writeAndFlush(listGroupMembersRequestPacket);
    }
}
