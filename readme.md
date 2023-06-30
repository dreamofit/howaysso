# howaysso 说明文档
## 项目背景
    用于对各网站与网站用户进行统一管理，可以一处登录多处直接进入系统而不需要重复登录，
    逻辑为sso进行授权登录然后统一跳转到目标网站，如果sso判断你就是对应用户已登录则不用继续登录，
    而每一个网站则单独成一个项目，sso相当于枢纽，将这些不相关系统统一起来。

## 项目需求
    1.单点登录系统支持所有授权网站进行统一登陆
    2.单点登录系统需要对用户进行管理
    3.单点登录系统需要对授权网站进行管理
    4.可以对用户进行授权访问能够访问的网站，对于无权限用户禁止访问

## 技术准备
    后端服务拟采用java，其中jdk版本基于17进行开发，使用springboot框架，数据库使用mybatis，使用dubbo进行分布式开发。
    后续升级可能使用的技术为redis缓存、docker容器、消息队列。
    前端使用react进行开发，以后端为准。

## 项目框架
    howaysso-core: 数据库核心层
    howaysso-processor： 处理器层，各服务核心处理逻辑
    howaysso-util: 工具库
    howaysso-api: dubbo api
    howaysso-access:接入层，包括RPC和HTTP方式接入

## 项目启动
    运行启动类ServiceInit就行，需要加入JVM参数：--add-opens java.base/java.lang=ALL-UNNAMED
    同时需要启动zk和配置对应的数据库

## Token生成规则 （待更新）
    1.username
    2.password
    3.appkey
    4.appsecret
    5.随机数
    6.时间戳
    算法设计粗略如下：
    随机数（4位）
    随机算法（2位）+（username+password）(8位)
    随机算法（2位）+（appkey+appsecret）(6位)
    签名：前面字符串用md5进行签名（4位）
    注意：具体算法见代码HowayToken


## 数据库
    用户表：记录用户基本信息和对应权限 【T】
    网站表：记录各授权网站需要的最低权限等级 【T】
    日志表：【F】(日志表暂时未做，考虑是否独立出去)
    1.	记录某用户何时登录某网站，登录成功还是失败
    2.	记录某管理对某用户/网站进行了XX操作
    权限说明：
    999：超级管理员 ：全系统唯一，可以对管理员进行删改操作，拥有最大权限
    99：管理员 ：由超级管理员进行授权，可以对用户进行限制操作，可以调整网络权限。可以访问管理网站
    9：超级会员 ：暂无
    3：普通会员 ：暂无
    2：普通用户 ：可以访问目前全部免费系统网站
    1：限制用户 ：部分网站进行限制访问，由管理员及以上进行处理
    0：永禁用户：此类用户被永封，包括电话在内的信息不可重新注册，由超级管理员进行
    
    建表语句如下：
    CREATE TABLE `user` (
    `id` int NOT NULL AUTO_INCREMENT,
    `name` varchar(64) NOT NULL COMMENT '姓名',
    `password` varchar(45) NOT NULL COMMENT '密码——md5加密',
    `tel` varchar(11) NOT NULL COMMENT '电话',
    `qq` varchar(12) DEFAULT NULL,
    `email` varchar(32) DEFAULT NULL COMMENT '邮箱',
    `site` varchar(64) DEFAULT NULL COMMENT '个人网站',
    `sign` varchar(128) DEFAULT NULL COMMENT '个性签名',
    `role` int NOT NULL DEFAULT '2' COMMENT '999：超级管理员 99：管理员 9：超级会员 3：普通会员 2：普通用户 1：限制用户 0:永禁用户',
    `backup` varchar(64) DEFAULT NULL COMMENT '备用字段',
    PRIMARY KEY (`id`),
    UNIQUE KEY `name_UNIQUE` (`name`),
    UNIQUE KEY `tel_UNIQUE` (`tel`)
    ) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb3;

    CREATE TABLE `site` (
    `id` int NOT NULL AUTO_INCREMENT,
    `name` varchar(32) NOT NULL COMMENT '系统名称',
    `url` varchar(64) NOT NULL COMMENT '具体链接地址',
    `rank` int NOT NULL DEFAULT '2' COMMENT '0-999,对应用户权限，为最低进入权限\n999：超级管理员 99：管理员 9：超级会员 3：普通会员 2：普通用户 1：限制用户 0:永禁用户',
    `enable` int NOT NULL DEFAULT '1' COMMENT '是否启用，0-未启用，1-启用',
    `appkey` varchar(8) NOT NULL,
    `appsecret` varchar(16) DEFAULT NULL,
    `backup` varchar(64) DEFAULT NULL COMMENT '备用字段',
    PRIMARY KEY (`id`),
    UNIQUE KEY `name_UNIQUE` (`name`),
    UNIQUE KEY `url_UNIQUE` (`url`),
    UNIQUE KEY `appkey_UNIQUE` (`appkey`)
    ) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb3;

## restful api说明 （待更新）
### 用户登录
    url:/user/login
    method:post
    body:
    {
        "name":"李四",
        "tel":"13267896799",
        "password":"123456", //必输
        "loginType":"0", //0:用户名密码登录、2:电话密码登录
    }
    返回示例：
    {"msg":"成功","condition":"","code":200,"data":{"token":"baegnu4a87a75675582c8af1140164286462cf16"}}

### 用户注册
    url:/user
    method:post
    body:
    {
        "name":"rose", 
        "password":"123456",
        "tel":"13267896709",
        //上面三必输
        "email"："",
        "site":""
    }
    返回示例：
    {"msg":"成功","condition":"","code":200,"data":{}}

### 用户信息更新
    url:/user
    method:put
    params:
        token=baegnu4a87a75675582c8af1140164286462cf16
    body:
    {
        "sign":"hhhh2233",
        "qq":"",
        "tel":"",
        "email":"",
        "site":""
    }
    返回示例:
    {"msg":"成功","condition":"","code":200,"data":{"token":"f4eqap4cf8ad5885582a88027bf16428651597cc"}}

### 管理员更新用户角色
    url:/user/{uid}
    method:put
    params:
        user/6?token=2d5birg4cf8ad58890e4b2f674164172490bde1
    body:
    {
         "role":"1"
    }
    返回示例：
    {"msg":"成功","condition":"","code":200,"data":{"token":"37dkss4cf72bf595582cf6f1eba16428653071e8"}}

### 查询所有用户
    url:/user
    method:get
    params:
        token=baegnu4a87a75675582c8af1140164286462cf16
    返回示例：
    {"msg":"成功","condition":"","code":200,"data":{"userList":[{"role":2,"name":"rose","tel":"13267896709","id":12}]}}

### 查询单个用户
    url:/user/{uid}
    method:get
    params:
        token=baegnu4a87a75675582c8af1140164286462cf16
    返回示例：
    {"msg":"成功","condition":"","code":200,"data":{"userList":[{"role":2,"name":"test","tel":"12345674567","id":7,"email":""}]}}

