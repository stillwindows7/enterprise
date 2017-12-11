# 使用 Eureka 做服务发现

## Zookeeper 做注册中心的缺陷

Peter Kelley（个性化教育初创公司 knewton 的一名软件工程师）发表了一篇文章说明为什么Zookeeper 用于服务发现是一个错误的做法，他主要提出了三个缺点：

1. ZooKeeper 无法很好的处理网络分区问题，当网络分区中的客户端节点无法到达 Quorum 时，会与 ZooKeeper 失去联系，从而也就无法使用其服务发现机制。

2. 服务发现系统应该是一个 AP 系统，设计上针对可用性；而 ZooKeeper 是一个 CP 系统。

3. ZooKeeper 的设置和维护非常困难，实际操作的时候也容易出错，比如在客户端重建 Watcher，处理 Session 和异常的时候。

当然， Peter Kelley 提出的这几个问题并不是不能克服的，并不能说明基于 ZooKeeper 就不能做好一个服务发现系统，但是我们可能有更简洁的方案来实现。

# Eureka 介绍

## 什么是 Eureka

官方的介绍在这里[Eureka Wiki](https://github.com/Netflix/eureka/wiki/Eureka-at-a-glance)。Eureka 是 Netflix 开源的一个 Restful 服务，主要用于服务的注册发现。Eureka 由两个组件组成： Eureka 服务器和 Eureka 客户端。 Eureka 服务器用作服务注册服务器。 Eureka 客户端是一个 Java 客户端，用来简化与服务器的交互、作为轮询负载均衡器，并提供服务的故障切换。 Netflix 在其生产环境中使用的是另外的客户端，它提供基于流量、资源利用率以及出错状态的加权负载均衡。

* 开源： 大家可以对实现一探究竟，甚至修改源代码。
* 可靠： 经过 Netflix 多年的生产环境考验，使用应该比较靠谱处心。
* 功能齐全： 不但提供了完整的注册发现服务，还有 Ribbon 等可以配合使用服务。
* 基于 Java： 对于 Java 程序员来说，使用起来，心里比较有底。
* Spring Cloud 可以使用 Spring Cloud，与 Eureka 进行了很好的集成，使用起来非常方便。

## Eureka 架构:

Netflix 主要是在 AWS 中使用 Eureka 的，虽然同时也支持本地环境，但是了解 AWS 的一些基础概念对于理解 Eureka 的设计非常有帮助。

### 区域与可用区

首先，我们先熟悉两个概念：

* 区域（Region）: AWS 云服务在全球不同的地方都有数据中心，比如北美、南美、欧洲和亚洲等。与此对应，根据地理位置我们把某个地区的基础设施服务集合称为一个区域。通过 AWS 的区域，一方面可以使得 AWS 云服务在地理位置上更加靠近我们的用户，另一方面使得用户可以选择不同的区域存储他们的数据以满足法规遵循方面的要求。美东（北佛吉尼亚）、美西（俄勒冈）、美西（北加利佛尼亚）、欧洲（爱尔兰）、亚太（新加坡）、亚太（东京）等。每个区域都有自己对应的编码，如：

| 区域                 			| 编码           			|
|----------------------	|----------------	|
| 亚太（东京）         		| ap-northeast-1 	|
| 亚太（新加坡）      		| ap-southeast-1 	|
| 亚太（悉尼）        		 	| ap-southeast-2 	|
| 欧洲（爱尔兰）       		| eu-west-1      	|
| 南美（圣保罗）       		| sa-east-1      	|
| 美东（北佛杰尼亚）   		| us-east-1      	|
| 美西（北加利佛尼亚） 		| us-west-1      	|
| 美西（俄勒冈）       		| us-west-2      	|

* 可用区（Zone）： AWS 的每个区域一般由多个可用区（AZ）组成，而一个可用区一般是由多个数据中心组成。AWS引入可用区设计主要是为了提升用户应用程序的高可用性。因为可用区与可用区之间在设计上是相互独立的，也就是说它们会有独立的供电、独立的网络等，这样假如一个可用区出现问题时也不会影响另外的可用区。在一个区域内，可用区与可用区之间是通过高速网络连接，从而保证有很低的延时。

每次当用户需要使用 EC2 相关资源的时候，他需要首先选择目标区域，如美东（北佛杰尼亚）us-east-1。然后在创建 EC2 产例的时候，用户可以选择实例所在的可用区，比如可以是 us-east-1a 或 us-east-1b 等。可用区的编码就是区域后面添加不同的英文字母。

### Eureka 架构说明

下图是 Eureka Wiki 中提供的架构图：

![Eureka 架构图](https://github.com/Netflix/eureka/raw/master/images/eureka_architecture.png)

从上面的架构图可以看出，主要有三种角色：

* Eureka Server
*** 通过 Register， Get，Renew 等 接口提供注册和发现

* Application Service （Service Provider）：
*** 服务提供方
*** 把自身服务实例注册到 Eureka Server

* Application Client （Service Consumer）：
*** 服务调用方
*** 通过 Eureka Server 获取服务实例，并调用 Application Service

他们主要进行的活动如下：

1. 每个 Region 有一个 Eureka Cluster， Region 中的每个 Zone 都至少有一个 Eureka Server。
2. Service 作为一个 Eureka Client，通过 register 注册到 Eureka Server，并且通过发送心跳的方式更新租约（renew leases）。如果 Eureka Client 到期没有更新租约，那么过一段时间后，Eureka Server 就会移除该 Service 实例。
3. 当一个 Eureka Server 的数据改变以后，会把自己的数据同步到其他 Eureka Server。
4. Application Client 也作为一个 Eureka Client 通过 Get 接口从 Eureka Server 中获取 Service 实例信息，然后直接调用 Service 实例。
5. Application Client 调用 Service 实例时，可以跨可用区调用。

### Eureka Demo

实际工作中，我们很少会直接使用 Eureka，因为 Spring Cloud 已经把 Eureka 与 Spring Boot 进行了集成，使用起来更为简单，所以我们使用 Spring Cloud 作为示例。

这里是官方提供的一个示例：[spring-cloud-eureka-example](https://github.com/ExampleDriven/spring-cloud-eureka-example)


### 启动 Eureka Server

Eureka Server 非常简单，只需要三个步骤： 

1. 在 pom.xml 中添加依赖：

```
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-eureka-server</artifactId>
</dependency>
```

2. 实现 Application，添加 annotation。 @EnableEurekaServer、@EnableDiscoveryClient 执行 main 方法启动 Eureka Server。

```
@SpringBootApplication
@EnableEurekaServer
@EnableDiscoveryClient
public class Application {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(Application.class, args);
    }
}
```

3. 运行 Application 即可启动 Server，启动 Server 后打开 [http://localhost:8761/](http://localhost:8761)，可以看到信息页面。

### 注册服务

把一个服务注册在 server 中需要以下几个步骤：

1. 添加 eureka 依赖

```
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-eureka</artifactId>
</dependency>
```

2. 添加 @EnableEurekaClient 注解

```
@EnableEurekaClient
public class Application
```

3. 在 application.yml 或者 application.properties 中添加配置

```
eureka:
  instance:
    leaseRenewalIntervalInSeconds: 1
    leaseExpirationDurationInSeconds: 2
  client:
    serviceUrl:
      defaultZone: http://127.0.0.1:8761/eureka/
    healthcheck:
      enabled: true
    lease:
      duration: 5
 
spring:
  application:
    name: customer-service
```

配置中有两项需要额外注意：

* eureka.client.serviceUrl.defaultZone：指定 Eureka 服务端的地址，当客户端没有专门进行配置时，就会使用这个默认地址。

* spring.application.name：服务注册所使用的名称，同时其他服务查找该服务时也使用该名称。我们启动该服务后，可以在管理页面中查看到该服务已经在注册中心中注册成功了。

### 服务发现与负载均衡（Ribbon + RestTemplate）

直接使用 Eureka Client 还是比较麻烦的，幸运的是，RestTemplate 整合了 Eureka Client，Ribbon 为我们提供了多样的负载均衡的功能，为我们提供了很多便利，我们所需要做的就是在 Spring 中注册一个 RestTemplate，并且添加 @LoadBalanced 注解

```
@Configuration
public class Config {
    @LoadBalanced
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
```

接下来，可以直接使用 RestTemplate 调用服务。服务的 URL 中包含了服务名称，例如：[http://customer-service/customer](http://customer-service/customer)，其中， customer-service 是服务名，而 customer 是该服务下的一个接口。

```
@Autowired
private RestTemplate restTemplate; 
 
public MessageWrapper<Customer> getCustomer(int id) { 
    Customer customer = restTemplate.exchange( "http://customer-service/customer/{id}", HttpMethod.GET, null, new ParameterizedTypeReference<Customer>() { }, id).getBody(); 
    return new MessageWrapper<>(customer, "server called using eureka with rest template"); 
}
```

### Eureka Api

如果使用的是非 Java 的语言客户端，可以通过 API 的方式进行集成。相关文档请查看 [https://github.com/Netflix/eureka/wiki/Eureka-REST-operations](https://github.com/Netflix/eureka/wiki/Eureka-REST-operations)
