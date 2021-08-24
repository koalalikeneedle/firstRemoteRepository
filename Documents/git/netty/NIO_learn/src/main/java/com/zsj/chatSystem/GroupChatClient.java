package com.zsj.chatSystem;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.security.acl.Group;
import java.util.Scanner;

public class GroupChatClient {


    //属性

    private final  String host;

    private final int port;


    public GroupChatClient(String host , int port){
        this.host = host;
        this.port = port;
    }

    public void run(){
        NioEventLoopGroup group = new NioEventLoopGroup();

        try {
            Bootstrap bootstrap = new Bootstrap()
                    .group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {

                            //得到pipeLine
                            ChannelPipeline pipeline = ch.pipeline();

                            //加入相关的handler
                            pipeline.addLast("decoder",new StringDecoder());
                            pipeline.addLast("encoder",new StringEncoder());

                            //加入自定义的handler。
                            pipeline.addLast(new GroupChatClientHandler());
                        }
                    });

            ChannelFuture channelFuture = bootstrap.connect(host, port).sync();
            //提示信息
            System.out.println(channelFuture.channel().localAddress()+" 本地地址");
            Channel channel = channelFuture.channel();
            //客户端需要输入消息
            Scanner scanner = new Scanner(System.in);
            while(scanner.hasNextLine()){
                String msg = scanner.nextLine();
                //通过channel发送到服务器端
                channel.writeAndFlush(msg+ "\r\n");
            }

            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {

            group.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        //客户端启动
        new GroupChatClient("127.0.0.1",7000).run();
    }

}
