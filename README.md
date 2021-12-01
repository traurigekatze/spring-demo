### spring-demo

##

#### 手写模拟 Spring v1.0

````text
实现功能
1. 支持 @Autowired、@Component、@ComponentScan 注解
2. 支持 @Lazy、@Scope 注解
3. 支持 @PostConstruct 注解
4. 支持 bean 之间互相依赖
5. 支持推断构造方法
6. 提供 getBean(String name)、getBean(Object.class) method 支持

实现思路
1. 解析 @ComponentScan 得到扫描路径
2. 根据扫描路径得到扫描到的 class
3. init bean definition map
   a. set bean class、scope、lazy
   b. generate bean name
4. do create non-lazy singleton bean
   a. create bean instance 
   b. autowired field
   c. do exec postConstruct
5. getBean

````
