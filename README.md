# Harcs
> 详细说明后续补充

目前已实现基本功能: redis 代理 -> 请求转发redis节点 -> 处理结果返回

入口: 

​	`org.nuaa.tomax.harcs.ProxyServer`执行main函数

需修改:

​    `org.nuaa.tomax.harcs.proxy.TempRedisProxyFactory`中的master节点的地址和端口

``` java
private TempRedisProxyFactory() {
    master = new JedisPool("127.0.0.1", 6379);
    nodes = new ArrayList<>(MAX_NODES_CONNECTION);
    nodes.add(master);
}
```



​	

