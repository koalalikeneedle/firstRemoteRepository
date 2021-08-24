package com.zsj.chatSystem;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.text.SimpleDateFormat;

public class GruopChatHandler extends SimpleChannelInboundHandler {


    //定义一个channel组，管理所有的channel
    //GlobalEventExecutor.INSTANCE是单例模式生成的，是全局的事件执行器。
    private static ChannelGroup channelGroup =   new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);


    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    //一旦连接，该执行器第一个被执行
    //将该channel加入到channelGroup中
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();

        channelGroup.writeAndFlush("客户端"+channel.remoteAddress()+" 加入聊天\n");
        channelGroup.add(channel);


    }


    //表示channel 处于活动状态是，提示上线
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress()+ " 上线了");
    }


    // 表示channel 处于非活动状态，提示离线
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress()+" 离线了");
    }


    //断开连接,将离线客户端离开消息推送给所有在线客户
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        channelGroup.writeAndFlush("[客户端]" + ctx.channel().remoteAddress() + " 离开了\n");

        channelGroup.remove(ctx.channel());

        System.out.println("channelGroup 的size: "+ channelGroup.size()  );
    }


    //读取数据
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {

        final Channel channel = ctx.channel();

        //遍历channelGroup，根据不同情况，得到不同的结果
        channelGroup.forEach(ch ->
        {
         if(ch != channel){
             //不是当前客户
             ch.writeAndFlush("客户 "+ channel.remoteAddress() + "发送消息"+msg+"\n");
         }else{
             //发送消息给自己
             ch.writeAndFlush("自己发送了消息"+msg+"\n");
         }
        });
    }


    //发生异常
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
      //关闭
        ctx.close();
    }
}
