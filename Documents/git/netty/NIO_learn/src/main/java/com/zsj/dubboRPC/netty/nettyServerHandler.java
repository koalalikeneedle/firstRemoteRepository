package com.zsj.dubboRPC.netty;

import com.zsj.dubboRPC.Consumer.ClientBootstrap;
import com.zsj.dubboRPC.provider.helloServiceInterfaceIml;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandler;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class nettyServerHandler extends ChannelInboundHandlerAdapter {




    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        //获取到客户端发送的消息，并调用服务
        System.out.println("msg = " + msg.toString());

        if(msg.toString().startsWith(ClientBootstrap.providerName)){
            String result = new helloServiceInterfaceIml().hello(msg.toString().substring(msg.toString().lastIndexOf("#")+1));
            ctx.writeAndFlush(result);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
