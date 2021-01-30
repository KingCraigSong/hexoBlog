---
layout: post
title: 数据库中IP地址的存储
date: 2020-01-29 10:52:36
cover: false
top_img: false
tags: 
- java
- 工具类
categories: 
- 学习笔记
- 计算机
- 编程随笔
---

大多数公司的表结构都需要经过DBA进行审核，有时候你会看到存储IP地址采用varchar(15)，这种方式都是传统的做法，这种方法需要占用15个字节，那么有更省空间的做法么？肯定是有的，那就是用int存储。如果采用int存储这里又有2种处理方式。

1. 利用MySQL函数进行处理。可以采用INET_ATON，INET_NTOA函数进行转换。
2. 利用开发语言的函数进行处理，以php进行举例。可以采用ip2long，long2ip函数进行转换；java语言处理工具类如下：

```java
/**
 * Created with IntelliJ IDEA.
 * Desc:  IP地址使用无符号整型存储
 * User:  Xiaomo Song
 * Date: 2018-11-26
 * Time: 17:12
 */

public class IPConvetor {
    /**
     * ip地址转成long型数字
     * 将IP地址转化成整数的方法如下：
     * 1、通过String的split方法按.分隔得到4个长度的数组
     * 2、通过左移位操作（<<）给每一段的数字加权，
     * 第一段的权为2的24次方，
     * 第二段的权为2的16次方，
     * 第三段的权为2的8次方，最后一段的权为1
     *
     * @param strIp
     * @return
     */
    public static long ipToLong(String strIp) {
        String[] ip = strIp.split("\\.");
        return (Long.parseLong(ip[0]) << 24) +
            (Long.parseLong(ip[1]) << 16) +
            (Long.parseLong(ip[2]) << 8) +
            Long.parseLong(ip[3]);
    }

    /**
     * 将十进制整数形式转换成127.0.0.1形式的ip地址
     * 将整数形式的IP地址转化成字符串的方法如下：
     * 1、将整数值进行右移位操作（>>>），右移24位，右移时高位补0，得到的数字即为第一段IP。
     * 2、通过与操作符（&）将整数值的高8位设为0，再右移16位，得到的数字即为第二段IP。
     * 3、通过与操作符吧整数值的高16位设为0，再右移8位，得到的数字即为第三段IP。
     * 4、通过与操作符吧整数值的高24位设为0，得到的数字即为第四段IP。
     *
     * @param longIp
     * @return
     */
    public static String longToIP(long longIp) {
        StringBuffer sb = new StringBuffer("");
        // 直接右移24位
        sb.append(String.valueOf((longIp >>> 24)));
        sb.append(".");
        // 将高8位置0，然后右移16位
        sb.append(String.valueOf((longIp & 0x00FFFFFF) >>> 16));
        sb.append(".");
        // 将高16位置0，然后右移8位
        sb.append(String.valueOf((longIp & 0x0000FFFF) >>> 8));
        sb.append(".");
        // 将高24位置0
        sb.append(String.valueOf((longIp & 0x000000FF)));
        return sb.toString();
    }

    public static void main(String[] args) {
        System.out.println(ipToLong("255.255.0.255"));
        System.out.println(longToIP(4294967295L));
    }
}
```