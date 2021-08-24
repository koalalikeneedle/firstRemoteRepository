package com.zsj.channelRead;



import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class fileReadChannel {

   public static void main(String[] args) throws IOException {
       File file = new File("/Users/apple/Documents/google/test.txt");
       FileInputStream fileInputStream = new FileInputStream(file);
       FileChannel channel = fileInputStream.getChannel();
       ByteBuffer byteBuffer = ByteBuffer.allocate((int) file.length());
       int read = channel.read(byteBuffer);
       System.out.println(new java.lang.String(byteBuffer.array()));
       fileInputStream.close();
   }
}
