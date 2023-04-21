package org.syh.demo.netty.chatapp.client.console;

import io.netty.channel.Channel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import org.syh.demo.netty.chatapp.protocol.request.CreateGroupRequestPacket;
import org.syh.demo.netty.chatapp.util.LoginUtil;

public class CreateGroupConsoleCommand implements ConsoleCommand {
    private static final String USER_NAME_SPLITER = ",";

    @Override
    public void exec(Scanner scanner, Channel channel) {
        CreateGroupRequestPacket createGroupRequestPacket = new CreateGroupRequestPacket();

        System.out.print("[Group Chat] Input username list, separated by commas: ");
        String userNameStr = scanner.nextLine();
        Set<String> userNameSet = new HashSet<>();
        for (String name: userNameStr.split(USER_NAME_SPLITER)) {
            if (!name.strip().isEmpty()) {
                userNameSet.add(name);
            }
        }
        userNameSet.add(LoginUtil.getLoginName(channel));
        createGroupRequestPacket.setUserNameList(new ArrayList<>(userNameSet));
        channel.writeAndFlush(createGroupRequestPacket);
    }   
}
