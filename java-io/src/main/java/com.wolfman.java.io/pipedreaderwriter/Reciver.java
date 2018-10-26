package com.wolfman.java.io.pipedreaderwriter;

import java.io.IOException;
import java.io.PipedReader;

/**
 * 接收者
 */
public class Reciver extends Thread {

    // 管道输入流对象
    // 它和“管道输出流(PipedWriter)”对象绑定，
    // 从而可以接收“管道输出流”的数据，再让用户读取。
    private PipedReader in = new PipedReader();

    // 获得“管道输入流对象”
    public PipedReader getPipedReader(){
        return in;
    }

    @Override
    public void run() {
//        readMessageOnce();
        readMessageContinued();
    }

    private void readMessageContinued() {
        int total = 0;
        while (true){
            char[] buf = new char[1024];
            try {
                int len = in.read(buf);
                total += len;
                System.out.println(new String(buf,0,len));
                // 若读取的字符总数>1024，则退出循环。
                if (total > 1024){
                    break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 从“管道输入流”中读取1次数据
    private void readMessageOnce() {
        // 虽然buf的大小是2048个字符，但最多只会从“管道输入流”中读取1024个字符。
        // 因为，“管道输入流”的缓冲区大小默认只有1024个字符。
        char[] buf = new char[2048];
        try {
            int len = in.read(buf);
            System.out.println(new String(buf,0,len));
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
