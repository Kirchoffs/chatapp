package org.syh.demo.netty.chatapp.client.console;

import java.util.Scanner;

import org.syh.demo.netty.chatapp.protocol.request.MessageRequestPacket;

import io.netty.channel.Channel;

public class SendToUserConsoleCommand implements ConsoleCommand {
    @Override
    public void exec(Scanner scanner, Channel channel) {
        System.out.print("Please enter the username of the person you want to talk to: ");
        String toUserId = scanner.nextLine();

        System.out.print("Please enter the message you want to send: ");
        String message = scanner.nextLine();

        channel.writeAndFlush(new MessageRequestPacket(toUserId, message));
    }
}
