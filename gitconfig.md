#### ----------20121203------------
## git基础配置
#### 1.配置用户名
##### git config --global user.name "你的用户名"
#### 2.配置邮箱
##### git config --global user.email "你的邮箱"
#### 3.编码配置
##### 避免git gui中的中文乱码
##### git config --global core.quotepath utf-8
##### 避免git status显示的中文文件名乱码
##### git config --global core.quotepath off
#### 4.其他
##### git config --global core.ignorecase false
## git ssh key pair 配置
#### 1.在git命令行窗口输入
##### ssh-keygen -t rsa -C "你的邮箱"
#### 2.然后一路回车，不要输入任何密码之类，生成ssh key pair
#### 3.在用户目录下生成.ssh文件夹，找到公钥和私钥 
##### id_rsa  id_rsa.pub 
#### 4.将公钥的内容复制 
#### 5.进入github网站，将公钥添加进去
#### settings--SSH and GPG keys--New SSH key--ADD SSH key
### 创建一个远程仓库
#### New repository--Create respository
### 初始化一个本地仓库
#### git init
### 将工作区文件添加到暂存区
#### git add hello.txt
### 将暂存区文件添加到本地仓库
#### git commit -m "内容描述"
### 关联本地仓库和远程仓库
#### git remote add origin 远程仓库地址
### 第一次推送：本地仓库提交到远程仓库
#### git push -u -f origin master
### 以后：本地仓库提交到远程仓库
#### git push origin master
###  -------------------------------------------
### 项目中文件上传到远程仓库
#### 新建一个远程仓库
#### 在/d/business目录下输入命令 
##### 克隆：git clone 远程仓库地址
#### 新建idea项目
#### File--settings--登录GitHub
#### 配置.gitignore文件
#### Terminal 输入命令
#### git init
#### git add .
#### git commit -m "first commit"
#### git remote add origin 远程仓库地址
#### 解除关联：(git remote remove origin)
#### git push -u origin master
### 后续提交：
#### git add 要提交到远程的文件
#### git commit -m "..."
#### git push origin 要提交的远程分支名
### --------------------------------------------
### 企业项目开发模式 
#### 项目采用： 分支开发，主干发布  
#### 创建分支：git checkout -b v1.0 origin/master
#### 将分支推送到远程 git push origin HEAD -u 
#### 检查远程，发现多了v1.0分支 
### 项目提交到github 
#### .gitignore文件 :告诉Git哪些文件不需要添加到版本管理中 
#### 忽略规则：
#### 此为注释 – 将被 Git 忽略
##### *.a       # 忽略所有 .a 结尾的文件
##### !lib.a    # 但 lib.a 除外 /TODO     # 仅仅忽略项目根目录下的 TODO 文件，不包括 subdir/TODO build/    # 忽略 build/ 目录下的所有文件
##### doc/*.txt # 会忽略 doc/notes.txt 但不包括 doc/server/arch.txt
#### git add .  //提交所有
#### --------------------------------------------
### git验证
#### 执行git --version，出现版本信息，安装成功。 
#### --------------------------------------------
### git常用命令 
#### git init 创建本地仓库 
#### git add  添加到暂存区 
#### git commit -m "描述" 提交到本地仓库 
#### git remote add origin 远程仓库地址   关联本地仓库和远程仓库
#### git remote remove origin   解除关联
#### 本地仓库提交到远程仓库: git push -u origin master 
#### git status 检查工作区文件状态 
#### git log  查看提交committed 
#### git reset --hard committid  版本回退 
#### git branch 查看分支 
#### git checkout -b dev 创建并切换到dev分支 
#### 切换分支：git checkout 分支名 
#### 分支合并: git merge branchname
#### 将分支推送到远程 git push origin HEAD -u 
#### 更新远程代码到本地仓库: git fetch
#### 拉取: git pull
#### -----------20181204--------------
#### 创建一个分支并推送到远程仓库
 ```
 git checkout -b dev
 git push origin head -u
 ```
#### 将idea项目提交到GitHub
 ```
 git add 要提交的文件名
 git commit -m "要提交的文件"
 git push origin 要提交到的分支名
 ```
#### 远程合并dev分支
 ```
 git checkout dev
 git pull origin dev
 git checkout master
 git merge dev
 git push origin master
 ```
 ## 数据库设计
 ### 创建数据库
 ```
 create database learnshopping;
 use learnshopping;
 ```
 ### 用户表
 ```
 create table neuedu_user(
 `id` int(11) not null auto_increment comment '用户id',
 `username` varchar(50) not null comment '用户名',
 `password` varchar(50) not null comment '密码',
 `email` varchar(50) not null comment '邮箱',
 `phone` varchar(11) not null comment '联系方式',
 `question` varchar(100) not null comment '密保问题',
 `answer` varchar(100) not null comment '答案',
 `role` int(4) not null default 0 comment '用户角色',
 `create_time` datetime comment '创建时间',
 `update_time` datetime comment '修改时间',
  PRIMARY KEY(`id`),
  UNIQUE KEY `user_name_index`(`username`) USING BTREE
 )ENGINE=InnoDB DEFAULT CHARSET=UTF8;
 ```
 ### 类别表
 ```
 create table neuedu_category(
 `id`        int(11)     not null auto_increment comment '类别id',
 `parent_id` int(11)     not null default 0 comment '父类id',
 `name`      varchar(50) not null comment '父类名称',
 `status`    int(4) default 1 comment '类别状态 1:正常 0:废弃',
 `create_time` datetime comment '创建时间',
 `update_time` datetime comment '修改时间',
  PRIMARY KEY(`id`)
  )ENGINE=InnoDB DEFAULT CHARSET=UTF8;
 
                    id      parent_id
 电子产品   1        1          0
 家店       2        2          1
 手机       2        3          1
 电脑       2        4          1
 相机       2        5          1
 华为手机   3        6          3
 小米手机   3        7          3
 p系列      4        8          6
 mate系列   4        9          6
 
 查看电子产品的商品----> 递归
 ```
 ### 商品表
```
 create table neuedu_product(
 `id`           int(11)     not null auto_increment comment '商品id',
 `category_id`  int(11)     not null comment '商品所属类别id，值引用类别表的id',
 `name`         varchar(100) not null comment '商品名称',
 `detail`       text comment '商品详情',
 `subtitle`     varchar(200) comment '商品副标题',
 `main_image`   varchar(200) comment '商品主图',
 `sub_images`   varchar(200) comment '商品子图',
 `price`        decimal(20,2) not null comment '商品价格，总共20位，小数2位，正数18位',
 `stock`        int(11) comment '商品库存',
 `status`       int(6)  default 1 comment '商品状态 1:在售 2:下架 3:删除',
 `create_time` datetime comment '创建时间',
 `update_time` datetime comment '修改时间',
  PRIMARY KEY(`id`)
 )ENGINE=InnoDB DEFAULT CHARSET=UTF8;
 ```
 ### 购物车表
 ```
 create table neuedu_cart(
 `id`           int(11)     not null auto_increment comment '购物车id',
 `user_id`      int(11)     not null comment '用户id',
 `product_id`   int(11)     not null comment '商品id',
 `quantity`     int(11)     not null comment '购买数量',
 `checked`      int(4)      default 1 comment '1:选中 2:未选中',
 `create_time` datetime comment '创建时间',
 `update_time` datetime comment '修改时间',
  PRIMARY KEY(`id`),
  key `user_id_index`(`user_id`) USING BTREE
 )ENGINE=InnoDB DEFAULT CHARSET=UTF8;
 ```
 ### 订单表
 ```
  create table neuedu_order(
 `id`           int(11)     not null auto_increment comment '订单id,主键',
 `order_no`     bigint(20)  not null comment '订单编号',
 `user_id`      int(11) not null comment '用户id',
 `payment`      decimal(20,2) not null comment '付款总金额,单位(元),保留两位小数',
 `payment_type` int(4) not null default 1 comment '付款方式 1:线上支付',
 `status`       int(10) not null comment '订单状态 0:已取消 10:未付款 20:已付款 30:已发货 40:已完成 50:已关闭',
 `shipping_id`  int(11) not null comment '收货地址id',
 `postage`      int(10) not null default 0 comment '运费',
 `payment_time` datetime default null comment '已付款时间',
 `send_time`    datetime default null comment '已发货时间',
 `close_time`   datetime default null comment '已关闭时间',
 `finish_time`  datetime default null comment '已结束时间',
 `create_time` datetime comment '已创建时间',
  `update_time` datetime comment '更新时间',
   PRIMARY KEY(`id`),
   key `order_no_index`(`order_no`) USING BTREE
 )ENGINE=InnoDB DEFAULT CHARSET=UTF8;
 ```
 ### 订单明细表
 ```
create table neuedu_order_item(
 `id`           int(11)     not null auto_increment comment '订单明细id,主键',
  `order_no`     bigint(20)  not null comment '订单编号',
  `user_id`      int(11) not null comment '用户id',
 `product_id`    int(11) not null comment '商品id',
 `product_name`  varchar(100) not null comment '商品名称',
 `product_image` varchar(200) comment '商品主图',
 `current_unit_price` decimal(20,2) not null comment '下单时商品的价格，单位(元)，保留两位小数',
 `quantity` int(10) not null comment '商品的购买数量',
 `total_price` decimal(20,2) not null comment '商品的总价格，单位(元)，保留两位小数',
 `create_time` datetime comment '已创建时间',
 `update_time` datetime comment '更新时间',
  PRIMARY KEY(`id`),
  key `order_no_index`(`order_no`) USING BTREE,
  key `order_no_user_id_index`(`order_no`,`user_id`) USING BTREE
 )ENGINE=InnoDB DEFAULT CHARSET=UTF8;
 ```
 
 ### 支付表
 ```
 create table neuedu_payinfo(
  `id`           int(11)     not null auto_increment comment '主键',
  `order_no`     bigint(20)  not null comment '订单编号',
  `user_id`      int(11) not null comment '用户id',
  `pay_platform` int(4)  not null default 1 comment '1:支付宝 2:微信',
  `platform_status` varchar(50) comment '支付状态',
  `platform_number` varchar(100) comment '流水号',
  `create_time` datetime comment '已创建时间',
  `update_time` datetime comment '更新时间',
   PRIMARY KEY(`id`)
  )ENGINE=InnoDB DEFAULT CHARSET=UTF8;
  ```
 ### 地址表
 ```
create table neuedu_shipping(
 `id` int(11) not null auto_increment,
 `user_id` int(11) not null,
 `receiver_name` varchar(20) default null comment '收货姓名',
 `receiver_phone` varchar(20) default null comment '收货固定电话',
 `receiver_mobile` varchar(20) default null comment '收货移动电话',
 `receiver_province` varchar(20) default null comment '省份',
 `receiver_city` varchar(20) default null comment '城市',
 `receiver_district` varchar(20) default null comment '区/县',
 `receiver_address` varchar(200) default null comment '详细地址',
 `receiver_zip` varchar(6) default null comment '邮编',
 `create_time` datetime not null comment '创建时间',
 `update_time` datetime not null comment '最后一次更新时间',
 PRIMARY KEY(`id`)
)ENGINE=InnoDB DEFAULT CHARSET=UTF8;
```
### 项目架构--四层架构
```
 视图层
 控制层controller
 业务逻辑层service
    接口和实现类
 Dao层
```

### Mybatis-generator插件
#### 配置db.properties
#### 配置generatorConfig.xml --自动生成dao层 实体类 和映射文件
```
<properties resource="db.properties"></properties>
<classPathEntry location="C:\Users\liumengyu\.m2\repository\mysql\mysql-connector-java\5.1.47\mysql-connector-java-5.1.47.jar"/>
```
##### 配置mysql的驱动包jar
##### 实体类：
```
		targetPackage="" --放在哪个包下边
		targetProject="" --放在哪个项目下
```
##### 配置sql文件
##### 生成Dao接口
##### 配置数据表：
```
  	    tableName="" --数据库中表的名称
		domainObjectName="" --生成的实体类的名字 首字母大写	
```
##### 运行：
###### MavenProjects-->Plugins--->mybatis-generator-->mybatis-generator:generate
### 搭建ssm框架
#### 引入依赖包
#### 配置文件
##### -spring.xml
##### 开启注解 --因为使用的是基于注解的IOC 
##### 开启基于注解的实物配置
##### 带有连接池的数据源
##### 分页插件
##### 配置mybatis Dao接口的代理实现类
##### -springmvc.xml
##### 开启注解--管理的是controller
##### 配置控制前可以返回json格式的数据
###### 前后端分离，服务器往前端返回是json数据
##### 视图解析器
##### 文件上传视图解析器
##### -mybatis-config.xml
##### -logback.xml
##### -web.xml
##### @RestController 向前端返回的数据是json格式的
##### 配置tomcat
#### --------------------------------------------

### 登录接口： 
```
1.参数非空校验
2.检查username是否存在
3.根据用户名和密码查询
4.处理结果并返回
   前端返回数据时 要把密码置空
   userInfo.setPassword("");
```
### 注册接口：
```
多参数用数据绑定接收(对象形式)
Impl实现类实现Service中的注册方法，实现类处理注册的业务逻辑，控制层调用业务逻辑层把数据返回给视图层

1.参数非空的校验
2.判断用户名是否存在
3.判断邮箱是否存在
在dao层中书写校验邮箱的方法(与数据库做交互)，映射类中写sql语句，实现类处理具体的业务逻辑
4.注册
5.返回结果
```
#### 为了规范，定义响应状态码(enum)
#### 对数据库中的密码通过MD5加密
#### 调用getMD5Code方法对密码加密
#### MD5加密特点：不可逆的加密算法(通过MD5加密变成密文之后，不能通过密文获取到原密码)
#### 注册密码变成密文的之后，登录密码也应进行加密，否则登录时输入明文密码会显示密码输入错误

#### ---------
#### 用户只有登录之后才能对购物车进行操作,所以要把用户登录的信息保存到session中
#### 通过HttpSession获取session当中的用户信息
#### 往session中保存数据：session.setAttribute(Const.CURRENTUSER,serverResponse.getData())


### 检查用户名或邮箱是否有效的接口
```
参数：String str、String type，str对应用户名或邮箱的值，type对应的是username或email
什么时候使用？注册接口在输入用户名或邮箱之后会反馈用户名或邮箱是否已存在，输入完成之后通过ajax异步加载调用接口返回数据，防止恶意调用接口
在service中定义检查用户名和密码的方法，在实现类中处理业务逻辑
1.参数的非空校验
通过调用StringUtils.isBlank()判断
2.判断用户名或邮箱是否存在
3.返回结果
```
### 重构登录和注册接口
#### 调用check_valid()


### 获取当前登录的用户的信息
#### 登录之后用户信息保存到了session当中，直接操作session获取信息


