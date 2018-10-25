## Serializable和Externalizable-序列化

### 一 序列化的作用和用途

序列化，就是为了**保存对象的状态**；而与之对应的反序列化，则可以**把保存的对象状态再读出来**。

通俗讲：**序列化/反序列化，是Java提供一种专门用于保存/恢复对象状态的机制**。

通常以下情况，可能会用到序列化：

1. 当你想把内存中的对象状态保存到一个文件中或者数据库中时候；
2. 当你想用套接字在网络上传输对象的时候。
3. 当你想通过RMI传输对象的时候。

### 二 演示程序—对象

~~~java
import java.io.*;

public class SerialDemo {

    private static final String FILE_NAME = "d://serial.tmp";

    public static void main(String[] args) {
        try {
            //=======将“对象”通过序列化保存=========
            // 获取文件TMP_FILE对应的对象输出流。
            // ObjectOutputStream中，只能写入“基本数据”或“支持序列化的对象”
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(FILE_NAME));
            // 创建Box对象，Box实现了Serializable序列化接口
            Box box = new Box("desk",80,48);
            // 将box对象写入到对象输出流out中，即相当于将对象保存到文件TMP_FILE中
            out.writeObject(box);
            // 打印“Box对象”
            System.out.println("testWrite box:"+box);
            out.close();

            //=======将“对象”通过序列化保存=========
            // 获取文件TMP_FILE对应的对象输入流。
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(FILE_NAME));
            // 从对象输入流中，读取先前保存的box对象。
            Box box1 = (Box) in.readObject();
            // 打印“Box对象”
            System.out.println("testWrite box1:"+box);
            in.close();
            
            //运行结果：
            //testWrite box:Box{name='desk', wight=80, hight=48}
		   //testWrite box1:Box{name='desk', wight=80, hight=48}
		   
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}

/**
 * Box类“支持序列化”。因为Box实现了Serializable接口。
 * 实际上，一个类只需要实现Serializable即可实现序列化，而不需要实现任何函数。
 */
class Box implements Serializable{

    private String name;

    private int wight;

    private int hight;

    public Box(String name, int wight, int hight) {
        this.name = name;
        this.wight = wight;
        this.hight = hight;
    }

    @Override
    public String toString() {
        return "Box{" +
                "name='" + name + '\'' +
                ", wight=" + wight +
                ", hight=" + hight +
                '}';
    }
}
~~~

通过上面的示例，我们知道：我们可以自定义类，让它支持序列化(即实现Serializable接口)，从而能支持对象的保存/恢复。 若要支持序列化，除了“自定义实现Serializable接口的类”之外；java的“基本类型”和“java自带的实现了Serializable接口的类”，都支持序列化。我们通过下面的示例去查看一下。 

### 三 演示示例—基本数据类型，java自带实现Serializable接口的类

~~~java
import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * “基本类型” 和 “java自带的实现Serializable接口的类” 对序列化的支持
 */
public class SerialDemo2 {

    private static final String TEMP_FILE_NAME = "d://object.tmp";

    public static void main(String[] args) {
        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(TEMP_FILE_NAME));
            out.writeBoolean(true);
            out.writeByte(96);
            out.writeChar('a');
            out.writeInt(2017);
            out.writeFloat(2.45f);
            out.writeDouble(1.414D);
            HashMap map = new HashMap();
            map.put("one", "red");
            map.put("two", "green");
            map.put("three", "blue");
            out.writeObject(map);
            // 写入自定义的Box对象，Box实现了Serializable接口
            Box box = new Box("desk", 80, 48);
            out.writeObject(box);
            out.close();

            ObjectInputStream in = new ObjectInputStream(new FileInputStream(TEMP_FILE_NAME));
            System.out.printf("boolean:%b\n" , in.readBoolean());
            System.out.printf("byte:%d\n" , (in.readByte()&0xff));
            System.out.printf("char:%c\n" , in.readChar());
            System.out.printf("int:%d\n" , in.readInt());
            System.out.printf("float:%f\n" , in.readFloat());
            System.out.printf("double:%f\n" , in.readDouble());
            // 读取HashMap对象
            map = (HashMap) in.readObject();
            Iterator iter = map.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry)iter.next();
                System.out.printf("%-6s -- %s\n" , entry.getKey(), entry.getValue());
            }
            in.close();

            //运行结果：
            //boolean:true
            //byte:65
            //char:a
            //int:20131015
            //float:3.140000
            //double:1.414000
            //two    -- green
            //one    -- red
            //three  -- blue

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}

class Box2 implements Serializable{

    private int width;

    private int height;

    private String name;

    public Box2(String name, int width, int height) {
        this.width = width;
        this.height = height;
        this.name = name;
    }

    @Override
    public String toString() {
        return "Box2{" +
                "width=" + width +
                ", height=" + height +
                ", name='" + name + '\'' +
                '}';
    }
}

~~~

“基本类型”、“java自带的支持Serializable接口的类”和“自定义实现Serializable接口的类”都能支持序列化。 

我们在介绍序列化定义时，说过“序列化/反序列化，是专门用于的保存/恢复对象状态的机制”。
从中，我们知道：序列化/反序列化，只支持保存/恢复对象状态，即仅支持保存/恢复类的成员变量，但不支持保存类的成员方法！
但是，序列化是不是对类的所有的成员变量的状态都能保存呢？
答案当然是**否定的**！
(01) 序列化对static和transient变量，是不会自动进行状态保存的。
​        transient的作用就是，用transient声明的变量，不会被自动序列化。
(02) 对于Socket, Thread类，不支持序列化。若实现序列化的接口中，有Thread成员；在对该类进行序列化操作时，编译会出错！
​        这主要是基于资源分配方面的原因。如果Socket，Thread类可以被序列化，但是被反序列化之后也无法对他们进行重新的资源分配；再者，也是没有必要这样实现。

下面，我们还是通过示例来查看“序列化对static和transient的处理”。

### 四 演示示例—static、transient关键字

~~~java
import java.io.*;

public class SerialDemo3 {

    private static final String FILE_NAME = "d://serial3.tmp";

    public static void main(String[] args) {
        testWrite();
        testRead();
        //运行结果：
        //testWrite box: [desk: (80, 48) ]
        //testRead  box: [desk: (80, 0) ]
    }
    
    /**
     * 将Box对象通过序列化，保存到文件中
     */
    private static void testWrite() {
        try {
            // 获取文件TMP_FILE对应的对象输出流。
            // ObjectOutputStream中，只能写入“基本数据”或“支持序列化的对象”
            ObjectOutputStream out = new ObjectOutputStream(
                    new FileOutputStream(FILE_NAME));
            // 创建Box对象，Box实现了Serializable序列化接口
            Box3 box = new Box3("desk", 80, 48);
            // 将box对象写入到对象输出流out中，即相当于将对象保存到文件TMP_FILE中
            out.writeObject(box);
            // 打印“Box对象”
            System.out.println("testWrite box: " + box);

            out.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 从文件中读取出“序列化的Box对象”
     */
    private static void testRead() {
        try {
            // 获取文件TMP_FILE对应的对象输入流。
            ObjectInputStream in = new ObjectInputStream(
                    new FileInputStream(FILE_NAME));
            // 从对象输入流中，读取先前保存的box对象。
            Box3 box = (Box3) in.readObject();
            // 打印“Box对象”
            System.out.println("testRead  box: " + box);
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

/**
 * Box类“支持序列化”。因为Box实现了Serializable接口。
 * 实际上，一个类只需要实现Serializable即可实现序列化，而不需要实现任何函数。
 */
class Box3 implements Serializable{

    private static int width;
    private transient int height;
    private String name;

    public Box3(String name, int width, int height) {
        this.name = name;
        this.width = width;
        this.height = height;
    }

    @Override
    public String toString() {
        return "["+name+": ("+width+", "+height+") ]";
    }
}
~~~

**结果分析**：

我们前面说过，“序列化不对static和transient变量进行状态保存”。因此，testWrite()中保存Box对象时，不会保存width和height的值。这点是毋庸置疑的！但是，为什么testRead()中读取出来的Box对象的width=80，而height=0呢？
先说，为什么height=0。因为Box对象中height是int类型，而int类型的默认值是0。
再说，为什么width=80。这是因为height是static类型，而static类型就意味着所有的Box对象都共用一个height值；而在testWrite()中，我们已经将height初始化为80了。因此，我们通过序列化读取出来的Box对象的height值，也被就是80。

理解上面的内容之后，我们应该可以推断出下面的代码的运行结果。 

~~~java
import java.io.*;

public class SerialDemo3 {

    private static final String FILE_NAME = "d://serial3.tmp";

    public static void main(String[] args) {
        testWrite();
        testRead();
        //运行结果：
        //testWrite box: [desk: (80, 48) ]
        //testRead  box: [desk: (100, 0) ]
    }

    /**
     * 将Box对象通过序列化，保存到文件中
     */
    private static void testWrite() {
        try {
            // 获取文件TMP_FILE对应的对象输出流。
            // ObjectOutputStream中，只能写入“基本数据”或“支持序列化的对象”
            ObjectOutputStream out = new ObjectOutputStream(
                    new FileOutputStream(FILE_NAME));
            // 创建Box对象，Box实现了Serializable序列化接口
            Box3 box = new Box3("desk", 80, 48);
            // 将box对象写入到对象输出流out中，即相当于将对象保存到文件TMP_FILE中
            out.writeObject(box);
            // 打印“Box对象”
            System.out.println("testWrite box: " + box);

            box = new Box3("room", 100, 50);

            out.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 从文件中读取出“序列化的Box对象”
     */
    private static void testRead() {
        try {
            // 获取文件TMP_FILE对应的对象输入流。
            ObjectInputStream in = new ObjectInputStream(
                    new FileInputStream(FILE_NAME));
            // 从对象输入流中，读取先前保存的box对象。
            Box3 box = (Box3) in.readObject();
            // 打印“Box对象”
            System.out.println("testRead  box: " + box);
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

/**
 * Box类“支持序列化”。因为Box实现了Serializable接口。
 * 实际上，一个类只需要实现Serializable即可实现序列化，而不需要实现任何函数。
 */
class Box3 implements Serializable{

    private static int width;
    private transient int height;
    private String name;

    public Box3(String name, int width, int height) {
        this.name = name;
        this.width = width;
        this.height = height;
    }

    @Override
    public String toString() {
        return "["+name+": ("+width+", "+height+") ]";
    }
}
~~~

SerialTest4.java 相比于 SerialTest3.java，在testWrite()中添加了一行代码box = new Box("room", 100, 50); 

现在，我们更加确认“序列化不对static和transient变量进行状态保存”。但是，若我们想要保存static或transient变量，能不能办到呢？ 当然可以！我们在类中重写两个方法writeObject()和readObject()即可。下面程序演示了如何手动保存static和transient变量。 

###五 演示示例—static、transient关键字属性如何进行状态保存

```java
import java.io.*;

public class SerialDemo4 {

    private static final String FILE_NAME = "d://serial3.tmp";

    public static void main(String[] args) {
        testWrite();
        testRead();
        //运行结果：
        //testWrite box: [desk: (80, 48) ]
        //testRead  box: [desk: (80, 48) ]
    }

    /**
     * 将Box对象通过序列化，保存到文件中
     */
    private static void testWrite() {
        try {
            // 获取文件TMP_FILE对应的对象输出流。
            // ObjectOutputStream中，只能写入“基本数据”或“支持序列化的对象”
            ObjectOutputStream out = new ObjectOutputStream(
                    new FileOutputStream(FILE_NAME));
            // 创建Box对象，Box实现了Serializable序列化接口
            Box4 box = new Box4("desk", 80, 48);
            // 将box对象写入到对象输出流out中，即相当于将对象保存到文件TMP_FILE中
            out.writeObject(box);
            // 打印“Box对象”
            System.out.println("testWrite box: " + box);

            box = new Box4("room", 100, 50);

            out.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 从文件中读取出“序列化的Box对象”
     */
    private static void testRead() {
        try {
            // 获取文件TMP_FILE对应的对象输入流。
            ObjectInputStream in = new ObjectInputStream(
                    new FileInputStream(FILE_NAME));
            // 从对象输入流中，读取先前保存的box对象。
            Box4 box = (Box4) in.readObject();
            // 打印“Box对象”
            System.out.println("testRead  box: " + box);
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

/**
 * Box类“支持序列化”。因为Box实现了Serializable接口。
 * 实际上，一个类只需要实现Serializable即可实现序列化，而不需要实现任何函数。
 */
class Box4 implements Serializable{

    private static int width;
    private transient int height;
    private String name;

    public Box4(String name, int width, int height) {
        this.name = name;
        this.width = width;
        this.height = height;
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();//使定制的writeObject()方法可以利用自动序列化中内置的逻辑。
        out.writeInt(height);
        out.writeInt(width);
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        height = in.readInt();
        width = in.readInt();
    }


    @Override
    public String toString() {
        return "["+name+": ("+width+", "+height+") ]";
    }
}
```

“序列化不会自动保存static和transient变量”，因此我们若要保存它们，则需要通过writeObject()和readObject()去手动读写。 

1. 通过writeObject()方法，写入要保存的变量。writeObject的原始定义是在ObjectOutputStream.java中，我们按照如下示例覆盖即可： 

   ~~~java
   private void writeObject(ObjectOutputStream out) throws IOException{ 
       out.defaultWriteObject();// 使定制的writeObject()方法可以利用自动序列化中内置的逻辑。 
       out.writeInt(ival);      // 若要保存“int类型的值”，则使用writeInt()
       out.writeObject(obj);    // 若要保存“Object对象”，则使用writeObject()
   }
   ~~~

2. 通过readObject()方法，读取之前保存的变量。readObject的原始定义是在ObjectInputStream.java中，我们按照如下示例覆盖即可： 

   ~~~java
   private void readObject(ObjectInputStream in) throws IOException,ClassNotFoundException{ 
       in.defaultReadObject();       // 使定制的readObject()方法可以利用自动序列化中内置的逻辑。 
       int ival = in.readInt();      // 若要读取“int类型的值”，则使用readInt()
       Object obj = in.readObject(); // 若要读取“Object对象”，则使用readObject()
   }
   ~~~

###六 演示示例—成员变量包含Thread不能序列化

~~~java
import java.io.*;

public class SerialDemo5 {

    private static final String FILE_NAME = "d://serial3.tmp";

    public static void main(String[] args) {
        testWrite();
        testRead();
        //运行结果：
        //testWrite box: [desk: (80, 48) ]
        //testRead  box: [desk: (80, 48) ]
    }

    /**
     * 将Box对象通过序列化，保存到文件中
     */
    private static void testWrite() {
        try {
            // 获取文件TMP_FILE对应的对象输出流。
            // ObjectOutputStream中，只能写入“基本数据”或“支持序列化的对象”
            ObjectOutputStream out = new ObjectOutputStream(
                    new FileOutputStream(FILE_NAME));
            // 创建Box对象，Box实现了Serializable序列化接口
            Box5 box = new Box5("desk", 80, 48);
            // 将box对象写入到对象输出流out中，即相当于将对象保存到文件TMP_FILE中
            out.writeObject(box);
            // 打印“Box对象”
            System.out.println("testWrite box: " + box);

            box = new Box5("room", 100, 50);

            out.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 从文件中读取出“序列化的Box对象”
     */
    private static void testRead() {
        try {
            // 获取文件TMP_FILE对应的对象输入流。
            ObjectInputStream in = new ObjectInputStream(
                    new FileInputStream(FILE_NAME));
            // 从对象输入流中，读取先前保存的box对象。
            Box5 box = (Box5) in.readObject();
            // 打印“Box对象”
            System.out.println("testRead  box: " + box);
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

/**
 * Box类“支持序列化”。因为Box实现了Serializable接口。
 * 实际上，一个类只需要实现Serializable即可实现序列化，而不需要实现任何函数。
 */
class Box5 implements Serializable{

    private static int width;
    private transient int height;
    private String name;

    private Thread thread = new Thread(){
        @Override
        public void run() {
            System.out.println("Serializable thread");
        }
    };


    public Box5(String name, int width, int height) {
        this.name = name;
        this.width = width;
        this.height = height;
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();//使定制的writeObject()方法可以利用自动序列化中内置的逻辑。
        out.writeInt(height);
        out.writeInt(width);
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        height = in.readInt();
        width = in.readInt();
    }


    @Override
    public String toString() {
        return "["+name+": ("+width+", "+height+") ]";
    }
}
~~~

**结果是，编译出错！** 

事实证明，不能对Thread进行序列化。若希望程序能编译通过，我们对Thread变量添加static或transient修饰即可！

### 八 Externalizable和完全定制序列化过程

如果一个类要完全负责自己的序列化，则实现Externalizable接口，而不是Serializable接口。

Externalizable接口定义包括两个方法writeExternal()与readExternal()。

需要注意的是：声明类实现Externalizable接口会有重大的安全风险。writeExternal()与readExternal()方法声明为public，恶意类可以用这些方法读取和写入对象数据。如果对象包含敏感信息，则要格外小心。

**说明**：

1. 实现Externalizable接口的类，不会像实现Serializable接口那样，会自动将数据保存。
2. 实现Externalizable接口的类，必须实现writeExternal()和readExternal()接口！否则，程序无法正常编译！
3. 实现Externalizable接口的类，必须定义不带参数的构造函数！否则，程序无法正常编译！
4. writeExternal() 和 readExternal() 的方法都是public的，不是非常安全！