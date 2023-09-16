# 项目效果展示

**管理端在线演示** 👉 [地址](http://project_sky.weitianshu.me/#/login) 

**接口文档在线查看** 👉 [地址](http://sky_apidoc.weitianshu.me/doc.html#/home) 

管理端-外卖商家使用

![image-20230912162049618](https://s2.loli.net/2023/09/12/T73yWPfqIjcVKxA.png)

用户端-点餐用户使用

![image-20230912162148087](https://s2.loli.net/2023/09/12/uVCOLE2GtcvPTjW.png)

**所有资源打包下载见文末**

# 项目配置

本项目已实现跳过微信支付环节，故不配置微信支付相关配置，已实现利用百度地图校验收获地址和店铺地址见的距离，即大于5公里不接单，用户端会下单失败。

## 数据库配置

新建一个数据库后，运行`sky_take_out.sql`文件导入各个表的初始结构，在`application-dev.yml`文件中添加数据库的相关配置如下：

```yaml
datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    host: mysql服务地址
    port: mysql服务端口号
    database: sky_take_out(连接的数据库名称)
    username: 用户名
    password: 密码
```

## Redis配置

安装redis后在上诉文件中按如下配置项添加如下配置信息：

```yaml
redis:
    host: redis运行的服务地址
    port: redis服务端口号
    database: 连接redis的第几号数据库
```

## 阿里云OSS配置

开通自己的OSS服务后填入如下配置信息：

```yaml
alioss:
    endpoint: oss-cn-shanghai.aliyuncs.com(在阿里云创建OSS时选择区域后可以得到，这里是上海的URL)
    access-key-id: 你账户的access-key-id，建议创建使用子账户更安全
    access-key-secret: 你账户access-key-id对应的密钥，建议创建使用子账户更安全
    bucket-name: 你创建的bucket的名称
```

## 微信小程序配置

申请到自己的小程序后，添加如下配置信息：

```yaml
wechat:
    appid: 小程序ID
    secret: 小程序ID对应的密钥
```

## 百度地图配置

在百度地图开放平台（https://lbsyun.baidu.com/index.php?title=%E9%A6%96%E9%A1%B5） 申请到应用后填入如下配置信息：

```yaml
baidumap:
    ak: 百度地图创建的应用ak
    webservice_geocoding_url: https://api.map.baidu.com/geocoding/v3 (地理编码API请求地址)
    webservice_-direction-lite_url: https://api.map.baidu.com/directionlite/v1/riding (轻量级路线规划骑行路线规划API请求地址)
```

## 店铺地址配置

可以在上述配置文件中自定义商家店铺的地址：

```yaml
shop:
    address: 自定义店铺地址
```

完整的配置文件如下图所示：

![image-20230912175740611](https://s2.loli.net/2023/09/12/qbURYsn3CNzwTO1.png)

## 管理端前端配置

管理端前端打包后可见于`sky_take_out管理端前端运行环境.zip`中，为了使`websocket`协议生效，`Nginx`配置文件中的代理端口请设置为`80`。具体如下图所示：

![image-20230912192501244](https://s2.loli.net/2023/09/12/RQBZfopJ2OLwdK3.png)

Windows操作系统可直接解压使用，请勿在包含中文的路径下解压。

mac OS请使用`homebrew`安装`Nginx`后修改`Nginx`的配置文件，其默认位置为：

```shell
/opt/homebrew/etc/nginx/nginx.conf
```

将压缩包中的`html\sky`文件夹直接拷贝到`Nginx`的默认运行目录下：

```shell
/opt/homebrew/var/www
```

之后修改配置文件中的`location`中`root`项为下：

```yaml
location / {
            root    /opt/homebrew/var/www/sky;
            index  index.html index.htm;
        }
```

之后启动`Nginx`即可，建议使用`nginx -t`命令检查配置文件是否正确。

## 用户端小程序

用户端小程序源码见我另外一个仓库，地址为：https://github.com/weitianpaxi/sky_take_out_applet 已删除微信支付相关功能代码。

# 项目简介

本项目（苍穹外卖）是专门为餐饮企业（餐厅、饭店）定制的一款软件产品，包括 系统管理后台 和 小程序端应用 两部分。其中系统管理后台主要提供给餐饮企业内部员工使用，可以对餐厅的分类、菜品、套餐、订单、员工等进行管理维护，对餐厅的各类数据进行统计，同时也可进行来单语音播报功能。小程序端主要提供给消费者使用，可以在线浏览菜品、添加购物车、下单、支付、催单等。

接下来，通过功能架构图来展示**管理端**和**用户端**的具体业务功能模块。

![image-20230912162406537](https://s2.loli.net/2023/09/12/d7NXLOjurpaVB3A.png)

1. 管理端功能

员工登录/退出 , 员工信息管理 , 分类管理 , 菜品管理 , 套餐管理 , 菜品口味管理 , 订单管理 ，数据统计，来单提醒。

2. 用户端功能

微信登录 , 收件人地址管理 , 用户历史订单查询 , 菜品规格查询 , 购物车功能 , 下单 , 支付、分类及菜品浏览。

# 产品原型

**管理端原型图：**

![image-20230912162617314](https://s2.loli.net/2023/09/12/w7MX1hBmTWNKVSQ.png)

**用户端原型图：**

![image-20230912162641105](https://s2.loli.net/2023/09/12/HCILNeyfqh2zERx.png)

1. 管理端

餐饮企业内部员工使用。 主要功能有: 

| 模块      | 描述                                                         |
| --------- | ------------------------------------------------------------ |
| 登录/退出 | 内部员工必须登录后,才可以访问系统管理后台                    |
| 员工管理  | 管理员可以在系统后台对员工信息进行管理，包含查询、新增、编辑、禁用等功能 |
| 分类管理  | 主要对当前餐厅经营的 菜品分类 或 套餐分类 进行管理维护， 包含查询、新增、修改、删除等功能 |
| 菜品管理  | 主要维护各个分类下的菜品信息，包含查询、新增、修改、删除、启售、停售等功能 |
| 套餐管理  | 主要维护当前餐厅中的套餐信息，包含查询、新增、修改、删除、启售、停售等功能 |
| 订单管理  | 主要维护用户在移动端下的订单信息，包含查询、取消、派送、完成，以及订单报表下载等功能 |
| 数据统计  | 主要完成对餐厅的各类数据统计，如营业额、用户数量、订单等     |

2. 用户端

移动端应用主要提供给消费者使用。主要功能有:

| 模块        | 描述                                                         |
| ----------- | ------------------------------------------------------------ |
| 登录/退出   | 用户需要通过微信授权后登录使用小程序进行点餐                 |
| 点餐-菜单   | 在点餐界面需要展示出菜品分类/套餐分类, 并根据当前选择的分类加载其中的菜品信息, 供用户查询选择 |
| 点餐-购物车 | 用户选中的菜品就会加入用户的购物车, 主要包含 查询购物车、加入购物车、删除购物车、清空购物车等功能 |
| 订单支付    | 用户选完菜品/套餐后, 可以对购物车菜品进行结算支付, 这时就需要进行订单的支付 |
| 个人信息    | 在个人中心页面中会展示当前用户的基本信息, 用户可以管理收货地址, 也可以查询历史订单数据 |

# 技术选型

关于本项目的技术选型, 我们将会从 用户层、网关层、应用层、数据层 这几个方面进行介绍，主要用于展示项目中使用到的技术框架和中间件等。

![image-20230912162843147](https://s2.loli.net/2023/09/12/BrsPvZ5HuUE3Dc7.png)

1. 用户层

本项目中在构建系统管理后台的前端页面，使用H5、Vue.js、ElementUI、apache echarts(展示图表)等技术。而在构建移动端应用时，使用微信小程序。

2. 网关层

使用`Nginx`进行反向代理和负载均衡。

3. 应用层

- SpringBoot： 快速构建Spring项目, 采用 "约定优于配置" 的思想, 简化Spring项目的配置开发。
- SpringMVC：SpringMVC是spring框架的一个模块，springmvc和spring无需通过中间整合层进行整合，可以无缝集成。
- Spring Task:  由Spring提供的定时任务框架。
- httpclient:  主要实现了对http请求的发送。JDK17内置，JDK1.8需要依赖第三方导包。
- Spring Cache:  由Spring提供的数据缓存框架
- JWT:  用于对应用程序上的用户进行身份验证的标记。
- 阿里云OSS:  对象存储服务，在项目中主要存储文件，如图片等。
- Swagger： 生成接口文档，并对接口进行测试。
- POI:  封装了对Excel表格的常用操作。
- WebSocket: 一种通信网络协议，使客户端和服务器之间的数据交换更加简单，用于项目的来单、催单功能实现。

4. 数据层

- MySQL： 关系型数据库, 本项目的核心业务数据都会采用MySQL进行存储。
- Redis：键值存储中间件，缓存数据。
- Mybatis： 本项目持久层将会使用Mybatis框架开发。

5. 基本工具

- git: 版本控制工具, 对项目中的代码进行管理。
- maven: 项目构建工具。
- junit：单元测试工具，开通过junit对功能进行单元测试。
- postman:  接口测试工具，模拟用户发起的各类HTTP请求，获取对应的响应结果。

# 资源下载
本项目以及用户端小程序代码全部打包下载链接：链接: https://pan.baidu.com/s/1s4DkWsubp8Szx-IkJJkxSA 提取码: 2333 
