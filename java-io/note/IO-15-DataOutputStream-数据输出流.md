## DataOutputStream-数据输出流

### 一 介绍

**DataOutputStream** 是数据输出流。它继承于FilterOutputStream。 

**DataOutputStream** 是用来装饰其它输出流，将DataOutputStream和**DataInputStream**输入流配合使用，“允许应用程序以与机器无关方式从底层输入流中读写基本 Java 数据类型”。 

### 二 源码分析

~~~java
package java.io;

public class DataOutputStream extends FilterOutputStream implements DataOutput {
    
    // “数据输出流”的字节数
    protected int written;

    // “数据输出流”对应的字节数组
    private byte[] bytearr = null;

    // 构造函数
    public DataOutputStream(OutputStream out) {
        super(out);
    }

    // 增加“输出值”
    private void incCount(int value) {
        int temp = written + value;
        if (temp < 0) {
            temp = Integer.MAX_VALUE;
        }
        written = temp;
    }

    // 将int类型的值写入到“数据输出流”中
    public synchronized void write(int b) throws IOException {
        out.write(b);
        incCount(1);
    }

    // 将字节数组b从off开始的len个字节，都写入到“数据输出流”中
    public synchronized void write(byte b[], int off, int len)
        throws IOException
    {
        out.write(b, off, len);
        incCount(len);
    }

    // 清空缓冲，即将缓冲中的数据都写入到输出流中
    public void flush() throws IOException {
        out.flush();
    }

    // 将boolean类型的值写入到“数据输出流”中
    public final void writeBoolean(boolean v) throws IOException {
        out.write(v ? 1 : 0);
        incCount(1);
    }

    // 将byte类型的值写入到“数据输出流”中
    public final void writeByte(int v) throws IOException {
        out.write(v);
        incCount(1);
    }

    // 将short类型的值写入到“数据输出流”中
    // 注意：short占2个字节
    public final void writeShort(int v) throws IOException {
        // 写入 short高8位 对应的字节
        out.write((v >>> 8) & 0xFF);
        // 写入 short低8位 对应的字节
        out.write((v >>> 0) & 0xFF);
        incCount(2);
    }

    // 将char类型的值写入到“数据输出流”中
    // 注意：char占2个字节
    public final void writeChar(int v) throws IOException {
        // 写入 char高8位 对应的字节
        out.write((v >>> 8) & 0xFF);
        // 写入 char低8位 对应的字节
        out.write((v >>> 0) & 0xFF);
        incCount(2);
    }

    // 将int类型的值写入到“数据输出流”中
    // 注意：int占4个字节
    public final void writeInt(int v) throws IOException {
        out.write((v >>> 24) & 0xFF);
        out.write((v >>> 16) & 0xFF);
        out.write((v >>>  8) & 0xFF);
        out.write((v >>>  0) & 0xFF);
        incCount(4);
    }

    private byte writeBuffer[] = new byte[8];

    // 将long类型的值写入到“数据输出流”中
    // 注意：long占8个字节
    public final void writeLong(long v) throws IOException {
        writeBuffer[0] = (byte)(v >>> 56);
        writeBuffer[1] = (byte)(v >>> 48);
        writeBuffer[2] = (byte)(v >>> 40);
        writeBuffer[3] = (byte)(v >>> 32);
        writeBuffer[4] = (byte)(v >>> 24);
        writeBuffer[5] = (byte)(v >>> 16);
        writeBuffer[6] = (byte)(v >>>  8);
        writeBuffer[7] = (byte)(v >>>  0);
        out.write(writeBuffer, 0, 8);
        incCount(8);
    }

    // 将float类型的值写入到“数据输出流”中
    public final void writeFloat(float v) throws IOException {
        writeInt(Float.floatToIntBits(v));
    }

    // 将double类型的值写入到“数据输出流”中
    public final void writeDouble(double v) throws IOException {
        writeLong(Double.doubleToLongBits(v));
    }

    // 将String类型的值写入到“数据输出流”中
    // 实际写入时，是将String对应的每个字符转换成byte数据后写入输出流中。
    public final void writeBytes(String s) throws IOException {
        int len = s.length();
        for (int i = 0 ; i < len ; i++) {
            out.write((byte)s.charAt(i));
        }
        incCount(len);
    }

    // 将String类型的值写入到“数据输出流”中
    // 实际写入时，是将String对应的每个字符转换成char数据后写入输出流中。
    public final void writeChars(String s) throws IOException {
        int len = s.length();
        for (int i = 0 ; i < len ; i++) {
            int v = s.charAt(i);
            out.write((v >>> 8) & 0xFF);
            out.write((v >>> 0) & 0xFF);
        }
        incCount(len * 2);
    }

    // 将UTF-8类型的值写入到“数据输出流”中
    public final void writeUTF(String str) throws IOException {
        writeUTF(str, this);
    }

    // 将String数据以UTF-8类型的形式写入到“输出流out”中
    static int writeUTF(String str, DataOutput out) throws IOException {
        //获取String的长度
        int strlen = str.length();
        int utflen = 0;
        int c, count = 0;

        // 由于UTF-8是1～4个字节不等；
        // 这里，根据UTF-8首字节的范围，判断UTF-8是几个字节的。
        for (int i = 0; i < strlen; i++) {
            c = str.charAt(i);
            if ((c >= 0x0001) && (c <= 0x007F)) {
                utflen++;
            } else if (c > 0x07FF) {
                utflen += 3;
            } else {
                utflen += 2;
            }
        }

        if (utflen > 65535)
            throw new UTFDataFormatException(
                "encoded string too long: " + utflen + " bytes");

        // 新建“字节数组bytearr”
        byte[] bytearr = null;
        if (out instanceof DataOutputStream) {
            DataOutputStream dos = (DataOutputStream)out;
            if(dos.bytearr == null || (dos.bytearr.length < (utflen+2)))
                dos.bytearr = new byte[(utflen*2) + 2];
            bytearr = dos.bytearr;
        } else {
            bytearr = new byte[utflen+2];
        }

        // “字节数组”的前2个字节保存的是“UTF-8数据的长度”
        bytearr[count++] = (byte) ((utflen >>> 8) & 0xFF);
        bytearr[count++] = (byte) ((utflen >>> 0) & 0xFF);

        // 对UTF-8中的单字节数据进行预处理
        int i=0;
        for (i=0; i<strlen; i++) {
           c = str.charAt(i);
           if (!((c >= 0x0001) && (c <= 0x007F))) break;
           bytearr[count++] = (byte) c;
        }

        // 对预处理后的数据，接着进行处理
        for (;i < strlen; i++){
            c = str.charAt(i);
            // UTF-8数据是1个字节的情况
            if ((c >= 0x0001) && (c <= 0x007F)) {
                bytearr[count++] = (byte) c;

            } else if (c > 0x07FF) {
                // UTF-8数据是3个字节的情况
                bytearr[count++] = (byte) (0xE0 | ((c >> 12) & 0x0F));
                bytearr[count++] = (byte) (0x80 | ((c >>  6) & 0x3F));
                bytearr[count++] = (byte) (0x80 | ((c >>  0) & 0x3F));
            } else {
                // UTF-8数据是2个字节的情况
                bytearr[count++] = (byte) (0xC0 | ((c >>  6) & 0x1F));
                bytearr[count++] = (byte) (0x80 | ((c >>  0) & 0x3F));
            }
        }
        out.write(bytearr, 0, utflen+2);
        return utflen + 2;
    }
    
    public final int size() {
        return written;
    }
}
~~~

### 三 示例代码

关于DataOutStream中API的详细用法，参考示例代码**(DataInputStreamTest.java)**