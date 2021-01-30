---
layout: post
title: 统计项目各类文档代码行数
tags: [utils]
published: true
excerpt: 可以统计某个项目下js、java、xml、html、properties、md的代码行数；可根据需要调整
---

// TODO
需要调整的是：改为后缀匹配模式；分类统计行数，以及总行数

```python
#python3
import os
import os.path

# 工程目录
local="D:/coding/java/lms/"
count = 0

def traverseDirByListdir(path):
    path = os.path.expanduser(path)
    for f in os.listdir(path):
        if ".js" in f or ".java" in f or ".xml" in f or ".html" in f or ".properties" in f or ".md" in f:
            addBackToIndex(path+f)
        elif "." not in f:
            traverseDirByListdir(path+f+"/")

def addBackToIndex(fileName):
    global count
    fileTmp = open(fileName,'r+',encoding='utf-8')
    lines = fileTmp.readlines() #读取所有行
    count =  count + len(lines)
    # print(len(lines))
    fileTmp.close()

traverseDirByListdir(local)
print(count)
```