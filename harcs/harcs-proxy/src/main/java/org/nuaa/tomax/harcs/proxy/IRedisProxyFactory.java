package org.nuaa.tomax.harcs.proxy;

import redis.clients.jedis.Jedis;

/**
 * @Name: IRedisProxyFactory
 * @Description: TODO
 * @Author: tomax
 * @Date: 2019-07-13 18:55
 * @Version: 1.0
 */
public interface IRedisProxyFactory {
    Jedis getMasterNode();

    Jedis chooseWorkNode(String key);

    void modifyMaster();

    void updateWorkNode();

    static IRedisProxyFactory get() {
        return TempRedisProxyFactory.getInstance();
    }
}
