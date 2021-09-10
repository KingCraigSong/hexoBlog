# Tomcat 安装设置及性能优化

    apache-tomcat-8.5.9.tar.gz
    下载地址：http://tomcat.apache.org/download-80.cgi

    作为进程启动时，需要下载 commons-daemon
    下载地址： https://www.apache.org/dist/commons/daemon/source/

## tar 安装

### 目录配置

  ```bash
  cd /root
  unzip apache-tomcat-8.5.9.zip
  mkdir -p /usr/local/java
  mv apache-tomcat-8.5.9 /usr/local/java/tomcat
  cd /usr/local/java/tomcat
  chmod 777 bin/*
  ```

### 配置环境变量

  ```ini
  echo "export TOMCAT_HOME=/usr/local/java/tomcat" >> /etc/profile
  echo "export CATALINA_HOME=/usr/local/java/tomcat" >> /etc/profile
  echo "export CATALINA_BASE=/usr/local/java/tomcat" >> /etc/profile
  source /etc/profile
  echo $TOMCAT_HOME
  echo $CATALINA_HOME
  echo $CATALINA_BASE
  ```

### Tomcat 启动/关闭方法

  ```bash
  ./bin/startup.sh
  ./bin/shutdown.sh
  ```

## 配置

### 热部署

    这个需要在conf/server.xml里面看是否reloadable选项设置为了false，默认是true.

### 数据源设置

1. context.xml
```xml
<Context>
  <ResourceLink global="jdbc/ggwl" name="jdbc/ggwl" type="javax.sql.DataSource"/>
</Context>
```

2. server.xml
```xml
<server>
  <Resource 
    scope="Shareable" 
    type="javax.sql.DataSource" 
    maxActive="10" 
    maxIdle="5" 
    maxWait="10000" 
    name="jdbc/ggwl" 
    factory="org.apache.commons.dbcp2.BasicDataSourceFactory" 
    driverClassName="com.mysql.jdbc.Driver" 
    url="jdbc:mysql://172.18.66.7:3306/wms?useUnicode=true&amp;characterEncoding=utf8" 
    username="wms"
    password="Wms%#!"/>
</server>

```