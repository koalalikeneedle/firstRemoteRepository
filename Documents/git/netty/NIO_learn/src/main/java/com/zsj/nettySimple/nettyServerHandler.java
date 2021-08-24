package com.zsj.nettySimple;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

import java.nio.ByteBuffer;


public class nettyServerHandler  extends ChannelInboundHandlerAdapter {

    /**
     *读取客户端发送的消息
     * @param ctx 上下文对象，含有管道 pipeline, 通道channel
     * @param msg 客户端发送的对象，默认为object;
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("server ctx = "+ctx);

        //将msg转换为一个ByteBuf
        //ByteBuf是Netty提供的，不是NIO的ByteBuffer
        ByteBuf buf = (ByteBuf) msg;
        System.out.println("客户端发送消息是" + buf.toString(CharsetUtil.UTF_8));
        System.out.println("客户端地址是"+ctx.channel().remoteAddress());
    }

    /**
     * 数据读取完毕
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {

        /**
         * ctx的writeAndFlush是 write+flush
         * 将数据写入到缓存中，并刷新
         * 一般需要对发送的数据进行编码
         */
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello, 客户端～", CharsetUtil.UTF_8));
    }


    //处理异常，一般需要关闭通道

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {

        ctx.channel().close();
    }
}
