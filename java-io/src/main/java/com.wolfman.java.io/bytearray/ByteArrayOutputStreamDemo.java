package com.wolfman.java.io.bytearray;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ByteArrayOutputStreamDemo {

    private static final int LEN = 5;

    // 对应英文字母“abcddefghijklmnopqrsttuvwxyz”
    private static final byte[] ArrayLetters = {0x61, 0x62, 0x63, 0x64, 0x65, 0x66, 0x67, 0x68, 0x69, 0x6A, 0x6B, 0x6C, 0x6D, 0x6E, 0x6F,
            0x70, 0x71, 0x72, 0x73, 0x74, 0x75, 0x76, 0x77, 0x78, 0x79, 0x7A};

    public static void main(String[] args) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(0x41);
        outputStream.write(0x42);
        outputStream.write(0x43);
        System.out.printf("outputStream=%s\n", outputStream);
        //输出结果：outputStream=ABC

        // 将ArrayLetters数组中从“3”开始的后5个字节写入到baos中。
        // 即对应写入“0x64, 0x65, 0x66, 0x67, 0x68”，即“defgh”
        outputStream.write(ArrayLetters,3,5);
        System.out.printf("outputStream=%s\n", outputStream);
        //输出结果：outputStream=ABCdefgh

        // 计算长度
        int size = outputStream.size();
        System.out.printf("size=%s\n",size);
        //输出结果：size=8

        // 转换成byte[]数组
        byte[] buf = outputStream.toByteArray();
        String str = new String(buf);
        System.out.printf("str=%s\n",str);

        // 将baos写入到另一个输出流中
        try{
            ByteArrayOutputStream outputStreamTwo = new ByteArrayOutputStream();
            outputStream.writeTo(outputStreamTwo);
            System.out.printf("outputStreamTwo=%s\n", outputStreamTwo);
            //输出结果：outputStreamTwo=ABCdefgh
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
