package com.wolfman.java.io.pipedreaderwriter;

import java.io.IOException;
import java.io.PipedWriter;

/**
 * 发送者线程
 */
public class Sender extends Thread {

    // 管道输出流对象。
    // 它和“管道输入流(PipedReader)”对象绑定，
    // 从而可以将数据发送给“管道输入流”的数据，然后用户可以从“管道输入流”读取数据。
    private PipedWriter out = new PipedWriter();

    // 获得“管道输出流”对象
    public PipedWriter getWriter() {
        return out;
    }

    @Override
    public void run() {
//        writeShortMessage();
        writeLongMessage();

    }

    private void writeLongMessage() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 102; i++) {
            sb.append("0123456789");
        }
        sb.append("abcdefghijklmnopqrstuvwxyz");
        String str = sb.toString();
        try {
            out.write(str);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 向“管道输出流”中写入一则较简短的消息："this is a short message"
    private void writeShortMessage() {
        String strInfo = "this is a message!";
        try {
            out.write(strInfo);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
