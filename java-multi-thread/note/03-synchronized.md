## synchronize 实现原理

### 一 synchronized 的使用

在多线程并发编程中synchronized一直是元老级角色，很多人都会称呼它为重量级锁。但是，随着Java SE 1.6对
synchronized进行了各种优化之后，有些情况下它就并不那么重了，Java SE 1.6中为了减少获得锁和释放锁带来的性能消耗而引入的偏向锁和轻量级锁，以及锁的存储结构和升级过程。我们仍然沿用前面使用的案例，然后通过
synchronized关键字来修饰在inc的方法上。再看看执行结果。

~~~java
public class Demo {
    private static int count = 0;
    public static void inc(){
        synchronized (Demo.class){//全局锁
        //synchronized (this){ 对象锁
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
~~~

### 二 synchronized 的三种应用方式

Synchronized 有三种方式来加锁，分别是：

1. 修饰实例方法，作用于当前实例加锁，进入同步代码前要获得当前实例的锁
2. 静态方法，作用于当前类对象加锁，进入同步代码前要获得当前类对象的锁
3. 修饰代码块，指定加锁对象，对给定对象加锁，进入同步代码库前要获得给定对象的锁。

#### 1 synchronized 括号后面的对象

synchronized扩后后面的对象是一把锁，在java中任意一个对象都可以成为锁，简单来说，我们把object比喻是一
个key，拥有这个key的线程才能执行这个方法，拿到这个key以后在执行方法过程中，这个key是随身携带的，并且只有一把。如果后续的线程想访问当前方法，因为没有key所以不能访问只能在门口等着，等之前的线程把key放回去。所以，synchronized锁定的对象必须是同一个，如果是不同对象，就意味着是不同的房间的钥匙，对于访问者来说是没有任何影响的。

#### 2 synchronized 的字节码指令

通过 javap -v 来查看对应代码的字节码指令，对于同步块的实现使用了monitorenter和monitorexit指令，前面我 们在讲 JMM 的时候，提到过这两个指令，他们隐式的执行了Lock和UnLock操作，用于提供原子性保证。 monitorenter 指令插入到同步代码块开始的位置、monitorexit指令插入到同步代码块结束位置，jvm需要保证每 个monitorenter都有一个monitorexit对应。 

这两个指令，本质上都是对一个对象的监视器(monitor)进行获取，这个过程是排他的，也就是说同一时刻只能有 一个线程获取到由 synchronized 所保护对象的监视器。 

线程执行到 monitorenter 指令时，会尝试获取对象所对应的 monitor 所有权，也就是尝试获取对象的锁，而执行 monitorexit，就是释放monitor的所有权。

### 三 synchronized 锁的原理

jdk1.6以后对 synchronized 锁进行了优化，包含偏向锁、轻量级锁、重量级锁，在了解 synchronized 锁之前，我们需要了解两个重要的概念，一个是对象头、另一个是monitor。 

#### 1 对象头

在 Hotspot 虚拟机中，对象在内存中的布局分为三块区域：对象头、实例数据和对齐填充，Java对象头是实现
synchronized 的锁对象的基础，一般而言，synchronized 使用的锁对象是存储在Java对象头里。它是轻量级锁和偏向锁的关键。

##### 1.1 Mawrk Word

Mark Word用于存储对象自身的运行时数据，如哈希码(HashCode)、GC分代年龄、锁状态标志、线程持有的
锁、偏向线程 ID、偏向时间戳等等。Java对象头一般占有两个机器码(在32位虚拟机中，1个机器码等于4字节，
也就是32bit)

![](https://github.com/wolfJava/wolfman-java/blob/master/java-multi-thread/img/sync-1.jpg?raw=true)

在源码中的体现：

​	如果想更深入了解对象头在JVM源码中的定义，需要关心几个文件，oop.hpp/markOop.hpp/oop.hpp，每个 Java Object 在 JVM 内部都有一个 native 的 C++ 对象 oop/oopDesc 与之对应。先在oop.hpp中看 oopDesc的定义：

![](https://github.com/wolfJava/wolfman-java/blob/master/java-multi-thread/img/sync-2.jpg?raw=true)

_mark 被声明在 oopDesc 类的顶部，所以这个 _mark 可以认为是一个 头部, 前面我们讲过头部保存了一些重要的
状态和标识信息，在markOop.hpp文件中有一些注释说明markOop的内存布局

![](https://github.com/wolfJava/wolfman-java/blob/master/java-multi-thread/img/sync-3.jpg?raw=true)

#### 2 monitor

什么是Monitor？我们可以把它理解为一个同步工具，也可以描述为一种同步机制。所有的Java对象是天生的 Monitor，每个object的对象里 markOop->monitor() 里可以保存 ObjectMonitor 的对象。从源码层面分析一下 monitor对象。

Ø oop.hpp下的oopDesc类是JVM对象的顶级基类，所以每个object对象都包含markOop img 

Ø markOop.hpp 中 markOopDesc 继承自 oopDesc，并扩展了自己的monitor方法，这个方法返回一个 ObjectMonitor指针对象img。

Ø objectMonitor.hpp,在hotspot虚拟机中，采用ObjectMonitor类来实现monitor， 

![](https://github.com/wolfJava/wolfman-java/blob/master/java-multi-thread/img/sync-4.jpg?raw=true)

### 四 synchronized 的锁升级和获取过程

了解了对象头以及monitor以后，接下来去分析synchronized的锁的实现，就会非常简单了。前面讲过synchronized 的锁是进行过优化的，引入了偏向锁、轻量级锁；锁的级别从低到高逐步升级， 无锁->偏向锁->轻量级锁->重量级锁。

#### 1 自旋锁（CAS）

自旋锁就是让不满足条件的线程等待一段时间，而不是立即挂起。看持有锁的线程是否能够很快释放锁。怎么自旋 呢？其实就是一段没有任何意义的循环。

虽然它通过占用处理器的时间来避免线程切换带来的开销，但是如果持有锁的线程不能在很快释放锁，那么自旋的
线程就会浪费处理器的资源，因为它不会做任何有意义的工作。所以，自旋等待的时间或者次数是有一个限度的，
如果自旋超过了定义的时间仍然没有获取到锁，则该线程应该被挂起。

#### 2 偏向锁

大多数情况下，锁不仅不存在多线程竞争，而且总是由同一线程多次获得，为了让线程获得锁的代价更低而引入了
偏向锁。当一个线程访问同步块并获取锁时，会在对象头和栈帧中的锁记录里存储锁偏向的线程ID，以后该线程在进入和退出同步块时不需要进行CAS操作来加锁和解锁，只需简单地测试一下对象头的Mark Word里是否存储着指向当前线程的偏向锁。如果测试成功，表示线程已经获得了锁。如果测试失败，则需要再测试一下Mark Word中偏向锁的标识是否设置成1(表示当前是偏向锁):如果没有设置，则使用CAS竞争锁；如果设置了，则尝试使用CAS将对象头的偏向锁指向当前线程。

#### 3 轻量级锁

引入轻量级锁的主要目的是在多没有多线程竞争的前提下，减少传统的重量级锁使用操作系统互斥量产生的性能消
耗。当关闭偏向锁功能或者多个线程竞争偏向锁导致偏向锁升级为轻量级锁，则会尝试获取轻量级锁。

#### 4 重量级锁

重量级锁通过对象内部的监视器(monitor)实现，其中 monitor 的本质是依赖于底层操作系统的Mutex Lock实 现，操作系统实现线程之间的切换需要从用户态到内核态的切换，切换成本非常高。 

前面我们在讲Java对象头的时候，讲到了monitor这个对象，在hotspot虚拟机中，通过ObjectMonitor类来实现 monitor。他的锁的获取过程的体现会简单很多。

![](https://github.com/wolfJava/wolfman-java/blob/master/java-multi-thread/img/sync-5.jpg?raw=true)

### 五 wait 和 notify

wait和notify是用来让线程进入等待状态以及使得线程唤醒的两个操作。

~~~java
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
~~~

~~~java
public class ThreadNotify extends Thread {
    private Object lock;
    public ThreadNotify(Object lock) {
        this.lock = lock;
    }
    @Override
    public void run() {
        synchronized (lock){
            System.out.println("开始执行 thread notify"); lock.notify();
            lock.notify();
            System.out.println("执行结束 thread notify");
        }
    }
}
~~~

#### 1 wait和notify的原理

调用 wait 方法，首先会获取监视器锁，获得成功以后，会让当前线程进入等待状态进入等待队列并且释放锁;然后
当其他线程调用 notify 或者 notifyall 以后，会选择从等待队列中唤醒任意一个线程，而执行完 notify 方法以后，并不会立马唤醒线程，原因是当前的线程仍然持有这把锁，处于等待状态的线程无法获得锁。必须要等到当前的线程执行完按 monitorexit 指令以后，也就是锁被释放以后，处于等待队列中的线程就可以开始竞争锁了。

![](https://github.com/wolfJava/wolfman-java/blob/master/java-multi-thread/img/sync-6.jpg?raw=true)

#### 2 wait 和 notify 为什么需要在 synchronized 里面

wait方法的语义有两个，一个是释放当前的对象锁、另一个是使得当前线程进入阻塞队列， 而这些操作都和监视器是相关的，所以wait必须要获得一个监视器锁，而对于notify来说也是一样，它是唤醒一个线程，既然要去唤醒，首先得知道它在哪里？所以就必须要找到这个对象获取到这个对象的锁，然后到这个对象的等待队列中去唤醒一个线程。