### File

### 一 File介绍

File 是“**文件**”和“**目录路径名**”的抽象表示形式。 

File 直接继承于Object，实现了Serializable接口和Comparable接口。实现Serializable接口，意味着File对象支持序列化操作。而实现Comparable接口，意味着File对象之间可以比较大小；

File能直接被存储在有序集合(如TreeSet、TreeMap中)。 

### 二 File函数列表

~~~java
// 静态成员
public static final String pathSeparator        // 路径分割符":"
public static final char pathSeparatorChar    // 路径分割符':'
public static final String separator            // 分隔符"/"
public static final char separatorChar        // 分隔符'/'

// 构造函数
File(File dir, String name)
File(String path)
File(String dirPath, String name)
File(URI uri)

// 成员函数
boolean    canExecute()    // 测试应用程序是否可以执行此抽象路径名表示的文件。
boolean    canRead()       // 测试应用程序是否可以读取此抽象路径名表示的文件。
boolean    canWrite()      // 测试应用程序是否可以修改此抽象路径名表示的文件。
int    compareTo(File pathname)    // 按字母顺序比较两个抽象路径名。
boolean    createNewFile()         // 当且仅当不存在具有此抽象路径名指定名称的文件时，不可分地创建一个新的空文件。
static File    createTempFile(String prefix, String suffix)    // 在默认临时文件目录中创建一个空文件，使用给定前缀和后缀生成其名称。
static File    createTempFile(String prefix, String suffix, File directory)    // 在指定目录中创建一个新的空文件，使用给定的前缀和后缀字符串生成其名称。
boolean    delete()             // 删除此抽象路径名表示的文件或目录。
void    deleteOnExit()       // 在虚拟机终止时，请求删除此抽象路径名表示的文件或目录。
boolean    equals(Object obj)   // 测试此抽象路径名与给定对象是否相等。
boolean    exists()             // 测试此抽象路径名表示的文件或目录是否存在。
File    getAbsoluteFile()    // 返回此抽象路径名的绝对路径名形式。
String    getAbsolutePath()    // 返回此抽象路径名的绝对路径名字符串。
File    getCanonicalFile()   // 返回此抽象路径名的规范形式。
String    getCanonicalPath()   // 返回此抽象路径名的规范路径名字符串。
long    getFreeSpace()       // 返回此抽象路径名指定的分区中未分配的字节数。
String    getName()            // 返回由此抽象路径名表示的文件或目录的名称。
String    getParent()          // 返回此抽象路径名父目录的路径名字符串；如果此路径名没有指定父目录，则返回 null。
File    getParentFile()      // 返回此抽象路径名父目录的抽象路径名；如果此路径名没有指定父目录，则返回 null。
String    getPath()            // 将此抽象路径名转换为一个路径名字符串。
long    getTotalSpace()      // 返回此抽象路径名指定的分区大小。
long    getUsableSpace()     // 返回此抽象路径名指定的分区上可用于此虚拟机的字节数。
int    hashCode()               // 计算此抽象路径名的哈希码。
boolean    isAbsolute()         // 测试此抽象路径名是否为绝对路径名。
boolean    isDirectory()        // 测试此抽象路径名表示的文件是否是一个目录。
boolean    isFile()             // 测试此抽象路径名表示的文件是否是一个标准文件。
boolean    isHidden()           // 测试此抽象路径名指定的文件是否是一个隐藏文件。
long    lastModified()       // 返回此抽象路径名表示的文件最后一次被修改的时间。
long    length()             // 返回由此抽象路径名表示的文件的长度。
String[]    list()           // 返回一个字符串数组，这些字符串指定此抽象路径名表示的目录中的文件和目录。
String[]    list(FilenameFilter filter)    // 返回一个字符串数组，这些字符串指定此抽象路径名表示的目录中满足指定过滤器的文件和目录。
File[]    listFiles()                        // 返回一个抽象路径名数组，这些路径名表示此抽象路径名表示的目录中的文件。
File[]    listFiles(FileFilter filter)       // 返回抽象路径名数组，这些路径名表示此抽象路径名表示的目录中满足指定过滤器的文件和目录。
File[]    listFiles(FilenameFilter filter)   // 返回抽象路径名数组，这些路径名表示此抽象路径名表示的目录中满足指定过滤器的文件和目录。
static File[]    listRoots()    // 列出可用的文件系统根。
boolean    mkdir()     // 创建此抽象路径名指定的目录。
boolean    mkdirs()    // 创建此抽象路径名指定的目录，包括所有必需但不存在的父目录。
boolean    renameTo(File dest)    // 重新命名此抽象路径名表示的文件。
boolean    setExecutable(boolean executable)    // 设置此抽象路径名所有者执行权限的一个便捷方法。
boolean    setExecutable(boolean executable, boolean ownerOnly)    // 设置此抽象路径名的所有者或所有用户的执行权限。
boolean    setLastModified(long time)       // 设置此抽象路径名指定的文件或目录的最后一次修改时间。
boolean    setReadable(boolean readable)    // 设置此抽象路径名所有者读权限的一个便捷方法。
boolean    setReadable(boolean readable, boolean ownerOnly)    // 设置此抽象路径名的所有者或所有用户的读权限。
boolean    setReadOnly()                    // 标记此抽象路径名指定的文件或目录，从而只能对其进行读操作。
boolean    setWritable(boolean writable)    // 设置此抽象路径名所有者写权限的一个便捷方法。
boolean    setWritable(boolean writable, boolean ownerOnly)    // 设置此抽象路径名的所有者或所有用户的写权限。
String    toString()    // 返回此抽象路径名的路径名字符串。
URI    toURI()    // 构造一个表示此抽象路径名的 file: URI。
URL    toURL()    // 已过时。 此方法不会自动转义 URL 中的非法字符。建议新的代码使用以下方式将抽象路径名转换为 URL：首先通过 toURI 方法将其转换为 URI，然后通过 URI.toURL 方法将 URI 装换为 URL。
~~~

1. **新建目录的常用方法**
   1. 方法1：根据相对路径新建目录

      ~~~java
      File file = new File("dir");
      file.mkdir();
      ~~~

   2. 方法2：根据绝对路径新建目录

      ~~~java
      File dir = new File("/home/skywang/dir");
      dir.mkdirs();
      ~~~

      1. 上面是在linux系统下新建目录“/home/skywang/dir”的源码。在windows下面，若要新建目录“D:/dir”，源码如下： 

         ~~~java
         File dir = new File("D:/dir");
         dir.mkdir();
         ~~~

   3. 方法3

      ~~~java
      URI uri = new URI("file:/home/skywang/dir"); 
      File dir = new File(uri);
      sub.mkdir();
      ~~~

      1. 说明： 和“方法2”类似，只不过“方法2”中传入的是完整路径，而“方法3”中传入的是完整路径对应URI。 

2. **新建子目录的几种常用方法**

   例如，我们想要在当前目录的子目录“dir”下，再新建一个子目录。有一下几种方法: 

   1. **方法1** 

      ~~~java
      File sub1 = new File("dir", "sub1");
      sub1.mkdir();
      ~~~

      上面的方法作用是，在当前目录下 "dir/sub1"。它能正常运行的前提是“sub1”的父目录“dir”已经存在！ 

   2. **方法2** 

      ~~~java
      File sub3 = new File("dir/sub3");
      sub3.mkdirs();
      ~~~

      上面的方法作用是，在当前目录下 "dir/sub3"。它不需要dir已经存在，也能正常运行；若“sub3”的父母路不存在，mkdirs()方法会自动创建父目录。 

   3. **方法3**

      ~~~java
      File sub4 = new File("/home/skywang/dir/sub4");
      sub4.mkdirs();
      ~~~

      上面的方法作用是，新建目录"/home/skywang/dir/sub3"。它不需要dir已经存在，也能正常运行；若“sub4”的父母路不存在，mkdirs()方法会自动创建父目录。 

   4. **方法4**

      ~~~java
      URI uri = new URI("file:/home/skywang/dir/sub5"); 
      File sub5 = new File(uri);
      sub5.mkdirs();
      ~~~

      和“方法3”类似，只不过“方法3”中传入的是完整路径，而“方法4”中传入的是完整路径对应URI。 

3. **新建文件的几种常用方法** 

   1. **方法1** 

      ~~~java
      try {
          File dir = new File("dir");    // 获取目录“dir”对应的File对象
          File file1 = new File(dir, "file1.txt");
          file1.createNewFile();
      } catch (IOException e) {
          e.printStackTrace();
      }
      ~~~

      上面代码作用是，在“dir”目录(相对路径)下新建文件“file1.txt”。 

   2. **方法2** 

      ~~~java
      try {
          File file2 = new File("dir", "file2.txt");
          file2.createNewFile();
      } catch (IOException e) {
          e.printStackTrace();
      }
      ~~~

      上面代码作用是，在“dir”目录(相对路径)下新建文件“file2.txt”。 

   3. **方法3** 

      ~~~java
      try {
          File file3 = new File("/home/skywang/dir/file3.txt");
          file3.createNewFile();
      } catch (IOException e) {
          e.printStackTrace();
      }
      ~~~

      上面代码作用是，下新建文件“/home/skywang/dir/file3.txt”(绝对路径)。这是在linux下根据绝对路径的方法，在windows下可以通过以下代码新建文件"D:/dir/file4.txt"。 

      ~~~java
      try {
          File file3 = new File("D:/dir/file4.txt");
          file3.createNewFile();
      } catch (IOException e) {
          e.printStackTrace();
      }
      ~~~

   4. **方法4** 

      ~~~java
      try {
          URI uri = new URI("file:/home/skywang/dir/file4.txt"); 
          File file4 = new File(uri);
          file4.createNewFile();
      } catch (IOException e) {
          e.printStackTrace();
      }
      ~~~

      和“方法3”类似，只不过“方法3”中传入的是完整路径，而“方法4”中传入的是完整路径对应URI。 


### 三 示例演示

~~~java
import java.io.File;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class FileTest {

    public static void main(String[] args) {
        testFileStaticFields() ;
        testFileDirAPIS() ;
    }

    public static void testFileStaticFields() {
        // 打印 路径分隔符":"
        System.out.printf("File.pathSeparator=\"%s\"\n", File.pathSeparator);
        // 打印 路径分隔符':'
        System.out.printf("File.pathSeparatorChar=\"%c\"\n", File.pathSeparatorChar);
        // 打印 分隔符"/"
        System.out.printf("File.separator=\"%s\"\n", File.separator);
        // 打印 分隔符'/'
        System.out.printf("File.separatorChar=\"%c\"\n", File.separatorChar);
    }

    public static void testFileDirAPIS() {
        try {
            // 新建目录 "dir"
            File dir = new File("dir");
            dir.mkdir();

            // 方法1：新建目录 "dir/sub1"。父目录“dir”必须已经存在！
            File sub1 = new File("dir", "sub1");
            sub1.mkdir();
            // 方法2：新建目录 "dir/sub2"。父目录“dir”必须已经存在！
            File sub2 = new File(dir, "sub2");
            sub2.mkdir();
            // 方法3：新建目录 "dir/sub3"。mkdirs()会自动创建不存在的父目录。
            File sub3 = new File("dir/sub3");
            sub3.mkdirs();
            // 方法4：新建目录 "dir/sub4"。根据“绝对路径”创建，前面3个方法都是根据“相对路径”创建。
            String dirPath = dir.getAbsolutePath();    // 获取“dir”的绝对路径
            String sub4AbsPath = dirPath + File.separator + "sub4";    // File.separator是分隔符"/"
            File sub4 = new File(sub4AbsPath);
            sub4.mkdirs();
            // 方法5：新建目录 "dir/sub5"。根据uri
            //file:/D:/workCloud/anew-learn/wolfman-ditribute/dir/sub2
            String uri_sub5_path = "file:\\"+ dirPath + File.separator + "sub5";
            URI uri_sub5 = new URI(uri_sub5_path.replace("\\","/"));
            File sub5 = new File(uri_sub5);
            sub5.mkdirs();

            // 方法1：新建文件 "dir/l1_normal.txt"
            File l1_normal = new File(dir, "l1_normal.txt");
            l1_normal.createNewFile();
            // 方法2：新建文件 "dir/.l1_hide.txt"。
            File l1_hide = new File("dir", ".l1_hide.txt"); // 在linux中, "."开头的文件是隐藏文件。
            l1_hide.createNewFile();
            // 方法3：新建文件 "dir/l1_abs.txt"。
            String dirAbsPah =  dir.getAbsolutePath();    // 获取dir的绝对路径
            String l1_abs_path = dirAbsPah+File.separator+"l1_abs.txt";
            File l1_abs = new File(l1_abs_path);
            l1_abs.createNewFile();
            //System.out.printf("l1_abs_path=%s\n", l1_abs_path);
            //System.out.printf("l1_abs path=%s\n", l1_abs.getAbsolutePath());
            // 方法4：新建文件 "dir/l1_uri.txt"。根据URI新建文件
//            String uri_path = "file:"+ dirAbsPah + File.separator + "l1_uri.txt";
//            URI uri_l1 = new URI(uri_path);
//            //System.out.printf("uri_l1=%s\n", l1_abs.getAbsolutePath());
//            File l1_uri = new File(uri_l1);
//            l1_uri.createNewFile();

            // 新建文件 "dir/sub/s1_normal"
            File s1_normal = new File(sub1, "s1_normal.txt");
            s1_normal.createNewFile();

            System.out.printf("%30s = %s\n", "s1_normal.exists()", s1_normal.exists());
            System.out.printf("%30s = %s\n", "s1_normal.getName()", s1_normal.getName());
            System.out.printf("%30s = %s\n", "s1_normal.getParent()", s1_normal.getParent());
            System.out.printf("%30s = %s\n", "s1_normal.getPath()", s1_normal.getPath());
            System.out.printf("%30s = %s\n", "s1_normal.getAbsolutePath()", s1_normal.getAbsolutePath());
            System.out.printf("%30s = %s\n", "s1_normal.getCanonicalPath()", s1_normal.getCanonicalPath());
            System.out.printf("%30s = %s is \"%s\"\n", "s1_normal.lastModified()", s1_normal.lastModified(), getModifyTime(s1_normal.lastModified()));
            System.out.printf("%30s = %s\n", "s1_normal.toURI()", s1_normal.toURI());


            // 列出“dir”目录下的“文件”和“文件夹”。
            // 注意：dir.listFiles()只会遍历目录dir，而不会遍历dir的子目录！
            System.out.println("---- list files and folders ----");
            File[] fs = dir.listFiles();
            for (File f:fs) {
                String fname = f.getName();
                String absStr = f.isAbsolute() ? "[Absolute]" : "";
                String hidStr = f.isHidden() ? "[Hidden]" : "";
                String dirStr = f.isDirectory() ? "[Directory]" : "";
                String fileStr = f.isFile() ? "[File]" : "";
                System.out.printf("%-30s  %s%s%s%s\n", fname, fileStr, dirStr, absStr, hidStr);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String getModifyTime(long millis) {
        // 获取Calendar对象
        Calendar cal = Calendar.getInstance();
        // 设置时间为 millis
        cal.setTimeInMillis(millis);
        // 获取格式化对象，它会按照"yyyy-MM-dd HH:mm:ss"格式化日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //System.out.printf("TIME %s\n", str);
        return sdf.format(cal.getTime());
    }
}
~~~

运行程序，会在源文件所在的目录新建目录"dir"及其子目录和子文件。
