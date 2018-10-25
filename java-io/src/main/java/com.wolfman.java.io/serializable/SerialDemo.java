package com.wolfman.java.io.serializable;

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
class Box implements Externalizable{

    private String name;

    private int wight;

    private int hight;

    public Box(){
    }

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

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {

    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {

    }
}