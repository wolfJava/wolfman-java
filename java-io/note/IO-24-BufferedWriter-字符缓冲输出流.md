## BufferedWriter-字符缓冲输出流

### 一 介绍

BufferedWriter 是缓冲字符输出流。它继承于Writer。 

BufferedWriter 的作用是为其他字符输出流添加一些缓冲功能。 

### 二 示例演示

~~~java
import java.io.*;
import java.util.Scanner;

public class BufferedWriterDemo {

    private static final int LEN = 5;

    private static final char[] ArrayLetters = new char[] {'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z'};

    public static void main(String[] args) {
        File file = new File("d://bufferwriter.txt");
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(file));
            // 将ArrayLetters数组的前10个字符写入到输出流中
            out.write(ArrayLetters, 0, 10);
            // 将“换行符\n”写入到输出流中
            out.write('\n');
            out.flush();
            readUserInput() ;
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private static void readUserInput() {
        System.out.println("please input a text:");
        Scanner reader = new Scanner(System.in);
        // 等待一个输入
        String str = reader.next();
        System.out.printf("the input is : %s\n", str);
    }
}
~~~

### 三 源码分析

**BufferedWriter.java**

~~~java
package java.io;

public class BufferedWriter extends Writer {

    // 输出流对象
    private Writer out;

    // 保存“缓冲输出流”数据的字符数组
    private char cb[];
    
    // nChars 是cb缓冲区中字符的总的个数
    // nextChar 是下一个要读取的字符在cb缓冲区中的位置
    private int nChars, nextChar;

    // 默认字符缓冲区大小
    private static int defaultCharBufferSize = 8192;

    // 行分割符
    private String lineSeparator;

    // 构造函数，传入“Writer对象”，默认缓冲区大小是8k
    public BufferedWriter(Writer out) {
        this(out, defaultCharBufferSize);
    }

    // 构造函数，传入“Writer对象”，指定缓冲区大小是sz
    public BufferedWriter(Writer out, int sz) {
        super(out);
        if (sz <= 0)
            throw new IllegalArgumentException("Buffer size <= 0");
        this.out = out;
        cb = new char[sz];
        nChars = sz;
        nextChar = 0;

        lineSeparator = java.security.AccessController.doPrivileged(
            new sun.security.action.GetPropertyAction("line.separator"));
    }

    // 确保“BufferedWriter”是打开状态
    private void ensureOpen() throws IOException {
        if (out == null)
            throw new IOException("Stream closed");
    }

    // 对缓冲区执行flush()操作，将缓冲区的数据写入到Writer中
    void flushBuffer() throws IOException {
        synchronized (lock) {
            ensureOpen();
            if (nextChar == 0)
                return;
            out.write(cb, 0, nextChar);
            nextChar = 0;
        }
    }

    // 将c写入到缓冲区中。先将c转换为char，然后将其写入到缓冲区。
    public void write(int c) throws IOException {
        synchronized (lock) {
            ensureOpen();
            if (nextChar >= nChars)
                flushBuffer();
            cb[nextChar++] = (char) c;
        }
    }

    // 返回a，b中较小的数
    private int min(int a, int b) {
        if (a < b) return a;
        return b;
    }

    // 将字符数组cbuf写入到缓冲中，从cbuf的off位置开始写入，写入长度是len。
    public void write(char cbuf[], int off, int len) throws IOException {
        synchronized (lock) {
            ensureOpen();
            if ((off < 0) || (off > cbuf.length) || (len < 0) ||
                ((off + len) > cbuf.length) || ((off + len) < 0)) {
                throw new IndexOutOfBoundsException();
            } else if (len == 0) {
                return;
            }

            if (len >= nChars) {
                /* If the request length exceeds the size of the output buffer,
                   flush the buffer and then write the data directly.  In this
                   way buffered streams will cascade harmlessly. */
                flushBuffer();
                out.write(cbuf, off, len);
                return;
            }

            int b = off, t = off + len;
            while (b < t) {
                int d = min(nChars - nextChar, t - b);
                System.arraycopy(cbuf, b, cb, nextChar, d);
                b += d;
                nextChar += d;
                if (nextChar >= nChars)
                    flushBuffer();
            }
        }
    }

    // 将字符串s写入到缓冲中，从s的off位置开始写入，写入长度是len。
    public void write(String s, int off, int len) throws IOException {
        synchronized (lock) {
            ensureOpen();

            int b = off, t = off + len;
            while (b < t) {
                int d = min(nChars - nextChar, t - b);
                s.getChars(b, b + d, cb, nextChar);
                b += d;
                nextChar += d;
                if (nextChar >= nChars)
                    flushBuffer();
            }
        }
    }

    // 将换行符写入到缓冲中
    public void newLine() throws IOException {
        write(lineSeparator);
    }

    // 清空缓冲区数据
    public void flush() throws IOException {
        synchronized (lock) {
            flushBuffer();
            out.flush();
        }
    }

    @SuppressWarnings("try")
    public void close() throws IOException {
        synchronized (lock) {
            if (out == null) {
                return;
            }
            try (Writer w = out) {
                flushBuffer();
            } finally {
                out = null;
                cb = null;
            }
        }
    }
}


~~~

