package com.wolfman.java.multi.thread.countdown;

import java.util.concurrent.CountDownLatch;

public class CountdownLatchDemo {
  public static void main(String[] args) throws InterruptedException {
    CountDownLatch countDownLatch=new CountDownLatch(3);
    new Thread(()->{
      countDownLatch.countDown();
    },"t1").start();
    new Thread(()->{
      countDownLatch.countDown();
    },"t2").start();
    new Thread(()->{
      countDownLatch.countDown();
    },"t3").start();
    countDownLatch.await();
    System.out.println("所有线程执行完毕");
  }
}
