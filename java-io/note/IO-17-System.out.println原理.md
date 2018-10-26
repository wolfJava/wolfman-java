## System.out.println("hello world")原理

初学java的第一个程序是"hello world" ：

~~~java
public class HelloWorld {
	public static void main(String[] args) {
    	System.out.println("hello world");
	}
}
~~~

上面程序到底是怎么在屏幕上输出“hello world”的呢？这就是本次讲解的内容，即System.out.println("hello world")的原理。 

我们先看看System.out.println的流程。先看看System.java中out的定义，源码如下： 

~~~java
public final class System {
    ...
    public final static PrintStream out = null;
    ...
}
~~~

**说明：**

​	out 是 System.java 的静态变量，而且 out 是 PrintStream 对象，PrintStream.java 中有许多重载的 println()方法。



**out的初始话过程**

 首先看看System.java的initializeSystemClass()方法。 

~~~java
private static void initializeSystemClass() {

        props = new Properties();
        initProperties(props); 

        sun.misc.VM.saveAndRemoveProperties(props);


        lineSeparator = props.getProperty("line.separator");
        sun.misc.Version.init();

        FileInputStream fdIn = new FileInputStream(FileDescriptor.in);
        FileOutputStream fdOut = new FileOutputStream(FileDescriptor.out);
        FileOutputStream fdErr = new FileOutputStream(FileDescriptor.err);
        setIn0(new BufferedInputStream(fdIn));
        setOut0(newPrintStream(fdOut, props.getProperty("sun.stdout.encoding")));
        setErr0(newPrintStream(fdErr, props.getProperty("sun.stderr.encoding")));

        loadLibrary("zip");

        Terminator.setup();

        sun.misc.VM.initializeOSEnvironment();

        Thread current = Thread.currentThread();
        current.getThreadGroup().add(current);

        setJavaLangAccess();

        sun.misc.VM.booted();
    }
~~~

我们只需要关注：即 

~~~java
FileOutputStream fdOut = new FileOutputStream(FileDescriptor.out); 
setOut0(new PrintStream(new BufferedOutputStream(fdOut, 128), true)); 
~~~

这两句话细分，为如下几步：

~~~java
FileDescriptor fd = FileDescriptor.out; 
FileOutputStream fdOut = new FileOutputStream(fd); 
BufferedOutputStream bufOut = new BufferedOutputStream(fdOut, 128); 
PrintStream ps = new PrintStream(bufout, true);
setOut0(ps);
~~~

**说明：**

1. 获取FileDescriptor.java中的静态成员out，out是一个FileDescriptor对象，它实际上是“标准输出(屏幕)”的标识符。 
2. 创建“标准输出(屏幕)”对应的“文件输出流”。 
3. 创建“文件输出流”对应的“缓冲输出流”。目的是为“文件输出流”添加“缓冲”功能。 
4. 创建“缓冲输出流”对应的“打印输出流”。目的是为“缓冲输出流”提供方便的打印接口，如print(), println(), printf()；使其能方便快捷的进行打印输出。 
5. 执行setOut0(ps); 



接下来，解析第5步的setOut0(ps)。查看System.java中setOut0()的声明，如下： 

~~~java
private static native void setOut0(PrintStream out);
~~~

从中，我们发现setOut0()是一个native本地方法。通过openjdk，我们可以找到它对应的源码，如下： 

~~~java
JNIEXPORT void JNICALL
Java_java_lang_System_setOut0(JNIEnv *env, jclass cla, jobject stream)
{
    jfieldID fid =
        (*env)->GetStaticFieldID(env,cla,"out","Ljava/io/PrintStream;");
    if (fid == 0)
        return;
    (*env)->SetStaticObjectField(env,cla,fid,stream);
}
~~~

**说明：**

这是个JNI函数，我们来对它进行简单的分析。 

1. 函数名
   1. JNIEXPORT void JNICALL Java_java_lang_System_setOut0(JNIEnv *env, jclass cla, jobject stream) 
   2. 这是JNI的静态注册方法，Java_java_lang_System_setOut0(JNIEnv *env, jclass cla, jobject stream)会和System.java中的setOut0(PrintStream out)关联； 
   3. 而且，参数stream 对应参数out。简单来说，我们调用setOut0()，实际上是调用的Java_java_lang_System_setOut0()。 
2. jfieldID fid = (*env)->GetStaticFieldID(env,cla,"out","Ljava/io/PrintStream;"); 
   1. **获取System.java的静态成员out的jfieldID**，"Ljava/io/PrintStream;"是说明out是java.io.PrintStream对象。 
   2. 获取out的jfieldID的作用，是我们需要通过操作“out的jfielID”来改变out的值。 
3. (*env)->SetStaticObjectField(env,cla,fid,stream); 
   1. **设置fid(fid就是out的jfieldID)对应的静态成员的值为stream。** 
   2. stream是我们传给Java_java_lang_System_setOut0()的参数，也就是传给setOut0的参数。 

总结上面的内容。我们知道，setOut0(PrintStream ps)的作用，**就是将ps设置为System.java的out静态变量。** 