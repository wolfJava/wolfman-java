package com.wolfman.java.io;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * FileReader 和 FileWriter 测试程序
 */
public class FileReaderWriterTest {

    private static final String FileName = "d://filerw.txt";

    private static final String CharsetName = "utf-8";

    public static void main(String[] args) {
        write();
        read();
    }

    private static void read() {
        // 方法1：新建FileInputStream对象
        // 新建文件“file.txt”对应File对象
        File file = new File(FileName);
        try {
            FileReader in = new FileReader(file);
            // 测试read()，从中读取一个字符
            char c1 = (char)in.read();
            System.out.println("c1="+c1);

            // 测试skip(long byteCount)，跳过4个字符
            in.skip(6);

            // 测试read(char[] cbuf, int off, int len)
            char[] buf = new char[10];
            in.read(buf, 0, buf.length);
            System.out.println("buf="+(new String(buf)));

            in.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void write() {
        // 创建文件“file.txt”对应File对象
        File file = new File(FileName);
        try {
            // 创建FileOutputStream对应FileWriter：将字节流转换为字符流，即写入out1的数据会自动由字节转换为字符。
            FileWriter out = new FileWriter(file);
            // 写入10个汉字
            out.write("字节流转为字符流示例");
            // 向“文件中”写入"0123456789"+换行符
            out.write("0123456789\n");

            out.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
