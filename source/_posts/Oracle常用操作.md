---
title: Oracle常用操作
date: 2021-02-25 09:04:06
tags: [数据库,oracle]
categories:
- 学习笔记
- 计算机
---



select * from v$session t1, v$locked_object t2 where t1.sid = t2.SESSION_ID; 

alter system kill session '28,26421'; 



drop sequence seq_t_test;
create sequence seq_t_test increment by 1 start with 你想要的值 maxvalue 999999999;





1 查看表空间路径
select * from dba_data_files;
2、创建用户表空间：
CREATE TABLESPACE NOTIFYDB DATAFILE '/oracle/oradata/test/notifydb.dbf' SIZE 200M AUTOEXTEND ON EXTENT MANAGEMENT LOCAL SEGMENT SPACE MANAGEMENT AUTO;
3、创建用户，指定密码和上边创建的用户表空间
CREATE USER hc_notify IDENTIFIED BY hc_password DEFAULT TABLESPACE NOTIFYDB;
4、赋予权限
grant connect,resource to hc_notify;
grant unlimited tablespace to hc_notify;
grant create database link to hc_notify;
grant select any sequence,create materialized view to hc_notify;

grant create view to wms;