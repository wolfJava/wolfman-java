## FilterInputStream

### 一 FilterInputStream介绍

**FilterInputStream** 的作用是用来“**封装其它的输入流，并为它们提供额外的功能**”。它的常用的子类有BufferedInputStream和DataInputStream。

**BufferedInputStream**的作用就是为“输入流提供缓冲功能，以及mark()和reset()功能”。

**DataInputStream** 是用来装饰其它输入流，它“允许应用程序以与机器无关方式从底层输入流中读取基本 Java 数据类型”。应用程序可以使用DataOutputStream(数据输出流)写入由DataInputStream(数据输入流)读取的数据。

### 二 源码分析

~~~java
package java.io;

import java.io.IOException;
import java.io.InputStream;

public class FilterInputStream extends InputStream {

    protected volatile InputStream in;

    protected FilterInputStream(InputStream in) {
        this.in = in;
    }

    public int read() throws IOException {
        return in.read();
    }

    public int read(byte b[]) throws IOException {
        return read(b, 0, b.length);
    }

    public int read(byte b[], int off, int len) throws IOException {
        return in.read(b, off, len);
    }

    public long skip(long n) throws IOException {
        return in.skip(n);
    }

    public int available() throws IOException {
        return in.available();
    }

    public void close() throws IOException {
        in.close();
    }

    public synchronized void mark(int readlimit) {
        in.mark(readlimit);
    }

    public synchronized void reset() throws IOException {
        in.reset();
    }
    
    public boolean markSupported() {
        return in.markSupported();
    }
}
~~~