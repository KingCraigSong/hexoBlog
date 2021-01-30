---
layout: post
title: SELF4J教程
tags: [logs]
published: true
excerpt: slf4j只是一个门面（facet），它不包含具体的实现，而是将一些log4j，java.logging等实现包装成统一的接口。
---

## 介绍

> slf4j只是一个门面（facet），它不包含具体的实现，而是将一些log4j，java.logging等实现包装成统一的接口。

借用下图展示了常用日志文件的关系：

![日志](/assets/img/logs/SEL4J_01.png)

- commons-logging和slf4j都是日志的接口，供用户使用，而没有提供实现！log4j,logback等等才是日志的真正实现。
- 当我们调用接口时，接口的工厂会自动寻找恰当的实现，返回一个实现的实例给我服务。这些过程都是透明化的，用户不需要进行任何操作！
- 这样，slf4j出现了，它通过简单的实现就能找到符合自己接口的实现类，如果不是满足自己标准的日志，可以通过一些中间实现比如上面！slf4j-log4j12.jar来进行适配。

## 使用SLF4J快速入门

    首先，项目中必须要包括slf4j-api.jar
    此外，还应该包括slf4j为具体实现所提供的适配器（如slf4j-log4j12.jar)
    以及，那个具体实现的jar包（如log4j-1.**.jar)。

我们以以下代码为例：

```java
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
public class Slf4jDemo {
    private static final Logger LOG = LoggerFactory.getLogger(Slf4jDemo.class);
    public static void main(String[] args) {
        LOG.error("Error Message!");
        LOG.warn("Warn Message!");
        LOG.info("Info Message!");
        LOG.debug("Debug Message!");
        LOG.trace("Trace Message!");
    }
}
//由于所使用的具体实现不同，日志输出也有不同的结果。
//这也反应了通过使用slf4j，使得可以方便的替换日志系统。
```

## 注意事项

注意build_path中不能有多个日志实现，否则会导致slf4j不知道该使用哪个实现，从而出现以下错误

```
SLF4J: Class path contains multiple SLF4J bindings.
SLF4J: Found binding in [jar:file:/Users/liaoliuqing/99_Project/1_myCodes/5_JavaEEDemo/lib/slf4j-log4j12-1.6.6.jar!/org/slf4j/impl/StaticLoggerBinder.class]
SLF4J: Found binding in [jar:file:/Users/liaoliuqing/99_Project/1_myCodes/5_JavaEEDemo/lib/slf4j-jdk14-1.6.6.jar!/org/slf4j/impl/StaticLoggerBinder.class]
SLF4J: See http://www.slf4j.org/codes.html#multiple_bindings for an explanation.
SLF4J: Actual binding is of type [org.slf4j.impl.Log4jLoggerFactory]
log4j:WARN No appenders could be found for logger (Slf4jDemo).
log4j:WARN Please initialize the log4j system properly.
log4j:WARN See http://logging.apache.org/log4j/1.2/faq.html#noconfig for more info.
```