package org.syh.demo.netty.chatapp.client.console;

import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.syh.demo.netty.chatapp.protocol.request.LogoutRequestPacket;
import org.syh.demo.netty.chatapp.util.LoginUtil;

import io.netty.channel.Channel;

public class LogoutConsoleCommand implements ConsoleCommand {
    private final Logger logger = LogManager.getLogger(LogoutConsoleCommand.class);
    private Object loginLock;

    public LogoutConsoleCommand(Object loginLock) {
        this.loginLock = loginLock;
    }

    @Override
    public void exec(Scanner scanner, Channel channel) {
        logger.debug("Send logout request");
        channel.writeAndFlush(new LogoutRequestPacket());

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
