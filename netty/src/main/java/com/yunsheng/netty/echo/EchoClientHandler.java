package com.yunsheng.netty.echo;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

public class EchoClientHandler extends SimpleChannelInboundHandler {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // 建立链接后，向server发送请求
//        ctx.writeAndFlush(Unpooled.copiedBuffer("client msg!", CharsetUtil.UTF_8));
//        因为netty默认是讲byte数组解码编码成ByteBuf
//        如果想传输不同类型的数据,需要在netty启动类里添加响应的编解码器.
        ctx.writeAndFlush("发一个字符串");
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf in = (ByteBuf) msg;
        System.out.println("Client received : " + in.toString(CharsetUtil.UTF_8));
    }
}
