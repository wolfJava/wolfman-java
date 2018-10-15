package com.wolfman.java.multi.thread.simple.grace;

import java.util.concurrent.LinkedBlockingQueue;

public class SaveProcessor extends Thread implements RequestProcessor{

    LinkedBlockingQueue<Request> requests = new LinkedBlockingQueue<Request>();

    @Override
    public void run() {
        while (true) {
            try {
                Request request=requests.take();
                System.out.println("begin save request info:"+request);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    //处理请求
    public void processRequest(Request request) {
        requests.add(request);
    }

}
