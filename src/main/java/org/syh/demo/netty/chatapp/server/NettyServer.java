package org.syh.demo.netty.chatapp.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.syh.demo.netty.chatapp.codec.PacketDecoder;
import org.syh.demo.netty.chatapp.codec.PacketEncoder;
import org.syh.demo.netty.chatapp.server.handler.LoginRequestHandler;
import org.syh.demo.netty.chatapp.server.handler.MessageRequestHandler;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class NettyServer {
    private static final Logger logger = LogManager.getLogger(NettyServer.class);
    
    private static final int PORT = 8000;

    public static void main(String[] args) {
        NioEventLoopGroup boosGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap
            .group(boosGroup, workerGroup)
            .channel(NioServerSocketChannel.class)
            .option(ChannelOption.SO_BACKLOG, 1024)
            .childOption(ChannelOption.SO_KEEPALIVE, true)
            .childOption(ChannelOption.TCP_NODELAY, true)
            .childHandler(new ChannelInitializer<NioSocketChannel>() {
                protected void initChannel(NioSocketChannel ch) {
                    ch.pipeline().addLast(new PacketDecoder());
                    ch.pipeline().addLast(new LoginRequestHandler());
                    ch.pipeline().addLast(new MessageRequestHandler());
                    ch.pipeline().addLast(new PacketEncoder());
                }
            });
        
        bind(serverBootstrap, PORT);
    }

    private static void bind(final ServerBootstrap serverBootstrap, final int port) {
        serverBootstrap.bind(port).addListener(future -> {
            if (future.isSuccess()) {
                logger.info("Server successfully bound to port {}", port);
            } else {
                logger.error("Failed to bind server to port {}", port);
            }
        });
    }
}
