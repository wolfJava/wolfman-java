package com.wolfman.java.multi.thread.atomic;

import java.util.concurrent.atomic.AtomicInteger;

public class AtomicIntegerDemo {

  private static AtomicInteger count = new AtomicInteger(0);

  public static synchronized void inc() {
    try {
      Thread.sleep(1);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    count.getAndIncrement();
  }

  public static void main(String[] args) throws InterruptedException {
    for (int i = 0; i < 1000; i++) {
      new Thread(() -> {
        AtomicIntegerDemo.inc();
      }).start();
    }
    Thread.sleep(4000);
    System.out.println(count.get());
  }


}
