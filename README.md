[![Build Status](https://travis-ci.org/Vant1032/TinySpring.svg?branch=master)](https://travis-ci.org/Vant1032/TinySpring)
[![codecov](https://codecov.io/gh/Vant1032/TinySpring/branch/master/graph/badge.svg)](https://codecov.io/gh/Vant1032/TinySpring)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/467a96a2d214405c93ad6e7c41ee5829)](https://www.codacy.com/app/Vant1032/TinySpring?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=Vant1032/TinySpring&amp;utm_campaign=Badge_Grade)

# 一个模仿spring的小框架
## 简介
这个项目是作者在学完了<a href="https://github.com/spring-projects/spring-framework" target="_blank">Spring框架</a>之后,为了加深理解特意实现一个简单版的Spring,做这个项目让自己明白了为什么Spring要这么设计

由于作者有过算法与数据结构竞赛的经历,有代码性能强迫症,见到代码就想优化,前期版本代码没有足够的层次划分,如果您发现任何bug,欢迎提issue

项目暂时不会实现xml配置TinySpring的功能,因为基于注解的配置更方便


## 实现功能
### tinyspring-core
1. 注解@Component(以及@Service,@Controller,@Repository),可以自定义名字或不写名字则自动Bean的名字是类名的首字母小写,多个同类型的Bean名字为从原来的名字后面添加数字从1开始
2. @Bean可以用在Config类的成员函数上
3. @Autowired,可以用在成员变量或constructor上,以及Config类的成员函数上,其require属性决定找不到Bean时的处理方式,可以装配接口以及抽象类
4. @Scope可以决定单例或是prototype模式构建bean,用在@Bean注解过的地方
5. 借助@ComponentScan指定basePackageClass扫描构建Bean
6. @Configuration标识Config类
7. AnnotationConfigApplicationContext支持多Config配置
8. @Primary标注首选Bean
9. @Qualifier以及被@Qualifier标注的注解来根据Qualifier自动配置Bean
### tinyspring-aop

### tinyspring-webmvc

## 优点
1. 实现了自动化测试,借助Codecov查看每次测试的代码覆盖率.借助自动化测试将许多bug剔除,保证框架可以投入使用,并且利用代码覆盖率辅助看自己测试的效果.
2. 通过查看test目录下的自动化测试,可以很容易的了解TinySpring的用法
3. 通过maven构建,目前没有外部依赖,test部分依赖junit5,JaCoCo进行自动化测试

## 详细用法
### tinyspring-core
1. @Qualifier 可以放置在:成员变量,构造器参数,方法参数,类(声明Bean时),注解(自定义Qualifier注解)上

### tinyspring-aop

### tinyspring-webmvc
