## CharArrayWriter-字符数组输出流

### 一 介绍

CharArrayReader 用于写入数据符，它继承于Writer。操作的数据是以字符为单位！ 

### 二 示例演示

~~~java
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
~~~

### 三 源码分析

Writer是CharArrayWriter的父类，我们先看看Writer的源码，然后再学CharArrayWriter的源码。 

**Writer.java**

~~~java
package java.io;

public abstract class Writer implements Appendable, Closeable, Flushable {

    private char[] writeBuffer;

    private static final int WRITE_BUFFER_SIZE = 1024;
    
    protected Object lock;

    protected Writer() {
        this.lock = this;
    }

    protected Writer(Object lock) {
        if (lock == null) {
            throw new NullPointerException();
        }
        this.lock = lock;
    }

    public void write(int c) throws IOException {
        synchronized (lock) {
            if (writeBuffer == null){
                writeBuffer = new char[WRITE_BUFFER_SIZE];
            }
            writeBuffer[0] = (char) c;
            write(writeBuffer, 0, 1);
        }
    }

    abstract public void write(char cbuf[], int off, int len) throws IOException;

    public void write(String str) throws IOException {
        write(str, 0, str.length());
    }

    public void write(String str, int off, int len) throws IOException {
        synchronized (lock) {
            char cbuf[];
            if (len <= WRITE_BUFFER_SIZE) {
                if (writeBuffer == null) {
                    writeBuffer = new char[WRITE_BUFFER_SIZE];
                }
                cbuf = writeBuffer;
            } else {    // Don't permanently allocate very large buffers.
                cbuf = new char[len];
            }
            str.getChars(off, (off + len), cbuf, 0);
            write(cbuf, 0, len);
        }
    }

    public Writer append(CharSequence csq) throws IOException {
        if (csq == null)
            write("null");
        else
            write(csq.toString());
        return this;
    }

    public Writer append(CharSequence csq, int start, int end) throws IOException {
        CharSequence cs = (csq == null ? "null" : csq);
        write(cs.subSequence(start, end).toString());
        return this;
    }

    public Writer append(char c) throws IOException {
        write(c);
        return this;
    }

    abstract public void flush() throws IOException;

    abstract public void close() throws IOException;

}
~~~

**CharArrayWriter.java**

~~~java
package java.io;

import java.util.Arrays;

public class CharArrayWriter extends Writer {
    
    // 字符数组缓冲
    protected char buf[];

    // 下一个字符的写入位置
    protected int count;

    // 构造函数：默认缓冲区大小是32
    public CharArrayWriter() {
        this(32);
    }

    // 构造函数：指定缓冲区大小是initialSize
    public CharArrayWriter(int initialSize) {
        if (initialSize < 0) {
            throw new IllegalArgumentException("Negative initial size: "
                                               + initialSize);
        }
        buf = new char[initialSize];
    }

    // 写入一个字符c到CharArrayWriter中
    public void write(int c) {
        synchronized (lock) {
            int newcount = count + 1;
            if (newcount > buf.length) {
                buf = Arrays.copyOf(buf, Math.max(buf.length << 1, newcount));
            }
            buf[count] = (char)c;
            count = newcount;
        }
    }

    // 写入字符数组c到CharArrayWriter中。off是“字符数组b中的起始写入位置”，len是写入的长度
    public void write(char c[], int off, int len) {
        if ((off < 0) || (off > c.length) || (len < 0) ||
            ((off + len) > c.length) || ((off + len) < 0)) {
            throw new IndexOutOfBoundsException();
        } else if (len == 0) {
            return;
        }
        synchronized (lock) {
            int newcount = count + len;
            if (newcount > buf.length) {
                buf = Arrays.copyOf(buf, Math.max(buf.length << 1, newcount));
            }
            System.arraycopy(c, off, buf, count, len);
            count = newcount;
        }
    }

    // 写入字符串str到CharArrayWriter中。off是“字符串的起始写入位置”，len是写入的长度
    public void write(String str, int off, int len) {
        synchronized (lock) {
            int newcount = count + len;
            if (newcount > buf.length) {
                buf = Arrays.copyOf(buf, Math.max(buf.length << 1, newcount));
            }
            str.getChars(off, off + len, buf, count);
            count = newcount;
        }
    }

    // 将CharArrayWriter写入到“Writer对象out”中
    public void writeTo(Writer out) throws IOException {
        synchronized (lock) {
            out.write(buf, 0, count);
        }
    }

    // 将csq写入到CharArrayWriter中
    // 注意：该函数返回CharArrayWriter对象
    public CharArrayWriter append(CharSequence csq) {
        String s = (csq == null ? "null" : csq.toString());
        write(s, 0, s.length());
        return this;
    }

    // 将csq从start开始(包括)到end结束(不包括)的数据，写入到CharArrayWriter中。
    // 注意：该函数返回CharArrayWriter对象！ 
    public CharArrayWriter append(CharSequence csq, int start, int end) {
        String s = (csq == null ? "null" : csq).subSequence(start, end).toString();
        write(s, 0, s.length());
        return this;
    }

    // 将字符c追加到CharArrayWriter中！
    // 注意：它与write(int c)的区别。append(char c)会返回CharArrayWriter对象。
    public CharArrayWriter append(char c) {
        write(c);
        return this;
    }

    // 重置
    public void reset() {
        count = 0;
    }

    // 将CharArrayWriter的全部数据对应的char[]返回
    public char toCharArray()[] {
        synchronized (lock) {
            return Arrays.copyOf(buf, count);
        }
    }

    // 返回CharArrayWriter的大小
    public int size() {
        return count;
    }

    
    public String toString() {
        synchronized (lock) {
            return new String(buf, 0, count);
        }
    }

    public void flush() { }

    public void close() { }

}
~~~

**说明：**

CharArrayWriter实际上是将数据写入到“字符数组”中去。 

1. 通过CharArrayWriter()创建的CharArrayWriter对应的字符数组大小是32。 
2. 通过CharArrayWriter(int size) 创建的CharArrayWriter对应的字符数组大小是size。 
3. write(int oneChar)的作用将int类型的oneChar换成char类型，然后写入到CharArrayWriter中。 
4. write(char[] buffer, int offset, int len) 是将字符数组buffer写入到输出流中，offset是从buffer中读取数据的起始偏移位置，len是读取的长度。 
5. write(String str, int offset, int count) 是将字符串str写入到输出流中，offset是从str中读取数据的起始位置，count是读取的长度。 
6. append(char c)的作用将char类型的c写入到CharArrayWriter中，然后返回CharArrayWriter对象。 
   1. append(char c)与write(int c)都是将单个字符写入到CharArrayWriter中。它们的区别是，append(char c)会返回CharArrayWriter对象，但是write(int c)返回void。 
7. append(CharSequence csq, int start, int end)的作用将csq从start开始(包括)到end结束(不包括)的数据，写入到CharArrayWriter中。 
   1. 该函数返回CharArrayWriter对象！ 
8. append(CharSequence csq)的作用将csq写入到CharArrayWriter中。 
   1. 该函数返回CharArrayWriter对象！ 
9. writeTo(OutputStream out) 将该“字符数组输出流”的数据全部写入到“输出流out”中。 

