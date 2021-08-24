package com.zsj.buffer;

import java.nio.IntBuffer;

public class simpleDemo {

    public static void main(String[] args) {

        /**
         * buffer是一个容器对象，底层是一个数组
         */
        IntBuffer intBuffer = IntBuffer.allocate(5);

        for(int i = 0; i < intBuffer.capacity();i ++){
            intBuffer.put(i*2);
        }

        intBuffer.flip();

        while(intBuffer.hasRemaining()){
            System.out.println(intBuffer.get());
        }
    }
}
