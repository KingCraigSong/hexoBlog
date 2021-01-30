---
layout: posts
title: 字符串转Double
date: 2020-01-29 10:41:14
cover: false
top_img: false
tags: 
- java
- 算法
categories: 
- 学习笔记
- 计算机
- 算法题解
---

Q：给你一个字符串，比如「123.456」，转换成 Double 类型

A：easy 啊

```java
Double d1 = Double.valueOf("123.456");
```

Q：这样就没意思了，不能用JDK里面的方法

A：我想想（内心：木有IDE == ，木有手册；此种情况我只知道某鹅厂大神可以徒手撸代码，直接编译不报错）

A：这样，我讲下我的思路吧，blablabla……

Q：嗯好，你写吧

A：……

好吧，保存一下。

```java
/**
 * Created with IntelliJ IDEA.
 * Desc: 给定字符串，转换成 Double；
 *       如果是 null 或者空串，直接返回0
 *       如果不是标准的数字格式“asdf”、“.12345”、“1234.”也返回0
 *       char->int，得到的是ASCII码，并不是真正的数值，
 *       但是鉴于0-9是连续编码的，我们可以这样获得 '1'-'0'=1
 *       To be continued...
 * User:  Xiaomo Song
 * Date: 2018-12-09
 * Time: 18:25
 */
public class String2Double {
    public static void main(String[] args) {
//        Metho1:
        Double d1 = Double.valueOf("123.233");
        System.out.println(d1);
//        Method2:
        Double d2 = String2Double.string2Double("123.456");
        System.out.println(d2);
    }

    public static Double string2Double(String str) {
        double result = 0.0;
        if (str == null || str.trim().equals("")) {
            System.out.println("未输入字符串");
            return result;
        }

        if (str.matches("^\\d+(\\.\\d+)?")) {
            char[] chars = str.toCharArray();
            int index = str.indexOf('.');
            if (index == -1) {
                index = chars.length;
            }
            for (int i=index-1 ;i>=0;i--){
                result+=Math.pow(10,index-1-i)*char2Int(chars[i]);
            }
            for(int i=index+1;i<chars.length;i++){
                result+=Math.pow(0.1,i-index)*char2Int(chars[i]);
            }
        } else {
            System.out.println("字符串中有其他字符");
        }
        return result;
    }

    private static int char2Int(char c) {
        return c - '0';
    }
}
```