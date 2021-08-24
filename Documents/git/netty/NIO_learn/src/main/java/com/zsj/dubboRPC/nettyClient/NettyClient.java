package com.zsj.dubboRPC.nettyClient;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;


import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NettyClient {



    //创建线程池

    private static ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    private static nettyClientHandler client ;

    //编写方法使用代理模式，获取一个代理对象
    public Object getBean(final Class<?> ServerClass , final String providerName){
        return Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class<?>[]{ServerClass}, (proxy,method,args)-> {
//            @Override
//            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
               if(client == null){
                   initClient();
               }

               //设置发送给服务器的消息
                client.setParam(providerName + args[0]);
                return executor.submit(client).get();
//            }
        });
    }

    //初始化客户端
    private static void initClient(){
        client = new nettyClientHandler();

        NioEventLoopGroup group = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY,true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new StringDecoder());
                        pipeline.addLast(new StringEncoder());
                        pipeline.addLast(client);
                    }
                });

        try {
            bootstrap.connect("127.0.0.1",9100).sync();
            //sync.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        }
//        } finally {
//            group.shutdownGracefully();
//        }
    }


}
