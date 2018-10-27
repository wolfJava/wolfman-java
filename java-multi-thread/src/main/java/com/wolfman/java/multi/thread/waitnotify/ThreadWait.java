package com.wolfman.java.multi.thread.waitnotify;

public class ThreadWait extends Thread {

    private Object lock;

    public ThreadWait(Object lock) {
        this.lock = lock;
    }

    @Override
    public void run() {
        System.out.println("开始执行 thread wait"); try {
            lock.wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("执行结束 thread wait");
    }
}
