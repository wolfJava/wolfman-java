package com.wolfman.java.io.serializable;

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
