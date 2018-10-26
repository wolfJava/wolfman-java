## InputStreamReader和OutputStreamWriter-字节流通向字符流的桥梁

### 一 介绍

InputStreamReader和OutputStreamWriter 是字节流通向字符流的桥梁：它使用指定的 charset 读写字节并将其解码为字符。 

InputStreamReader 的作用是将“字节输入流”转换成“字符输入流”。它继承于Reader。 

OutputStreamWriter 的作用是将“字节输出流”转换成“字符输出流”。它继承于Writer。 

### 二 代码演示

~~~java
import java.io.*;

/**
 * InputStreamReader 和 OutputStreamWriter 测试程序
 */
public class InputStreamReaderOutputStreamWriterDemo {

    private static final String FileName = "d://file.txt";

    private static final String CharsetName = "utf-8";

    //private static final String CharsetName = "gb2312";

    public static void main(String[] args) {
        writer();   //写入文件内容
        reader();   //读取文件内容
    }

    private static void reader() {
        // 方法1：新建FileInputStream对象
        // 新建文件“file.txt”对应File对象
        File file = new File(FileName);
        try {
            InputStreamReader in = new InputStreamReader(new FileInputStream(file),CharsetName);
            // 测试read()，从中读取一个字符
            char c1 = (char)in.read();
            System.out.println("c1="+c1);

            // 测试skip(long byteCount)，跳过4个字符
            in.skip(4);

            // 测试read(char[] cbuf, int off, int len)
            char[] buf = new char[10];
            in.read(buf,0,buf.length);
            System.out.println("buf="+(new String(buf)));
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void writer() {
        // 创建文件“file.txt”对应File对象
        File file = new File(FileName);
        try {
            // 创建FileOutputStream对应OutputStreamWriter：将字节流转换为字符流，即写入out的数据会自动由字节转换为字符。
            OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(file),CharsetName);
            // 写入10个汉字
            out.write("字节流转为字符流示例");
            // 向“文件中”写入"0123456789"+换行符
            out.write("0123456789\n");
            out.close();
        } catch (IOException  e) {
            e.printStackTrace();
        }
    }

}
~~~

**运行结果：**

c1=字
buf=字符流示例01234

**结果说明**： 

1. write() 的作用是将“内容写入到输出流”。写入的时候，会将写入的内容转换utf-8编码并写入。 
2. read() 的作用是将“内容读取到输入流”。读取的时候，会将内容转换成utf-8的内容转换成字节并读出来。 

### 三 源码分析

**InputStreamReader.java**  

~~~java
package java.io;

import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import sun.nio.cs.StreamDecoder;

// 将“字节输入流”转换成“字符输入流”
public class InputStreamReader extends Reader {

    private final StreamDecoder sd;

    // 根据in创建InputStreamReader，使用默认的编码
    public InputStreamReader(InputStream in) {
        super(in);
        try {
            sd = StreamDecoder.forInputStreamReader(in, this, (String)null); // ## check lock object
        } catch (UnsupportedEncodingException e) {
            // The default encoding should always be available
            throw new Error(e);
        }
    }

    // 根据in创建InputStreamReader，使用编码charsetName(编码名)
    public InputStreamReader(InputStream in, String charsetName)
        throws UnsupportedEncodingException
    {
        super(in);
        if (charsetName == null)
            throw new NullPointerException("charsetName");
        sd = StreamDecoder.forInputStreamReader(in, this, charsetName);
    }

    // 根据in创建InputStreamReader，使用编码cs
    public InputStreamReader(InputStream in, Charset cs) {
        super(in);
        if (cs == null)
            throw new NullPointerException("charset");
        sd = StreamDecoder.forInputStreamReader(in, this, cs);
    }

    // 根据in创建InputStreamReader，使用解码器dec
    public InputStreamReader(InputStream in, CharsetDecoder dec) {
        super(in);
        if (dec == null)
            throw new NullPointerException("charset decoder");
        sd = StreamDecoder.forInputStreamReader(in, this, dec);
    }

    // 获取解码器
    public String getEncoding() {
        return sd.getEncoding();
    }

    // 读取并返回一个字符
    public int read() throws IOException {
        return sd.read();
    }

    // 将InputStreamReader中的数据写入cbuf中，从cbuf的offset位置开始写入，写入长度是length
    public int read(char cbuf[], int offset, int length) throws IOException {
        return sd.read(cbuf, offset, length);
    }

    // 能否从InputStreamReader中读取数据
    public boolean ready() throws IOException {
        return sd.ready();
    }

    // 关闭InputStreamReader
    public void close() throws IOException {
        sd.close();
    }
}

~~~

**OutputStreamWriter.java**

~~~java
package java.io;

import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import sun.nio.cs.StreamEncoder;

// 将“字节输出流”转换成“字符输出流”
public class OutputStreamWriter extends Writer {

    private final StreamEncoder se;

    // 根据out创建OutputStreamWriter，使用编码charsetName(编码名)
    public OutputStreamWriter(OutputStream out, String charsetName)
        throws UnsupportedEncodingException
    {
        super(out);
        if (charsetName == null)
            throw new NullPointerException("charsetName");
        se = StreamEncoder.forOutputStreamWriter(out, this, charsetName);
    }

    // 根据out创建OutputStreamWriter，使用默认的编码
    public OutputStreamWriter(OutputStream out) {
        super(out);
        try {
            se = StreamEncoder.forOutputStreamWriter(out, this, (String)null);
        } catch (UnsupportedEncodingException e) {
            throw new Error(e);
        }
    }

    // 根据out创建OutputStreamWriter，使用编码cs
    public OutputStreamWriter(OutputStream out, Charset cs) {
        super(out);
        if (cs == null)
            throw new NullPointerException("charset");
        se = StreamEncoder.forOutputStreamWriter(out, this, cs);
    }

    // 根据out创建OutputStreamWriter，使用编码器enc
    public OutputStreamWriter(OutputStream out, CharsetEncoder enc) {
        super(out);
        if (enc == null)
            throw new NullPointerException("charset encoder");
        se = StreamEncoder.forOutputStreamWriter(out, this, enc);
    }

    // 获取编码器enc
    public String getEncoding() {
        return se.getEncoding();
    }

    // 刷新缓冲区
    void flushBuffer() throws IOException {
        se.flushBuffer();
    }

    // 将单个字符写入到OutputStreamWriter中
    public void write(int c) throws IOException {
        se.write(c);
    }

    // 将字符数组cbuf从off开始的数据写入到OutputStreamWriter中，写入长度是len
    public void write(char cbuf[], int off, int len) throws IOException {
        se.write(cbuf, off, len);
    }

    // 将字符串str从off开始的数据写入到OutputStreamWriter中，写入长度是len
    public void write(String str, int off, int len) throws IOException {
        se.write(str, off, len);
    }

    // 刷新“输出流”
    // 它与flushBuffer()的区别是：flushBuffer()只会刷新缓冲，而flush()是刷新流，flush()包括了flushBuffer。
    public void flush() throws IOException {
        se.flush();
    }

    public void close() throws IOException {
        se.close();
    }
}
~~~