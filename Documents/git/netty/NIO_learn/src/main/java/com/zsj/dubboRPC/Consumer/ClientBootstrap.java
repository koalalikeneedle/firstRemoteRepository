package com.zsj.dubboRPC.Consumer;

import com.zsj.dubboRPC.nettyClient.NettyClient;
import com.zsj.dubboRPC.publicInterface.helloInterface;

public class ClientBootstrap {


    public static  final  String providerName = "helloService#hello#";

    public static void main(String[] args) {
        NettyClient nettyClient = new NettyClient();
        helloInterface client = (helloInterface)nettyClient.getBean(helloInterface.class, providerName);

        String hello = client.hello("你好，dubbo~");
        System.out.println(hello);

    }
}
