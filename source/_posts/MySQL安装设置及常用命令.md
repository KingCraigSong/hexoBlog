---
layout: posts
title: MySQL安装设置及常用命令
date: 2020-01-29 10:56:22
tags: MySQL
categories: 
- 学习笔记
- 计算机
- 软件教程
---

## MySQL安装

### linux下使用yum安装mysql

#### 安装

查看有没有安装过：

```bash
  yum list installed mysql*
  rpm -qa | grep mysql*
```

查看有没有安装包：

```bash
  yum list mysql*
```

安装mysql客户端：

```bash
  yum install mysql
```

安装mysql 服务器端：

```bash
  yum install mysql-server
  yum install mysql-devel
```

#### 启动\停止

数据库字符集设置

  ```ini
  #mysql配置文件/etc/my.cnf中加入
  default-character-set=utf8
  ```

启动mysql服务：

  ```bash
  service mysqld start
  /etc/init.d/mysqld start
  ```

开机启动：

  ```bash
  chkconfig -add mysqld
  ```

查看开机启动设置是否成功

  ```bash
  chkconfig --list | grep mysql*
  ```

停止：

  ```bash
  service mysqld stop
  ```

#### 登录

创建root管理员：

  ```bash
  mysqladmin -u root password 123456
  ```

登录：

  ```bash
  mysql -u root -p
  ```

忘记密码：

  ```bash
  service mysqld stop
  mysqld_safe --user=root --skip-grant-tables
  mysql -u root
  use mysql
  update user set password=password("new_pass") where user="root";
  flush privileges;
  ```

#### 配置账户权限

创建账户

  ```sql
  create user 'test'@'localhost' identified by '123456';
  ```

授权

  ```sql
  GRANT ALL ON datebase_name.* TO 'your_mysql_name'@'your_client_host';
  ```

刷新授权

  ```sql
  flush privileges;
  ```

#### 远程访问

  开放防火墙的端口号
  mysql增加权限：mysql库中的user表新增一条记录host为“%”，user为“root”

#### Linux MySQL的几个重要目录

数据库目录

  ```bash
  /var/lib/mysql/
  ```

配置文件

  ```bash
  /usr/share /mysql
  ```

相关命令

  ```bash
  /usr/bin
  ```

启动脚本

  ```bash
  /etc/rc.d/init.d/
  ```

## MySQL操作

### 配置

### 启动、停止、重启

### 导入导出

### 修改root密码

- **方法1： 用SET PASSWORD命令**
  首先登录MySQL。
  格式：mysql> set password for 用户名@localhost = password(‘新密码’);
  例子：mysql> set password for root@localhost = password(‘123’);

- **方法2：用mysqladmin**
  格式：mysqladmin -u用户名 -p旧密码 password 新密码
  例子：mysqladmin -uroot -p123456 password 123

- **方法3：用UPDATE直接编辑user表**
  首先登录MySQL。
  mysql> use mysql;
  mysql> update user set password=password(‘123’) where user=’root’ and host=’localhost’;
  mysql> flush privileges;

## Navicat

### MySQL的表

### MySQL的视图

### MySQL的函数

存储过程待学习

### MySQL的事件

### MySQL的报表

### MySQL的计划

### MySQL的备份

### MySQL的模型

## 常见问题及处理办法

### Caused by: java.sql.SQLException: Value '0000-00-00 00:00:00' can not be represented as java.sql.Timestamp

```properties
#原因：因为数据库里面存储了这样子的值，所以查询的时候报错了
#解决方案：在url路径哪里加上一句话
jdbc.url=jdbc:mysql://ip地址:端口号/数据库?characterEncoding=utf8&zeroDateTimeBehavior=convertToNull
```

### sql_mode 问题

在 /etc/my.cnf中设置

```ini
sql_mode=
```

#### sql_mode常用值

```ini
    ONLY_FULL_GROUP_BY
对于GROUP BY聚合操作，如果在SELECT中的列，没有在GROUP BY中出现，那么这个SQL是不合法的，因为列不在GROUP BY从句中。

    NO_AUTO_VALUE_ON_ZERO
该值影响自增长列的插入。默认设置下，插入0或NULL代表生成下一个自增长值。如果用户希望插入的值为0，该列又是自增长的，那么这个选项就有用了。

    STRICT_TRANS_TABLES
在该模式下，如果一个值不能插入到一个事务表中，则中断当前的操作，对非事务表不做限制

    NO_ZERO_IN_DATE
在严格模式下，不允许日期和月份为零

    NO_ZERO_DATE
设置该值，mysql数据库不允许插入零日期，插入零日期会抛出错误而不是警告。

    ERROR_FOR_DIVISION_BY_ZERO
在INSERT或UPDATE过程中，如果数据被零除，则产生错误而非警告。如 果未给出该模式，那么数据被零除时MySQL返回NULL

    NO_AUTO_CREATE_USER
禁止GRANT创建密码为空的用户

    NO_ENGINE_SUBSTITUTION
如果需要的存储引擎被禁用或未编译，那么抛出错误。不设置此值时，用默认的存储引擎替代，并抛出一个异常

    PIPES_AS_CONCAT
将"||"视为字符串的连接操作符而非或运算符，这和Oracle数据库是一样的，也和字符串的拼接函数Concat相类似

    ANSI_QUOTES
启用ANSI_QUOTES后，不能用双引号来引用字符串，因为它被解释为识别符
```