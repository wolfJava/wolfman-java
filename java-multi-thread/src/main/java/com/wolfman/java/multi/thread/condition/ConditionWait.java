package com.wolfman.java.multi.thread.condition;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class ConditionWait implements Runnable {
  private Lock lock;
  private Condition condition;

  public ConditionWait(Lock lock, Condition condition) {
    this.lock = lock;
    this.condition = condition;
  }

  @Override
  public void run() {
    System.out.println("begin -ConditionDemoWait");
    try {
      lock.lock();
      condition.await();
      System.out.println("end - ConditionDemoWait");
    } catch (InterruptedException e) {
      e.printStackTrace();
    } finally {
      lock.unlock();
    }
  }

}
