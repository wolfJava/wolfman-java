## jvm 调优-工具篇

jvm监控分析工具一般分为两类，一种是jdk自带的工具，一种是第三方的分析工具。

jdk自带工具一般在jdk bin目录下面，以exe的形式直接点击就可以使用，其中包含分析工具已经很强大，几乎涉及了方方面面，但是我们最常使用的只有两款：jconsole.exe和jvisualvm.exe；

第三方的分析工具有很多，各自的侧重点不同，比较有代表性的：MAT(Memory Analyzer Tool)、GChisto等。

对于大型 JAVA 应用程序来说，再精细的测试也难以堵住所有的漏洞，即便我们在测试阶段进行了大量卓有成效的工作，很多问题还是会在生产环境下暴露出来，并且很难在测试环境中进行重现。JVM 能够记录下问题发生时系统的部分运行状态，并将其存储在堆转储 (Heap Dump) 文件中，从而为我们分析和诊断问题提供了重要的依据。其中 VisualVM 和 MAT 是 dump 文件的分析利器。

### 一 jdk 自带的工具

#### 1 jconsole

Jconsole（Java Monitoring and Management Console）是从java5开始，在JDK中自带的java监控和管理控台，用于对 JVM 中内存，线程和类等的监控，是一个基于 JMX（java management extensions）的 GUI 性能监测工具。jconsole 使用 jvm 的扩展机制获取并展示虚拟机中运行的应用程序的性能和资源消耗等信息。















































