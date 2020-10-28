## 学习笔记
- ### 使用 GCLogAnalysis.java 自己演练一遍串行/并行/CMS/G1的案例
    - 运行命令```javac -encoding utf-8 GCLogAnalysis.java```
    - #### 串行GC
        - 运行命令```java -XX:+UseSerialGC -XX:+PrintGCDetails -Xmx512m GCLogAnalysis```
        - 运行结果
        ```
            [GC (Allocation Failure) [DefNew: 69376K->8640K(78016K), 0.0119704 secs] 69376K->24899K(251456K), 0.0122358 secs] [Times: user=0.02 sys=0.00, real=0.01 secs]
            ...
            [GC (Allocation Failure) [DefNew: 78014K->8639K(78016K), 0.0141863 secs][Tenured: 187586K->170831K(187720K), 0.0188096 secs] 238770K->170831K(265736K), [Metaspace: 2633K->2633K(1056768K)], 0.0340889 secs] [Times: user=0.02 sys=0.02, real=0.03 secs]
            ...
            [Full GC (Allocation Failure) [Tenured: 349461K->340471K(349568K), 0.0415942 secs] 506452K->340471K(506816K), [Metaspace: 2633K->2633K(1056768K)], 0.0422082 secs] [Times: user=0.05 sys=0.00, real=0.04 secs]
            ...
            执行结束!共生成对象次数:11192
            Heap
             def new generation   total 157248K, used 5950K [0x00000000e0000000, 0x00000000eaaa0000, 0x00000000eaaa0000)
              eden space 139776K,   4% used [0x00000000e0000000, 0x00000000e05cf8b0, 0x00000000e8880000)
              from space 17472K,   0% used [0x00000000e8880000, 0x00000000e8880000, 0x00000000e9990000)
              to   space 17472K,   0% used [0x00000000e9990000, 0x00000000e9990000, 0x00000000eaaa0000)
             tenured generation   total 349568K, used 348677K [0x00000000eaaa0000, 0x0000000100000000, 0x0000000100000000)
               the space 349568K,  99% used [0x00000000eaaa0000, 0x00000000fff21440, 0x00000000fff21600, 0x0000000100000000)
             Metaspace       used 2639K, capacity 4486K, committed 4864K, reserved 1056768K
              class space    used 291K, capacity 386K, committed 512K, reserved 1048576K
        ```
        - 分析
            - GC：表示这是一次垃圾回收，如果有Full修饰，表示这是一次full GC
            - Allocation Failure：此次GC是因为young区没有足够的空间导致内存分配失败触发的
            - DefNew：表示young区发生GC，即新生代，该名称是收集器的名称
            - [DefNew: 139765K->139765K(157248K), 0.0003002 secs]：[新生代GC: GC前已使用容量->GC后已使用容量(新生代总容量), GC耗时]
            - Tenured：表示Old区发生GC
            - [Tenured: 340471K->349558K(349568K), 0.0254521 secs]：[老年代GC: GC前已使用容量->GC后已使用容量(老年代总容量), GC耗时]
            - 238770K->170831K(265736K)：GC前Java堆使用量->GC后Java堆使用容量(Java堆总容量)
            - Metaspace：元空间发生了GC
            - [Metaspace: 2633K->2633K(1056768K)], 0.0318588 secs]：[Metaspace: meta区GC前使用容量->meta区GC后使用容量(meta区总容量)], GC耗时]
            - [Times: user=0.05 sys=0.00, real=0.04 secs]
                - real：指的是在此次GC事件中所花费的总时间；
                - user：指的是CPU工作在用户态所花费的时间；
                - sys：指的是CPU工作在内核态所花费的时间。
                - user + sys 就是CPU花费的实际时间，注意这个值统计了所有CPU上的时间，如果进程工作在多线程的环境下，这个值是会超出 real 所记录的值的，即 user + sys >= real
                
    - #### 并行GC
        - 运行命令```java -XX:+UseParallelGC -XX:+PrintGCDetails -Xmx512m GCLogAnalysis```
        - 运行结果
        ```
            [GC (Allocation Failure) [PSYoungGen: 65010K->10748K(75776K)] 65010K->24143K(249344K), 0.0047742 secs] [Times: user=0.03 sys=0.05, real=0.00 secs]
            ...
            [Full GC (Ergonomics) [PSYoungGen: 10743K->0K(163840K)] [ParOldGen: 158455K->144686K(283648K)] 169198K->144686K(447488K), [Metaspace: 2633K->2633K(1056768K)], 0.0185485 secs] [Times: user=0.08 sys=0.00, real=0.02 secs]
            [Full GC (Ergonomics) [PSYoungGen: 20354K->0K(116736K)] [ParOldGen: 261108K->217765K(349696K)] 281462K->217765K(466432K), [Metaspace: 2633K->2633K(1056768K)], 0.0357068 secs] [Times: user=0.06 sys=0.00, real=0.04 secs]
            ...
            [Full GC (Ergonomics) [PSYoungGen: 58403K->0K(116736K)] [ParOldGen: 346725K->347134K(349696K)] 405128K->347134K(466432K), [Metaspace: 2633K->2633K(1056768K)], 0.0366310 secs] [Times: user=0.17 sys=0.00, real=0.04 secs]
            执行结束!共生成对象次数:9845
            Heap
             PSYoungGen      total 116736K, used 3020K [0x00000000f5580000, 0x0000000100000000, 0x0000000100000000)
              eden space 58880K, 5% used [0x00000000f5580000,0x00000000f5873020,0x00000000f8f00000)
              from space 57856K, 0% used [0x00000000fc780000,0x00000000fc780000,0x0000000100000000)
              to   space 57856K, 0% used [0x00000000f8f00000,0x00000000f8f00000,0x00000000fc780000)
             ParOldGen       total 349696K, used 347134K [0x00000000e0000000, 0x00000000f5580000, 0x00000000f5580000)
              object space 349696K, 99% used [0x00000000e0000000,0x00000000f52ffa18,0x00000000f5580000)
             Metaspace       used 2639K, capacity 4486K, committed 4864K, reserved 1056768K
              class space    used 291K, capacity 386K, committed 512K, reserved 1048576K
        ```
        - 分析
            - Ergonomics：表示此次GC是因为JVM自适应调节触发的
            - PSYoungGen：表示GC发生的区域是young区
            - ParOldGen：表示此次GC发生的区域是old区

    - #### CMS
        - 运行命令```java -XX:+UseConcMarkSweepGC -XX:+PrintGCDetails -Xmx512m GCLogAnalysis```
        - 运行结果
        ```
            [GC (Allocation Failure) [ParNew: 69376K->8640K(78016K), 0.0042976 secs] 69376K->25652K(251456K), 0.0047561 secs] [Times: user=0.00 sys=0.00, real=0.01 secs]
            ...
            [GC (Allocation Failure) [ParNew: 78016K->8640K(78016K), 0.0115251 secs] 141906K->96962K(251456K), 0.0119596 secs] [Times: user=0.06 sys=0.00, real=0.01 secs]
            [GC (CMS Initial Mark) [1 CMS-initial-mark: 88322K(173440K)] 99132K(251456K), 0.0011011 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
            [CMS-concurrent-mark-start]
            [CMS-concurrent-mark: 0.001/0.001 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
            [CMS-concurrent-preclean-start]
            [CMS-concurrent-preclean: 0.000/0.000 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
            [CMS-concurrent-abortable-preclean-start]
            [GC (Allocation Failure) [ParNew: 77950K->8639K(78016K), 0.0095904 secs] 166273K->118671K(251456K), 0.0112007 secs] [Times: user=0.00 sys=0.00, real=0.01 secs]
            ...
            [GC (Allocation Failure) [ParNew: 77535K->8639K(78016K), 0.0130944 secs] 393648K->351752K(421792K), 0.0132549 secs] [Times: user=0.08 sys=0.00, real=0.01 secs]
            [CMS-concurrent-abortable-preclean: 0.006/0.218 secs] [Times: user=0.66 sys=0.06, real=0.22 secs]
            [GC (CMS Final Remark) [YG occupancy: 12457 K (78016 K)][Rescan (parallel) , 0.0002031 secs][weak refs processing, 0.0000355 secs][class unloading, 0.0002008 secs][scrub symbol table, 0.0002814 secs][scrub string table, 0.0001066 secs][1 CMS-remark: 343112K(343776K
            )] 355570K(421792K), 0.0013509 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
            [CMS-concurrent-sweep-start]
            [CMS-concurrent-sweep: 0.000/0.000 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
            [CMS-concurrent-reset-start]
            [CMS-concurrent-reset: 0.001/0.001 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
            ...
            [CMS-concurrent-reset: 0.000/0.000 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
            [GC (Allocation Failure) [ParNew: 156684K->156684K(157248K), 0.0003987 secs][CMS: 347720K->349510K(349568K), 0.0468196 secs] 504405K->363954K(506816K), [Metaspace: 2633K->2633K(1056768K)], 0.0493029 secs] [Times: user=0.05 sys=0.00, real=0.05 secs]
            [GC (CMS Initial Mark) [1 CMS-initial-mark: 349510K(349568K)] 364675K(506816K), 0.0013339 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
            [CMS-concurrent-mark-start]
            执行结束!共生成对象次数:11087
            Heap
             par new generation  [CMS-concurrent-mark: 0.001/0.001 secs] total 157248K, used 20749K [0x00000000e0000000, 0x00000000eaaa0000, 0x00000000eaaa0000)
              eden space 139776K,  14% used [0x00000000e0000000, 0x00000000e14435a8, 0x00000000e8880000)
              from space 17472K,   0% used [0x00000000e9990000, 0x00000000e9990000, 0x00000000eaaa0000)
              to   space 17472K,   0% used [0x00000000e8880000, 0x00000000e8880000, 0x00000000e9990000)
             concurrent mark-sweep generation [Times: user=0.00 sys=0.00, real=0.00 secs]
             total 349568K, used 349510K [0x00000000eaaa0000, 0x0000000100000000, 0x0000000100000000)
             Metaspace       used 2639K, capacity 4486K, committed 4864K, reserved 1056768K
            [CMS-concurrent-preclean-start]
              class space    used 291K, capacity 386K, committed 512K, reserved 1048576K

        ```
        - 分析
            - ParNew：表示新生代发生GC
            - CMS Initial Mark：初始标记阶段，开始收集老年代GC Roots和直接引用的对象，此处也表示当前触发了老年代的GC
            - [[1 CMS-initial-mark: 88322K(173440K)] 99132K(251456K), 0.0011011 secs]：[[1 初始标记阶段: 当前老年代使用量(老年代总量)] 当前堆使用量(当前堆总量)，耗时时间]
            - CMS-concurrent-mark-start：并发标记开始，根据Initial Mark标记的根对象，标记所有的老年代存活对象
            - [CMS-concurrent-mark: 0.001/0.001 secs]：该阶段的持续时间/时钟时间
            - CMS-concurrent-preclean-start：并发预清理阶段，标记前一个阶段已经标记，但是在这一阶段引用状态又发生变化的对象
            - CMS-concurrent-abortable-preclean-start：可终止并发预清理阶段，该阶段从CMS-concurrent-preclean-start阶段结束一直持续到CMS Final Remark，会尽量尝试清空eden区防止CMS GC后又立刻进行Minor GC
            - CMS Final Remark：最终标记阶段，完成老年代中所有存活对象的标记
            - [YG occupancy: 12457 K (78016 K)]：年轻代使用情况
            - [Rescan (parallel) , 0.0002031 secs]：重新标记所花的时间
            - [weak refs processing, 0.0000355 secs]：处理弱引用所花的时间
            - [class unloading, 0.0002008 secs]：下载无用class所花的时间
            - [scrub symbol table, 0.0002814 secs]：清理元数据所花的时间
            - [scrub string table, 0.0001066 secs]：清理内部字符串所花的时间
            - [1 CMS-remark: 343112K(343776K)] 355570K(421792K), 0.0013509 secs]
            - CMS-concurrent-sweep-start：并发清理阶段，进行垃圾回收
            - CMS-concurrent-reset-start：并发重置阶段，重置 CMS 算法相关的内部数据，为下一次 GC 循环做准备
    - #### G1
        - 运行命令```java -XX:+UseG1GC -Xmx512m -XX:+PrintGC -XX:+PrintGCDateStamps -Xloggc:gc.demo.log GCLogAnalysis```
        - 运行结果
        ```
            [GC pause (G1 Evacuation Pause) (young) 26M->8980K(254M), 0.0028786 secs]
            ...
            [GC pause (G1 Humongous Allocation) (young) (initial-mark) 125M->100M(254M), 0.0033527 secs]
            [GC concurrent-root-region-scan-start]
            [GC concurrent-root-region-scan-end, 0.0002982 secs]
            [GC concurrent-mark-start]
            [GC concurrent-mark-end, 0.0009019 secs]
            [GC remark, 0.0009053 secs]
            [GC cleanup 110M->110M(254M), 0.0005681 secs]
            ...
            [Full GC (Allocation Failure)  437M->340M(512M), 0.0317689 secs]
            ...
            [GC pause (G1 Evacuation Pause) (young) 393M->370M(512M), 0.0018345 secs]
            [GC pause (G1 Evacuation Pause) (mixed)-- 396M->380M(512M), 0.0026021 secs]
            [GC pause (G1 Humongous Allocation) (young) (initial-mark) 381M->380M(512M), 0.0008806 secs]
            [GC concurrent-root-region-scan-start]
            [GC concurrent-root-region-scan-end, 0.0001741 secs]
            [GC concurrent-mark-start]
            [GC concurrent-mark-end, 0.0017648 secs]
            [GC remark, 0.0011781 secs]
            [GC cleanup 403M->403M(512M), 0.0006581 secs]
            执行结束!共生成对象次数:11116

        ```
        - 分析
            - GC pause：表示GC造成了停顿
            - (G1 Evacuation Pause)：将活着的对象从一个区域拷贝到另一个区域，也称作转移阶段，此处表示该操作触发了GC
            - (G1 Humongous Allocation)：表示此次GC是由于为大型对象分配内存造成的
            - (young)：表示这是一个Young GC事件
            - (initial-mark)：表示进行了初始标记，标记存活对象
            - GC concurrent-root-region-scan-start：Root区扫描开始
            - GC concurrent-root-region-scan-end：Root区扫描结束
            - GC concurrent-mark-start：并发标记开始
            - GC concurrent-mark-end：并发标记结束
            - GC remark：再次标记
            - GC cleanup：清理
