package com.zsj.dubboRPC.provider;

import com.zsj.dubboRPC.publicInterface.helloInterface;

public class helloServiceInterfaceIml implements helloInterface {

    @Override
    public String hello(String msg) {

        System.out.println("服务端收到消息："+msg);
       if(msg != null){
           return"你好,客户端,我已收到你的消息:["+msg+"]";
       }else{
           return "你好，客户端" ;
       }

    }
}
