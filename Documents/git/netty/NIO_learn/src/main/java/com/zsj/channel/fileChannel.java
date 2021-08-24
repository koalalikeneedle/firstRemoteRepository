package com.zsj.channel;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class fileChannel {

    public static void main(String[] args) throws IOException {
        String s = "zsj测试channel";

        FileOutputStream fileOutputStream = null;

        try {
            fileOutputStream = new FileOutputStream("/Users/apple/Documents/google/test.txt");
            FileChannel channel = fileOutputStream.getChannel();
            ByteBuffer buffer = ByteBuffer.allocate(1024);

            buffer.put(s.getBytes());
            buffer.flip();
            channel.write(buffer);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            fileOutputStream.close();
        }

    }
}
