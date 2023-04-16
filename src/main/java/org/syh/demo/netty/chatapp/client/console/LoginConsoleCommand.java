package org.syh.demo.netty.chatapp.client.console;

import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.syh.demo.netty.chatapp.protocol.request.LoginRequestPacket;
import org.syh.demo.netty.chatapp.util.LoginUtil;

import io.netty.channel.Channel;

public class LoginConsoleCommand implements ConsoleCommand {
    private final Logger logger = LogManager.getLogger(LoginConsoleCommand.class);
    private Object loginLock;

    public LoginConsoleCommand(Object loginLock) {
        this.loginLock = loginLock;
    }

    @Override
    public void exec(Scanner scanner, Channel channel) {
        LoginRequestPacket loginRequestPacket = new LoginRequestPacket();

        System.out.print("Please enter your username: ");
        loginRequestPacket.setUsername(scanner.nextLine());

        System.out.print("Please enter your password: ");
        loginRequestPacket.setPassword(scanner.nextLine());

        channel.writeAndFlush(loginRequestPacket);

        synchronized (loginLock) {
            while (!LoginUtil.hasLoginUpdate(channel)) {
                try {
                    loginLock.wait();
                } catch (InterruptedException e) {
                    logger.warn(e.getMessage());
                }
            }
            LoginUtil.unsetLoginUpdate(channel);
        }
    }
}
