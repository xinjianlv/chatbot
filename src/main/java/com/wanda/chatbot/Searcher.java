package com.wanda.chatbot;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.lang.management.ManagementFactory;

public class Searcher {

    public static String pid = "";
	public static void main(String[] args) throws InterruptedException {
        pid = ManagementFactory.getRuntimeMXBean().getName().split("@")[0];
		EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
		ServerBootstrap b = new ServerBootstrap();
        b.group(bossGroup, workerGroup)
        .channel(NioServerSocketChannel.class)
        .option(ChannelOption.SO_BACKLOG, 128)
        .handler(new LoggingHandler(LogLevel.TRACE))
        .childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            public void initChannel(SocketChannel ch) throws Exception {
                ChannelPipeline p = ch.pipeline();
                p.addLast("http-decoder", new HttpRequestDecoder());
                p.addLast("http-aggregator", new HttpObjectAggregator(65535));
                p.addLast("http-encoder", new HttpResponseEncoder());
                p.addLast("handler", new HttpServerInboundHandler());
            }
        });
        ChannelFuture f = b.bind("127.0.0.1", 11765).sync();
//        ChannelFuture f = b.bind("10.209.20.198", 12765).sync();
        f.channel().closeFuture().sync();
	}
}
