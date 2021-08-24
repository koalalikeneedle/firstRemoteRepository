package com.zsj.dubboRPC.nettyClient;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.Callable;

public class nettyClientHandler extends ChannelInboundHandlerAdapter implements Callable {


    private ChannelHandlerContext context; //上下文

    private String result;  //返回的结果

    private String param; //客户端调用方法时传入的参数

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        context = ctx;
    }

    @Override
    public synchronized void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("开始接收数据");
        result = msg.toString();
        notify();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }


    //会被代理对象调用，发送数据给服务器端，-》wait,等待被唤醒，返回结果
    @Override
    public synchronized Object call() throws Exception {
        System.out.println("开始发送数据");
        context.writeAndFlush(param);
        wait();
        return result;
    }

    void setParam(String param){
        System.out.println(param);
        this.param = param;
    }
}
