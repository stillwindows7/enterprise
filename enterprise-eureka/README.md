# 使用 Eureka 做服务发现

## Zookeeper 做注册中心的缺陷

Peter Kelley（个性化教育初创公司 knewton 的一名软件工程师）发表了一篇文章说明为什么Zookeeper 用于服务发现是一个错误的做法，他主要提出了三
个缺点：

1. ZooKeeper 无法很好的处理网络分区问题，当网络分区中的客户端节点无法到达 Quorum 时，会与 ZooKeeper 失去联系，从而也就无法使用其服务发现
机制。

2. 服务发现系统应该是一个 AP 系统，设计上针对可用性；而 ZooKeeper 是一个 CP 系统。

3. ZooKeeper 的设置和维护非常困难，实际操作的时候也容易出错，比如在客户端重建 Watcher，处理 Session 和异常的时候。

当然， Peter Kelley 提出的这几个问题并不是不能克服的，并不能说明基于 ZooKeeper 就不能做好一个服务发现系统，但是我们可能有更简洁的方案来实现。

# Eureka 介绍

## 什么是 Eureka

官方的介绍在这里[Eureka Wiki](https://github.com/Netflix/eureka/wiki/Eureka-at-a-glance)。Eureka 是 Netflix 开源的一个 Restful 
服务，主要用于服务的注册发现。Eureka 由两个组件组成： Eureka 服务器和 Eureka 客户端。 Eureka 服务器用作服务注册服务器。 Eureka 客户端是一个 
Java 客户端，用来简化与服务器的交互、作为轮询负载均衡器，并提供服务的故障切换。 Netflix 在其生产环境中使用的是另外的客户端，它提供基于流量、资源
利用率以及出错状态的加权负载均衡。

