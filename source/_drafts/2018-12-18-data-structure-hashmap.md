---
layout: post
title: HashMap 初步整理
tags: [Data structure,draft]
published: true
excerpt: HashMap 是一个用于存储Key-Value键值对的集合，每一个键值对也叫做 Entry；键值对（Entry）分散存储在一个数组当中，这个数组就是HashMap的主干；HashMap 数组每一个元素的初始值都是 Null。
---

## 简介

- HashMap 是一个用于存储Key-Value键值对的集合，每一个键值对也叫做 Entry；
- 键值对（Entry）分散存储在一个数组当中，这个数组就是HashMap的主干；
- HashMap 数组每一个元素的初始值都是 Null。

## 常用方法

### Put

调用 hashMap.put("apple", 0) ，插入一个Key为“apple”的元素。这时候我们需要利用一个哈希函数来确定Entry的插入位置（index）：index = Hash（"apple"），假定最后计算出的index是2，那么结果会存在数组index=2的位置中。

但是，因为HashMap的长度是有限的，当插入的Entry越来越多时，会出现index（key的hash值）冲突的情况。那么这个时候可以使用链表来解决。
HashMap 数组的每一个元素不止是一个 Entry 对象，也是一个链表的头节点。
每一个 Entry 对象通过 Next 指针指向它的下一个 Entry 节点。当新来的Entry映射到冲突的数组位置时，只需要插入到对应的链表即可。
新来的Entry节点插入链表时，使用的是“头插法”，应为发明者认为后插入的Entry被查找的几率较大

### Get

查找时，根据key的hash值来找到index，这时如果之前的hash没有冲突的话，直接就可以取到value；但是如果hash冲突了，这里key值对不上，然后往下找Entry，找到key对应的Entry就可以取到我们要的value值了。

## 默认长度及扩容

> HashMap的默认长度是16 ，自动扩展或初始化时，长度必须是2的幂

### Hash算法的实现

其采用了位运算的方式如何进行位运算呢？有如下的公式（Length是HashMap的长度）：
index = HashCode(Key) & (Length -1)

下面以值为“book”的 Key 来演示整个过程：

1. 计算 book 的 hashcode，结果为十进制的 3029737，二进制的101110001110101110 1001。
2. 假定 HashMap 长度是默认的16，计算Length-1的结果为十进制的15，二进制的1111。
3. 把以上两个结果做与运算，101110001110101110 1001 & 1111 = 1001，十进制是9，所以 index=9。（与运算：和，同位上都为1则为1，否则为0)

Hash 算法最终得到的 index 结果，取决于 Key 的 Hashcode 值的最后几位。

### 为什么长度必须是2的幂

这是因为长度16或者其他2的幂，Length-1 的值是所有二进制位全为1，比如15是1111，7是111，这种情况下，index 的结果等同于 HashCode 后几位的值。
只要输入的 HashCode 本身分布均匀，Hash 算法的结果就是均匀的。

### HashMap扩容与Rehash

> HashMap的容量是有限的。当经过多次元素插入，使得HashMap达到一定饱和度时，Key映射位置发生冲突的几率会逐渐提高。这时候，HashMap需要扩展它的长度，也就是进行Resize。

#### 影响发生Resize的因素

1.Capacity
HashMap的当前长度，是2的幂。

2.LoadFactor
HashMap负载因子，默认值为0.75f。
衡量HashMap是否进行Resize的条件如下：HashMap.Size >= Capacity * LoadFactor

#### HashMap的resize经历以下两个步骤

1.扩容
创建一个新的Entry空数组，长度是原数组的2倍。

2.ReHash
遍历原Entry数组，把所有的Entry重新Hash到新数组。这是因为长度扩大以后，Hash的规则也随之改变。

要记录下 HashMap 遍历时删除某些 entry

---
整理自 [https://blog.csdn.net/Jae_Peng/article/details/79562432](https://blog.csdn.net/Jae_Peng/article/details/79562432)