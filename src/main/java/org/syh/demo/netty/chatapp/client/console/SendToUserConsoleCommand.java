package org.syh.demo.netty.chatapp.client.console;

import java.util.Scanner;

import org.syh.demo.netty.chatapp.protocol.request.MessageRequestPacket;

import io.netty.channel.Channel;

public class SendToUserConsoleCommand implements ConsoleCommand {
    @Override
    public void exec(Scanner scanner, Channel channel) {
        System.out.print("Enter user name: ");
        String toUserId = scanner.nextLine();

        System.out.print("Enter message: ");
        String message = scanner.nextLine();

        channel.writeAndFlush(new MessageRequestPacket(toUserId, message));
    }
}
