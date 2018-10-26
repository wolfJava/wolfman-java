package com.wolfman.java.io.reader;

import java.io.CharArrayReader;
import java.io.IOException;

/**
 * 字符输入流
 */
public class CharArrayReaderDemo {

    private static final int Len = 5;

    private static final char[] ArrayLetters = new char[]{'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z'};

    public static void main(String[] args) {
        // 创建CharArrayReader字符流，内容是ArrayLetters数组
        CharArrayReader chars = new CharArrayReader(ArrayLetters);
        try {
            // 从字符数组流中读取5个字符
            for (int i = 0; i < Len; i++) {
                // 若能继续读取下一个字符，则读取下一个字符
                if (chars.ready()){
                    // 读取“字符流的下一个字符”
                    char tmp = (char)chars.read();
                    System.out.printf("%d : %c\n", i, tmp);
                    //运行结果：
                    //0 : a
                    //1 : b
                    //2 : c
                    //3 : d
                    //4 : e
                }
            }
            // 若“该字符流”不支持标记功能，则直接退出
            if (!chars.markSupported()){
                System.out.println("make not supported!");
                return;
            }

            // 标记“字符流中下一个被读取的位置”。即--标记“f”，因为因为前面已经读取了5个字符，所以下一个被读取的位置是第6个字符”
            // (01), CharArrayReader类的mark(0)函数中的“参数0”是没有实际意义的。
            // (02), mark()与reset()是配套的，reset()会将“字符流中下一个被读取的位置”重置为“mark()中所保存的位置”
            chars.mark(0);

            // 跳过5个字符。跳过5个字符后，字符流中下一个被读取的值应该是“k”。
            chars.skip(5);

            // 从字符流中读取5个数据。即读取“klmno”
            char[] buf = new char[5];
            chars.read(buf,0,Len);
            System.out.printf("buf=%s\n", String.valueOf(buf));
            //运行结果：buf=klmno

            // 重置“字符流”：即，将“字符流中下一个被读取的位置”重置到“mark()所标记的位置”，即f。
            chars.reset();

            // 从“重置后的字符流”中读取5个字符到buf中。即读取“fghij”
            chars.read(buf,0,Len);
            System.out.printf("buf=%s\n", String.valueOf(buf));
            //运行结果：buf=fghij

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
