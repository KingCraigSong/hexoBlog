---
layout: post
title: 图 Graph
tags: [Data structure,draft]
published: true
excerpt: 图表示「多对多」的关系，可退化成树、链表
---

# 图 Graph

>表示「多对多」的关系

## 包含：

- 一组顶点：通常用V（Vertex）表示顶点集合
- 一组边，通常用E（Edge）表示边的集合
  - 边是定点对 $$(v,w) \in E$$ ，其中 $$v,w \in V$$ ,   v-----w
  - 有向边 <v,w> 表示由 v 指向 w 的边（单行线），v----->w
  - 不考虑重边和自回路

## 定义

类型名称：图（Graph）
数据对象集：G(V,E)由一个非空的有限顶点集合V和一个有限边集合E组成
操作集：对于任意图 $$G \in Graph$$，以及 $$v \in V$$, $$e \in E$$

无向图、有向图

## 表示，经典的两种方法

- 邻接矩阵：G[n][n]——n个顶点，从0到n-1编号
  - G[i][j]=1, 若 $$<v_i,v_j>$$ 是G中的边，对于网络，可以存放权重（表示没有的时候有待讨论）
  - G[i][j]=0, 否则
  - 可以使用一个n(n+1)/2的数组A存储，则 $$<v_i,v_j>$$ 在 A[i(i+1)/2+j]
  - 好处：
    - 直观、简单、好理解
    - 方便查找任一两顶点间是否有边
    - 方便查找任一顶点的「邻接点」（有边直接相邻的顶点）
    - 方便计算任一顶点的「度」（从该点发出的边数叫「出度」，指向该点的边数叫「入度」）
      - 无向图：对应行（或列）非0元素的个数
      - 有向图：对应行非0元素的个数是「出度」，对应列非0元素的个数是「入度」
  - 缺点：
    - 浪费空间 —— 存稀疏图（点多边少），有大量无效元素
      - 对稠密图（尤其是完全图）还是很合算的
    - 浪费时间 —— 统计稀疏图中一共有多少边
- 邻接表：G[n] 为指针数组，对应矩阵每行一个链表，只存非0元素；表示法不唯一，链表中存相临节点
  - 好处：
    - 方便找一个顶点的所有「邻接点」
    - 节约稀疏图的空间（需要n个头指针，2E个节点）
    - 方便计算任一节点的度
      - 有向图方便；有向图只能计算出度，入度需要构建逆邻接表
  - 缺点：
    - 不方便检查任两节点是否有边

## 遍历

1.深度优先搜索（Depth First Search，DFS）栈

```c
    void DFS(Vertex V){
        visited[V] = true;
        for(V 的每个邻接点 W){
            if(!visited[W])
                DFS(W);
        }
    }
```

2.广度优先搜索（Breadth First Search，BFS）队列