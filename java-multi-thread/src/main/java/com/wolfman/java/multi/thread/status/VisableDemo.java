package com.wolfman.java.multi.thread.status;

import java.util.concurrent.TimeUnit;

public class VisableDemo {

    private  static volatile boolean stop = false;

    public static void main(String[] args) throws InterruptedException {

        Thread thread = new Thread(()->{
            int i = 0;
            while (!stop){
                i++;
            }
        });
        thread.start();
        TimeUnit.SECONDS.sleep(1);
        stop = true;
    }


}
