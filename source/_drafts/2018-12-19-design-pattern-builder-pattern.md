---
layout: post
title: 建造者模式(Builder Pattern)
subtitle: 创建型模式(Creational pattern)
tags: [Design pattern]
published: true
excerpt: 造者模式(Builder Pattern)：将一个复杂对象的构建与它的表示分离，使得同样的构建过程可以创建不同的表示。建造者模式是一步一步创建一个复杂的对象，它允许用户只通过指定复杂对象的类型和内容就可以构建它们，用户不需要知道内部的具体构建细节。建造者模式属于对象创建型模式。根据中文翻译的不同，建造者模式又可以称为生成器模式。
---

> **不甚理解**

## 模式定义

造者模式(Builder Pattern)：将一个复杂对象的构建与它的表示分离，使得同样的构建过程可以创建不同的表示。

建造者模式是一步一步创建一个复杂的对象，它允许用户只通过指定复杂对象的类型和内容就可以构建它们，用户不需要知道内部的具体构建细节。建造者模式属于对象创建型模式。根据中文翻译的不同，建造者模式又可以称为生成器模式。

## 模式结构

- Builder：抽象建造者
- ConcreteBuilder：具体建造者
- Director：指挥者
- Product：产品角色

![class_Builder](/assets/img/pattern/class_Builder.jpg)

## 时序图

![seq_Builder](/assets/img/pattern/seq_Builder.jpg)

## 代码示例

```java
// 建造者角色：
public abstract class Builder {
    public abstract void buildPart1();
    public abstract void buildPart2();
    public abstract void buildPart3();
}

// 监工角色：
public class Director {    // 将一个复杂的构建过程与其表示相分离
    private Builder builder;    // 针对接口编程，而不是针对实现编程
    public Director(Builder builder) {
        this.builder = builder;
    }
    public void setBuilder(Builder builder) {
        this.builder = builder;
    }

    public void construct() {   // 控制（定义）一个复杂的构建过程
        builder.buildPart1();
        for (int i = 0; i < 5; i++) {   // 提示：如果想在运行过程中替换构建算法，可以考虑结合策略模式。
            builder.buildPart2();
        }
        builder.buildPart3();
    }
}

//具体的建造者角色：
/**
 * 此处实现了建造纯文本文档的具体建造者。
 * 可以考虑再实现一个建造HTML文档、XML文档，或者其它什么文档的具体建造者。
 * 这样，就可以使得同样的构建过程可以创建不同的表示
 */
public class ConcreteBuilder1 extends Builder {
  private StringBuffer buffer = new StringBuffer();//假设 buffer.toString() 就是最终生成的产品

    @Override
    public void buildPart1() {//实现构建最终实例需要的所有方法
        buffer.append("Builder1 : Part1\n");
    }

    @Override
    public void buildPart2() {
        buffer.append("Builder1 : Part2\n");
    }

    @Override
    public void buildPart3() {
        buffer.append("Builder1 : Part3\n");
    }

    public String getResult() {//定义获取最终生成实例的方法
        return buffer.toString();
    }
}

// 客户角色：
public class Client {
    public void testBuilderPattern() {
        ConcreteBuilder1 b1 = new ConcreteBuilder1();//建造者
        Director director = new Director(b1);//监工
        director.construct();//建造实例(监工负责监督，建造者实际建造)
        String result = b1.getResult();//获取最终生成结果
        System.out.printf("the result is :%n%s", result);
    }
}
```

## 实例

实例：KFC套餐

建造者模式可以用于描述KFC如何创建套餐：套餐是一个复杂对象，它一般包含主食（如汉堡、鸡肉卷等）和饮料（如果汁、 可乐等）等组成部分，不同的套餐有不同的组成部分，而KFC的服务员可以根据顾客的要求，一步一步装配这些组成部分，构造一份完整的套餐，然后返回给顾客。

![KFCBuilder](/assets/img/pattern/KFCBuilder.jpg)

## 模式分析

抽象建造者类中定义了产品的创建方法和返回方法;

建造者模式的结构中还引入了一个指挥者类Director，该类的作用主要有两个：一方面它隔离了客户与生产过程；另一方面它负责控制产品的生成过程。指挥者针对抽象建造者编程，客户端只需要知道具体建造者的类型，即可通过指挥者类调用建造者的相关方法，返回一个完整的产品对象

在客户端代码中，无须关心产品对象的具体组装过程，只需确定具体建造者的类型即可，建造者模式将复杂对象的构建与对象的表现分离开来，这样使得同样的构建过程可以创建出不同的表现。

## 优点

- 在建造者模式中， 客户端不必知道产品内部组成的细节，将产品本身与产品的创建过程解耦，使得相同的创建过程可以创建不同的产品对象。
- 每一个具体建造者都相对独立，而与其他的具体建造者无关，因此可以很方便地替换具体建造者或增加新的具体建造者， 用户使用不同的具体建造者即可得到不同的产品对象 。
- 可以更加精细地控制产品的创建过程 。将复杂产品的创建步骤分解在不同的方法中，使得创建过程更加清晰，也更方便使用程序来控制创建过程。
- 增加新的具体建造者无须修改原有类库的代码，指挥者类针对抽象建造者类编程，系统扩展方便，符合“开闭原则”。

## 缺点

- 建造者模式所创建的产品一般具有较多的共同点，其组成部分相似，如果产品之间的差异性很大，则不适合使用建造者模式，因此其使用范围受到一定的限制。
- 如果产品的内部变化复杂，可能会导致需要定义很多具体建造者类来实现这种变化，导致系统变得很庞大。

## 适用环境

- 需要生成的产品对象有复杂的内部结构，这些产品对象通常包含多个成员属性。
- 需要生成的产品对象的属性相互依赖，需要指定其生成顺序。
- 对象的创建过程独立于创建该对象的类。在建造者模式中引入了指挥者类，将创建过程封装在指挥者类中，而不在建造者类中。
- 隔离复杂对象的创建和使用，并使得相同的创建过程可以创建不同的产品。

## 模式应用

- 在很多游戏软件中，地图包括天空、地面、背景等组成部分，人物角色包括人体、服装、装备等组成部分，可以使用建造者模式对其进行设计，通过不同的具体建造者创建不同类型的地图或人物。

## 总结

- 建造者模式将一个复杂对象的构建与它的表示分离，使得同样的构建过程可以创建不同的表示。建造者模式是一步一步创建一个复杂的对象，它允许用户只通过指定复杂对象的类型和内容就可以构建它们，用户不需要知道内部的具体构建细节。建造者模式属于对象创建型模式。
- 建造者模式包含如下四个角色：抽象建造者为创建一个产品对象的各个部件指定抽象接口；具体建造者实现了抽象建造者接口，实现各个部件的构造和装配方法，定义并明确它所创建的复杂对象，也可以提供一个方法返回创建好的复杂产品对象；产品角色是被构建的复杂对象，包含多个组成部件；指挥者负责安排复杂对象的建造次序，指挥者与抽象建造者之间存在关联关系，可以在其construct()建造方法中调用建造者对象的部件构造与装配方法，完成复杂对象的建造
- 在建造者模式的结构中引入了一个指挥者类，该类的作用主要有两个：一方面它隔离了客户与生产过程；另一方面它负责控制产品的生成过程。指挥者针对抽象建造者编程，客户端只需要知道具体建造者的类型，即可通过指挥者类调用建造者的相关方法，返回一个完整的产品对象。
- 建造者模式的主要优点在于客户端不必知道产品内部组成的细节，将产品本身与产品的创建过程解耦，使得相同的创建过程可以创建不同的产品对象，每一个具体建造者都相对独立，而与其他的具体建造者无关，因此可以很方便地替换具体建造者或增加新的具体建造者，符合“开闭原则”，还可以更加精细地控制产品的创建过程；其主要缺点在于由于建造者模式所创建的产品一般具有较多的共同点，其组成部分相似，因此其使用范围受到一定的限制，如果产品的内部变化复杂，可能会导致需要定义很多具体建造者类来实现这种变化，导致系统变得很庞大。
- 建造者模式适用情况包括：需要生成的产品对象有复杂的内部结构，这些产品对象通常包含多个成员属性；需要生成的产品对象的属性相互依赖，需要指定其生成顺序；对象的创建过程独立于创建该对象的类；隔离复杂对象的创建和使用，并使得相同的创建过程可以创建不同类型的产品。

---
原文出处：[图说设计模式](http://design-patterns.readthedocs.org/zh_CN/latest/index.html)