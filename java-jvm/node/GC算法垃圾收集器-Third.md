## GC算法 垃圾收集器

**概述：**

垃圾收集（Garbage Collection）通常被称为“GC”。

JVM中，程序计数器、虚拟机栈、本地方法栈都是随线程而生随线程而灭，栈帧随着方法的进入和退出做入栈和出栈操作，实现了自动的内存清理，因此，我们的内存垃圾回收主要集中于java堆和方法区中，再程序运行期间，这部分内存的分配和使用都是动态的。

### 一 对象存活判断

判断对象是否存活一般有两种方式：

#### 1 引用计数——jvm没有用到

每个对象有一个引用计数属性，新增一个引用时计数加1，引用释放时计数减1，计数为0时可以回收。此方法简单，无法解决对象相互循环引用的问题。

#### 2 可达性分析（Reachability Analysis）

从GC Roots开始向下搜索，搜索所走过的路径成为引用链。当一个对象到GC Roots没有任何引用链相连时，则证明此对象是不可用的。不可达对象。

在Java语言中，GC Roots包括：

1. 虚拟机栈引用的对象。
2. 方法区中类静态属性实体引用的对象。
3. 方法区中常量引用的对象。
4. 本地方法栈中JNI引用的对象。

### 二 垃圾收集算法

#### 1 标记清除算法（Mark-Sweep）

如它名字一样，算法分为“标记”和“清除”两个阶段；首先标记出所有需要回收的对象，在标记完成后统一回收掉所有被标记的对象。之所以说它是最基础的收集算法，是因为后续的收集算法都是基于这种思路并对其缺点进行改进而得到的。

他的主要缺点有两个：一个是效率问题，标记和清除过程的效率都不高；另外一个是空间问题，标记清除之后会产生大量不连续内存碎片，空间碎片太多可能会导致：当程序在以后的运行过程中需要分配较大对象时，无法找到足够的连续内存而不得不提前触发另一次垃圾收集动作。

![](https://github.com/wolfJava/wolfman-java/blob/master/java-jvm/img/gc-1.jpg?raw=true)

#### 2 复制回收算法（Copying）

它将可用内存按容量划分为大小相等的两块，每次只使用其中的一块，当这一块的内存用完了，就将还存活的对象复制到另外一块上面，然后再把已使用过的内存空间一次清理掉。

这样使得每次都是对其中一块进行内存回收，内存分配时也就不用考虑内存碎片等复杂情况，只要移动堆顶指针，按顺序分配内存即可，实现简单，运行高效。只是这种算法的代价是将内存缩小为原来的一半，持续复制长生存期的对象则导致效率降低。

![](https://github.com/wolfJava/wolfman-java/blob/master/java-jvm/img/gc-2.jpg?raw=true)

#### 3 标记-压缩/整理算法

复制收集算法在对象存活率较高时就要执行较多的复制操作，效率将会变低。更关键的是，如果不愿意浪费50%的空间，就需要有额外的空间进行分配担保，以应对被使用的内存中所有对象都100%存活的极端情况，所以在老年代一般不能直接选用这种算法。

根据老年代的特点，有人提出了另外一种“标记-整理”（Mark-Compact）算法，标记过程仍然与“标记-清除”算法一样，但后续步骤不是直接对可回收对象进行清理，而是让所有存活的对象都向一端移动，然后直接清理掉端边界以外的内存

![](https://github.com/wolfJava/wolfman-java/blob/master/java-jvm/img/gc-3.jpg?raw=true)

#### 4 分代收集算法（Generational Collection）

GC分代的基本假设：绝大部分对象的声明周期都非常短暂，存活时间短。

该算法把Java堆分为新生代和老年代，这样就可以根据各个年代的特点采用最适当的收集算法。在新生代中，每次垃圾收集时都发现有大批对象死去，只有少量存活，那就选用复制算法，只需要付出少量存活对象的复制成本就可以完成收集。而老年代中因为对象存活率高、没有额外空间对他进行分配担保，就必须使用“标记-清理”或“标记-整理”算法来进行回收。

### 三 垃圾收集器

如果说收集算法是内存回收的方法论，垃圾收集器就是内存回收的具体实现。

#### 1 Serial收集器——串行收集器

串行收集器是最古老的，最稳定以及效率高的收集器，可能会产生较长的停顿，只使用一个线程去回收。新生代、老年代使用串行回收；新生代复制算法、老年代标记-压缩；垃圾收集的过程中会Stop TheWorld（服务暂停）；

参数控制：-XX：+UseSerialGC 串行收集器

![](https://github.com/wolfJava/wolfman-java/blob/master/java-jvm/img/gc-4.jpg?raw=true)

#### 2 ParNew收集器

ParNew收集器其实就是Serial收集器的多线程版本。

新生代并行，老年代串行；新生代复制算法、老年代标记-压缩

参数控制：

-XX：+UseParNewGC ParNew收集器

-XX：ParrallelGCThreads 限制线程数量

![](https://github.com/wolfJava/wolfman-java/blob/master/java-jvm/img/gc-5.jpg?raw=true)

#### 3 Parallel收集器

Parallel Scavenge收集器类似ParNew收集器，Parallel收集器更关注系统的吞吐量。可以通过参数来打开自适应调节策略，虚拟机会根据当前系统的运行情况收集性能监控信息，动态调整这些参数以提供最适合的停顿时间或最大的吞吐量；也可以通过参数控制GC的时间不大于多少号秒或者比例；

新生代复制算法、老年代标记-压缩；

参数控制

-XX：+UseParallelGC 使用Parallel收集器 + 老年带串行

#### 4 Parallel | Old 收集器

Parallel Old是Parallel Scavenge收集器的老年代版本，使用多线程和“标记-整理”算法。这个收集器是在JDK 1.6中才开始提供

参数控制:

-XX:+UseParallelOldGC 使用Parallel收集器 + 老年代并行

#### 5 CMS收集器

CMS(Concurrent Mark Sweep)收集器是一种以获取最短回收停顿时间为目标的收集器。目前很大一部分的Java应用都集中在互联网站或B/S系统的服务端上，这类引用尤其重视服务的响应速度,希望系统停顿时间最短,以给用户带来较好的体验。

从名字（包含“Mark Sweep”）上就可以看出CMS收集器是基于“标记-清除”算法实现的，它的运作过程相对于前面几种收集器来说要更复杂一些，整个过程分为4个步骤，包括：

1. 初始标记（CMS initial mark）
2. 并发标记（CMS concurrent mark）
3. 重新标记（CMS remark）
4. 并发清除（CMS concurrent sweep）

其中初始标记、重新标记这两个步骤仍然需要“Stop The World”。

初始标记仅仅只是标记一下GC Roots能直接关联到的对象，速度很快。

并发标记阶段就是进行GC Roots Tracing的过程。

重新标记阶段是为了修正并发标记期间，因用户程序继续运作而导致标记产生变动的那一部分对象的标记记录，这个阶段的停顿时间一般会比初始标记阶段稍长一些，但远比并发标记的时间短。

由于整个过程中耗时最长的并发标记和并发清除过程中，收集器线程都可以与用户线程一起工作，所以总体来说，CMS收集器的内存回收过程是与用户线程一起并发地执行。老年代收集器（新生代使用ParNew）

优点：并发收集、低停顿

缺点：产生大量空间碎片、并发阶段会降低吞吐量

参数控制：

-XX：+UseConcMarkSweepGC 使用CMS收集器

-XX：+UseCMSCompactAtFullCollection Full GC后，进行一次碎片整理；整理过程是独占的，会引起停顿时间变长。

-XX：+CMSFullGCsBeforeCompaction 设置进行几次Full GC后，进行一次碎片整理

-XX：ParallelCMSThreads 设定CMS的线程数量（一般情况约等于可用CPU数量）

![](https://github.com/wolfJava/wolfman-java/blob/master/java-jvm/img/gc-6.jpg?raw=true)

#### 6 G1收集器

G1是目前技术发展的最前沿成果之一，HotSpot开发团队赋予它的使命是未来可以替换掉JDK1.5中发布的CMS收集器。与CMS收集器相比G1收集器有以下特点：

1. 空间整合

2. 1. G1收集器采用标记整理算法，不会产生内存空间碎片。分配大对象时不会因为无法找到连续空间而提前触发下一次GC

3. 可预测停顿

4. 1. 这是G1的另一大优势，降低停顿时间是G1和CMS的共同关注点，但G1除了追求低停顿外，还能建立可预测的停顿时间模型，能让使用者明确指定在一个长度为N毫秒的时间片段内，消耗在垃圾收集上的时间不得超过N毫秒，这几乎已经是实时Java（RTSJ）的垃圾收集器的特征了。

上面提到的垃圾收集器，收集的范围都是整个新生代或老年代，而G1不再是这样。使用G1收集器时，Java堆的内存布局与其他收集器有很大差别，它讲整个java堆划分为多个大小相等的独立区域（Region），虽然还保留有新生代和老年代的概念，但新生代和老年代不再是物理隔阂了，他们都是一部分（可以不连续）Region的集合。

![](https://github.com/wolfJava/wolfman-java/blob/master/java-jvm/img/gc-7.jpg?raw=true)

G1的新生代收集和ParNew类似，当新生代占用达到一定比例的时候，开始触发收集。和CMS类似，G1收集器收集老年代对象会有短暂停顿。

收集步骤：

1. 标记阶段

2. 1. 首先初始标识（Inital-Mark），这个阶段是停顿的（Stop the World Event），并且会触发一次普通的Mintor GC。对应 GC log：GC pause（young）（inital-mark）

3. Root Region Scanning

4. 1. 程序运行过程中会回收survivor区（存活到老年代），这一过程必须再young GC之前完成。

5. Concurrent Mark

6. 1. 在整个堆中进行并发标记(和应用程序并发执行)，此过程可能被young GC中断。在并发标记阶段，若发现区域对象中的所有对象都是垃圾，那个这个区域会被立即回收(图中打X)。同时，并发标记过程中，会计算每个区域的对象活性(区域中存活对象的比例)。

![](https://github.com/wolfJava/wolfman-java/blob/master/java-jvm/img/gc-8.jpg?raw=true)

4. Remark

- 1. 再标记，会有短暂停顿(STW)。再标记阶段是用来收集并发标记阶段产生新的垃圾(并发阶段和应用程序一同运行)；G1中采用了比CMS更快的初始快照算法:snapshot-at-the-beginning (SATB)。

5. Copy/Clean up

- 1. 多线程清除失活对象，会有STW。G1将回收区域的存活对象拷贝到新区域，清除Remember Sets，并发清空回收区域并把它返回到空闲区域链表中。

![](https://github.com/wolfJava/wolfman-java/blob/master/java-jvm/img/gc-9.jpg?raw=true)

6. 复制/清除过程后。回收区域的活性对象已经被集中回收到深蓝色和深绿色区域。

![](https://github.com/wolfJava/wolfman-java/blob/master/java-jvm/img/gc-10.jpg?raw=true)

#### 7 常用的收集器组合

![](https://github.com/wolfJava/wolfman-java/blob/master/java-jvm/img/gc-11.jpg?raw=true)

|       | 新生代GC策略      | 年老代GC策略  | 说明                                                         |
| ----- | ----------------- | ------------- | ------------------------------------------------------------ |
| 组合1 | Serial            | Serial Old    | Serial和Serial Old都是单线程进行GC，特点就是GC时暂停所有应用线程。 |
| 组合2 | Serial            | CMSSerial Old | CMS（Concurrent Mark Sweep）是并发GC，实现GC线程和应用线程并发工作，不需要暂停所有应用线程。另外，当CMS进行GC失败时，会自动使用Serial Old策略进行GC。 |
| 组合3 | ParNew            | CMS           | 使用-XX:+UseParNewGC选项来开启。ParNew是Serial的并行版本，可以指定GC线程数，默认GC线程数为CPU的数量。可以使用-XX:ParallelGCThreads选项指定GC的线程数。如果指定了选项-XX:+UseConcMarkSweepGC选项，则新生代默认使用ParNew GC策略。 |
| 组合4 | ParNew            | Serial Old    | 使用-XX:+UseParNewGC选项来开启。新生代使用ParNew GC策略，年老代默认使用Serial Old GC策略。 |
| 组合5 | Parallel Scavenge | Serial Old    | Parallel Scavenge策略主要是关注一个可控的吞吐量：应用程序运行时间 / (应用程序运行时间 + GC时间)，可见这会使得CPU的利用率尽可能的高，适用于后台持久运行的应用程序，而不适用于交互较多的应用程序。 |
| 组合6 | Parallel Scavenge | Parallel Old  | Parallel Old是Serial Old的并行版本                           |
| 组合7 | G1GC              | G1GC          | -XX:+UnlockExperimentalVMOptions -XX:+UseG1GC#开启-XX:MaxGCPauseMillis =50    #暂停时间目标-XX:GCPauseIntervalMillis =200  #暂停间隔目标-XX:+G1YoungGenSize=512m  #年轻代大小-XX:SurvivorRatio=6     #幸存区比例 |