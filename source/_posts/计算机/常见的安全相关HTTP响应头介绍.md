---
title: 常见的安全相关HTTP响应头介绍
date: 2021-04-21 08:57:23
cover: false
top_img: false
tags: [HTTP,网络安全]
categories: 学习笔记
---

以下这些，可以通过设置Nginx返回头部信息，或者在代码里添加filter，统一添加返回头信息。

## X-Frame-Options

1. 介绍
`X-Frame-Options` 是为了减少点击劫持（Clickjacking）而引入的一个响应头。Chrome4+、Firefox3.6.9+、IE8+均支持，详细的浏览器支持情况[看这里](https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/X-Frame-Options?redirectlocale=en-US&redirectslug=The_X-FRAME-OPTIONS_response_header#browser_compatibility)。
如果某页面被不被允许的页面以`<iframe>`或`<frame>`的形式嵌入，IE会显示类似于“此内容无法在框架中显示”的提示信息，Chrome和Firefox都会在控制台打印信息。由于嵌入的页面不会加载，这就减少了点击劫持的发生。

2. 用例
```config
x-frame-options: SAMEORIGIN
```

3. 取值：
- DENY：不允许被任何页面嵌入；
- SAMEORIGIN：不允许被本域以外的页面嵌入；
- ALLOW-FROM uri：不允许被指定的域名以外的页面嵌入（Chrome现阶段不支持）；

## X-XSS-Protection

1. 介绍
顾名思义，这个响应头是用来防范XSS的。浏览器提供的XSS保护机制并不完美，但是开启后仍然可以提升攻击难度，如果没有特殊需要，建议开启它。

2. 用例
```config
X-XSS-Protection: 1; mode=block
```

3. 取值：
- 0：禁用XSS保护；
- 1：启用XSS保护；
- 1; mode=block：启用XSS保护，并在检查到XSS攻击时，停止渲染页面（例如IE8中，检查到攻击时，整个页面会被一个#替换）；

## X-Content-Type-Options

1. 介绍
互联网上的资源有各种类型，通常浏览器会根据响应头的 `Content-Type` 字段来分辨它们的类型。
例如：`text/html` 代表html文档，`image/png`是PNG图片，`text/css`是CSS样式文档。

然而，有些资源的 `Content-Type` 是错的或者未定义。这时，某些浏览器会启用 `MIME-sniffing` 来猜测该资源的类型，解析内容并执行。
例如，我们即使给一个html文档指定`Content-Type为text/plain`，在IE8-中这个文档依然会被当做html来解析。利用浏览器的这个特性，攻击者甚至可以让原本应该解析为图片的请求被解析为JavaScript。

2. 用例
```config
X-Content-Type-Options: nosniff
```

3. 取值：
这个响应头的值只能是 nosniff，可用于IE8+和Chrome。另外，它还被Chrome用于扩展下载，[见这里](https://developer.chrome.com/extensions/hosting)。

## Strict-Transport-Security

1. 介绍
HTTP Strict Transport Security，简称为 HSTS 。它允许一个HTTPS网站，要求浏览器总是通过HTTPS来访问它。
现阶段，除了Chrome浏览器，Firefox4+，以及Firefox的NoScript扩展都支持这个响应头。

我们知道HTTPS相对于HTTP有更好的安全性，而很多HTTPS网站，也可以通过HTTP来访问。开发人员的失误或者用户主动输入地址，都有可能导致用户以HTTP访问网站，降低了安全性。一般，我们会通过Web Server发送301/302重定向来解决这个问题。
现在有了HSTS，可以让浏览器帮你做这个跳转，省一次HTTP请求。另外，浏览器本地替换可以保证只会发送HTTPS请求，避免被劫持。

2. 用例
```config
strict-transport-security: max-age=16070400; includeSubDomains
```

3. 取值：
`includeSubDomains` 是可选的，用来指定是否作用于子域名。支持HSTS的浏览器遇到这个响应头，会把当前网站加入`HSTS`列表，然后在max-age指定的秒数内，当前网站所有请求都会被重定向为https。即使用户主动输入 `http://` 或者不输入协议部分，都将重定向到 `https://` 地址。

Chrome内置了一个HSTS列表，默认包含Google、Paypal、Twitter、Linode等等服务。我们也可以在Chrome输入`chrome://net-internals/#hsts`，进入HSTS管理界面。在这个页面，你可以增加/删除/查询`HSTS`记录。例如，你想一直以`https`访问某网址，通过`“add Domain”`加上去就好了。
查看Chrome内置的全部HSTS列表，或者想把自己的网站加入这个列表，[请点这里](https://www.chromium.org/hsts)。

## X-Content-Security-Policy

1. 介绍
Content Security Policy，简称 CSP。顾名思义，这个规范与内容安全有关，主要是用来定义页面可以加载哪些资源，减少 XSS 的发生。
CSP 协议可以控制的内容非常多。而且如果不特别指定 unsafe-inline 时，页面上所有 inline 样式和脚本都不会执行；不特别指定 unsafe-eval，页面上不允许使用 new Function，setTimeout，eval 等方式执行动态代码。在限制了页面资源来源之后，被 XSS 的风险确实小不少。

当然，仅仅依靠 CSP 来防范 XSS 是远远不够的，不支持全部浏览器是它的硬伤。不过，鉴于低廉的开发成本，加上也没什么坏处


2. 用例
```config
Content-Security-Policy: default-src 'self'
```
注：default-src 是 CSP 指令，多个指令之间用英文分号分割；‘self’ 是指令值，多个指令值用英文空格分割

3. 取值：

| 指令        | 指令值示例       | 说明                                                                                                                                                                   |
| ----------- | ---------------- | ---------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| default-src | ‘self’ cnd.a.com | 定义针对所有类型（js、image、css、web font，ajax 请求，iframe，多媒体等）资源的默认加载策略，某类型资源如果没有单独定义策略，就使用默认的。                            |
| script-src  | ‘self’ js.a.com  | 定义针对 JavaScript 的加载策略。                                                                                                                                       |
| style-src   | ‘self’ css.a.com | 定义针对样式的加载策略。                                                                                                                                               |
| img-src     | ‘self’ img.a.com | 定义针对图片的加载策略。                                                                                                                                               |
| connect-src | ‘self’           | 针对 Ajax、WebSocket 等请求的加载策略。不允许的情况下，浏览器会模拟一个状态为 400 的响应。                                                                             |
| font-src    | font.a.com       | 针对 WebFont 的加载策略。                                                                                                                                              |
| object-src  | ‘self’           | 针对 `<object>`、`<embed>` 或 `<applet>` 等标签引入的 flash 等插件的加载策略。                                                                                         |
| media-src   | media.a.com      | 针对 `<audio>` 或 `<video>` 等标签引入的 HTML 多媒体的加载策略。                                                                                                       |
| frame-src   | ‘self’           | 针对 frame 的加载策略。                                                                                                                                                |
| sandbox     | allow-forms      | 对请求的资源启用 sandbox（类似于 iframe 的 sandbox 属性）。                                                                                                            |
| report-uri  | /report-uri      | 告诉浏览器如果请求的资源不被策略允许时，往哪个地址提交日志信息。 特别的：如果想让浏览器只汇报日志，不阻止任何内容，可以改用 `Content-Security-Policy-Report-Only` 头。 |




待处理
```xml
<init-param>
    <param-name>Referrer-Policy</param-name>
    <param-value>no-referrer-when-downgrade</param-value>
</init-param>
<init-param>
    <param-name>X-Permitted-Cross-Domain-Policies</param-name>
    <param-value>master-only</param-value>
</init-param>
<init-param>
    <param-name>X-Download-Options</param-name>
    <param-value>noopen</param-value>
</init-param>

```




————————————————
版权声明：本文为CSDN博主「小白旗」的原创文章，遵循CC 4.0 BY-SA版权协议，转载请附上原文出处链接及本声明。
原文链接：https://blog.csdn.net/qq_37788558/article/details/105460837

