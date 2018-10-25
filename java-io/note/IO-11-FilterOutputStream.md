## FilterOutputStream

### 一 FilterOutputStream 介绍

FilterOutputStream 的作用是用来“封装其它的输出流，并为它们提供额外的功能”。它主要包括BufferedOutputStream, DataOutputStream和PrintStream。

1. BufferedOutputStream的作用就是为“输出流提供缓冲功能”。
2. DataOutputStream 是用来装饰其它输出流，将DataOutputStream和DataInputStream输入流配合使用，“允许应用程序以与机器无关方式从底层输入流中读写基本 Java 数据类型”。
3. PrintStream 是用来装饰其它输出流。它能为其他输出流添加了功能，使它们能够方便地打印各种数据值表示形式。

### 二 源码分析

~~~java
package java.io;

public class FilterOutputStream extends OutputStream {
    
    protected OutputStream out;
    
    public FilterOutputStream(OutputStream out) {
        this.out = out;
    }
    
    public void write(int b) throws IOException {
        out.write(b);
    }
    
    public void write(byte b[]) throws IOException {
        write(b, 0, b.length);
    }

    public void write(byte b[], int off, int len) throws IOException {
        if ((off | len | (b.length - (len + off)) | (off + len)) < 0)
            throw new IndexOutOfBoundsException();

        for (int i = 0 ; i < len ; i++) {
            write(b[off + i]);
        }
    }

    public void flush() throws IOException {
        out.flush();
    }

    @SuppressWarnings("try")
    public void close() throws IOException {
        try (OutputStream ostream = out) {
            flush();
        }
    }
}

~~~

