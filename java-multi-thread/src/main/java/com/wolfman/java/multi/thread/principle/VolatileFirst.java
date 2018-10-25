package com.wolfman.java.multi.thread.principle;

public class VolatileFirst {

    private static int x = 0, y = 0;

    private static int a = 0, b = 0;

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> {
            a = 1;
            x = b; });
        Thread t2 = new Thread(() -> {
            b = 1;
            y = a; });
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println("x=" + x + "->y=" + y);
        //有四种结果
        // x=0,y=1; x=1,y=0; x=1,y=1; x=0,y=0;
        // t1->x=b t2->b=1 t2->y=a t1->a=1
    }
}
