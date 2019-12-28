# < JAVA - 大作业（2）仿qq即时通讯软件 >

## 背景

+ JAVA上机大作业：设计一个仿qq即时通讯软件

> 任务简要叙述：设计一款仿QQ的个人用户即时通讯软件，能够实现注册，登陆，与好友聊天等功能。要求使用GUI界面设计，网络通信，数据库连接，泛型容器等技术。

+ 注意：
	+ 参考该代码时注意修改Config.java中的IP地址为自己(服务器工程)的IP地址
	
	+ 参考该代码时注意修改DBManage.java中的数据库连接内容为自己的数据库连接
	
	+ 客户端运行Login.java启动客户端；服务器运行Start.java启动服务器
	
	+ 客户端/服务端都有相应需要导入的额外包，可以在如下网站搜索需要的jar包：[**MvnJar**](https://www.mvnjar.com/ "MvnJar")
	
	+ 阿里大于发送短信验证码需要自己去注册账户填写自己的AK；163邮箱同理需要对应修改


## 需求分析

+ 客户端：
	1. 注册账户：使用手机或者email注册，要求使用验证码验证手机号或者email再注册。
	2. 找回密码：使用手机/email找回密码，要求使用验证码验证是否本人操作再找回
	3. 登陆账户：使用系统自动生成的qq号与自己设置的密码进行登陆，能够记住密码与自动登陆，设置登陆状态，保留多账号登陆，二维码登陆的接口。
	4. 主界面：好友列表/群组列表/常用联系人，可以被抢占下线
	5. 聊天框：与好友聊天，字体设置，抖动好友，发送文件
	6. 个人资料：查看修改个人资料
	7. 搜索框：搜索QQ号，添加好友
	
+ 服务器：
	+ 多线程，能承载大容量用户体积。
通信消息及时更新，账号只许唯一登陆。

## 概要设计

### 架构/业务流程图

![](https://img2018.cnblogs.com/blog/1816059/201912/1816059-20191218175627073-1126039787.jpg)

![](https://img2018.cnblogs.com/blog/1816059/201912/1816059-20191218175648320-88682434.jpg)

![](https://img2018.cnblogs.com/blog/1816059/201912/1816059-20191218175653813-1438092706.jpg)

### 数据库 - 数据表

```
用户表：
Uid			varchar(100)	key		用户编号
Qqnumber	varchar(100)	唯一索引		qq号
Password 	varchar(100)
Netname 	varchar(100)
Info 		varchar(200)
State		varchar(200)		//账户是否锁定
Createtime	datetime
Img			varchar(100)
onlinestate	varchar(100)		//在线状态 – 离线/隐身/在线….

个人资料表：
Uid				varchar(100)
Network			varchar(100)
Info				varchar(200)
Phonenumber		varchar(100)
Email				varchar(100)
Yy					int
Mm					int
Dd					int
Back(个人说明)	varchar(500)
Gend				varchar(10)
Realname			varchar(100)
Profession		varchar(100)
Hometown			varchar(100)
Relation			varchar(100)
Bloodtype		varchar(10)
Img				varchar(100)
Qqnumber			varchar(100)

好友表
Logid(登记编号)	varchar(100)	key
Uid		普通索引
Friendid
Createtime
```


## 文件目录

![客户端工程文件目录](https://img2018.cnblogs.com/blog/1816059/201912/1816059-20191218175714240-494029542.png)

![服务器端工程文件目录](https://img2018.cnblogs.com/blog/1816059/201912/1816059-20191218175728569-253681249.png)


## 前端界面一览

![](https://img2018.cnblogs.com/blog/1816059/201912/1816059-20191218180025522-1988864029.png)

## My Blog：https://www.cnblogs.com/ymjun/
