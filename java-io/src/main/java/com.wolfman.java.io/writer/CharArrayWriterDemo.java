package com.wolfman.java.io.writer;

import java.io.CharArrayWriter;
import java.io.IOException;

public class CharArrayWriterDemo {

    private static final int Len = 5;

    private static final char[] ArrayLetters = new char[]{'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z'};

    public static void main(String[] args) {
        try{
            // 创建CharArrayWriter字符流
            CharArrayWriter writer = new CharArrayWriter();
            // 写入“A”个字符
            writer.write('A');
            // 写入字符串“BC”个字符
            writer.write("BC");
            System.out.printf("writer=%s\n", writer);
            //运行结果：writer=ABC

            // 将ArrayLetters数组中从“3”开始的后5个字符(defgh)写入到caw中。
            writer.write(ArrayLetters,3,5);
            System.out.printf("writer=%s\n", writer);
            //运行结果：writer=ABCdefgh

            // (01) 写入字符0
            // (02) 然后接着写入“123456789”
            // (03) 再接着写入ArrayLetters中第8-12个字符(ijkl)
            writer.append('0').append("123456789").append(String.valueOf(ArrayLetters), 8, 12);
            System.out.printf("writer=%s\n", writer);
            //运行结果：writer=ABCdefgh0123456789ijkl

            // 计算长度
            int size = writer.size();
            System.out.printf("size=%s\n", size);
            //运行结果：size=22

            // 转换成char[]数组
            char[] buf = writer.toCharArray();
            System.out.printf("buf=%s\n", String.valueOf(buf));
            //运行结果：buf=ABCdefgh0123456789ijkl

            // 将caw写入到另一个输出流中
            CharArrayWriter writer2 = new CharArrayWriter();
            writer.writeTo(writer2);
            System.out.printf("writer2=%s\n", writer2);
            //运行结果：writer2=ABCdefgh0123456789ijkl

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
