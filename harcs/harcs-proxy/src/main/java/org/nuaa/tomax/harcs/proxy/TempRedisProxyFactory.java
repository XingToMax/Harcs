package org.nuaa.tomax.harcs.proxy;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.ArrayList;
import java.util.List;

/**
 * @Name: TempRedisProxyFactory
 * @Description: TODO
 * @Author: tomax
 * @Date: 2019-07-13 19:11
 * @Version: 1.0
 */
public class TempRedisProxyFactory implements IRedisProxyFactory {
    public static final int MAX_NODES_CONNECTION = 10;

    private volatile JedisPool master = null;
    private volatile List<JedisPool> nodes = null;

    private volatile static TempRedisProxyFactory instance = null;

    private TempRedisProxyFactory() {
        master = new JedisPool("127.0.0.1", 6379);
        nodes = new ArrayList<>(MAX_NODES_CONNECTION);
        nodes.add(master);
    }

    public static TempRedisProxyFactory getInstance() {
        if (instance == null) {
            synchronized (TempRedisProxyFactory.class) {
                if (instance == null) {
                    instance = new TempRedisProxyFactory();
                }
            }
        }
        return instance;
    }

    @Override
    public Jedis getMasterNode() {
        return master.getResource();
    }

    @Override
    public Jedis chooseWorkNode() {
        return nodes.get(0).getResource();
    }


}
