---
layout: post
title: 基础编程题目集 6-10 阶乘计算升级版
tags: [programing, PTA]
published: true
excerpt: 基础编程题目集 6-10 阶乘计算升级版 （20 分）,要求实现一个打印非负整数阶乘的函数。
---

# 基础编程题目集 6-10 阶乘计算升级版

> 本题要求实现一个打印非负整数阶乘的函数。

## 函数接口定义：

```c
void Print_Factorial ( const int N );
```

其中N是用户传入的参数，其值不超过1000。如果N是非负整数，则该函数必须在一行中打印出N!的值，否则打印“Invalid input”。

## 裁判测试程序样例：

```c
#include <stdio.h>

void Print_Factorial ( const int N );

int main()
{
    int N;
    scanf("%d", &N);
    Print_Factorial(N);
    return 0;
}

/* 你的代码将被嵌在这里 */
```

## 输入样例：

```shell
15
```

## 输出样例：

```shell
1307674368000
```

## 题解

```c
void Print_Factorial ( const int N ){
    int a[3000];
    int temp,num,digit;           //temp：每一位的结果  num:进位   digit：结果的位数
    int i,j;
    a[0]=1;
    digit=1;                      //从第1位开始
    if(N>=0) {
        for(i=2;i<=N;i++){
            num=0;
            for(j=0;j<digit;j++){
              temp=a[j]*i+num;
              a[j]=temp%10;       //把当前位的数字存入数字
              num=temp/10;        //向前面一位进位
            }
            while(num){           //当i的阶乘算完，若有进位，则数组需扩大
                a[digit]=num%10;
                num/=10;
                digit++;
            }
        }
        for(i=digit-1;i>=0;i--){
            printf("%d",a[i]);
        }
    }
    else printf("Invalid input");
}
```

## java方案

```java
public class Factorial {
    public static void main(String[] args) {
        factorial(1000);
    }

    private static void factorial(int N) {
        int[] result = new int[2570]; //原文 3000
        int tmp, digit, num;
        digit = 1;
        result[0]=1;
        if (N > 0) {
            for (int i = 2; i <= N; i++) {
                num = 0;
                for (int j = 0; j < digit; j++) {
                    tmp = result[j] * i + num;
                    result[j] = tmp % 10;
                    num = tmp / 10;
                }
                while (num > 0) {
                    result[digit] = num % 10;
                    num /= 10;
                    digit++;
                }
            }
            for (int i = digit - 1; i >= 0; i--) {
                System.out.print(result[i]);
            }
        } else {
            System.out.println("Invalid Input");
        }
    }
}
```