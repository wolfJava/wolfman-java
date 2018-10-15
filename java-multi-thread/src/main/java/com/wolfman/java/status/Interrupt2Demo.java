package com.wolfman.java.status;

import java.util.concurrent.TimeUnit;

public class Interrupt2Demo {

    private static int i;

    public static void main(String[] args) throws
            InterruptedException{
        Thread thread=new Thread(()->{
            while(true){
                boolean ii=Thread.currentThread().isInterrupted();
                if(ii){
                    System.out.println("before:"+ii);
                    Thread.interrupted();//对线程进行复位，中断标识为false
                    System.out.println("after:"+Thread.currentThread()
                            .isInterrupted());
                }
                i++;
                System.out.println("Num:"+i);
            }
        });
        thread.start();
        TimeUnit.SECONDS.sleep(1);
        thread.interrupt();//设置中断标识,中断标识为 true
    }

}
