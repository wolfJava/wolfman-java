## FileInputStream 和 FileOutputStream-文件输入输出流

### 一 FileInputStream 和 FileOutputStream 介绍

FileInputStream 是文件输入流，它继承于 InputStream。通常使用它从某个文件中获得输入字节。

FileOutputStream 是文件输出流，它继承与 OutputStream。通常使用它将数据写入 File 或 FileDescritor 的输出流。

### 二 函数接口

**FileInputStream 函数接口：**

~~~java
	// 构造函数1：创建“File对象”对应的“文件输入流”
	public FileInputStream(File file)
	// 构造函数2：创建“文件描述符”对应的“文件输入流”
	public FileInputStream(FileDescriptor fdObj)
	// 构造函数3：创建“文件(路径为path)”对应的“文件输入流”
    public FileInputStream(String name)

    // 返回“剩余的可读取的字节数”或者“skip的字节数”
    public native int available() throws IOException;
    // 关闭“文件输入流”
	public void close()
    // 返回“FileChannel”
    public FileChannel getChannel()
    // 返回“文件描述符”
    public final FileDescriptor getFD()
    // 返回“文件输入流”的下一个字节
    public int read()
    // 读取“文件输入流”的数据并存在到buffer，从byteOffset开始存储，存储长度是byteCount。
    public int read(byte b[], int off, int len)
    // 跳过byteCount个字节
    public native long skip(long n)
~~~

**FileOutputStream 函数接口：**

~~~java
	// 构造函数1：创建“File对象”对应的“文件输入流”；默认“追加模式”是false，即“写到输出的流内容”不是以追加的方式添加到文件中。
	public FileOutputStream(File file)
    // 构造函数2：创建“File对象”对应的“文件输入流”；指定“追加模式”。
    public FileOutputStream(File file, boolean append)
    // 构造函数3：创建“文件描述符”对应的“文件输入流”；默认“追加模式”是false，即“写到输出的流内容”不是以追加的方式添加到文件中。
    public FileOutputStream(FileDescriptor fdObj)
    // 构造函数4：创建“文件(路径为path)”对应的“文件输入流”；默认“追加模式”是false，即“写到输出的流内容”不是以追加的方式添加到文件中。
    FileOutputStream(String name)
    // 构造函数5：创建“文件(路径为path)”对应的“文件输入流”；指定“追加模式”。
    FileOutputStream(String name, boolean append)
    
    // 关闭“输出流”
    public void close() 
    // 返回“FileChannel”
    public FileChannel getChannel()
    // 返回“文件描述符”
    public final FileDescriptor getFD()
    // 将b写入到“文件输出流”中，从b的off开始写，写入长度是len。
    public void write(byte b[], int off, int len)
    // 写入字节oneByte到“文件输出流”中
    public void write(int b)
~~~

### 三 示例演示

~~~java
import java.io.*;

/**
 * FileInputStream 和 FileOutputStream 测试程序
 */
public class FileStreamDemo {

    private static final String FILE_NAME = "d://file.txt";

    public static void main(String[] args) {
        testWrite();
        testRead();
    }

    /**
     * 运行结果：
     * 在源码所在目录生成文件"file.txt"，文件内容是“abcdefghijklmnopqrstuvwxyz0123456789”
     *
     * 加入，我们将 FileOutputStream fileOut2 = new FileOutputStream(file, true);
     * 修改为 FileOutputStream fileOut2 = new FileOutputStream(file, false);
     * 然后再执行程序，“file.txt”的内容变成"0123456789"。
     * 原因是：
     *  (01) FileOutputStream fileOut2 = new FileOutputStream(file, true);
     *      `它是以“追加模式”将内容写入文件的。即写入的内容，追加到原始的内容之后。
     * (02) FileOutputStream fileOut2 = new FileOutputStream(file, false);
     *      它是以“新建模式”将内容写入文件的。即删除文件原始的内容之后，再重新写入。
     */
    private static void testWrite() {
        try {
            // 创建文件“file.txt”对应File对象
            File file = new File(FILE_NAME);
            // 创建文件“file.txt”对应的FileOutputStream对象，默认是关闭“追加模式”
            FileOutputStream out = new FileOutputStream(file);
            // 创建FileOutputStream对应的PrintStream，方便操作。PrintStream的写入接口更便利
            PrintStream outPrint = new PrintStream(out);
            // 向“文件中”写入26个字母
            outPrint.print("abcdefghijklmnopqrstuvwxyz");
            outPrint.close();

            // 创建文件“file.txt”对应的FileOutputStream对象，打开“追加模式”
            FileOutputStream out1 = new FileOutputStream(file,true);
            // 创建FileOutputStream对应的PrintStream，方便操作。PrintStream的写入接口更便利
            PrintStream outPrint1 = new PrintStream(out1);
            // 向“文件中”写入"0123456789"+换行符
            outPrint1.print("0123456789");
            outPrint1.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * FileInputStream 演示程序
     */
    private static void testRead() {
        try {
            // 方法1：新建FileInputStream对象
            // 新建文件“file.txt”对应File对象
            File file = new File(FILE_NAME);
            FileInputStream in1 = new FileInputStream(file);

            // 方法2：新建FileInputStream对象
            FileInputStream in2 = new FileInputStream(FILE_NAME);

            // 方法3：新建FileInputStream对象
            // 获取文件“file.txt”对应的“文件描述符”
            FileDescriptor fdin = in2.getFD();
            // 根据“文件描述符”创建“FileInputStream”对象
            FileInputStream in3 = new FileInputStream(fdin);

            // 测试read()，从中读取一个字节
            char c1 = (char) in1.read();
            System.out.println("c1=" + c1);
            //运行结果：c1=a

            // 测试skip(long byteCount)，跳过25个字节
            in1.skip(25);

            // 测试read(byte[] buffer, int byteOffset, int byteCount)
            byte[] buf = new byte[10];
            in1.read(buf, 0, buf.length);
            System.out.println("buf=" + (new String(buf)));
            //运行结果：buf=0123456789

            // 创建“FileInputStream”对象对应的BufferedInputStream
            BufferedInputStream bufIn = new BufferedInputStream(in3);
            // 读取一个字节
            char c2 = (char) bufIn.read();
            System.out.println("c2=" + c2);
            //运行结果：c2=a
            in1.close();
            in2.close();
            in3.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
~~~
