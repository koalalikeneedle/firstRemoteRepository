package com.zsj.dubboRPC.provider;


import com.zsj.dubboRPC.netty.nettyServer;

//提供服务提供者，即nettyServer
public class Serverbootstrap {


    public static void main(String[] args) {
        nettyServer.startServer("127.0.0.1",9100);

    }
}
