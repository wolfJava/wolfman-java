## 22-FileReader和FileWriter

### 一 介绍

FileReader 是用于读取字符流的类，它继承于InputStreamReader。要读取原始字节流，请考虑使用 FileInputStream。 

FileWriter 是用于写入字符流的类，它继承于OutputStreamWriter。要写入原始字节流，请考虑使用 FileOutputStream。 

### 二 示例演示

~~~java
import java.io.*;

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
~~~

**运行结果：**

c1=字
buf=流示例0123456



### 三 源码分析

**FileReader.java**

~~~java
package java.io;

public class FileReader extends InputStreamReader {

    public FileReader(String fileName) throws FileNotFoundException {
        super(new FileInputStream(fileName));
    }

    public FileReader(File file) throws FileNotFoundException {
        super(new FileInputStream(file));
    }

    public FileReader(FileDescriptor fd) {
        super(new FileInputStream(fd));
    }

}

~~~

**FileWriter.java**

~~~java
package java.io;

public class FileWriter extends OutputStreamWriter {

    public FileWriter(String fileName) throws IOException {
        super(new FileOutputStream(fileName));
    }

    public FileWriter(String fileName, boolean append) throws IOException {
        super(new FileOutputStream(fileName, append));
    }

    public FileWriter(File file) throws IOException {
        super(new FileOutputStream(file));
    }

    public FileWriter(File file, boolean append) throws IOException {
        super(new FileOutputStream(file, append));
    }

    public FileWriter(FileDescriptor fd) {
        super(new FileOutputStream(fd));
    }

}
~~~