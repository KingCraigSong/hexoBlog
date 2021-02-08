---
title: Java导入Excel特殊字符处理
top_img: false
cover: false
date: 2021-02-03 11:25:18
updated:
tags:
- java
- 工具集
categories:
- [学习笔记,计算机,编程随笔]
- [代码片段]
---

导入Excel数据时，经常出现单元格里有空格、制表符、不显示的特殊字符；下面进行处理

```java
   /**
    * 去除字符串中的空格、制表符、特殊字符:
    *
    * \t \r \n
    * `"\u202C"` `"\u202D"` `"\u202E"`
    * @param str
    * @return
    */
    public static String replaceBlank(String str) {
        if (str == null) {
            return null;
        }
        Pattern p = Pattern.compile("[\u202C\u202D\u202E\\s(\\t\\n)\\n]");
        Matcher m = p.matcher(str);
        return m.replaceAll("");
    }
```