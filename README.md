# DRMS

数字版权保护系统

 Digital rights management system

功能：登录、注册、上传文件并加密、下载文件并解密，下载非自己上传的文件不可打开或打开无显示，实现文件保护

v4.1.0更新：通过ActiveX获取客户端MAC，仅支持IE浏览器

v4.2.0更新：改进密码传输过程，对密码进行SHA256加密后再传输，提高安全性

## 一、设计目的

### 1.1 背景

随着全球信息化进程的推进以及信息技术向各个领域的不断延伸，数字出版产业的发展势头强劲，并日益成为我国出版产业变革的“前沿阵地”。有预测数据显示，到2020年，我国网络出版的销售额将占到出版产业的50%，而到2030年，90%的图书都将是网络版本。

数字版权也就是各类出版物、信息资料的网络出版权，可以通过新兴的数字媒体传播内容的权利。包括制作和发行各类电子书、电子杂志、手机出版物等的版权。

一般出版社都具有该社所出版图书资料的自行出版数字版权，少数有转授权，即可以将该数字出版权授予第三方机构进行使用。

### 1.2 定义

数字版权保护（Digital Rights Management，DRM）是对网络中传播的数字作品进行版权保护的主要手段。DRM是由美国出版商协会来定义的：“在数字内容交易过程中对知识产权进行保护的技术，工具和处理过程”。DRM是采取信息安全技术手段在内的系统解决方案，在保证合法的、具有权限的用户对数字信息（如数字图像、音频、视频等）正常使用的同时，保护数字信息创作者和拥有者的版权，根据版权信息获得合法收益，并在版权受到侵害时能够鉴别数字信息的版权归属及版权信息的真伪。

数字版权保护技术就是对各类数字内容的知识产权进行保护的一系列软硬件技术，用以保证数字内容在整个生命周期内的合法使用，平衡数字内容价值链中各个角色的利益和需求，促进整个数字化市场的发展和信息的传播。具体来说，包括对数字资产各种形式的使用进行描述、识别、交易、保护、监控和跟踪等各个过程。数字版权保护技术贯穿数字内容从产生到分发、从销售到使用的整个内容流通过程，涉及整个数字内容价值链。

## 二、设计要求

（1）服务器获得请求客户端机器的硬件信息，比如：MAC，CPU或者硬盘序列号等；

（2）通过用户名和硬件信息生成Hash值，用此Hash值对受保护的文件（文档、语音、图像、视频）进行加密；

（3）客户端机器根据用户名和硬件信息按照同样的方法生成Hash，对文件进行解密使用。

## 三、设计方案

### 3.1 总体设计

考虑到用户使用的便捷性、维护的高效性，本系统设计为B/S架构，前端向用户展示，提供登录、注册、上传文件、下载文件、文件展示等功能，后台为前端提供支持。

总体设计如图1所示：

![图示 描述已自动生成](media/4544a88c2d9b972ef6c9d0a406e3fb9e.png)

<center>图1 系统总体设计


![图示 描述已自动生成](media/038387878ffe51192b413658064355d7.png)

<center>图2 系统流程图


### 3.2 前端设计

前端使用bootstrap框架、thymeleaf模板进行开发，整体风格颜色为绿色，给人以安全的感觉，选取“数字版权保护系统”的英文缩写DRMS作为系统文字logo，并选取绿色盾牌作为系统logo，寓意系统的安全。

![](media/065951f12974b88501d6a5bfb0b0551c.png)
![](media/627f803bdcbe68f7e8d085582d90181f.png)

图3 系统文字logo 图4 系统logo

### 3.2 后台设计

后台使用Java语言，使用Spring Boot快速开发，整合Mybatis进行数据库操作，提高了开发效率，简化开发流程。

用户登录注册过程中，对密码进行加盐处理，提高了用户登陆注册的安全性。

文件上传下载过程中，使用AES算法进行加解密，并且直接对文件流进行加解密操作，避免产生缓存文件，大大提高了加解密效率，提升用户使用体验。

另外，使用阿里云文件存储OSS服务存储文件，而不是放在网站服务器上，分离部署，提高系统整体安全性，防止网站被攻击后造成文件泄露。

开发完成后，系统部署到阿里云服务器，可以直接使用。

### 3.3 数据库设计

数据库使用MySQL，并使用phpmyadmin进行可视化处理，数据库内设置两张表，分别为用户表User和文件列表FileList。

#### 3.3.1 用户表User

| 字段名   | 类型        | 备注       |
| -------- | ----------- | ---------- |
| id       | int(8)      | 用户id     |
| username | varchar(20) | 用户名     |
| pwdHash  | varchar(20) | 密码的哈希 |
| sault    | varchar(20) | 盐值       |
| counter  | int(10)     | 计数器     |

![图片包含 表格 描述已自动生成](media/64dff09d9722fc69cb920ca75d636025.png)

备注：

（1）仅存储密码的哈希值，防止被抓包获取明文密码；

（2）设置盐值，提高猜测密码的难度；

（3）设置计数器，防止重放攻击。

#### 3.3.2 文件列表FileList

| 字段名   | 类型        | 备注     |
| -------- | ----------- | -------- |
| uuid     | varchar(60) | 文件id   |
| filename | varchar(60) | 文件名   |
| filesize | varchar(10) | 文件大小 |
| uploader | varchar(20) | 上传者   |
| uptime   | varchar(20) | 上传时间 |

## 四、实现方法

### 4.1 前端实现

| 页面文件              | 备注             |
| --------------------- | ---------------- |
| login.html            | 登录页面         |
| login-failed.html     | 登录失败跳转     |
| register.html         | 注册页面         |
| register-success.html | 注册成功跳转     |
| index.jsp             | 主页             |
| upload-file.jsp       | 上传文件页面     |
| delete-failed.html    | 删除文件失败跳转 |

![图示 描述已自动生成](media/1143f5fecba06fe20eabcfba19f75313.png)

<center>图5 前端首页布局


### 4.2 后台实现

#### 4.2.1 Controller控制器

| controller         | 备注             |
| ------------------ | ---------------- |
| IndexController    | 页面转发处理     |
| UserController     | 登录注册处理     |
| UserFileController | 文件上传下载处理 |

#### 4.2.2 Entity实体类

| entity   | 备注     |
| -------- | -------- |
| User     | 用户实体 |
| UserFile | 文件实体 |

#### 4.2.3 Filter过滤器

| filter      | 备注           |
| ----------- | -------------- |
| LoginFilter | 未登录禁止访问 |

#### 4.2.4 Mapper数据库操作

| mapper         | 备注         |
| -------------- | ------------ |
| UserMapper     | 登陆注册操作 |
| UserFileMapper | 文件信息操作 |

#### 4.2.5 Service服务层

| service         | 备注             |
| --------------- | ---------------- |
| UserService     | 登录注册服务     |
| UserFileService | 文件上传下载服务 |

#### 4.2.6 Utils工具类

| utils          | 备注                      |
| -------------- | ------------------------- |
| FileCryptoUtil | 文件流加解密              |
| GetMacUtil     | 获取本机MAC地址用于加解密 |
| OSSClientUtil  | 连接配置阿里云OSS         |

## 五、测试结果

### 5.1 注册

![图形用户界面, 应用程序
描述已自动生成](media/6d2823137d1d87406870e10e75508c76.png)

<center>图6 注册页面


![图形用户界面, 文本, 应用程序
描述已自动生成](media/f6de43a6206fd70802f37e9bfc5236e1.png)

<center>图7 注册用户名不能为空提示


![文本 中度可信度描述已自动生成](media/6807555f3235ba8d675851341fb78ed8.png)

<center>图8 账号已存在提示


![图形用户界面, 文本 描述已自动生成](media/8713a8f95910040cd424dbc52fab5dbb.png)

<center>图9 输入的两次注册密码不一致提示


### 5.2 登录

![图形用户界面, 应用程序
描述已自动生成](media/07a3bff9d6ff7019bc5f06237374ce17.png)

<center>图10 登录页面


### 5.3 首页

![图形用户界面, 文本, 应用程序, 电子邮件
描述已自动生成](media/44e0ab271e598090c9e1c7ed5304fa1e.png)

<center>图11 首页文件列表


### 5.4 上传文件

![图形用户界面, 应用程序, Teams
描述已自动生成](media/b6de47f5ba04cb5711b10303b3272a5f.png)

<center>图12 上传文件页面


### 5.5 上传成功返回首页

![图形用户界面, 文本, 应用程序, 电子邮件
描述已自动生成](media/0c8b2d7b2a602baec3d81222fce5b9d4.png)

<center>图13 首页文件列表更新


### 5.6 上传者下载文件

![图形用户界面, 文本, 应用程序, 电子邮件
描述已自动生成](media/e3e00040c398a16bd2c3be45ddfffa91.png)

<center>图14 成功下载


![文本 描述已自动生成](media/df8ef0eb2da6b668d52e2d466c3556a5.png)

<center>图15 成功打开


### 5.6 非上传者下载文件

![图形用户界面, 文本, 应用程序
描述已自动生成](media/d713a9e100c4395b4448d1c3b4f2feb4.png)

<center>图16 文本文件显示空白


![图形用户界面, 文本, 应用程序
描述已自动生成](media/10bc942c28b2b9af74955aa9cf473c3b.png)

<center>图17 图片、音频、视频文件无法打开


### 5.7 阿里云OSS存储

![](media/dd0b1b04612edaa1d7d35b8ea969aa0b.png)

<center>图18 阿里云OSS


## 六、设计心得

本次项目完成过程不能说一帆风顺，系统实现前期，我使用Dao设计模式进行设计，由于之前写过一些Web系统，因此系统的登录、注册以及文件列表展示的部分开发比较快，并且考虑到登录注册的安全问题，以及考虑到与密码学的知识相结合，我使用了密码加盐的处理方式，并且使用了计数器防止重放攻击。但是如何使用浏览器获取客户端的硬件信息难住了我。通过查找资料，我找到了一种方法，即通过使用浏览器的ActiveX控件读取本地文件的方法来获取客户端系统信息，然而ActiveX控件目前也逐步被淘汰，只有IE浏览器才能够使用，但目前我还没有找到更好的途径，因此暂时采用了这种方法。

本系统的主要功能便是文件的上传和下载，并且要进行加解密认证。上传和下载文件倒是比较容易实现，如何进行加解密才是重中之重。通过比较，我选择AES算法进行加解密，密钥为“用户名+MAC”的HASH值。那么如何在上传和下载过程中进行加解密呢？一开始我选择使用保留缓存文件的方法，用户上传的文件先保存到服务器的缓存目录下，再读取出来进行加密后存放到文件目录。但这种方式存在安全隐患，服务器被攻击后容易泄露文件，因此我使用加解密文件流的方式，在文件上传和下载过程中直接对文件流进行加解密，并且将文件存储到阿里云OSS，提高了文件的安全性。至此，系统基本开发完毕，为了美观，我还设计了系统logo；为了方便使用，我还将系统部署到云服务器，并配置域名与https。

五一劳动节放假之前，系统已经完成。于是我又学习了Spring Boot、Mybatis等技术栈，并改写我的系统，使用了Spring Boot开发并且整合Mybatis进行数据库操作。改进后的系统更加整洁，运行维护也更加高效了。
Spring Boot开发版本：https://github.com/Wenzhao299/FRMS

总体来说，我在本项目开发过程中，学到了很多新知识，也实践了讲专业所学知识与实际开发结合，更加深入地体会到专业知识的应用方法，而不仅仅是停留在书面阶段。作为一名计算机方面专业的学生，动手能力十分重要，在以后的学习生活工作中，我也会更加注意自己的动手实践能力，提高自己的综合素质。
