---
layout: post
title: Comparator使用
tags: [programing]
published: true
excerpt: Comparator使用，Collections.sort、List.sort()、stream().sort()等场景中
---
# Comparator使用

## JDK

```java
Collections.sort(list,
    Comparator.comparing(MinitaskVo::getStartPlace, Comparator.nullsLast(String::compareTo))
    //按年月日，不按具体时间
    .thenComparing(e->DateUtil.formatDate(e.getOutWarehouseDt(),"yyyyMMdd"),Comparator.nullsLast(String::compareTo))
    .thenComparing(MinitaskVo::getDeliveryId, Comparator.nullsLast(String::compareTo))
    .thenComparing(MinitaskVo::getPlateNo, Comparator.nullsLast(String::compareTo))
    );
```