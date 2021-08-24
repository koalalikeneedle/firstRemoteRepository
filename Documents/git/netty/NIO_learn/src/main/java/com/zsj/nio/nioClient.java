package com.zsj.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class nioClient {
    public static void main(String[] args) {

        //得到一个网络通道
        try {
            SocketChannel socketChannel = SocketChannel.open();

            //设置非阻塞
            socketChannel.configureBlocking(false);

            //提供服务器端的ip和端口
            InetSocketAddress inetSocketAddress = new InetSocketAddress("127.0.0.1", 6666);

            //连接服务器

            if( !socketChannel.connect(inetSocketAddress)){
                //没有连接成功
                while( !socketChannel.finishConnect()){
                    System.out.println("连接需要时间，客户端不会阻塞，可以做其他的工作");
                }
            }
            //连接成功，发送数据
            String s = " hello , 服务器";

            //将数据以字节数组的方式写入到buffer中
            ByteBuffer wrap = ByteBuffer.wrap(s.getBytes());
            //发送数据，即将buffer中的数据写入到channel中
            socketChannel.write(wrap);
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
