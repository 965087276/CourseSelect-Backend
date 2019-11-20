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