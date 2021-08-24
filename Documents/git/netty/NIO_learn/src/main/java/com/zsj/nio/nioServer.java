package com.zsj.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

public class nioServer {

    public static void main(String[] args) throws IOException {

        //创建ServerSocketChananel，用于后面产生ServerSocket
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        //得到一个selector对象
        Selector selector = Selector.open();

        //绑定一个端口为6666，在服务器端进行监听
        serverSocketChannel.socket().bind(new InetSocketAddress(6666));

        //将其设置为非阻塞
        serverSocketChannel.configureBlocking(false);

        //将serverSocketChannel注入到selector中并设置关心事件类型为OP_ACCEPT
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        //循环等待客户端连接
        while(true){

            //如果等待一秒后没有连接发生，则继续循环
            if(selector.select(1000) == 0){
                System.out.println("服务器等待了一秒后，没有连接");
                continue;
            }

            //如果返回的值大于0，可以获取到selectionKey集合
            //通过selectionKey集合反向获取通道
            Set<SelectionKey> selectionKeys = selector.selectedKeys();

            //遍历集合
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while(iterator.hasNext()){

                //获取对应的selectionKey
                SelectionKey key = iterator.next();

                //根据key 对通道发生的事件做相应的处理
                if(key.isAcceptable()){
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    System.out.println("客户端连接成功 生成了一个 socketChannel "+socketChannel.hashCode());

                    //将socketChannel设置为非阻塞模式
                    socketChannel.configureBlocking(false);
                    //注册到selector中
                    socketChannel.register(selector,SelectionKey.OP_READ, ByteBuffer.allocate(1024));

                }

                if(key.isReadable()){

                    SocketChannel channel = (SocketChannel)key.channel();
                    //获取到该channel关联的buffer
                    ByteBuffer attachment = (ByteBuffer)key.attachment();
                    channel.read(attachment);
                    System.out.println("from 客户端"+new String(attachment.array()));

                }
                iterator.remove();
            }
        }

    }
}
