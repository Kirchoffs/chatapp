package org.syh.demo.netty.chatapp.client.console;

import java.util.Scanner;

import org.syh.demo.netty.chatapp.protocol.request.ExitGroupRequestPacket;

import io.netty.channel.Channel;

public class ExitGroupConsoleCommand implements ConsoleCommand {
    @Override
    public void exec(Scanner scanner, Channel channel) {
        ExitGroupRequestPacket exitGroupRequestPacket = new ExitGroupRequestPacket();
        
        System.out.print("Input group ID to exit: ");
        String groupId = scanner.nextLine();

        exitGroupRequestPacket.setGroupId(groupId);
        channel.writeAndFlush(exitGroupRequestPacket);
    }
}
