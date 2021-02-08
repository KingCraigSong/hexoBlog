---
layout: posts
title: Java开发者易犯错误Top10
date: 2020-01-31 16:38:59
cover: false
top_img: false
tags:
  - java
categories:
  - 学习笔记
  - 计算机
  - 编程随笔
---

## 数组转换为数组列表

```java
List<String> list = Arrays.asList(arr);
```

Arrays.asList()将返回一个数组内部是私有静态类的ArrayList，这不是java.util.ArrayList类，java.util.Arrays.ArrayList类有set()、 get()、 contains()方法，但是没有任何加元素的方法，因此它的大小是固定的。你应该这么做来创建一个真正的数组：

```java
ArrayList<String> arrayList = new ArrayList<String>(Arrays.asList(arr));
```

ArrayList的构造函数能够接受一个集合类型，这也是java.util.Arrays.ArrayList的超级类型。

## 检查一个数组包含一个值

开发者经常这么做：

```java
Set<String> set = new HashSet<String>(Arrays.asList(arr));
return set.contains(targetValue);
```

代码可以工作，但是没有必要首先转换列表到Set，转换一个列表到一个Set需要额外的时间。因此你可以把它简化为：

```java
Arrays.asList(arr).contains(targetValue);
```

或

```java
for(String s: arr){
    if(s.equals(targetValue))
        return true;
}
return false;
```

第一个比第二个更具可读性

## 在一个循环中从一个列表里删除一个元素

考虑下面删除元素的代码在迭代中的结果：

```java
ArrayList<String> list = new ArrayList<String>(Arrays.asList("a", "b", "c", "d"));

for (int i = 0; i < list.size(); i++) {
    list.remove(i);
}
System.out.println(list);
```

输出是：

```bash
[b, d]
```

该方法有一个严重的问题，当一个元素被删除时，列表收缩的大小以及指针改变了。所以想要在循环内利用指针删除多个元素是无法正常进行的。
这种情况下使用迭代器才是正确的方法，foreach循环在Java中的工作像是一个迭代器，但实际上并不是，考虑下面的代码：

```java
ArrayList<String> list = new ArrayList<String>(Arrays.asList("a", "b", "c", "d"));

for (String s : list) {
    if (s.equals("a"))
        list.remove(s);
}
```

它会报出ConcurrentModificationException异常。
相反下面这个就可以正常工作。

```java
ArrayList<String> list = new ArrayList<String>(Arrays.asList("a", "b", "c", "d"));

Iterator<String> iter = list.iterator();

while (iter.hasNext()) {
    String s = iter.next();
    if (s.equals("a")) {
        iter.remove();
    }
}
```

.next()必须在.remove()之前被调用。在foreach循环中，编译器将在删除元素操作之后调用.next()，这也是导致ConcurrentModificationException异常的原因，你可以点击此处查看ArrayList.iterator()的源代码。

## Hashtable vs HashMap

根据算法的常规，Hashtable是对数据结构的称呼。但是在Java中，数据结构的名称是HashMap。Hashtable和HashMap关键不同之一是Hashtable是同步的。
关于这一点可查看HashMap vs. TreeMap vs. Hashtable vs. LinkedHashMap

## 使用集合的原始类型

在Java中，原始类型和无限制的通配符类型很容易被混淆。以Set为例，Set是原始类型，而Set(?)则是无限制的通配符类型。
考虑下面的代码，以一个原始类型List作为参数：

```java
public static void add(List list, Object o){
    list.add(o);
}
public static void main(String[] args){
    List<String> list = new ArrayList<String>();
    add(list, 10);
    String s = list.get(0);
}
```

该代码会抛出一个异常：

```console
Exception in thread "main" java.lang.ClassCastException: java.lang.Integer cannot be cast to java.lang.String
at ...
```

使用原始类型集合是危险的，因为原始类型集合跳过了泛型类型检查，也不安全。Set、Set<?>和Set<Object>之间有很大的不同。
详细可查看Raw type vs. Unbounded wildcard和Type Erasure。

## 访问级别

开发者经常对类域使用public，这很容易通过直接引用获得域值，但这是一个非常糟糕的设计。根据经验来说是给予成员的访问级别越低越好。
详细情况可点击查看Java中成员访问级别：public、protected、private

## ArrayList VS LinkedList

如果你不知道ArrayList和LinkedList之间的区别时，你可能会经常的选用ArrayList，因为它看起来看熟悉。然而它们之间有巨大的性能不同。简单的来说，如果有大量的添加/删除操作，并且没有很多的随机存取操作时，LinkedList应该是你的首选。

## Mutable VS Immutable

Immutable对象有很多优势，比如简单、安全等等。但它要求每一个不同的值都需要有一个不同的对象，而太多的对象可能会导致垃圾收集的高成本。所以对Mutable和Immutable的选择应该有一个平衡点。
一般来说，Mutable对象用于避免产生过多的中间对象，经典的例子是连接大量的字符串数。如果你使用Immutable字符串，那么会产生很多符合垃圾收集条件的对象。这对CPU是浪费时间和精力的，当其可以使用Mutable对象作为正确的解决方案。（如StringBuilder）

```java
String result="";
for(String s: arr){
    result = result + s;
}
```

这里还有一些其他Mutable对象可取的情况。例如mutable对象传递到方法中允许你在不跳过太多语法的情况下收集多个结果。另一个例子是排序和过滤，你可以构建一个带有原有集合的方法，并返回一个已排序的，不过这对大的集合来说会造成更大的浪费。

## Super和Sub构造函数

在Java中，如果一个类没有定义一个构造函数，编译器会默认的为类插入一个无参数构造函数。如果一个构造函数是在Super类中定义的，这种情况下Super(String s)，编译器不会插入默认的无参数构造函数。
另一方面，Sub类的构造函数，无论带不带有参数，都会调用无参数的Super构造函数。
编译器在Sub类中试图将Super()插入到两个构造函数中，但是Super默认的构造函数是没有定义的，编译器才会报错。如何解决这一问题？你只需在Super类中添加一个Super()构造函数，如下所示：

```java
public Super(){
    System.out.println("Super");
}
```

或移除自定义的Super构造函数，又或者在Sub函数中添加super(value)。

## ""或构造函数？

字符串可以通过两种方式创建：

```java
//1. use double quotes
String x = "abc";
//2. use constructor
String y = new String("abc");
```

它们之间有何不同？下面的例子可以给出答案：

```java
String a = "abcd";
String b = "abcd";
System.out.println(a == b); // True
System.out.println(a.equals(b)); // True

String c = new String("abcd");
String d = new String("abcd");
System.out.println(c == d); // False
System.out.println(c.equals(d)); // True
```

关于它们如何在内存中分布的更多细节可以查看[《使用""或构造函数创建Java字符串》](https://www.programcreek.com/2014/03/create-java-string-by-double-quotes-vs-by-constructor/)




