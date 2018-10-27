package com.wolfman.java.multi.thread.mysynchronized;

public class Demo {

    private static int count = 0;
    public static void inc(){
        synchronized (Demo.class){
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            count++;
        }
    }
    public static void main(String[] args) throws InterruptedException {
        for(int i=0;i<1000;i++){
            new Thread(()->Demo.inc()).start();
        }
        Thread.sleep(3000);
        System.out.println("运行结果"+count);
    }

}
