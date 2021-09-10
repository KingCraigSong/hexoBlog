# Linux 服务器设置和常用命令

## 进程相关

### 查看进程pid

```bash
ps -ef|grep 进程名
```

## 防火墙及安全

### centos7 linux开端口号

开放端口号方法(--permanent 永久开放)

```bash
firewall-cmd --add-port=8080/tcp --permanent
```

重启加载规则

```bash
firewall-cmd --reload
```

查询端口是否开启命令：

```bash
firewall-cmd --query-port=8080/tcp
```

## 环境配置

### JDK 1.8

安装之前先检查一下系统有没有自带open-jdk

  ```bash
  rpm -qa |grep java
  rpm -qa |grep jdk
  rpm -qa |grep gcj
  ```

如果没有输入信息表示没有安装。

> 如果安装可以使用rpm -qa | grep java | xargs rpm -e --nodeps 批量卸载所有带有Java的文件  这句命令的关键字是java

首先检索包含java的列表

```bash
yum list java*
```

检索1.8的列表

```bash
yum list java-1.8*
```

安装1.8.0的所有文件

```bash
yum install java-1.8.0-openjdk* -y
```

使用命令检查是否安装成功

```bash
java -version
```

## 安装软件

### CentOS更换yum安装源

> 源所在的目录:     /etc/yum.repos.d/    ==在更换之前可以先备份自己的源,防止出错==

1. Centos 5.X 安装阿里或者网易的源

  ``` linux
  wget -O /etc/yum.repos.d/CentOS-Base.repo http://mirrors.aliyun.com/repo/Centos-5.repo
  wget -O /etc/yum.repos.d/CentOS-Base.repo http://mirrors.163.com/.help/CentOS5-Base-163.repo
  ```

2.Centos 6.X 安装阿里或者网易的源

  ``` linux
  wget -O /etc/yum.repos.d/CentOS-Base.repo http://mirrors.aliyun.com/repo/Centos-6.repo
  wget -O /etc/yum.repos.d/CentOS-Base.repo http://mirrors.163.com/.help/CentOS6-Base-163.repo
  ```

3.下载成功以后执行命令

  ``` linux
  yum clean all
  yum makecache
  ```

4.当yum makecache完成以后,源就安装完了

### CentOS 安装rz和sz命令

  ```bash
  yum install lrzsz
  ```

### CentOS 安装Mysql

  见MySQL总结

### CentOS 安装tomcat

  见tomcat总结
