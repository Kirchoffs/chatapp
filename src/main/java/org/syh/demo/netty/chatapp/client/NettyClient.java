package org.syh.demo.netty.chatapp.client;

import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.syh.demo.netty.chatapp.client.console.ConsoleCommandManager;
import org.syh.demo.netty.chatapp.client.console.LoginConsoleCommand;
import org.syh.demo.netty.chatapp.client.handler.CreateGroupResponseHandler;
import org.syh.demo.netty.chatapp.client.handler.ExitGroupResponseHandler;
import org.syh.demo.netty.chatapp.client.handler.GroupMessageResponseHandler;
import org.syh.demo.netty.chatapp.client.handler.HeartBeatTimerHandler;
import org.syh.demo.netty.chatapp.client.handler.JoinGroupResponseHandler;
import org.syh.demo.netty.chatapp.client.handler.ListGroupMembersResponseHandler;
import org.syh.demo.netty.chatapp.client.handler.LoginResponseHandler;
import org.syh.demo.netty.chatapp.client.handler.LogoutResponseHandler;
import org.syh.demo.netty.chatapp.client.handler.MessageResponseHandler;
import org.syh.demo.netty.chatapp.client.handler.MessageStatusResponseHandler;
import org.syh.demo.netty.chatapp.codec.PacketDecoder;
import org.syh.demo.netty.chatapp.codec.PacketEncoder;
import org.syh.demo.netty.chatapp.codec.Splitter;
import org.syh.demo.netty.chatapp.handler.IMIdleStateHandler;
import org.syh.demo.netty.chatapp.util.LoginUtil;
import org.syh.demo.netty.chatapp.util.OrdinalUtil;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class NettyClient {
    private static final int MAX_RETRY = 5;
    private static final String HOST = "127.0.0.1";
    private static final int PORT = 8000;
    private static final int CONNECT_TIMEOUT_MILLIS = 5000;

    private static final Logger logger = LogManager.getLogger(NettyClient.class);
    private static Object loginLock = new Object();

    public static void main(String[] args) {
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        Bootstrap bootstrap = new Bootstrap();
        bootstrap
            .group(workerGroup)
            .channel(NioSocketChannel.class)
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, CONNECT_TIMEOUT_MILLIS)
            .option(ChannelOption.SO_KEEPALIVE, true)
            .option(ChannelOption.TCP_NODELAY, true)
            .handler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) {
                    ch.pipeline().addLast(new IMIdleStateHandler());
                    ch.pipeline().addLast(new Splitter());
                    ch.pipeline().addLast(new PacketDecoder());
                    ch.pipeline().addLast(new LoginResponseHandler(loginLock));
                    ch.pipeline().addLast(new LogoutResponseHandler(loginLock));
                    ch.pipeline().addLast(new CreateGroupResponseHandler());
                    ch.pipeline().addLast(new JoinGroupResponseHandler());
                    ch.pipeline().addLast(new ExitGroupResponseHandler());
                    ch.pipeline().addLast(new ListGroupMembersResponseHandler());
                    ch.pipeline().addLast(new GroupMessageResponseHandler());
                    ch.pipeline().addLast(new MessageResponseHandler());
                    ch.pipeline().addLast(new MessageStatusResponseHandler());
                    ch.pipeline().addLast(new PacketEncoder());
                    ch.pipeline().addLast(new HeartBeatTimerHandler());
                }
            });
            
        connect(bootstrap, HOST, PORT, MAX_RETRY);
    }

    private static void connect(Bootstrap bootstrap, String host, int port, int retry) {
        bootstrap.connect(host, port).addListener(future -> {
            if (future.isSuccess()) {
                logger.info("Connection established successfully!");
                Channel channel = ((ChannelFuture) future).channel();
                startConsoleThread(channel);
            } else if (retry == 0) {
                logger.error("Unable to establish connection after {} attempts. Exiting program.", MAX_RETRY);
            } else {
                int order = MAX_RETRY - retry + 1;
                int delay = 1 << order;
                logger.warn("Connection attempt failed. Retrying for the {} time...", OrdinalUtil.getOrdinal(MAX_RETRY - retry + 1));
                bootstrap
                    .config()
                    .group()
                    .schedule(
                        () -> connect(bootstrap, host, port, retry - 1), 
                        delay, 
                        TimeUnit.SECONDS
                    );
            }
        });
    }

    private static void startConsoleThread(Channel channel) {
        ConsoleCommandManager consoleCommandManager = new ConsoleCommandManager(loginLock);
        LoginConsoleCommand loginConsoleCommand = new LoginConsoleCommand(loginLock);
        
        Scanner scanner = new Scanner(System.in);
        new Thread(() -> {
            while (!Thread.interrupted()) {
                if (LoginUtil.hasLogin(channel)) {
                    consoleCommandManager.exec(scanner, channel);
                } else {
                    loginConsoleCommand.exec(scanner, channel);
                }
            }
            scanner.close();
        }).start();
    }
}
