package com.yunsheng.netty.httpserver;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpContentCompressor;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;

import java.net.InetSocketAddress;

public class HttpServer {
    public static void main(String[] args) {
        try {
            new HttpServer().start(8090);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void start(int port) throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        // 使用主从多线程模型
        try {
            serverBootstrap.group(bossGroup, workerGroup)
                    // 设置Channel为NIOChannel
                    .channel(NioServerSocketChannel.class)
                    .localAddress(new InetSocketAddress(port))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            // 注意顺序
                            socketChannel.pipeline()
                                    // 编解码处理器
                                    .addLast("codec", new HttpServerCodec())
                                    // 压缩处理器
                                    .addLast("compressor", new HttpContentCompressor())
                                    // 消息聚合处理器
                                    .addLast("aggregator", new HttpObjectAggregator(65536))
                                    // 自定义处理器
                                    .addLast("handler", new HttpServerHandler());
                        }
                    })
                    //        ServerBootstrap 设置 Channel 属性有option和childOption两个方法，option 主要负责设置 Boss 线程组，而 childOption 对应的是 Worker 线程组。
                    .childOption(ChannelOption.SO_KEEPALIVE, true);
            // bind() 方法会真正触发启动
//        ChannelFuture bind = serverBootstrap.bind();
            // sync() 方法则会阻塞，直至整个启动过程完成
//        ChannelFuture channelFuture = bind.sync();
            // 一般连起来写
            ChannelFuture future = serverBootstrap.bind().sync();
            System.out.println("Http Server started， Listening on " + port);
            // 让线程进入 wait 状态，这样服务端可以一直处于运行状态，如果没有这行代码，bind 操作之后就会进入 finally 代码块，整个服务端就退出结束了。
            future.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }


    }
}
