package org.syh.demo.netty.chatapp.client.console;

import io.netty.channel.Channel;

import java.util.Scanner;

import org.syh.demo.netty.chatapp.protocol.request.GroupMessageRequestPacket;

public class SendToGroupConsoleCommand implements ConsoleCommand {
    @Override
    public void exec(Scanner scanner, Channel channel) {
        System.out.print("Enter group ID: ");
        String toGroupId = scanner.nextLine();

        System.out.print("Enter message: ");
        String message = scanner.nextLine();

        channel.writeAndFlush(new GroupMessageRequestPacket(toGroupId, message));
    }
}
