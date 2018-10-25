package com.wolfman.java.io.filter.buffered;

import java.io.*;

/**
 * BufferedInputStream 测试程序
 *
 * @author skywang
 */
public class BufferedInputStreamTest {

    private static final int LEN = 26;

    public static void main(String[] args) {
        testBufferedInputStream() ;
    }
    /**
     * BufferedInputStream的API测试函数
     */
    private static void testBufferedInputStream() {

        // 创建BufferedInputStream字节流，内容是ArrayLetters数组
        try {
            File file = new File("file.txt");
            InputStream in =
                    new BufferedInputStream(
                            new FileInputStream(file), 512);

            // 从字节流中读取5个字节。“abcde”，a对应0x61，b对应0x62，依次类推...
            for (int i=0; i<LEN; i++) {
                // 若能继续读取下一个字节，则读取下一个字节
                if (in.available() >= 0) {
                    // 读取“字节流的下一个字节”
                    int tmp = in.read();
                    System.out.printf("%d : 0x%s\n", i, Integer.toHexString(tmp));
                    //运行结果：
                    //0 : 0x61
                    //1 : 0x62
                    //2 : 0x63
                    //3 : 0x64
                    //4 : 0x65
                }
            }

            // 若“该字节流”不支持标记功能，则直接退出
            if (!in.markSupported()) {
                System.out.println("make not supported!");
                return ;
            }

            // 标记“当前索引位置”，即标记第6个位置的元素--“f”
            // 1024对应marklimit
            in.mark(1024);

            // 跳过22个字节。
            in.skip(22);

            // 读取5个字节
            byte[] buf = new byte[LEN];
            in.read(buf, 0, LEN);
            // 将buf转换为String字符串。
            String str1 = new String(buf);
            System.out.printf("str1=%s\n", str1);
            //运行结果：str1=12345

            // 重置“输入流的索引”为mark()所标记的位置，即重置到“f”处。
            in.reset();
            // 从“重置后的字节流”中读取5个字节到buf中。即读取“fghij”
            in.read(buf, 0, LEN);
            // 将buf转换为String字符串。
            String str2 = new String(buf);
            System.out.printf("str2=%s\n", str2);
            //运行结果：str2=fghij

            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}