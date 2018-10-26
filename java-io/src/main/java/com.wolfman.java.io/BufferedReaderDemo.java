package com.wolfman.java.io;

import java.io.*;

public class BufferedReaderDemo {

    private static final int Len = 5;

    public static void main(String[] args) {

        // 创建BufferedReader字符流，内容是ArrayLetters数组
        File file = new File("d://bufferedreader.txt");
        try {

            BufferedReader in = new BufferedReader(new FileReader(file));
            // 从字符流中读取5个字符。“abcde”
            for (int i = 0; i < 5; i++) {
                // 若能继续读取下一个字符，则读取下一个字符
                if (in.ready()){
                    // 读取“字符流的下一个字符”
                    int tmp = in.read();
                    System.out.printf("%d : %c\n", i, tmp);
                }
            }

            // 若“该字符流”不支持标记功能，则直接退出
            if (!in.markSupported()){
                System.out.println("make not supported!");
                return;
            }

            // 标记“当前索引位置”，即标记第6个位置的元素--“f”
            // 1024对应marklimit
            in.mark(1024);
            // 跳过22个字符。
            in.skip(22);

            // 读取5个字符
            char[] buf = new char[Len];
            in.read(buf, 0, Len);
            System.out.printf("buf=%s\n", String.valueOf(buf));
            // 读取该行剩余的数据
            System.out.printf("readLine=%s\n", in.readLine());

            // 重置“输入流的索引”为mark()所标记的位置，即重置到“f”处。
            in.reset();
            // 从“重置后的字符流”中读取5个字符到buf中。即读取“fghij”
            in.read(buf, 0, Len);
            System.out.printf("buf=%s\n", String.valueOf(buf));

            in.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
