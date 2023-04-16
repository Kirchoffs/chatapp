package org.syh.demo.netty.chatapp.client.console;

import java.util.Scanner;

import org.syh.demo.netty.chatapp.protocol.request.JoinGroupRequestPacket;

import io.netty.channel.Channel;

public class JoinGroupConsoleCommand implements ConsoleCommand {
    @Override
    public void exec(Scanner scanner, Channel channel) {
        JoinGroupRequestPacket joinGroupRequestPacket = new JoinGroupRequestPacket();

        System.out.print("Input group ID to join: ");
        String groupId = scanner.nextLine();

        joinGroupRequestPacket.setGroupId(groupId);
        channel.writeAndFlush(joinGroupRequestPacket);
    }
}
