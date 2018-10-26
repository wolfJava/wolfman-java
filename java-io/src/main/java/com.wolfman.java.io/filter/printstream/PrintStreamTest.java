package com.wolfman.java.io.filter.printstream;

import java.io.*;

public class PrintStreamTest {

    public static void main(String[] args) {
        // 下面3个函数的作用都是一样：都是将字母“abcde”写入到文件“file.txt”中。
        testPrintStreamConstrutor1();
        testPrintStreamConstrutor2();
        testPrintStreamConstrutor3();
        //运行结果：abcde

        // 测试write(), print(), println(), printf()等接口。
        testPrintStreamAPIS();
        //运行结果：
        //hello PrintStream
        //A65BCDE is 5
    }

    /**
     * 测试write(), print(), println(), printf()等接口。
     */
    private static void testPrintStreamAPIS() {
        final byte[] arr={0x61, 0x62, 0x63, 0x64, 0x65 }; // abced
        try {
            // 创建文件对应FileOutputStream
            PrintStream out = new PrintStream("d://other.txt");
            // 将字符串“hello PrintStream”+回车符，写入到输出流中
            out.println("hello PrintStream");
            // 将0x41写入到输出流中
            // 0x41对应ASCII码的字母'A'，也就是写入字符'A'
            out.write(0x41);
            // 将字符串"65"写入到输出流中。
            // out.print(0x41); 等价于 out.write(String.valueOf(0x41));
            out.print(0x41);
            out.print(0x61);
            // 将字符'B'追加到输出流中
            out.append('B');
            // 将"CDE is 5" + 回车  写入到输出流中
            String str = "CDE";
            int num = 5;
            out.printf("%s is %d\n", str, num);
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * PrintStream(String fileName) 的测试函数
     * 函数的作用，就是将字母“abcde”写入到文件“printStream3.txt”中
     */
    private static void testPrintStreamConstrutor3() {
        final byte[] arr = {0x61,0x62,0x63,0x64,0x65};

        try {
            PrintStream out = new PrintStream("d://printStream3.txt");
            out.write(arr);
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * PrintStream(File file) 的测试函数
     * 函数的作用，就是将字母“abcde”写入到文件“printStream3.txt”中
     */
    private static void testPrintStreamConstrutor2() {
        final byte[] arr = {0x61,0x62,0x63,0x64,0x65};

        File file = new File("d://printStream2.txt");
        try {
            PrintStream out = new PrintStream(file);
            out.write(arr);
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * PrintStream(OutputStream out) 的测试函数
     * 函数的作用，就是将字母“abcde”写入到文件“file.txt”中
     */
    private static void testPrintStreamConstrutor1() {

        // 0x61对应ASCII码的字母'a'，0x62对应ASCII码的字母'b', ...
        final byte[] arr = {0x61, 0x62, 0x63, 0x64, 0x65 };//abcde
        // 创建文件“printStream.txt”的File对象
        File file = new File("d://printStream.txt");
        try {
            // 创建文件对应FileOutputStream
            PrintStream out = new PrintStream(new FileOutputStream(file));
            // 将“字节数组arr”全部写入到输出流中
            out.write(arr);
            // 关闭输出流
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
