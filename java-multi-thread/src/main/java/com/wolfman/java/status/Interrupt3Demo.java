package com.wolfman.java.status;

import java.util.concurrent.TimeUnit;

public class Interrupt3Demo {

    public static void main(String[] args) throws
            InterruptedException{
        Thread thread=new Thread(()->{
            while(true){
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    //抛出该异常，会将复位标识设置为 false
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        TimeUnit.SECONDS.sleep(1);
        thread.interrupt();//设置复位标识为 true
        TimeUnit.SECONDS.sleep(1);
        System.out.println(thread.isInterrupted());//false
    }

}
