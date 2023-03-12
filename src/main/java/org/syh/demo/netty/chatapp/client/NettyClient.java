package org.syh.demo.netty.chatapp.client;

import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.syh.demo.netty.chatapp.client.handler.ClientHandler;
import org.syh.demo.netty.chatapp.util.Ordinal;

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
                    ch.pipeline().addLast(new ClientHandler());
                }
            });
            
        connect(bootstrap, HOST, PORT, MAX_RETRY);
    }

    private static void connect(Bootstrap bootstrap, String host, int port, int retry) {
        bootstrap.connect(host, port).addListener(future -> {
            if (future.isSuccess()) {
                logger.info("Connection established successfully!");
                Channel channel = ((ChannelFuture) future).channel();
            } else if (retry == 0) {
                logger.error("Unable to establish connection after {} attempts. Exiting program.", MAX_RETRY);
            } else {
                int order = (MAX_RETRY - retry) + 1;
                int delay = 1 << order;
                logger.warn("Connection attempt failed. Retrying for the {} time...", Ordinal.getOrdinal(retry));
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
}
