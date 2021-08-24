package com.zsj.nettySimple;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import jdk.nashorn.internal.ir.CallNode;

public class nettyServer {
    public static void main(String[] args) throws InterruptedException {

        /**
         * bossGroup只是处理连接请求，真正和客户端业务处理，会交给workerGroup完成
         * 两个都是无线循环，均为线程组。
         */
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();


        //创建服务器端的启动对象，配置参数
        ServerBootstrap bootstrap = new ServerBootstrap();

        //使用链式编程进行设置, 使用try finally用于关闭服务器端
        try{
            bootstrap.group(bossGroup,workerGroup) // 设置两个线程组
                    .channel(NioServerSocketChannel.class) //使用NioServerSocketChannel作为服务器的通道实现
                    .option(ChannelOption.SO_BACKLOG,128) //设置线程队列得到连接个数
                    .childOption(ChannelOption.SO_KEEPALIVE,true) // 设置保持活动连接状态
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        //为pipeline设置处理器
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new nettyServerHandler());
                        }
                    }); //给我们的workerGruop的Eventloop对应的管道设置处理器
            System.out.println(".......服务器 is ready 。。。。");

            //绑定一个端口并且同步，生成了一个ChannelFuture 对象
            //启动服务器并绑定端口
            ChannelFuture channelFuture = bootstrap.bind(6668).sync();

            //对关闭通道进行监听
            channelFuture.channel().closeFuture().sync();
        }finally {

            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }
}
