package com.yunsheng.netty.consumer;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Timer;

@Slf4j
public class Consumer {
    private final Bootstrap bootstrap;
    private final EventLoopGroup eventLoopGroup;

    public Consumer() {
        bootstrap = new Bootstrap();
        eventLoopGroup = new NioEventLoopGroup(4);
        bootstrap.group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline()
                                .addLast();
                    }
                });
        log.info("netty client start");


    }

    public void sendRequest() {
        log.info("===sendRequest===");

        // 建立链接
        ChannelFuture channelFuture = bootstrap.connect("10.190.24.54", 12580);
        channelFuture.addListener(future -> {
            if (channelFuture.isSuccess()) {
                log.info("connect rpc server {} on port {} success");
            } else {
                log.error("connect rpc server {} on port {} fail");
                channelFuture.cause().printStackTrace();
                eventLoopGroup.shutdownGracefully();
            }
            // 发送请求
            channelFuture.channel().writeAndFlush("hello");
        });

    }

    public static void main(String[] args) {
        Consumer consumer = new Consumer();
        consumer.sendRequest();
    }
}
