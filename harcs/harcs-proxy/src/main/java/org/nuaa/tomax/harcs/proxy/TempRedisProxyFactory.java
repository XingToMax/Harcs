package org.nuaa.tomax.harcs.proxy;

import org.nuaa.tomax.harcs.client.RedisClient;
import org.nuaa.tomax.harcs.envirnoment.RedisSystem;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.ArrayList;
import java.util.List;

/**
 * @Name: TempRedisProxyFactory
 * @Description: only master node work
 * @Author: tomax
 * @Date: 2019-07-13 19:11
 * @Version: 1.0
 */
public class TempRedisProxyFactory implements IRedisProxyFactory {
    private volatile JedisPool master = null;
    private volatile List<JedisPool> nodes = null;

    private volatile static TempRedisProxyFactory instance = null;

    private TempRedisProxyFactory() {
        master = new JedisPool("127.0.0.1", 6379);
        nodes = new ArrayList<>(RedisSystem.MAX_NODES_CONNECTION);
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
    public Jedis chooseWorkNode(String key) {
        return nodes.get(0).getResource();
    }

    @Override
    public void modifyMaster() {
        // TODO
    }

    @Override
    public void updateWorkNode() {
        // TODO
    }

    @Override
    public RedisClient getMasterClient() {
        return null;
    }

    @Override
    public RedisClient chooseWorkClient(String key) {
        return null;
    }
}
