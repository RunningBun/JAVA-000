## 学习笔记
- #### Hello.java字节码分析
    - Hello.java原文件
    ```java
      public class Hello {
      
          public static void main(String[] args) {
              int a = 12;
              int b = 3;
              int add = a + b;
              int sub = a - b;
              int mul = a * b;
              float div = a / b;
      
              int[] numbers = {1, 4, 6};
              int sum = 0;
      
              for (int i : numbers) {
                  if (i % 2 == 0) {
                      sum += i;
                  }
              }
          }
      }

    ```
    - 字节码文件
    ```
    Classfile /D:/study/Java进阶训练营/JAVA-000/Week_01/src/main/java/Hello.class
      Last modified 2020-10-19; size 481 bytes
      MD5 checksum 26f9d3f3849b1bbc506712175840e32b
      Compiled from "Hello.java"
    public class Hello
      minor version: 0 //JDK小版本号
      major version: 52 //JDK大版本号
      flags: ACC_PUBLIC, ACC_SUPER //该类的权限修饰符
    Constant pool:  // 常量池，数字相当于是常量池里的索引
       #1 = Methodref          #3.#15         // java/lang/Object."<init>":()V // 方法引用（符号引用）
       #2 = Class              #16            // Hello // 类引用
       #3 = Class              #17            // java/lang/Object
       #4 = Utf8               <init> // 这是字节码中构造函数的名称，而名称就是一段字符串，所以就会按照编码标识
       #5 = Utf8               ()V
       #6 = Utf8               Code
       #7 = Utf8               LineNumberTable
       #8 = Utf8               main
       #9 = Utf8               ([Ljava/lang/String;)V
      #10 = Utf8               StackMapTable
      #11 = Class              #18            // "[Ljava/lang/String;"
      #12 = Class              #19            // "[I"
      #13 = Utf8               SourceFile
      #14 = Utf8               Hello.java
      #15 = NameAndType        #4:#5          // "<init>":()V // 返回值
      #16 = Utf8               Hello
      #17 = Utf8               java/lang/Object
      #18 = Utf8               [Ljava/lang/String;
      #19 = Utf8               [I
    {
      public Hello(); // 构造函数
        descriptor: ()V // 方法描述符，这里的V表示void
        flags: ACC_PUBLIC // 权限修饰符
        Code:
          stack=1, locals=1, args_size=1
             0: aload_0
             1: invokespecial #1                  // Method java/lang/Object."<init>":()V
             4: return
          LineNumberTable:
            line 1: 0
    
      public static void main(java.lang.String[]);  // main方法
        descriptor: ([Ljava/lang/String;)V // 方法描述符，[表示引用了一个数组类型，L则表示引用的类后面跟的就是类名
        flags: ACC_PUBLIC, ACC_STATIC
        Code:
          // 操作数栈的深度1，当调用一个方法的时候，实际上在JVM里对应的是一个栈帧入栈出栈的过程
          // 本地变量表最大长度（slot为单位），64位的是2，其他是1，索引从0开始，如果是非static方法索引0代表this，后面是入参，后面是本地变量
          // 1个参数，实例方法多一个this参数
          stack=4, locals=13, args_size=1
             0: bipush        12  //将int常量值12读取压入栈中，因为取值为-128~127，所以采用bipush指令
             2: istore_1          //栈顶出栈，将int值保存到本地变量1
             3: iconst_3          //int常量值3入栈
             4: istore_2          //栈顶出栈，将int值保存到本地变量2
             5: iload_1           //从局部int变量1中取值，入栈
             6: iload_2           //从局部int变量2中取值，入栈
             7: iadd    //将栈顶两项int值相加，结果入栈
             8: istore_3    //栈顶出栈，将int值保存到本地变量3
             9: iload_1     //从局部int变量1中取值，入栈
            10: iload_2     //从局部int变量2中取值，入栈
            11: isub    //将栈顶两int类型数相减，结果入栈
            12: istore        4 //将栈顶int类型值保存到局部变量4中
            14: iload_1     //从局部int变量1中取值，入栈
            15: iload_2     //从局部int变量2中取值，入栈
            16: imul    //将栈顶两int类型数相乘，结果入栈
            17: istore        5 //将栈顶int类型值保存到局部变量5中
            19: iload_1     //从局部int变量1中取值，入栈
            20: iload_2     //从局部int变量2中取值，入栈
            21: idiv    //将栈顶两int类型数相除，结果入栈
            22: i2f     将栈顶int类型值转换为float类型值
            23: fstore        6 //将栈顶int类型值保存到局部变量6中
            25: iconst_3    //int常量值3入栈
            26: newarray       int //创建int类型的数组
            28: dup     //复制栈顶一个字长的数据，将复制后的数据压栈
            29: iconst_0    //int常量值0入栈，此处为数组下标0
            30: iconst_1    //int常量值1入栈，此处为下标0的元素值
            31: iastore     //将栈顶int类型值保存到指定int类型数组的指定项
            32: dup
            33: iconst_1    //int常量值1入栈，此处为数组下标1
            34: iconst_4    //int常量值4入栈，此处为下标1的元素值
            35: iastore
            36: dup
            37: iconst_2    //int常量值2入栈，此处为数组下标2
            38: bipush        6     //int常量值6入栈，此处为下标2的元素值
            40: iastore
            41: astore        7     //将一个操作数栈的值存储到数组变量7中的指令
            43: iconst_0
            44: istore        8
            46: aload         7     //将数组元素7中的数据入栈
            48: astore        9     //将栈顶值保存到变量9中
            50: aload         9
            52: arraylength         //获取一维数组的长度
            53: istore        10
            55: iconst_0
            56: istore        11
            58: iload         11
            60: iload         10
            62: if_icmpge     92 //若栈顶两int类型值前大于等于后则跳转到92下标
            65: aload         9
            67: iload         11
            69: iaload
            70: istore        12
            72: iload         12
            74: iconst_2
            75: irem                //将栈顶两int类型数取模，结果入栈
            76: ifne          86    //若栈顶int类型值不为0则跳转到86下标
            79: iload         8
            81: iload         12
            83: iadd                //将栈顶两int类型数相加，结果入栈
            84: istore        8
            86: iinc          11, 1 //本地变量11加1
            89: goto          58    //跳转到58下标
            92: return              //void函数返回
          LineNumberTable:      // 行号表
            line 4: 0           // 源代码的第4行，0代表字节码里的0
            line 5: 3
            line 6: 5
            line 7: 9
            line 8: 14
            line 9: 19
            line 11: 25
            line 12: 43
            line 14: 46
            line 15: 72
            line 16: 79
            line 14: 86
            line 19: 92
          StackMapTable: number_of_entries = 3
            frame_type = 255 /* full_frame */
              offset_delta = 58
              locals = [ class "[Ljava/lang/String;", int, int, int, int, int, float, class "[I", int, class "[I", int, int ]
              stack = []
            frame_type = 27 /* same */
            frame_type = 248 /* chop */
              offset_delta = 5
    }
    SourceFile: "Hello.java"
    ```

- ####  JVM 参数配置
    - 运行命令 ```jps -v```
    - 返回结果
    ```
        19728 Jps -Denv.class.path=.;D:\Program Files\Java\jdk-8u241\lib;D:\Program Files\Java\jdk-8u241\lib\dt.jar;D:\Program Files\Java\jdk-8u241\lib\tools.jar -Dapplication.home=D:\Program Files\Java\jdk-8u241 -Xms8m
        26180 OilManagementApplication -agentlib:jdwp=transport=dt_socket,address=127.0.0.1:57543,suspend=y,server=n -XX:TieredStopAtLevel=1 -Xverify:none -Dspring.output.ansi.enabled=always -Dcom.sun.management.jmxremote -Dspring.liveBeansView.mbeanDomain -Dspring.applica
        tion.admin.enabled=true -javaagent:C:\Users\Luo\.IntelliJIdea2019.1\system\captureAgent\debugger-agent.jar -Dfile.encoding=UTF-8
        38292 Launcher -Xmx700m -Djava.awt.headless=true -Djdt.compiler.useSingleThread=true -Dpreload.project.path=D:/VCS/git/java/oil-management-system -Dpreload.config.path=C:/Users/Luo/.IntelliJIdea2019.1/config/options -Dcompile.parallel=false -Drebuild.on.dependency.
        change=true -Djava.net.preferIPv4Stack=true -Dio.netty.initialSeedUniquifier=-5748834904778032121 -Dfile.encoding=GBK -Duser.language=zh -Duser.country=CN -Didea.paths.selector=IntelliJIdea2019.1 -Didea.home.path=D:\Program Files\JetBrains\IntelliJ IDEA 2019.1 -Did
        ea.config.path=C:\Users\Luo\.IntelliJIdea2019.1\config -Didea.plugins.path=C:\Users\Luo\.IntelliJIdea2019.1\config\plugins -Djps.log.dir=C:/Users/Luo/.IntelliJIdea2019.1/system/log/build-log -Djps.fallback.jdk.home=D:/Program Files/JetBrains/IntelliJ IDEA 2019.1/jr
        e64 -Djps.fallback.jdk.version=1.8.0_202-release -Dio.netty.noUnsafe=true -Djava.io.tmpdir=C:/Users/Luo/.IntelliJIdea2019.1/system/compile-server/oil-management-parent_1ebf4a13/_temp_ -Djps.backward.ref.index.builder=true -Dkotlin.incremental.c
        51764  -Xms128m -Xmx750m -XX:ReservedCodeCacheSize=240m -XX:+UseConcMarkSweepGC -XX:SoftRefLRUPolicyMSPerMB=50 -ea -Dsun.io.useCanonCaches=false -Djava.net.preferIPv4Stack=true -Djdk.http.auth.tunneling.disabledSchemes="" -XX:+HeapDumpOnOutOfMemoryError -XX:-OmitSt
        ackTraceInFastThrow -javaagent:D:\Program Files\JetBrains\IntelliJ IDEA 2019.1\bin\jetbrains-agent.jar -Djb.vmOptionsFile=D:\Program Files\JetBrains\IntelliJ IDEA 2019.1\bin\idea64.exe.vmoptions -Didea.jre.check=true -Dide.native.launcher=true -Didea.paths.selector
        =IntelliJIdea2019.1 -XX:ErrorFile=C:\Users\Luo\java_error_in_idea_%p.log -XX:HeapDumpPath=C:\Users\Luo\java_error_in_idea.hprof
        46040 GradleDaemon -XX:+HeapDumpOnOutOfMemoryError -Xmx1024m -Dfile.encoding=GBK -Duser.country=CN -Duser.language=zh -Duser.variant
        48904 KotlinCompileDaemon -Djava.awt.headless=true -Djava.rmi.server.hostname=127.0.0.1 -Xmx700m -Dkotlin.incremental.compilation=true -Dkotlin.incremental.compilation.js=true
        49768 Launcher -Xmx700m -Djava.awt.headless=true -Djava.endorsed.dirs="" -Djdt.compiler.useSingleThread=true -Dpreload.project.path=D:/study/Java进阶训练营/JAVA-000/Week_01 -Dpreload.config.path=C:/Users/Luo/.IntelliJIdea2019.1/config/options -Dexternal.project.con
        fig=C:\Users\Luo\.IntelliJIdea2019.1\system\external_build_system\week_01.beeb0ad8 -Dcompile.parallel=false -Drebuild.on.dependency.change=true -Djava.net.preferIPv4Stack=true -Dio.netty.initialSeedUniquifier=-5748834904778032121 -Dfile.encoding=GBK -Duser.language
        =zh -Duser.country=CN -Didea.paths.selector=IntelliJIdea2019.1 -Didea.home.path=D:\Program Files\JetBrains\IntelliJ IDEA 2019.1 -Didea.config.path=C:\Users\Luo\.IntelliJIdea2019.1\config -Didea.plugins.path=C:\Users\Luo\.IntelliJIdea2019.1\config\plugins -Djps.log.
        dir=C:/Users/Luo/.IntelliJIdea2019.1/system/log/build-log -Djps.fallback.jdk.home=D:/Program Files/JetBrains/IntelliJ IDEA 2019.1/jre64 -Djps.fallback.jdk.version=1.8.0_202-release -Dio.netty.noUnsafe=true -Djava.io.tmpdir=C:/Users/Luo/.Intelli
    ```

    - 运行命令```jps -m```
    - 返回结果
    ```
        22032 Jps -m
        26180 OilManagementApplication
        38292 Launcher D:/Program Files/JetBrains/IntelliJ IDEA 2019.1/lib/forms-1.1-preview.jar;D:/Program Files/JetBrains/IntelliJ IDEA 2019.1/lib/guava-25.1-jre.jar;D:/Program Files/JetBrains/IntelliJ IDEA 2019.1/lib/slf4j-api-1.7.25.jar;D:/Program Files/JetBrains/Intel
        liJ IDEA 2019.1/lib/aether-transport-file-1.1.0.jar;D:/Program Files/JetBrains/IntelliJ IDEA 2019.1/lib/jps-builders-6.jar;D:/Program Files/JetBrains/IntelliJ IDEA 2019.1/lib/commons-logging-1.2.jar;D:/Program Files/JetBrains/IntelliJ IDEA 2019.1/lib/maven-aether-p
        rovider-3.3.9.jar;D:/Program Files/JetBrains/IntelliJ IDEA 2019.1/lib/util.jar;D:/Program Files/JetBrains/IntelliJ IDEA 2019.1/lib/netty-resolver-4.1.32.Final.jar;D:/Program Files/JetBrains/IntelliJ IDEA 2019.1/lib/nanoxml-2.2.3.jar;D:/Program Files/JetBrains/Intel
        liJ IDEA 2019.1/lib/aether-connector-basic-1.1.0.jar;D:/Program Files/JetBrains/IntelliJ IDEA 2019.1/lib/commons-lang3-3.4.jar;D:/Program Files/JetBrains/IntelliJ IDEA 2019.1/lib/aether-util-1.1.0.jar;D:/Progr
        51764
        46040 GradleDaemon 4.10.3
        48904 KotlinCompileDaemon --daemon-runFilesPath C:\Users\Luo\AppData\Local\kotlin\daemon --daemon-autoshutdownIdleSeconds=7200 --daemon-compilerClasspath D:\Program Files\JetBrains\IntelliJ IDEA 2019.1\plugins\Kotlin\kotlinc\lib\kotlin-compiler.jar
        49768 Launcher D:/Program Files/JetBrains/IntelliJ IDEA 2019.1/lib/forms-1.1-preview.jar;D:/Program Files/JetBrains/IntelliJ IDEA 2019.1/lib/guava-25.1-jre.jar;D:/Program Files/JetBrains/IntelliJ IDEA 2019.1/lib/slf4j-api-1.7.25.jar;D:/Program Files/JetBrains/Intel
        liJ IDEA 2019.1/lib/aether-transport-file-1.1.0.jar;D:/Program Files/JetBrains/IntelliJ IDEA 2019.1/lib/jps-builders-6.jar;D:/Program Files/JetBrains/IntelliJ IDEA 2019.1/lib/commons-logging-1.2.jar;D:/Program Files/JetBrains/IntelliJ IDEA 2019.1/lib/maven-aether-p
        rovider-3.3.9.jar;D:/Program Files/JetBrains/IntelliJ IDEA 2019.1/lib/util.jar;D:/Program Files/JetBrains/IntelliJ IDEA 2019.1/lib/netty-resolver-4.1.32.Final.jar;D:/Program Files/JetBrains/IntelliJ IDEA 2019.1/lib/nanoxml-2.2.3.jar;D:/Program Files/JetBrains/Intel
        liJ IDEA 2019.1/lib/aether-connector-basic-1.1.0.jar;D:/Program Files/JetBrains/IntelliJ IDEA 2019.1/lib/commons-lang3-3.4.jar;D:/Program Files/JetBrains/IntelliJ IDEA 2019.1/lib/aether-util-1.1.0.jar;D:/Progr
    ```
- #### 使用G1 GC分析JVM
    - 运行命令```jps -l``` 
    - 返回结果
    ```
        42740 org.jetbrains.kotlin.daemon.KotlinCompileDaemon
        51764
        42824 com.scil.oil.management.OilManagementApplication
        45352 sun.tools.jps.Jps
        46248 org.jetbrains.jps.cmdline.Launcher
        52776 org.jetbrains.jps.cmdline.Launcher
    ```

    - 运行命令```jmap -heap 42824``
    - 返回结果
    ```
        Attaching to process ID 42824, please wait...
        Debugger attached successfully.
        Server compiler detected.
        JVM version is 25.241-b07
        
        using thread-local object allocation.
        Garbage-First (G1) GC with 6 thread(s)
        
        Heap Configuration:
           MinHeapFreeRatio         = 40
           MaxHeapFreeRatio         = 70
           MaxHeapSize              = 4250927104 (4054.0MB)
           NewSize                  = 1363144 (1.2999954223632812MB)
           MaxNewSize               = 2550136832 (2432.0MB)
           OldSize                  = 5452592 (5.1999969482421875MB)
           NewRatio                 = 2
           SurvivorRatio            = 8
           MetaspaceSize            = 21807104 (20.796875MB)
           CompressedClassSpaceSize = 1073741824 (1024.0MB)
           MaxMetaspaceSize         = 17592186044415 MB
           G1HeapRegionSize         = 1048576 (1.0MB)
        
        Heap Usage:
        G1 Heap:
           regions  = 4054
           capacity = 4250927104 (4054.0MB)
           used     = 222822392 (212.49999237060547MB)
           free     = 4028104712 (3841.5000076293945MB)
           5.241736368293179% used
        G1 Young Generation:
        Eden Space:
           regions  = 127
           capacity = 143654912 (137.0MB)
           used     = 133169152 (127.0MB)
           free     = 10485760 (10.0MB)
           92.7007299270073% used
        Survivor Space:
           regions  = 13
           capacity = 13631488 (13.0MB)
           used     = 13631488 (13.0MB)
           free     = 0 (0.0MB)
           100.0% used
        G1 Old Generation:
           regions  = 73
           capacity = 109051904 (104.0MB)
           used     = 76021752 (72.49999237060547MB)
           free     = 33030152 (31.50000762939453MB)
           69.71153112558218% used
        
        35528 interned Strings occupying 4040176 bytes.
    ```
