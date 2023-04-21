package org.syh.demo.netty.chatapp.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.syh.demo.netty.chatapp.codec.PacketCodecHandler;
import org.syh.demo.netty.chatapp.codec.Splitter;
import org.syh.demo.netty.chatapp.handler.IMIdleStateHandler;
import org.syh.demo.netty.chatapp.server.handler.AuthHandler;
import org.syh.demo.netty.chatapp.server.handler.HeartBeatRequestHandler;
import org.syh.demo.netty.chatapp.server.handler.IMHandler;
import org.syh.demo.netty.chatapp.server.handler.LoginRequestHandler;

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
                    ch.pipeline().addLast(new IMIdleStateHandler());
                    ch.pipeline().addLast(new Splitter());
                    ch.pipeline().addLast(PacketCodecHandler.INSTANCE);
                    ch.pipeline().addLast(LoginRequestHandler.INSTANCE);
                    ch.pipeline().addLast(HeartBeatRequestHandler.INSTANCE);
                    ch.pipeline().addLast(AuthHandler.INSTANCE);
                    ch.pipeline().addLast(IMHandler.INSTANCE);
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
