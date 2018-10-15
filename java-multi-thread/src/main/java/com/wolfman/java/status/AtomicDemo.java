package com.wolfman.java.status;

public class AtomicDemo {

    private static int count = 0;

    public static void icc(){
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        count++;
    }

    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 1000; i++) {
            new Thread(AtomicDemo::icc).start();
        }
        Thread.sleep(4000);
        System.out.println("运行结果：" + count);
    }




}
