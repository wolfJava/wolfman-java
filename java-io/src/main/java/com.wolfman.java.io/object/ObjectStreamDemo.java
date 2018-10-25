package com.wolfman.java.io.object;

import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ObjectStreamDemo {

    private static final String TEMP_FILE_NAME = "d://object.tmp";

    public static void main(String[] args) {
        write();
        read();
        //运行结果
        //boolean:true
        //byte:96
        //char:a
        //int:2017
        //float:2.450000
        //double:1.414000
        //one    -- red
        //two    -- green
        //three  -- blue
        //box: Box{width=80, height=48, name='desk'}
    }

    private static void write() {
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void read() {
        try {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(TEMP_FILE_NAME));
            System.out.printf("boolean:%b\n" , in.readBoolean());
            System.out.printf("byte:%d\n" , (in.readByte()&0xff));
            System.out.printf("char:%c\n" , in.readChar());
            System.out.printf("int:%d\n" , in.readInt());
            System.out.printf("float:%f\n" , in.readFloat());
            System.out.printf("double:%f\n" , in.readDouble());
            // 读取HashMap对象
            HashMap map = (HashMap) in.readObject();
            Iterator iter = map.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry)iter.next();
                System.out.printf("%-6s -- %s\n" , entry.getKey(), entry.getValue());
            }
            // 读取Box对象，Box实现了Serializable接口
            Box box = (Box) in.readObject();
            System.out.println("box: " + box);
            in.close();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}

class Box implements Serializable{

    private int width;

    private int height;

    private String name;

    public Box(String name, int width, int height) {
        this.width = width;
        this.height = height;
        this.name = name;
    }

    @Override
    public String toString() {
        return "Box{" +
                "width=" + width +
                ", height=" + height +
                ", name='" + name + '\'' +
                '}';
    }
}
