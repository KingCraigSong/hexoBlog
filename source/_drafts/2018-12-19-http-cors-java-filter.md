---
layout: post
title: CORS跨域
tags: [http]
published: true
excerpt: 根据浏览器的同源策略, 当请求的地址与来源地址的协议、域名、端口中的任一值不相同时, 均视为是一个跨域的请求。Cross-Origin Resource Sharing(CORS) 是W3C为浏览器制定的可以跨域通信的规范。通过使用 XMLHttpRequest 对象, CORS可以让开发者方便的进行跨域通信, 就像在使用同域通信一样。
---

## 什么是跨域

Cross-Origin Resource Sharing(CORS) 是W3C为浏览器制定的可以跨域通信的规范。通过使用 XMLHttpRequest 对象, CORS可以让开发者方便的进行跨域通信, 就像在使用同域通信一样。

CORS的使用十分简单。想象一下有一个网站 a.com 想要获取另一个网站 b.com 的数据。但由于浏览器的同源策略, 这样的请求将会被禁止. 这时我们可以使用CORS, 通过添加一些特殊的请求\响应头, 可以让 a.com 访问 b.com 的数据。

根据浏览器的同源策略, 当请求的地址与来源地址的协议、域名、端口中的任一值不相同时, 均视为是一个跨域的请求。

## 解决跨域问题

1. jsonp跨域

    JSONP（JSON with Padding：填充式JSON)，应用JSON的一种新方法，JSON、JSONP的区别：
    - JSON返回的是一串数据、JSONP返回的是脚本代码(包含一个函数调用)
    - JSONP 只支持get请求、不支持post请求(类似往页面添加一个script标签，通过src属性去触发对指定地址的请求,故只能是Get请求)

2. nginx反向代理：

    www.baidu.com/index.html需要调用www.sina.com/server.php，可以写一个接口www.baidu.com/server.php，由这个接口在后端去调用www.sina.com/server.php并拿到返回值，然后再返回给index.html

3. PHP端修改header

    header(‘Access-Control-Allow-Origin:*’);//允许所有来源访问
    header(‘Access-Control-Allow-Method:POST,GET’);//允许访问的方式

4. document.domain

    跨域分为两种，一种xhr不能访问不同源的文档，另一种是不同window之间不能进行交互操作;
    - document.domain主要是解决第二种情况，且只能适用于主域相同子域不同的情况；
    - document.domain的设置是有限制的，我们只能把document.domain设置成自身或更高一级的父域，且主域必须相同。例如：a.b.example.com中某个文档的document.domain可以设成a.b.example.com、b.example.com 、example.com中的任意一个，但是不可以设成c.a.b.example.com，因为这是当前域的子域，也不可以设成baidu.com，因为主域已经不相同了。
    - 兼容性：所有浏览器都支持；
    - 优点：可以实现不同window之间的相互访问和操作；
    - 缺点：只适用于父子window之间的通信，不能用于xhr；只能在主域相同且子域不同的情况下使用；

### Filter文件

```java
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * 支持 CORS 跨域
 * web先OPTIONS请求，若 Access-Control-Allow-Origin：*，则发送真正的请求
 * @author
 * @since 1.0.0
 */
public class CorsFilter implements Filter {

    private String allowOrigin;
    private String allowMethods;
    private String allowCredentials;
    private String allowHeaders;
    private String exposeHeaders;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        allowOrigin = filterConfig.getInitParameter("allowOrigin");
        allowMethods = filterConfig.getInitParameter("allowMethods");
        allowCredentials = filterConfig.getInitParameter("allowCredentials");
        allowHeaders = filterConfig.getInitParameter("allowHeaders");
        exposeHeaders = filterConfig.getInitParameter("exposeHeaders");
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
     throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        if (!CStringUtils.isEmpty(allowOrigin)) {
            if("*".equals(allowOrigin)){
                response.setHeader("Access-Control-Allow-Origin", "*");
            }else {
                List<String> allowOriginList = Arrays.asList(allowOrigin.split(","));
                if (!CollectionUtils.isEmpty(allowOriginList)) {
                    String currentOrigin = request.getHeader("Origin");
                    if (allowOriginList.contains(currentOrigin)) {
                        response.setHeader("Access-Control-Allow-Origin", currentOrigin);
                    }
                }
            }
        }
        if (!StringUtils.isEmpty(allowMethods)) {
            response.setHeader("Access-Control-Allow-Methods", allowMethods);
        }
        if (!StringUtils.isEmpty(allowCredentials)) {
            response.setHeader("Access-Control-Allow-Credentials", allowCredentials);
        }
        if (!StringUtils.isEmpty(allowHeaders)) {
            response.setHeader("Access-Control-Allow-Headers", allowHeaders);
        }
        if (!StringUtils.isEmpty(exposeHeaders)) {
            response.setHeader("Access-Control-Expose-Headers", exposeHeaders);
        }
        if("OPTIONS".equals(request.getMethod())){
            return;
        }
        chain.doFilter(req, res);
    }

    @Override
    public void destroy() {
    }
}
```

#### web.xml配置

```xml
<filter>
  <filter-name>CorsFilter</filter-name>
  <filter-class>com.sgai.sadp.core.cors.CorsFilter</filter-class>
  <init-param>
    <param-name>allowOrigin</param-name>
    <param-value>*</param-value>
  </init-param>
  <init-param>
    <param-name>allowMethods</param-name>
    <param-value>GET,PUT,DELETE,POST</param-value>
  </init-param>
  <init-param>
    <param-name>allowHeaders</param-name>
    <param-value>Origin,X-Requested-With,Content-Type,Accept</param-value>
  </init-param>
</filter>
<filter-mapping>
  <filter-name>CorsFilter</filter-name>
  <url-pattern>/*</url-pattern>
</filter-mapping>
```
