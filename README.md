[![Build Status](https://travis-ci.org/Vant1032/TinySpring.svg?branch=master)](https://travis-ci.org/Vant1032/TinySpring)
[![codecov](https://codecov.io/gh/Vant1032/TinySpring/branch/master/graph/badge.svg)](https://codecov.io/gh/Vant1032/TinySpring)

# 一个模仿spring的小框架

## 实现功能
1. 注解@Bean,可以自定义名字或不写名字则自动Bean的名字是类名的首字母小写
2. @Autowired,可以用在成员变量或constructor上
3. @Scope可以决定单例或是prototype模式构建bean
4. 借助@ComponentScan指定basePackageClass扫描构建Bean

## 优点
1. 实现了自动化测试,借助Codecov查看每次测试的代码覆盖率.借助自动化测试将许多bug剔除,保证框架可以投入使用,并且利用代码覆盖率辅助看自己测试的效果.
2. 通过查看test目录下的自动化测试,可以很容易的了解TinySpring的用法
3. 通过maven构建,目前没有外部依赖,test部分依赖junit5,cobertura进行自动化测试

## 简介
这个项目是作者在学完了Spring框架之后,为了加深理解特意实现一个简单版的Spring,做这个项目让自己明白了为什么Spring要这么设计