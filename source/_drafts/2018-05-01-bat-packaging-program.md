---
layout: post
title: mvn项目bat打包脚本
tags: [bat, draft]
published: true
excerpt: mvn项目可以直接在命令行或者使用ide打包，但是涉及到生产和测试环境的文件替换、变量修改等，可以使用脚本打包。
---

以下代码需要根据实际情况调整，【待优化】

## 入口

```console
@echo off
title project_name Packaging Program

set s_root_dir=D:\coding\java\project_name
set JAVA_HOME=E:\java\jdk1.8.0_144

:menu
cd %s_root_dir%\

echo ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
echo Select your choice, then press enter
echo ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

echo 1.Make sadp jar
echo 2.Make local war
echo 3.Make test war
echo 4.Make production war
echo 5.Quit

:sel

set /p choice= choice:

if /i %choice%==1 goto jar
if /i %choice%==2 goto local
if /i %choice%==3 goto test
if /i %choice%==4 goto pro
if /i %choice%==5 goto end

echo Your choice is invalid, please try again!

goto sel

:jar
call  make-sadp-jar.bat
goto menu

:test
call  make-war.bat test
goto menu

:pro
call  make-war.bat pro
goto menu

:end
echo operate sucess,press any key to continue&pause>nul
```

## 打包代码

```console
@echo off

set s_root_dir=D:\coding\java\project_name
set project_name=project_name
set replace_dir=%s_root_dir%\replace

if %1==test goto replaceTestFiles
if %1==pro goto replaceProFiles

:replaceTestFiles
copy /y %replace_dir%\test\jdbc.properties %s_root_dir%\%project_name%\src\main\config
goto makejar

:replaceProFiles
copy /y %replace_dir%\pro\jdbc.properties %s_root_dir%\%project_name%\src\main\config
goto makejar

:makejar
echo makejar---------------------------------
cd %s_root_dir%\%project_name%\
mvn clean package
echo %time% make %1-war successful
```