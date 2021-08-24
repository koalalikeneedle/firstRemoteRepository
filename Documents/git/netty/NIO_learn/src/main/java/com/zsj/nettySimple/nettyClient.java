package com.zsj.nettySimple;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class nettyClient {
    public static void main(String[] args) {

        NioEventLoopGroup clientGroup = new NioEventLoopGroup();

        //客户端使用bootstrap
        Bootstrap bootstrap = new Bootstrap();

        try{
            //设置相关参数，使用链式编程的方式
            bootstrap.group(clientGroup) //设置线程组
                    .channel(NioSocketChannel.class) // 设置客户端通道的实现类（反射）
                    .handler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new nettyClientHandler());
                        }
                    });

            System.out.println("客户端 ok..");

            //启动客户端去连接服务器端
            ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 6668).sync();

            //关闭通道进行监听
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            clientGroup.shutdownGracefully();
        }


    }
}
