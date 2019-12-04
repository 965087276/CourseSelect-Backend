# 讨论日志
## 2019.11.6
### 数据库的设计
* 课程时间冲突，表如何设计?
* 数据库有哪些表？
* 教室冲突如何设计？
## 2019.11.10
### 数据库用户表的修改
原来的表：

| 学生 | 学号 | 姓名 | 邮箱 | 手机号 | 密码 | 盐  | 专业 | 单位 | 学院 |
| ---- | ---- | ---- | ---- | ------ | --- | --- | --- | --- | ---- |

| 教师 | 教工号 | 姓名 | 邮箱 | 手机号 | 密码 | 盐  | 单位 | 学院 |
| --- | ------ | --- | --- | ------ | --- | --- | --- | --- |

| 管理员 | 工号 | 姓名 | 邮箱 | 手机号 | 密码 |  盐  | 
| ------ | --- | --- | --- | ------ | --- | --- |

提取出共性

| 用户 | 工号 | 姓名 | 邮箱 | 手机号 | 密码 | 盐  | 学院 | 单位 | 专业 | 类型 |
| --- | --- | --- | --- | ------ | ---- | --- | ---- | ---- | ---- | ---- |

注： 
1. 类型字段区分管理员、学生、老师3种用户。
2. 管理员学院、单位、设置为默认值。
## 密码保存
加盐保存

[加盐密码保存的最通用方法是？ - CoderZh的回答 - 知乎](https://www.zhihu.com/question/20299384)
## 权限验证

## 2019.11.16
### 前后端如何调试
### Autowired警告
在cn.ict.course.controller.UserController.java中以下代码会报警告：
```java
@Autowired // Field injection is not recommended.
private UserService userService;
```
参考[【Spring】浅谈spring为什么推荐使用构造器注入](https://www.cnblogs.com/joemsu/p/7688307.html)
spring框架常见的3种注入方式
#### field注入
```java
@Controller
public class FooController {
  @Autowired
  //@Inject
  private FooService fooService;
  
  //简单的使用例子，下同
  public List<Foo> listFoo() {
      return fooService.list();
  }
}
```
优点：
1. 注入方式简单，字段+`Autowired`。
2. 代码简洁。

缺点：
1. 对于IOC容器以外的环境，除了使用反射来提供它需要的依赖之外，无法复用该实现类。
2. 可能会导致循环依赖。
#### constructor注入（Spring官方推荐）
```java
@Controller
public class FooController {
  
  private final FooService fooService;
  
  @Autowired
  public FooController(FooService fooService) {
      this.fooService = fooService;
  }
  
  //使用方式上同，略
}
```
优点：
1. 依赖不可变：final关键字。
2. 依赖不为空（省去了我们对其检查）：当要实例化FooController的时候，由于自己实现了有参数的构造函数，所以不会调用默认构造函数，那么就需要Spring容器传入所需要的参数，所以就两种情况：1、有该类型的参数->传入，OK 。2：无该类型的参数->报错。所以保证不会为空，Spring总不至于传一个null进去吧 :-( 
3. 完全初始化的状态：这个可以跟上面的依赖不为空结合起来，向构造器传参之前，要确保注入的内容不为空，那么肯定要调用依赖组件的构造方法完成实例化。而在Java类加载实例化的过程中，构造方法是最后一步（之前如果有父类先初始化父类，然后自己的成员变量，最后才是构造方法，这里不详细展开。）。所以返回来的都是初始化之后的状态。（REST思想）

缺点：
* 大量依赖注入，代码臃肿。但此时应考虑是否该类有太多责任，违反了类的**单一性职责原则**。
#### setter注入
```java
@Controller
public class FooController {
  
  private FooService fooService;
  
  //使用方式上同，略
  @Autowired
  public void setFooService(FooService fooService) {
      this.fooService = fooService;
  }
}
```
可实现类在之后重新配置或者重新注入。
#### 总结
##### constructor注入的好处
1. 保证依赖不可变（final关键字）
2. 保证依赖不为空（省去了我们对其检查）
3. 保证返回客户端（调用）的代码的时候是完全初始化的状态
4. 避免了循环依赖
5. 提升了代码的可复用性
##### 依赖注入注意的地方
1. 类设计注意检查单一性原则，避免类太过臃肿，承担太多责任。
2. 如果一个依赖有多种实现方式，可以使用`@Qualifier`，在构造方法里选择对应的名字注入，也可以使用field或者setter的方式来手动配置要注入的实现。

### spring boot的好处
了解token

jwt（json web token）

参考
[基于Token的WEB后台认证机制](https://www.cnblogs.com/xiekeli/p/5607107.html)

## 2019.11.20
IntelliJ中使用Git导入repository出错的问题。

描述：包含前后端的repository直接使用Git无法导入，maven无法构建相关依赖。
解决办法：Git导入时不选择默认导入，改换maven，并选择后端目录。
## 2019.11.21
输入信息需要验证，可以参考Timo

package com.linln.admin.system.validator;
## 2019.11.23
springboot各个分层不明确

service层与controller、dao层关系？

### （坑）使用Swagger添加用户调试错误
描述：
使用Swagger向以下API发送POST请求，
```txt
http://localhost:8080/xk/api/add
```
无法添加用户，并返回Response body如下
```$xslt
{
  "timestamp": "2019-11-22T16:53:19.939+0000",
  "status": 405,
  "error": "Method Not Allowed",
  "message": "Request method 'GET' not supported",
  "path": "/xk/api/login"
}
```
观察发现以下错误，
![](_v_images/20191123005533505_2897.png =300x)

思路：
1. 可能是Controller代码出现错误，但是检查代码后发现url映射正确，排除。
2. 可能是Swagger出现错误，但是google没有搜到与问题相符的情况。
3. 使用Postman调试，发现返回结果相同。
4. 更改用户权限配置代码，
```java
Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
filterChainDefinitionMap.put("/xk/api/login", "anon");
// 添加下面这行代码，允许无需认证即可访问/xk/api/add
filterChainDefinitionMap.put("/xk/api/add", "anon"); 
filterChainDefinitionMap.put("/xk/api/logout", "anon");
// 拦截根目录下的所有路径，需要放行的路径必须在之前添加
filterChainDefinitionMap.put("/xk/api/**", "userAuth");
```
更改后重新运行，错误解决。
#### 总结
一定要提前规定好api，并在权限代码中加入，权限问题时刻牢记。
### （坑）用户登录验证失败
描述，用户登陆时，密码验证总是失败。

原因：

```java
@Bean
public UserRealm getRealm() {
    UserRealm userRealm = new UserRealm();
    // 需要把自定义的CredentialsMatcher加入到userRealm中
    userRealm.setCredentialsMatcher(hashedCredentialsMatcher());
    return userRealm;
}
```
需要把自定义的凭证匹配器加入到userRealm中。
## 2019.11.23
### 基本类型与对象
什么时候用基本数据类型（int），什么时候用Integer。
### （坑）跨域
### 跨域是什么
![](_v_images/20191124190100545_6271.png =500x)

**当协议、子域名、主域名、端口号中任意一个不相同时，都算作不同域**。不同域之间相互请求资源，就算作“跨域”。

![](_v_images/20191124190157959_6435.png =600x)

[九种跨域方式实现原理（完整版）](https://juejin.im/post/5c23993de51d457b8c1f4ee1)
### 跨域之使用token认证信息。

前后端分离情况下，REST是无状态的，shiro是根据sessionID来识别是不是同一个request，但如果前后分离的话，就会出现跨域的问题，session很可能就会发生变化。

解决方法：这样就需要用一个标记来表明是同一个请求。

参考博客：[前后分离，使用自定义token作为shiro认证标识，实现springboot整合shiro](https://my.oschina.net/sprouting/blog/3059282)
### 跨域之过滤OPTIONS请求
前后端分离项目中，由于跨域，会导致复杂请求，即会发送preflighted request，这样会导致在GET／POST等请求之前会先发一个OPTIONS请求，但OPTIONS请求并不带shiro的'authToken'字段（shiro的SessionId），即OPTIONS请求不能通过shiro验证，会返回未认证的信息。

解决方法：过滤可以访问的请求类型。
```java
// 直接过滤可以访问的请求类型
private static final String REQUET_TYPE = "OPTIONS";
@Override
public boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
    if (((HttpServletRequest) request).getMethod().toUpperCase().equals(REQUET_TYPE)) {
        return true;
    }
    return super.isAccessAllowed(request, response, mappedValue);
}
```
## 2019.11.27
### excel上传
* 学院、课程属性、教师username、教室在数据库表中存不存在（可以后期添加）
* 课程唯一性，一门课有多个时间、教室
* 教室与课程时间（开课周、周几、节次），教师与课程时间（开课周、周几、节次）
## 2019.11.28
### Controller与Service分层
原来的层次结构：
```
TeacherController -> TeacherService
StudentController -> StudentService
AdminController -> AdminService
```
缺点：教师和管理员都可以增加或者修改课程，所以Service层会有重复代码

新的层次结构：
```
TeacherController & StudentController & AdminController -> CourseController
```
操作课程直接调用CourseService的服务即可。
### 代码更简洁可读
```java
// 修改前
for (College college : colleges) {
            collegeName.add(college.getName());
}

// 函数式编程
List<String> collegeNames = colleges
                .stream()
                .map(College::getName)
                .collect(Collectors.toList());
```
## 2019.11.30
### jpa查询list
orm中自定义sql语句查询，失败，
```java
@Query(value = "SELECT * FROM course_schedule WHERE classroom IN :classrooms")
List<CourseSchedule> findByClassrooms(@Param("classrooms") List<String> classrooms);
```
事实上jpa支持list查询，修改后
```java
List<CourseSchedule> findByClassroom(@Param(value = "classrooms") List<String> classrooms);
```
参考：[JPA槽点之multiple in list查询引发的问题](https://blog.csdn.net/qq_17776287/article/details/78926291)
### 添加课程冲突
教师添加课程
#### 可能存在的情况
* 教师 - 课程时间，存在冲突
* 教室 - 课程时间，存在冲突
#### 处理
1. 课程添加时，检索该课程教师教授的所有课程CoursePrevious，比较新添加的课程CourseCurrent是否与CoursePrevious存在冲突。
2. 课程添加时，检索传入的教室已有的课程CoursePrevious，比较新添加的课程CourseCurrent是否与CoursePrevious存在冲突。
#### 注意的点
1. 传入的课程时间表为一个List，如CourseCurrent，需要将CoursePrevious与CourseCurrent合并后比较是否存在冲突。
2. 由于传入课程表为一个List，所有可能有多个教室，需要使用教室List进行查询。
3. 判断冲突的代码过多，需要抽象出来。
## 2019.12.3
### git push冲突
两个人同时修改后端代码，导致版本冲突。

解决方法：我这边先git pull下来，然后解决代码提交冲突，重新push

### 预选课程添加时出现抛出异常
1. 检查添加的预选课程信息，发现之前已经添加过一次，可以推断出是重复添加课程导致存储异常。
2. 修改代码，service层定义判断课程冲突的接口。并修改添加预选课程的代码。
3. 重新运行，异常不再抛出，返回重复添加课程信息。
### 添加预选课程使用PostMan调试参数错误
最开始参数放在body中，后来发现springboot处理body参数需要在传入实例之前加上@RequestBody注解。

如果我们只是简单地用String类型，那么默认是放在Query Params中，并绑定到request参数中。

修改传入的对象为JSONObject，并加上@RequestBody注解，然后提取相关参数。

参考[What is difference between @RequestBody and @RequestParam?](https://stackoverflow.com/questions/28039709/what-is-difference-between-requestbody-and-requestparam)
### 使用filter过滤查询课程信息（包括课程时间）
## 2019.12.4
### 单元测试用户登录shiro抛出异常
描述：使用Swagger测试正常，但是在SpringBoot单元测试抛出异常：
```
org.springframework.web.util.NestedServletException: Request processing failed; nested exception is org.apache.shiro.UnavailableSecurityManagerException: No SecurityManager accessible to the calling code, either bound to the org.apache.shiro.util.ThreadContext or as a vm static singleton.  This is an invalid application configuration.
...
```
原因：mock模拟请求不会加载web.xml，也就不会配置Shiro，但是SpringBoot需要配置shiroFilter（包含securityManager），导致mock请求的时候没有找到SecurityManager，解决办法是在securityManager()方法中设置securityManager：
```java
@Bean
public SecurityManager securityManager() {
    DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
    //自定义的shiro session 缓存管理器
    securityManager.setSessionManager(sessionManager());
    securityManager.setRealm(getRealm());
    SecurityUtils.setSecurityManager(securityManager);
    return securityManager;
}
```
