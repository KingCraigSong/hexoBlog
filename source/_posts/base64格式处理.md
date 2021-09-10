---
title: base64格式处理
top_img: false
cover: false
date: 2021-02-02 14:40:17
updated:
tags:
categories:
---

```java
/**
 * Base64 支持类型，并转换数据格式
 */
public enum Base64Type {
    IMAGE_GIF("data:image/gif;base64,","gif"),
    IMAGE_PNG("data:image/png;base64,","png"),
    IMAGE_JPG("data:image/jpg;base64,","jpg"),
    IMAGE_JPEG("data:image/jpeg;base64,","jpeg"),
    IMAGE_ICON("data:image/x-icon;base64,","ico"),
    TEXT_PLAIN("data:text/plain,bas64,","txt"),
    APPLICATION_PDF("data:application/pdf;base64,","pdf"),
    APPLICATION_DOC("data:application/doc;base64,","doc"),
    APPLICATION_DOCX("data:application/docx;base64,","docx"),
    APPLICATION_XLS("data:application/xls;base64,","xls"),
    APPLICATION_XLSX("data:application/xlsx;base64,","xlsx"),
    APPLICATION_PPT("data:application/ppt;base64,","ppt"),
    APPLICATION_PPTX("data:application/pptx;base64,","pptx"),
    APPLICATION_OTHER("data:application/*;base64,","*"),
    
    ;
    
    private final String prefix;
    private final String type;
    Base64Type(String prefix,String type){
        this.prefix = prefix;
        this.type = type;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getType() {
        return type;
    }
}

```
```java
/**
 * @功能  精确计算base64字符串文件大小（单位：B）
 * @注意  base64字符串(不含data:audio/wav;base64,文件头)
 */
public static double base64file_size( String base64String )  {

            //1.获取base64字符串长度(不含data:audio/wav;base64,文件头)
        int size0 = base64String.length();

        //2.获取字符串的尾巴的最后10个字符，用于判断尾巴是否有等号，正常生成的base64文件'等号'不会超过4个
        String tail = base64String.substring(size0-10);

        //3.找到等号，把等号也去掉,(等号其实是空的意思,不能算在文件大小里面)
        int equalIndex = tail.indexOf("=");        
        if(equalIndex > 0) {
                size0 = size0 - (10 - equalIndex);
        }

        //4.计算后得到的文件流大小，单位为字节
        return size0 -( (double)size0 / 8 ) * 2;
}
```
