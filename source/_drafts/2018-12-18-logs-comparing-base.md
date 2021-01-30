---
layout: post
title: 日志框架简介
tags: [logs]
published: true
excerpt: 日志框架的发展史及相关简介
---

## 常见框架

1. log4j
1. logback
1. SLF4J
1. commons-logging
1. j.u.l (即java.util.logging)

其中，1、3为同一个作者(Ceki)所写。4被很多开源项目所用，5是Java原生库(以下用j.u.l简写来代替)，但是在Java 1.4中才被引入。

## 发展

### Logging frameworks的上古时期

在上古时期，Java打日志依赖System.out.println(), System.err.println()或者e.printStackTrace()。Debug日志被写入STDOUT流,错误日志被写入STDERR流。

这种方式目前小脚本中也依然使用广泛。但是在生产环境或大的项目中，Debug日志通常被重定向到/dev/null中: >/dev/null, 错误日志被重定向到本地文件中: 2>stderr.log。看起来很完美，是吗？实则不然，这样打日志有一个非常大的缺陷：无法可定制化。

具体来讲，没有一个类似开关的东东来切换是否打印Debug日志，当我们定位问题时需要输出Debug日志到文件去查看，而不是到/dev/null里，是吗？日志无法定制化，我们只能硬编码到代码里，不需要时再注释掉相关代码，重新编译。

还有一些缺陷，比如：无法更细粒度地输出日志，换句话说，缺少当前成熟的日志框架常见的LOG LEVEL控制。

而Java本身也没有提供相应的Library，在这样恶劣的境况下，Log4j勇敢地站了出来，拯救劳苦大众。

### J.U.L姗姗来迟

后来，Sun公司开始意识到JDK需要一个记录日志的特性。受Log4j的启发，Sun在Java1.4版本中引入了一个新的API, 叫java.util.logging, 但是，j.u.l功能远不如Log4j完善，如果开发者要使用它，就意味着需要自己写Appenders(Sun称它为Handlers)，而且，只有两个Handlers可被使用：Console和File，这就意味着，开发者只能将日志写入Console和文件。

如前面所述，j.u.l在Java 1.4才被引入，在这之前，并没有官方的日志库供开发者使用。于是便有了很多日志相关的"轮子"。我想这应该是当前会有如此多日志框架的一个很重要的原因。

回顾历史，一方面，在Java 1.4之前，第三方日志库已经被广泛使用了，占得了先机。另一方面，j.u.l在被引入时性能和可用性都很差，直到1.5甚至以后才有了显著提升。

### Logging facades出现及进化

由于项目的日志打印必然依赖以上两个框架中至少一个，无论是j.u.l还是log4j,开发者必须去两个都配置。这时候，Apache的commons-logging出现了。本质上来讲，commons-logging并非一个日志打印框架，而是一个API bridge, 它起到一个连接和沟通的作用，开发者可以使用它来兼容logging frameworks(j.u.l和log4j)。有了它，第三方库就可以使用commons-logging来做一个中间层，去灵活选择j.u.l或者log4j，而不必强加依赖。

然而commons-logging对j.u.l和log4j的配置问题兼容得并不好，更糟糕的是，使用commons-logging可能会遇到类加载问题，导致NoClassDefFoundError的错误出现。

最终，log4j的创始人Ceki发起了另一个项目，这便是大名鼎鼎的SLF4j 日志框架，该框架可以看成是log4j的升级版。需要说明的是，log4j 2.0已经被加入Apache基金会，过去几年已经被大幅改善，社区活跃度也非常高，借助开源社区的力量，log4j 2.0目前被加入越来越多得现代化特性，一定程度上，甚至超越了log4j的升级版logback(稍后介绍)，关于log4j 2.0的新特性，请参见这篇文章：THE NEW LOG4J 2.0

据slf4j的作者Ceki说，首先，slf4j是不仅仅是一个logging framework, 而且一个logging facdes, 借助slf4j的log4j adapter, 开发者从slf4j切换到log4j不需要额外改动一行代码，只需要从CLASS_PATH中排除掉slf4j-log4j12.jar。如果想从log4j迁移到logback, 在CLASS_PATH添加slf4j-log4j12.jar, 并将log4j.properties转换为logback.xml即可，这里有一个在线工具可以自动完成转换: logback.xml translator。

slf4j提供了很大的灵活度，开发者可以借助它去灵活选择底层的日志框架。比如，当下更多的开发者比较倾向于使用log4j的升级版logback,因为它具有较log4j更多更好的特性：

配置文件支持xml和Groovy语法(版本号>= 0.9.22)

自动重载有变更的配置文件

自动压缩历史日志

打印异常信息时自动包含package名称级版本号

Filters

其它一些很棒的特性

需要说明的是，logback是slf4j接口的一套具体实现，又是同一个作者，因而保证了其和log4j相近的使用方式，也具有slf4j的全部特性。

此外，对于一些大型框架及服务的开发者，需要考虑客户端用户的体验。比如jstorm, 你不能只考虑自己的喜好，或许有人偏好使用slf4j开发jstorm topology, 而另一些人喜欢用logback。这种情况下，你应该使用slf4j，把最终logging framework的选择权留给用户。

最后，除了slf4j比j.u.l或者log4j更好用，还有一个选择slf4j的现实原因：Java圈的非常多开发者更钟情于slf4j作为他们的logging API, 随大流有时候能少很多不必要的麻烦。

### 日志参数化打印的支持(parameterized logging)

slf4j除了包含该log4j的全部特性外，还提供了parameterized logging特性。这个特性非常有用，它允许开发者在打印日志时借助{}来实现参数化打印：

``` java
logger.debug("The attribute value is {}", fooIns.getAttribute());
```

logback复用了slf4j的API，这意味着使用logback实际上是在使用slf4j的API，不难看出，logback同样支持parameterized logging特性。

---
原文来自：[https://blog.csdn.net/xn_28/article/details/62350938](https://blog.csdn.net/xn_28/article/details/62350938)
